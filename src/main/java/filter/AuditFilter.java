package filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class AuditFilter extends GenericFilterBean {
    private static final Logger log = LoggerFactory.getLogger(AuditFilter.class);

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest http = (HttpServletRequest) request;
        String user = (http.getUserPrincipal() != null) ? http.getUserPrincipal().getName() : "ANONYMOUS";
        String uri = http.getRequestURI();
        log.debug("[AUDIT] user={} uri={}", user, uri);

        chain.doFilter(request, response);
    }
}
