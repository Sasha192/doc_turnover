package app.security.controllers;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.common.base.Preconditions;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.security.Principal;

@Component
public class AuthenticationWrapper {

    private AuthenticationManager authManager;

    public SecurityContext getSecurityContext(HttpServletRequest req) {
        SecurityContext sc = null;
        HttpSession session = req.getSession();
        if (session != null) {
            Object o = session.getAttribute(SPRING_SECURITY_CONTEXT_KEY);
            if (null != o && o instanceof SecurityContext) {
                sc = (SecurityContext) o;
            }
        }
        if (sc == null) {
            sc = SecurityContextHolder.getContext();
        }
        return sc;
    }

    public Authentication getAuthentication(HttpServletRequest req) {
        SecurityContext sc = getSecurityContext(req);
        Preconditions.checkNotNull(sc);
        return getAuthentication(sc, req);
    }

    public Object getPrincipal(HttpServletRequest request) {
        Authentication authentication = getAuthentication(request);
        Preconditions.checkNotNull(authentication);
        return authentication.getPrincipal();
    }

    private Authentication getAuthentication(SecurityContext sc,
                                             HttpServletRequest request) {
        Authentication auth = sc.getAuthentication();
        if (auth == null) {
            // @TODO : remove this lines of code -> create view for 403, 404, 500, 400, and *
            HttpSession session = request.getSession(true);
            UsernamePasswordAuthenticationToken authReq
                    = new UsernamePasswordAuthenticationToken(
                    "sasha192.bunin@gmail.com", "sasha192.bunin@gmail.com");
            if (authManager == null) {
                ServletContext servletContext = request.getServletContext();
                WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
                authManager = webApplicationContext.getBean(AuthenticationManager.class);
            }
            auth = authManager.authenticate(authReq);
            sc.setAuthentication(auth);
            session.setAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    sc);
            return auth;
        } else {
            return auth;
        }
    }
}
