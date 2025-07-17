package my.project.xmlconverter.utils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;
import org.slf4j.LoggerFactory;

/**
 * Утилита для настройки логгера приложения.
 * Конфигурирует формат вывода, уровень логирования и файл для записи логов.
 */
public class LoggerConfigurator {

    private final static String LOG_FILE = "log.file";
    private final static String DEFAULT_LOG_FILE = "application.log";
    private final static String LOG_LEVEL = "log.level";
    private final static String DEFAULT_LOG_LEVEL = "INFO";
    private final static String LOG_PATTERN = "log.file.pattern";
    private final static String DEFAULT_LOG_PATTERN = "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n";

    /**
     * Настраивает логгер приложения на основе параметров из application.properties
     */
    public static void configure() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.reset();

        String logFile = PropertiesUtil.get(LOG_FILE, DEFAULT_LOG_FILE);
        String logLevel = PropertiesUtil.get(LOG_LEVEL, DEFAULT_LOG_LEVEL );
        String filePattern = PropertiesUtil.get(LOG_PATTERN, DEFAULT_LOG_PATTERN);

        FileAppender<ILoggingEvent> fileAppender = new FileAppender<>();
        fileAppender.setContext(loggerContext);
        fileAppender.setName("FILE");
        fileAppender.setFile(logFile);

        PatternLayoutEncoder fileEncoder = new PatternLayoutEncoder();
        fileEncoder.setContext(loggerContext);
        fileEncoder.setPattern(filePattern);
        fileEncoder.start();

        fileAppender.setEncoder(fileEncoder);
        fileAppender.start();

        Logger rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.toLevel(logLevel));
        rootLogger.addAppender(fileAppender);
    }
}