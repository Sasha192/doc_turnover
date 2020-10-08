package app.security.wrappers;

import app.configuration.spring.constants.Constants;
import app.customtenant.service.interfaces.IPerformerService;
import app.customtenant.service.interfaces.IPerformerUpdateEventListenerService;
import app.security.models.DefaultUserDetails;
import app.security.models.UserDto;
import app.security.models.auth.CustomUser;
import app.security.models.auth.RememberMeToken;
import app.security.service.IUserCreation;
import app.security.service.IUserService;
import app.security.utils.RememberMeUtil;
import java.io.IOException;
import java.util.Set;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

@Component
public class UserManagementWrapper
        implements IAuthenticationWrapper,
        IAuthenticationManagement {

    private static final Logger LOGGER = Logger.getLogger("intExceptionLogger");

    private final IUserService userService;

    private final IPerformerService performerService;

    private final IUserCreation userCreation;

    private final AuthenticationManager authManager;

    private final IPerformerUpdateEventListenerService listenerService;

    private final RememberMeUtil rememberMeUtil;

    @Autowired
    public UserManagementWrapper(IUserService userService,
                                 IPerformerService performerService,
                                 IUserCreation userCreation,
                                 //AuthenticationManager authManager,
                                 IPerformerUpdateEventListenerService listenerService,
                                 RememberMeUtil rememberMeUtil) {
        this.userService = userService;
        this.performerService = performerService;
        this.userCreation = userCreation;
        this.authManager = null;
        this.listenerService = listenerService;
        this.rememberMeUtil = rememberMeUtil;
    }

    @Override
    public void authenticate(HttpServletRequest request,
                             HttpServletResponse response,
                             UserDto dto) {
        CustomUser customUser;
        if ((customUser = userService.retrieveByName(dto.getEmail())) == null) {
            customUser = userCreation.create(dto);
        }
        auth(request, customUser);
        if (dto.getRemember() != null) {
            boolean rememmberMe = dto.getRemember().booleanValue();
            if (rememmberMe) {
                RememberMeToken token = rememberMeUtil.getRememberMeToken(customUser, request);
                userService.registerRememberMeToken(token);
                setTokenToCookies(response, token);
            }
        }
    }

    @Override
    public void authenticate(CustomUser user,
                             HttpServletRequest request)
            throws IOException {
        HttpSession session = request.getSession(true);
        Set<GrantedAuthority> authorities = DefaultUserDetails
                .mapAuthorities(user.getRole());
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(), user.getPassword(), authorities
                );
        if (session != null) {
            setToSession(token, user.getLogin(), user, session);
        }
    }

    private void setTokenToCookies(HttpServletResponse response,
                                   RememberMeToken token) {
        Cookie uuid = new Cookie(Constants.REMEMBER_ME_UUID, token.getUuid().toString());
        uuid.setMaxAge(Constants.VALID_REMEMBER_ME_TOKEN_TIME_SEC);
        uuid.setHttpOnly(true);
        uuid.setPath("/");
        Cookie id = new Cookie(Constants.REMEMBER_ME_ID, token.getId().toString());
        id.setMaxAge(Constants.VALID_REMEMBER_ME_TOKEN_TIME_SEC);
        id.setHttpOnly(true);
        id.setPath("/");
        response.addCookie(uuid);
        response.addCookie(id);
    }

    private void setToSession(Authentication auth,
                              String username,
                              CustomUser user,
                              HttpSession session) {
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
        session.setAttribute(Constants.SPRING_SECURITY_CONTEXT_KEY, sc);
        session.setAttribute(
                Constants.CUSTOM_USER_SESSION_KEY, user
        );
        session.setMaxInactiveInterval(
                Constants.MAX_INACTIVE_SESSION_INTERVAL_SECONDS
        );
    }

    private void auth(HttpServletRequest request,
                            CustomUser user) {
        HttpSession session = null;
        session = request.getSession(true);
        UsernamePasswordAuthenticationToken authReq
                = new UsernamePasswordAuthenticationToken(
                user.getEmail(), user.getPassword());
        Authentication auth = authManager.authenticate(authReq);
        setToSession(auth, user.getLogin(), user, session);
    }

    @Override
    public void invalidate(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Cookie cid = null;
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(Constants.REMEMBER_ME_ID)) {
                    cid = cookie;
                    break;
                }
            }
            try {
                if (cid != null) {
                    Long id = Long.valueOf(cid.getValue());
                    userService.removeRememberMeToken(id);
                }
            } catch (NumberFormatException ex) {
                LOGGER.error(ex.getMessage());
                LOGGER.error(ex.getStackTrace());
            }
        }
        cleanData(request, response);
        request.getSession().invalidate();
    }

    private void cleanData(final HttpServletRequest req, final HttpServletResponse res) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context == null ? null :
                context.getAuthentication();
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        UserManagementWrapper.removeCookies(req, res);
        logoutHandler.logout(req, res, authentication);
    }

    private static void removeCookies(HttpServletRequest req, HttpServletResponse res) {
        Cookie[] cookies = req.getCookies();
        for (Cookie cookie : cookies) {
            cookie.setValue(null);
            cookie.setMaxAge(0);
            cookie.setSecure(true);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            res.addCookie(cookie);
        }
    }

    @Override
    public SecurityContext getSecurityContext(HttpServletRequest req) {
        SecurityContext sc = null;
        HttpSession session = req.getSession();
        if (session != null) {
            Object o = session.getAttribute(Constants.SPRING_SECURITY_CONTEXT_KEY);
            if (null != o && o instanceof SecurityContext) {
                sc = (SecurityContext) o;
            }
        }
        if (sc == null) {
            sc = SecurityContextHolder.getContext();
        }
        return sc;
    }

    @Override
    public Object getPrincipal(HttpServletRequest request) {
        Authentication authentication = getAuthentication(request);
        if (!(authentication == null)) {
            return authentication.getPrincipal();
        }
        return null;
    }

    @Override
    public Authentication getAuthentication(HttpServletRequest req) {
        SecurityContext sc = getSecurityContext(req);
        if (!(sc == null)) {
            return sc.getAuthentication();
        }
        return null;
    }

    /*@Override
    public Performer retrievePerformer(HttpServletRequest request) {
        Performer performer = null;
        HttpSession session = request.getSession();
        if (session != null) {
            performer = retrievePerformer(session);
            return performer;
        }
        Principal principal = (Principal) getPrincipal(request);
        if ((principal instanceof UserDetails)) {
            performer = performerService.retrieveByUsername(
                    ((UserDetails) principal).getUsername()
            );
            session = request.getSession(true);
            session.setAttribute(Constants.PERFORMER_SESSION_KEY, performer);
        }
        Preconditions.checkNotNull(performer);
        return performer;
    }*/

    /*private Performer retrievePerformer(HttpSession session) {
        Performer performer = (Performer) session
                .getAttribute(Constants.PERFORMER_SESSION_KEY);
        Long id = performer.getId();
        if (!listenerService.toUpdate(id)) {
            return performer;
        } else {
            performer = performerService.findOne(id);
            listenerService.wasUpdated(id);
            session.setAttribute(
                    Constants.PERFORMER_SESSION_KEY, performer
            );
            return performer;
        }
    }*/
}
