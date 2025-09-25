package filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

public class RequestLoggingFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        // Trace ID 발급 (요청 단위 추적)
        String traceId = UUID.randomUUID().toString(); // 요청마다 Trace ID를 생성 -> 로그끼리 연결해주기 위해
        MDC.put("traceId", traceId); // MDC = 로그에 traceId를 계속 붙여서 출력할 수 있는 공간

        long start = System.currentTimeMillis();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String query = request.getQueryString();

        // 민감정보 마스킹 예시 (아주 단순화)
        String maskedQuery = (query == null) ? "" : query.replaceAll("(?i)(password|pwd)=([^&]+)", "$1=****");

        log.info("[REQ][{}] {} {}{}", traceId, method, uri, (maskedQuery.isEmpty() ? "" : "?" + maskedQuery));

        try {
            filterChain.doFilter(request, response); // 실제 컨트롤러로 요청 전달
        } finally {
            long took = System.currentTimeMillis() - start;
            int status = response.getStatus();
            log.info("[RES][{}] status={} took={}ms", traceId, status, took);
            MDC.clear();
        }
    }
}
