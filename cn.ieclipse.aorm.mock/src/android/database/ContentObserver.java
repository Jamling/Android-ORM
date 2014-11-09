/*
 * Copyright (C) 2007 The Android Open Source Project
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

package android.database;

import android.net.Uri;
import android.os.Handler;

/**
 * Receives call backs for changes to content. Must be implemented by objects
 * which are added to a {@link ContentObservable}.
 */
public abstract class ContentObserver {
    private final Object mLock = new Object();
    
    Handler mHandler;
    
    /**
     * Creates a content observer.
     * 
     * @param handler
     *            The handler to run {@link #onChange} on, or null if none.
     */
    public ContentObserver(Handler handler) {
        mHandler = handler;
    }
    
    /**
     * This method is called when a content change occurs.
     * <p>
     * Subclasses should override this method to handle content changes.
     * </p>
     * 
     * @param selfChange
     *            True if this is a self-change notification.
     */
    public void onChange(boolean selfChange) {
        // Do nothing. Subclass should override.
    }
    
    /**
     * This method is called when a content change occurs. Includes the changed
     * content Uri when available.
     * <p>
     * Subclasses should override this method to handle content changes. To
     * ensure correct operation on older versions of the framework that did not
     * provide a Uri argument, applications should also implement the
     * {@link #onChange(boolean)} overload of this method whenever they
     * implement the {@link #onChange(boolean, Uri)} overload.
     * </p>
     * <p>
     * Example implementation:
     * 
     * <pre>
     * <code>
     * // Implement the onChange(boolean) method to delegate the change notification to
     * // the onChange(boolean, Uri) method to ensure correct operation on older versions
     * // of the framework that did not have the onChange(boolean, Uri) method.
     * {@literal @Override}
     * public void onChange(boolean selfChange) {
     *     onChange(selfChange, null);
     * }
     * 
     * // Implement the onChange(boolean, Uri) method to take advantage of the new Uri argument.
     * {@literal @Override}
     * public void onChange(boolean selfChange, Uri uri) {
     *     // Handle change.
     * }
     * </code>
     * </pre>
     * 
     * </p>
     * 
     * @param selfChange
     *            True if this is a self-change notification.
     * @param uri
     *            The Uri of the changed content, or null if unknown.
     */
    public void onChange(boolean selfChange, Uri uri) {
        onChange(selfChange);
    }
    
}
