package app.security.controllers.filters;

import app.models.basic.Performer;
import app.security.models.SimpleRole;
import java.io.IOException;
import java.util.Set;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.filter.GenericFilterBean;

public class AccessFilter extends GenericFilterBean {

    private SimpleRole[] roles;

    private String accessUri;

    public AccessFilter(String accessUri, SimpleRole... roles) {
        this.roles = roles;
        this.accessUri = accessUri;
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain)
            throws IOException, ServletException {
        // @TODO : check if smbd pass to wrong performer, except MANAGER, G_MANAGER
        // @TODO : add filter for no HttpServlerRequest!!!
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            String uri = request.getRequestURI();
            if (!uri.startsWith(accessUri)) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            } else {
                Performer performer = SessionPerformerUtil.retrievePerformer(request.getSession());
                if (performer != null) {
                    Set<SimpleRole> performerRoles = performer.getRoles();
                    if (allow(performerRoles)) {
                        filterChain.doFilter(servletRequest, servletResponse);
                        return;
                    } else {
                        RestrictRedirectUtil.restrict(servletResponse);
                        return;
                    }
                } else {
                    RestrictRedirectUtil.restrict(servletResponse);
                    return;
                }
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean allow(Set<SimpleRole> perfRoles) {
        for (int i = 0; i < roles.length; i++) {
            if (perfRoles.contains(roles[i])) {
                return true;
            }
        }
        return false;
    }
}
