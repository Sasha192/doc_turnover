package app.security.wrappers.impl;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class CustomRequestContextHolder {
    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder
                .getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes)requestAttributes)
                    .getRequest();
            return request;
        }
        return null;
    }
}