package app.security.controllers.filters;

import java.io.IOException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class RestrictRedirectUtil {
    public static void restrict(ServletResponse servletResponse)
            throws IOException {
        ((HttpServletResponse) servletResponse)
                .sendRedirect("/block/access");
    }
}
