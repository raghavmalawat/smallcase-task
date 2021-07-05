package com.smallcase;

import io.sentry.Sentry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class LogFactory {
    private static final Logger logger = LogManager.getRootLogger();

    @PostConstruct
    public void initialiseLogFactory() throws URISyntaxException {
        logger.info("log factory initialized");
        String log4jConfigFile = "log.xml";
        System.setProperty("log4j.configurationFile", log4jConfigFile);
        LoggerContext context = (LoggerContext)LogManager.getContext(false);
        context.setConfigLocation(new URI(log4jConfigFile));
        getLogger(LogFactory.class).info(log4jConfigFile);
        getLogger(LogFactory.class).error("Logging enabled");
    }

    public static Logger getLogger(Class<?> clazz) {
        return LogManager.getLogger(clazz);
    }

}
