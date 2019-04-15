package com.addplus.server.api.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 异常打印工具类 Created by Jerry on 2016/6/3.
 */
public class ExceptionUtil {
    final private static String COLON = ":";
    final private static String EX = "Exception" + COLON;
    final private static String CL = "----Class" + COLON;
    final private static String ME = "----Method" + COLON;
    final private static String NEXTLINE = "\n";
    private static Logger logger = LoggerFactory.getLogger(ExceptionUtil.class);

    /**
     * 获取当前异常的详细位置和类型
     *
     * @param e
     */
    public static String getExceptionInfo(Exception e) {
        String returnMessage = "";
        if (e != null) {
            StackTraceElement[] st = e.getStackTrace();
            String className;
            String method;
            String exceptionClassName;
            for (StackTraceElement stackTraceElement : st) {
                exceptionClassName = e.getClass().getName();
                className = stackTraceElement.getClassName();
                method = stackTraceElement.getMethodName();
                int lineNum = stackTraceElement.getLineNumber();
                returnMessage += EX + exceptionClassName + CL + className + COLON + lineNum + ME + method + NEXTLINE;
            }
        }
        return returnMessage;
    }

    /**
     * 打印当前异常的详细位置和类型到对应类日志
     *
     * @param e
     * @param log
     */
    public static void printExceptionInfo(Exception e, Logger log) {
        if (e != null && log != null) {
            StackTraceElement[] st = e.getStackTrace();
            String className;
            String method;
            String exceptionClassName;
            for (StackTraceElement stackTraceElement : st) {
                exceptionClassName = e.getClass().getName();
                className = stackTraceElement.getClassName();
                method = stackTraceElement.getMethodName();
                int lineNum = stackTraceElement.getLineNumber();
                log.error(EX + exceptionClassName + CL + className + COLON + lineNum + ME + method);
            }
        }
    }

    /**
     * 打印当前异常的详细位置和类型到对应类日志
     *
     * @param e
     */
    public static void printExceptionInfo(Exception e) {
        if (e != null) {
            StackTraceElement[] st = e.getStackTrace();
            String exceptionClassName;
            String className;
            String method;
            for (StackTraceElement stackTraceElement : st) {
                exceptionClassName = e.getClass().getName();
                className = stackTraceElement.getClassName();
                method = stackTraceElement.getMethodName();
                int lineNum = stackTraceElement.getLineNumber();
                logger.error(EX + exceptionClassName + CL + className + COLON + lineNum + ME + method);
            }
        }
    }
}
