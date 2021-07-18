package com.example.restservice;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
@Order(2)
public class RequestResponseLoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

    @Autowired
    private MeterRegistry meterRegistry;

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        chain.doFilter(request, response);
        logger.info("Logging Request  {} : {}", req.getMethod(), req.getRequestURI());
        String methodName = req.getMethod();
        String uriName = req.getRequestURI();
        int statusCodeName = res.getStatus();
        Tag methodTag = Tag.of("method", methodName);
        Tag uriTag = Tag.of("uri", uriName);
        Tag statusCodeTag = Tag.of("statusCode", String.valueOf(statusCodeName));
        ImmutableList<Tag> tags = ImmutableList.of(methodTag, uriTag, statusCodeTag);
        meterRegistry.counter("custom-metric-filter-req-res", tags).increment();
        }






//        if (req.getMethod().equals("POST") &&
//                (res.getStatus() == 201 || res.getStatus() == 200)) {
//            meterRegistry.counter("custom_counter_request").increment();
//        }
//
//        Gauge gaugePOST = Gauge.builder("mario_gauge", list, List::size)
//                .strongReference(true)
//                .register(meterRegistry);
//        logger.info(String.valueOf(gaugePOST.value()));
//        logger.info("Logging Response :{} - Status Code: {}", res.getContentType(), res.getStatus());

}


