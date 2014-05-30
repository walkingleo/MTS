package com.cdg.mtsbank.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.apache.log4j.Logger;

public class LogUtil {

    public static void logExceptionStackTrace(Logger log, Exception e) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream p = new PrintStream(os);
        e.printStackTrace(p);
        log.error(os.toString());
    }

}
