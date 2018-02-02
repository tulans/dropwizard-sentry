package com.tulans;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.DeferredProcessingAware;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.logging.AbstractAppenderFactory;
import io.dropwizard.logging.async.AsyncAppenderFactory;
import io.dropwizard.logging.filter.LevelFilterFactory;
import io.dropwizard.logging.layout.LayoutFactory;
import io.sentry.Sentry;
import io.sentry.logback.SentryAppender;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;


@Getter
@Setter
@JsonTypeName("sentry")
public class MetricsFileAppenderFactory<E extends DeferredProcessingAware> extends AbstractAppenderFactory {

    @NotNull
    @JsonProperty
    private String dsn = null;

    @JsonProperty
    private Map<String, String> fieldMap;

    private String getDsnString(){
        StringBuilder fieldBuilder = new StringBuilder();
        if(StringUtils.isEmpty(getDsn())){
            return null;
        }
        StringBuilder buildDsn = new StringBuilder();
        buildDsn.append(getDsn());

        Map<String, String> fields = getFieldMap();
        fields.forEach((key, value)->{
            if(fieldBuilder.length()>0){
                fieldBuilder.append("&");
            }
            fieldBuilder.append(key);
            fieldBuilder.append("=");
            fieldBuilder.append(value);
        });
        buildDsn.append("?").append(fieldBuilder);

        return buildDsn.toString();
    }

    @Override
    public Appender<ILoggingEvent> build(LoggerContext context, String applicationName, LayoutFactory layoutFactory, LevelFilterFactory levelFilterFactory, AsyncAppenderFactory asyncAppenderFactory) {
        checkNotNull(context);

        if(getDsnString() != null) {
            Sentry.init(getDsnString());
        }

        final SentryAppender appender = new SentryAppender();
        appender.setContext(context);

        appender.addFilter(levelFilterFactory.build(threshold));
        appender.start();

        final Appender<ILoggingEvent> asyncAppender = wrapAsync(appender, asyncAppenderFactory, context);
        addDroppingRavenLoggingFilter(asyncAppender);

        return asyncAppender;
    }

    private void addDroppingRavenLoggingFilter(Appender<ILoggingEvent> appender) {
        final Filter<ILoggingEvent> filter = new MetricsMarkerFilter();
        filter.start();
        appender.addFilter(filter);
    }
}
