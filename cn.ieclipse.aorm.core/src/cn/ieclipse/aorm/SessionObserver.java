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

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

/**
 * @author Jamling
 * 
 */
class SessionObserver extends ContentObserver {
    
    private Session session;
    
    public SessionObserver(Handler handler, Session session) {
        super(handler);
        this.session = session;
    }
    
    @Override
    public void onChange(boolean selfChange) {
        session.onChange(selfChange, null);
    }
    
    @Override
    public void onChange(boolean selfChange, Uri uri) {
        session.onChange(selfChange, uri);
    }
}
