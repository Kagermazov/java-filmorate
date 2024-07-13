package ru.yandex.practicum.filmorate;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Component
public class LoggingFilter extends GenericFilterBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(req);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(res);

        try {
            chain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            // Log the HTTP request
            String requestBody = new String(wrappedRequest.getContentAsByteArray());
            LOGGER.info("HTTP Request - {} {} : {}", req.getMethod(), req.getRequestURI(), requestBody);
            
            // Ensure to copy content from wrapped response to original response
            wrappedResponse.copyBodyToResponse();
        }
    }
}