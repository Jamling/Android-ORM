/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;

/**
 * API for sending log output.
 * 
 * <p>
 * Generally, use the Log.v() Log.d() Log.i() Log.w() and Log.e() methods.
 * 
 * <p>
 * The order in terms of verbosity, from least to most is ERROR, WARN, INFO,
 * DEBUG, VERBOSE. Verbose should never be compiled into an application except
 * during development. Debug logs are compiled in but stripped at runtime.
 * Error, warning and info logs are always kept.
 * 
 * <p>
 * <b>Tip:</b> A good convention is to declare a <code>TAG</code> constant in
 * your class:
 * 
 * <pre>
 * private static final String TAG = &quot;MyActivity&quot;;
 * </pre>
 * 
 * and use that in subsequent calls to the log methods.
 * </p>
 * 
 * <p>
 * <b>Tip:</b> Don't forget that when you make a call like
 * 
 * <pre>
 * Log.v(TAG, &quot;index=&quot; + i);
 * </pre>
 * 
 * that when you're building the string to pass into Log.d, the compiler uses a
 * StringBuilder and at least three allocations occur: the StringBuilder itself,
 * the buffer, and the String object. Realistically, there is also another
 * buffer allocation and copy, and even more pressure on the gc. That means that
 * if your log message is filtered out, you might be doing significant work and
 * incurring significant overhead.
 */
public final class Log {
    
    /**
     * Priority constant for the println method; use Log.v.
     */
    public static final int VERBOSE = 2;
    
    /**
     * Priority constant for the println method; use Log.d.
     */
    public static final int DEBUG = 3;
    
    /**
     * Priority constant for the println method; use Log.i.
     */
    public static final int INFO = 4;
    
    /**
     * Priority constant for the println method; use Log.w.
     */
    public static final int WARN = 5;
    
    /**
     * Priority constant for the println method; use Log.e.
     */
    public static final int ERROR = 6;
    
    /**
     * Priority constant for the println method.
     */
    public static final int ASSERT = 7;
    
    private Log() {
    }
    
    /**
     * Send a {@link #VERBOSE} log message.
     * 
     * @param tag
     *            Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg
     *            The message you would like logged.
     */
    public static int v(String tag, String msg) {
        return println_native(LOG_ID_MAIN, VERBOSE, tag, msg);
    }
    
    /**
     * Send a {@link #VERBOSE} log message and log the exception.
     * 
     * @param tag
     *            Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg
     *            The message you would like logged.
     * @param tr
     *            An exception to log
     */
    public static int v(String tag, String msg, Throwable tr) {
        return println_native(LOG_ID_MAIN, VERBOSE, tag, msg + '\n'
                + getStackTraceString(tr));
    }
    
    /**
     * Send a {@link #DEBUG} log message.
     * 
     * @param tag
     *            Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg
     *            The message you would like logged.
     */
    public static int d(String tag, String msg) {
        return println_native(LOG_ID_MAIN, DEBUG, tag, msg);
    }
    
    /**
     * Send a {@link #DEBUG} log message and log the exception.
     * 
     * @param tag
     *            Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg
     *            The message you would like logged.
     * @param tr
     *            An exception to log
     */
    public static int d(String tag, String msg, Throwable tr) {
        return println_native(LOG_ID_MAIN, DEBUG, tag, msg + '\n'
                + getStackTraceString(tr));
    }
    
    /**
     * Send an {@link #INFO} log message.
     * 
     * @param tag
     *            Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg
     *            The message you would like logged.
     */
    public static int i(String tag, String msg) {
        return println_native(LOG_ID_MAIN, INFO, tag, msg);
    }
    
    /**
     * Send a {@link #INFO} log message and log the exception.
     * 
     * @param tag
     *            Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg
     *            The message you would like logged.
     * @param tr
     *            An exception to log
     */
    public static int i(String tag, String msg, Throwable tr) {
        return println_native(LOG_ID_MAIN, INFO, tag, msg + '\n'
                + getStackTraceString(tr));
    }
    
    /**
     * Send a {@link #WARN} log message.
     * 
     * @param tag
     *            Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg
     *            The message you would like logged.
     */
    public static int w(String tag, String msg) {
        return println_native(LOG_ID_MAIN, WARN, tag, msg);
    }
    
    /**
     * Send a {@link #WARN} log message and log the exception.
     * 
     * @param tag
     *            Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg
     *            The message you would like logged.
     * @param tr
     *            An exception to log
     */
    public static int w(String tag, String msg, Throwable tr) {
        return println_native(LOG_ID_MAIN, WARN, tag, msg + '\n'
                + getStackTraceString(tr));
    }
    
    /**
     * Checks to see whether or not a log for the specified tag is loggable at
     * the specified level.
     * 
     * The default level of any tag is set to INFO. This means that any level
     * above and including INFO will be logged. Before you make any calls to a
     * logging method you should check to see if your tag should be logged. You
     * can change the default level by setting a system property: 'setprop
     * log.tag.&lt;YOUR_LOG_TAG> &lt;LEVEL>' Where level is either VERBOSE,
     * DEBUG, INFO, WARN, ERROR, ASSERT, or SUPPRESS. SUPPRESS will turn off all
     * logging for your tag. You can also create a local.prop file that with the
     * following in it: 'log.tag.&lt;YOUR_LOG_TAG>=&lt;LEVEL>' and place that in
     * /data/local.prop.
     * 
     * @param tag
     *            The tag to check.
     * @param level
     *            The level to check.
     * @return Whether or not that this is allowed to be logged.
     * @throws IllegalArgumentException
     *             is thrown if the tag.length() > 23.
     */
    public static native boolean isLoggable(String tag, int level);
    
    /*
     * Send a {@link #WARN} log message and log the exception.
     * 
     * @param tag Used to identify the source of a log message. It usually
     * identifies the class or activity where the log call occurs.
     * 
     * @param tr An exception to log
     */
    public static int w(String tag, Throwable tr) {
        return println_native(LOG_ID_MAIN, WARN, tag, getStackTraceString(tr));
    }
    
    /**
     * Send an {@link #ERROR} log message.
     * 
     * @param tag
     *            Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg
     *            The message you would like logged.
     */
    public static int e(String tag, String msg) {
        return println_native(LOG_ID_MAIN, ERROR, tag, msg);
    }
    
    /**
     * Send a {@link #ERROR} log message and log the exception.
     * 
     * @param tag
     *            Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg
     *            The message you would like logged.
     * @param tr
     *            An exception to log
     */
    public static int e(String tag, String msg, Throwable tr) {
        return println_native(LOG_ID_MAIN, ERROR, tag, msg + '\n'
                + getStackTraceString(tr));
    }
    
    /**
     * Low-level logging call.
     * 
     * @param priority
     *            The priority/type of this log message
     * @param tag
     *            Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg
     *            The message you would like logged.
     * @return The number of bytes written.
     */
    public static int println(int priority, String tag, String msg) {
        return println_native(LOG_ID_MAIN, priority, tag, msg);
    }
    
    /**
     * Handy function to get a loggable stack trace from a Throwable
     * 
     * @param tr
     *            An exception to log
     */
    public static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }
        
        // This is to reduce the amount of log spew that apps do in the
        // non-error
        // condition of the network being unavailable.
        Throwable t = tr;
        while (t != null) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }
        
        StringWriter sw = new StringWriter();
        PrintWriter pw = new FastPrintWriter(sw, false, 256);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }
    
    /** @hide */
    public static final int LOG_ID_MAIN = 0;
    /** @hide */
    public static final int LOG_ID_RADIO = 1;
    /** @hide */
    public static final int LOG_ID_EVENTS = 2;
    /** @hide */
    public static final int LOG_ID_SYSTEM = 3;
    
    /** @hide */
    private static int println_native(int bufID, int priority, String tag,
            String msg) {
        System.out.println(msg);
        return 0;
    }
}
