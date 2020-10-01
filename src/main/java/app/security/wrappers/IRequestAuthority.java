package app.security.wrappers;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public interface IRequestAuthority {

    public boolean hasAuthority(HttpServletRequest request,
                                SimpleGrantedAuthority authority);

}
