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

package android.content;

import android.database.ContentObserver;
import android.net.Uri;

/**
 * This class provides applications access to the content model.
 * 
 * <div class="special reference"> <h3>Developer Guides</h3>
 * <p>
 * For more information about using a ContentResolver with content providers,
 * read the <a href="{@docRoot}
 * guide/topics/providers/content-providers.html">Content Providers</a>
 * developer guide.
 * </p>
 */
public abstract class ContentResolver {
    /**
     * Register an observer class that gets callbacks when data identified by a
     * given content URI changes.
     * 
     * @param uri
     *            The URI to watch for changes. This can be a specific row URI,
     *            or a base URI for a whole class of content.
     * @param notifyForDescendents
     *            If <code>true</code> changes to URIs beginning with
     *            <code>uri</code> will also cause notifications to be sent. If
     *            <code>false</code> only changes to the exact URI specified by
     *            <em>uri</em> will cause notifications to be sent. If true,
     *            than any URI values at or below the specified URI will also
     *            trigger a match.
     * @param observer
     *            The object that receives callbacks when changes occur.
     * @see #unregisterContentObserver
     */
    public final void registerContentObserver(Uri uri,
            boolean notifyForDescendents, ContentObserver observer) {
        
    }
    
    /**
     * Unregisters a change observer.
     * 
     * @param observer
     *            The previously registered observer that is no longer needed.
     * @see #registerContentObserver
     */
    public final void unregisterContentObserver(ContentObserver observer) {
    }
    
    /**
     * Notify registered observers that a row was updated and attempt to sync changes
     * to the network.
     * To register, call {@link #registerContentObserver(android.net.Uri , boolean, android.database.ContentObserver) registerContentObserver()}.
     * By default, CursorAdapter objects will get this notification.
     *
     * @param uri The uri of the content that was changed.
     * @param observer The observer that originated the change, may be <code>null</null>.
     * The observer that originated the change will only receive the notification if it
     * has requested to receive self-change notifications by implementing
     * {@link ContentObserver#deliverSelfNotifications()} to return true.
     */
    public void notifyChange(Uri uri, ContentObserver observer) {
        notifyChange(uri, observer, true /* sync to network */);
    }

    /**
     * Notify registered observers that a row was updated.
     * To register, call {@link #registerContentObserver(android.net.Uri , boolean, android.database.ContentObserver) registerContentObserver()}.
     * By default, CursorAdapter objects will get this notification.
     * If syncToNetwork is true, this will attempt to schedule a local sync using the sync
     * adapter that's registered for the authority of the provided uri. No account will be
     * passed to the sync adapter, so all matching accounts will be synchronized.
     *
     * @param uri The uri of the content that was changed.
     * @param observer The observer that originated the change, may be <code>null</null>.
     * The observer that originated the change will only receive the notification if it
     * has requested to receive self-change notifications by implementing
     * {@link ContentObserver#deliverSelfNotifications()} to return true.
     * @param syncToNetwork If true, attempt to sync the change to the network.
     * @see #requestSync(android.accounts.Account, String, android.os.Bundle)
     */
    public void notifyChange(Uri uri, ContentObserver observer, boolean syncToNetwork) {
        // notifyChange(uri, observer, syncToNetwork, UserHandle.myUserId());
    }
}
