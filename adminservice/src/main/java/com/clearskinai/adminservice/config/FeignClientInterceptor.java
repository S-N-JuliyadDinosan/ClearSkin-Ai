package com.clearskinai.adminservice.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    public void apply(RequestTemplate template) {
        try {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

            if (requestAttributes != null) {
                HttpServletRequest request =
                        (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);

                if (request != null) {
                    String authHeader = request.getHeader(AUTHORIZATION_HEADER);
                    if (authHeader != null && !authHeader.isEmpty()) {
                        template.header(AUTHORIZATION_HEADER, authHeader);
                    }
                }
            }
        } catch (Exception e) {
            // Avoid crashing Feign if no request context is present
            System.out.println("Feign interceptor could not resolve request: " + e.getMessage());
        }
    }
}
