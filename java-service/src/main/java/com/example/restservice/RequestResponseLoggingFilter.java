package com.example.restservice;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


@Component
@Order(2)
public class RequestResponseLoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

    @Autowired
    private MeterRegistry meterRegistry;
    private final AtomicLong queueRequest = new AtomicLong();


    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        queueRequest.incrementAndGet();
        chain.doFilter(request, response);
        queueRequest.decrementAndGet();
        //logger.info("Logging Request  {} : {}", req.getMethod(), req.getRequestURI());
        Gauge gauge = Gauge.builder("queue.requests", queueRequest, queueRequest-> queueRequest.get())
                .strongReference(true)
                .register(meterRegistry);
        logger.info("****GAUGE  {}: ", gauge.value());
        countMyCustomMetrics(req, res);
    }


    public void countMyCustomMetrics(HttpServletRequest request, HttpServletResponse response) {
        List<Tag> tags = new ArrayList<>();
        String methodName = request.getMethod();
        String uriName = request.getRequestURI();
        int statusCodeName = response.getStatus();
        Tag methodTag = Tag.of("method", methodName);
        Tag uriTag = Tag.of("uri", uriName);
        Tag statusCodeTag = Tag.of("statusCode", String.valueOf(statusCodeName));
        tags.add(methodTag);
        tags.add(uriTag);
        tags.add(statusCodeTag);
        meterRegistry.counter("custom-metric-filter-req-res", tags).increment();
    }


}


