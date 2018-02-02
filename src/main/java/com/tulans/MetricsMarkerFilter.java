package com.tulans;


import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class MetricsMarkerFilter extends Filter<ILoggingEvent> {
    @Override
    public FilterReply decide(ILoggingEvent event) {
        if (event.getLoggerName().startsWith("io.sentry")) {
            return FilterReply.DENY;
        } else {
            return FilterReply.NEUTRAL;
        }
    }
}
