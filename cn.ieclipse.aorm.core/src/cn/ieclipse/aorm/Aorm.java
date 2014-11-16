/*
 * Copyright 2010-2014 Jamling(li.jamling@gmail.com).
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
package cn.ieclipse.aorm;

import android.util.Log;

/**
 * Aorm settings
 * 
 * @author Jamling
 * 
 */
public final class Aorm {
    
    private static boolean debug = false;
    private static boolean supportExtend = true;
    private static boolean exactInsertOrUpdate = false;
    private static final String TAG = "AORM";
    
    private Aorm() {
        //
    }
    
    /**
     * Enable/Disable debug to print SQL.
     * 
     * @param enable
     *            debug flag, default false.
     */
    public static void enableDebug(boolean enable) {
        debug = enable;
    }
    
    public static void allowExtend(boolean allow) {
        supportExtend = allow;
    }
    
    public static boolean allowExtend() {
        return supportExtend;
    }
    
    /**
     * Set use actuarial insertOrUpdate
     * 
     * @param exactInsertOrUpdate
     *            If true, will query the object from database, insert if not
     *            exists or update if exist, otherwise insert when PK is 0 or
     *            update when PK more than 0 (maybe update fail)
     */
    public static void setExactInsertOrUpdate(boolean exactInsertOrUpdate) {
        Aorm.exactInsertOrUpdate = exactInsertOrUpdate;
    }
    
    /**
     * Get exactInsertOrUpdat
     * 
     * @return whether use actuarial insertOrUpdate strategy
     */
    static boolean getExactInsertOrUpdate() {
        return Aorm.exactInsertOrUpdate;
    }
    
    /**
     * Print log message on Android using {@link Log android.util.Log}
     * 
     * @param msg
     *            logging message.
     */
    public static void logv(String msg) {
        if (debug) {
            android.util.Log.v(TAG, msg);
        }
    }
}
