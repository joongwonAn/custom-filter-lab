package filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class PerformanceMonitoringFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(PerformanceMonitoringFilter.class);
    private static final long SLOW_THRESHOLD_MS = 1000L; // 1초

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        long start = System.nanoTime();
        filterChain.doFilter(request, response);
        long tookMs = (System.nanoTime() - start) / 1_000_000;

        if (tookMs > SLOW_THRESHOLD_MS) {
            log.warn("[SLOW] {} took {}ms", request.getRequestURI(), tookMs);
        } else {
            log.debug("[OK] {} took {}ms", request.getRequestURI(), tookMs);
        }
    }
}
