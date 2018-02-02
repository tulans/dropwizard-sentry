package com.tulans;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import io.dropwizard.configuration.ConfigurationException;
import io.dropwizard.logging.async.AsyncLoggingEventAppenderFactory;
import io.dropwizard.logging.filter.ThresholdLevelFilterFactory;
import io.dropwizard.logging.layout.DropwizardLayoutFactory;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNull;


public class MetricsFileAppenderFactoryTest {

    private final LoggerContext context = new LoggerContext();
    private final DropwizardLayoutFactory layoutFactory = new DropwizardLayoutFactory();
    private final ThresholdLevelFilterFactory levelFilterFactory = new ThresholdLevelFilterFactory();
    private final AsyncLoggingEventAppenderFactory asyncAppenderFactory = new AsyncLoggingEventAppenderFactory();

    @Test
    public void hasValidDefaults() throws IOException, ConfigurationException {
        final MetricsFileAppenderFactory factory = new MetricsFileAppenderFactory();

        assertNull("default dsn is unset", factory.getDsn());
    }

    @Test(expected = NullPointerException.class)
    public void buildRavenAppenderShouldFailWithNullContext() {
        new MetricsFileAppenderFactory().build(null, "", null, levelFilterFactory, asyncAppenderFactory);
    }

    @Test
    public void buildRavenAppenderShouldWorkWithValidConfiguration() {
        final MetricsFileAppenderFactory raven = new MetricsFileAppenderFactory();
        final String dsn = "https://user:pass@app.getsentry.com/id";

        Appender<ILoggingEvent> appender =
                raven.build(context, dsn, layoutFactory, levelFilterFactory, asyncAppenderFactory);

    }

}
