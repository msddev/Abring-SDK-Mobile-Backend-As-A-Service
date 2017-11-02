package ir.abring.abringlibrary;

import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.Date;

public class Logger {
    private static Logger instance = new Logger();
    private static String MESSAGE_TEMPLATE = "<Log:{0} - {1} - {2}>";
    private static String MESSAGE_TEMPLATE_WITH_EXCEPTION = "<Log:{0} - {1} - {2}  \n{3}\n>";
    private File logFile;
    private static int LOG_FILE_MAX_SIZE = 1024 * 1024;
    private Writer writer;

    private static enum LogLevel {
        info("info"), error("error"), debug("debug");
        private String str;

        LogLevel(String str) {
            this.str = str;
        }

        public String toString() {
            return this.str;
        }
    }

    private Logger() {
    }

    public static Logger getInstance() {
        return instance;
    }

    public void error(String message, Object[] args, Throwable t) {
        write(LogLevel.error, message, args, t);
    }

    public void info(String message, Object[] args) {
        write(LogLevel.info, message, args, null);
    }

    public void debug(String message, Object[] args) {
        write(LogLevel.debug, message, args, null);
    }

    private void checkLogFile() {
        try {
            logFile = new File(
                    Environment.getExternalStorageDirectory(),
                    Abring.getContext().getString(R.string.app_name).concat(".log"));
            if (logFile.exists()) {
                if (logFile.length() > LOG_FILE_MAX_SIZE) {
                    if (writer != null) {
                        writer.close();
                        writer = null;
                    }
                    logFile.delete();
                }
            }
            if (writer == null) {
                writer = new FileWriter(logFile, true);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized void write(LogLevel logLevel, String message, Object[] args,
                                    Throwable t) {
        // if SDCard is not mounted logging is disabled
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return;
        }
        checkLogFile();
        try {
            if (message != null || t != null) {
                writer.write(getFormatMessage(logLevel, message, args, t) + "\n");
                writer.flush();
            }
        } catch (IOException e1) {
            throw new RuntimeException(e1);
        }
    }

    private String getFormatMessage(LogLevel logLevel, String message,
                                    Object[] args, Throwable t) {
        String formattedMessage = message == null ? "" : MessageFormat.format(message, args);
        if (t != null) {
            return MessageFormat.format(MESSAGE_TEMPLATE_WITH_EXCEPTION,
                    logLevel.toString(), new Date(), formattedMessage,
                    getExceptionStack(t));
        } else {
            return MessageFormat.format(MESSAGE_TEMPLATE, logLevel.toString(),
                    new Date(), formattedMessage);
        }
    }

    private String getExceptionStack(Throwable t) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintWriter writer = new PrintWriter(out);
            t.printStackTrace(writer);
            writer.close();
            return out.toString("UTF-8");
        } catch (UnsupportedEncodingException e1) {
            throw new RuntimeException(e1);
        }
    }
}
