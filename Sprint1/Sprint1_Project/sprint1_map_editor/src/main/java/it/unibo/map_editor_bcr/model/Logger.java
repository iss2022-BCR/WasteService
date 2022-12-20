package it.unibo.map_editor_bcr.model;

import it.unibo.map_editor_bcr.model.settings.LogLevel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static Logger instance;
    private static LogLevel logLevel;
    private static PrintWriter logFileWriter;
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private Logger() {
        logLevel = new LogLevel();
        logLevel.toFile = false;
    }

    public static synchronized Logger getInstance() {
        if(instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void openLogWriter(String path) {
        if(!path.endsWith("/")) {
            path += "/";
        }
        File logDirectory = new File(path);
        if(!logDirectory.exists() || !logDirectory.isDirectory()) {
            logDirectory.mkdir();
        }
        try {
            String nowDate = this.getCurrentLocalDateTimeString();
            String filename = "log_" + nowDate + ".txt";
            logFileWriter = new PrintWriter(new FileWriter(path + filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void closeLogWriter() {
        if(logFileWriter != null) {
            logFileWriter.close();
        }
    }

    public void setLogLevel(LogLevel newLogLevel) {
        // Close writer if logs directory changed or if we're not logging to file anymore
        if(logLevel.toFile && (!newLogLevel.toFile ||
                !logLevel.directory.equals(newLogLevel.directory))) {
            this.closeLogWriter();
        }

        // Open new writer if logs directory changed, or we start logging to file
        if(newLogLevel.toFile && (!logLevel.toFile ||
                !logLevel.directory.equals(newLogLevel.directory))) {
            this.openLogWriter(newLogLevel.directory);
        }

        logLevel.toStandardOutput = newLogLevel.toStandardOutput;
        logLevel.toFile = newLogLevel.toFile;
        logLevel.directory = newLogLevel.directory;
    }
    public void setLogLevel(boolean stdOut, boolean file, String filename) {
        LogLevel logLevel = new LogLevel();
        logLevel.toStandardOutput = stdOut;
        logLevel.toFile = file;
        logLevel.directory = filename;

        this.setLogLevel(logLevel);
    }

    public void logMessage(String msg) {
        if(logLevel.toStandardOutput) {
            System.out.println(msg);
        }
        if(logLevel.toFile && !logLevel.directory.isEmpty()) {
            logFileWriter.println(msg);
        }
    }

    private String getCurrentLocalDateTimeString() {
        LocalDateTime ldt = LocalDateTime.now();
        return formatter.format(ldt);
    }
}
