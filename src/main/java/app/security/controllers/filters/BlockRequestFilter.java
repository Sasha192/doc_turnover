package app.security.controllers.filters;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.springframework.web.filter.GenericFilterBean;

public class BlockRequestFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain)
            throws IOException,
            ServletException {
        /*if (servletRequest instanceof HttpServletRequest) {
            String requestUri = ((HttpServletRequest) servletRequest).getRequestURI();
            if (requestUri.matches("\\/.*")) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
            if (requestUri.matches(Constants.RESOURCES_PATH_REGEX)) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
            if (requestUri.matches(Constants.BAD_REQUEST_REGEX)) {
                ((HttpServletResponse) servletResponse).sendRedirect("/block/entry");
                return;
            }
        }*/
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
