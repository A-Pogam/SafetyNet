package com.SafetyNet.SafetyNet.logger;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class EndpointsLogger implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(EndpointsLogger.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String requestURL = java.net.URLDecoder.decode(request.getRequestURL().toString(), StandardCharsets.UTF_8);

        if (!request.getParameterMap().isEmpty()) {
            String requestParameters = "?" + request.getParameterMap().entrySet().stream()
                    .map(e -> e.getKey() + "=" + String.join(", ", e.getValue())).collect(Collectors.joining(" "));

            logger.info("URL requested : {} {}{}", request.getMethod(), requestURL, requestParameters);
        } else {
            logger.info("Endpoint requested : {} {}", request.getMethod(), requestURL);
        }


        return true;
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception exception) throws Exception {
        int responseStatus = response.getStatus();


        switch (responseStatus) {
            case 200:
                logger.info("Response : Status {} OK", responseStatus);
                break;
            case 201:
                String responseLocation = java.net.URLDecoder.decode(response.getHeader("Location"),
                        StandardCharsets.UTF_8);
                logger.info("Response : Status {} Created - Location : {}", responseStatus, responseLocation);
                break;
            case 204:
                logger.info("Response : Status {} No Content - The request is fine, the response is empty.",
                        responseStatus);
                break;
            case 400:
                logger.error(
                        "Response : Status {} Bad Request - The request is wrong. Please check the body and the parameters.",
                        responseStatus);
                break;
            case 404:
                logger.error(
                        "Response : Status {} Not Found - The request is wrong. Please check the URL, not the parameters or variables.",
                        responseStatus);
                break;
            case 500:
                logger.error(
                        "Response : Status {} Internal Server Error - Server side problem, the request is probably fine.",
                        responseStatus);
                break;
            default:
                logger.error("Status unknown");
        }

    }
}