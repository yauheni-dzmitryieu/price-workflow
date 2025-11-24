package com.yd.price.workflow.config;

import com.yd.price.workflow.rest.filter.CorrelationIdFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    /**
     * Registers and configures the {@link CorrelationIdFilter}.
     * <p>
     * This filter is responsible for adding or retrieving a <b>Correlation ID</b>
     * to/from the incoming HTTP request. This ID is crucial for tracing requests
     * across multiple services and log entries.
     * </p>
     *
     * @return A {@link FilterRegistrationBean} for the {@link CorrelationIdFilter},
     * configured to apply to all URL patterns (`/*`) with a high precedence
     * (order 1) to ensure it executes early in the filter chain.
     */
    @Bean
    public FilterRegistrationBean<CorrelationIdFilter> correlationIdFilter() {
        FilterRegistrationBean<CorrelationIdFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new CorrelationIdFilter());
        bean.addUrlPatterns("/*");
        bean.setOrder(1);
        return bean;
    }
}
