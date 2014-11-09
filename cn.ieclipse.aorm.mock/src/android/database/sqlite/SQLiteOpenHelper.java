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

package android.database.sqlite;

/**
 * A helper class to manage database creation and version management.
 * 
 * <p>
 * You create a subclass implementing {@link #onCreate}, {@link #onUpgrade} and
 * optionally {@link #onOpen}, and this class takes care of opening the database
 * if it exists, creating it if it does not, and upgrading it as necessary.
 * Transactions are used to make sure the database is always in a sensible
 * state.
 * 
 * <p>
 * This class makes it easy for {@link android.content.ContentProvider}
 * implementations to defer opening and upgrading the database until first use,
 * to avoid blocking application startup with long-running database upgrades.
 * 
 * <p>
 * For an example, see the NotePadProvider class in the NotePad sample
 * application, in the <em>samples/</em> directory of the SDK.
 * </p>
 * 
 * <p class="note">
 * <strong>Note:</strong> this class assumes monotonically increasing version
 * numbers for upgrades.
 * </p>
 */
public abstract class SQLiteOpenHelper {
    private static final String TAG = SQLiteOpenHelper.class.getSimpleName();
    
    // When true, getReadableDatabase returns a read-only database if it is just
    // being opened.
    // The database handle is reopened in read/write mode when
    // getWritableDatabase is called.
    // We leave this behavior disabled in production because it is inefficient
    // and breaks
    // many applications. For debugging purposes it can be useful to turn on
    // strict
    // read-only semantics to catch applications that call getReadableDatabase
    // when they really
    // wanted getWritableDatabase.
    private static final boolean DEBUG_STRICT_READONLY = false;
    
    private final String mName;
    private final int mNewVersion;
    
    private SQLiteDatabase mDatabase;
    
    /**
     * Create a helper object to create, open, and/or manage a database. This
     * method always returns very quickly. The database is not actually created
     * or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     * 
     * @param context
     *            to use to open or create the database
     * @param name
     *            of the database file, or null for an in-memory database
     * @param factory
     *            to use for creating cursor objects, or null for the default
     * @param version
     *            number of the database (starting at 1); if the database is
     *            older, {@link #onUpgrade} will be used to upgrade the
     *            database; if the database is newer, {@link #onDowngrade} will
     *            be used to downgrade the database
     */
    public SQLiteOpenHelper(String name, int version) {
        this.mName = name;
        this.mNewVersion = version;
    }
    
    /**
     * Return the name of the SQLite database being opened, as given to the
     * constructor.
     */
    public String getDatabaseName() {
        return mName;
    }
    
    /**
     * Create and/or open a database that will be used for reading and writing.
     * The first time this is called, the database will be opened and
     * {@link #onCreate}, {@link #onUpgrade} and/or {@link #onOpen} will be
     * called.
     * 
     * <p>
     * Once opened successfully, the database is cached, so you can call this
     * method every time you need to write to the database. (Make sure to call
     * {@link #close} when you no longer need the database.) Errors such as bad
     * permissions or a full disk may cause this method to fail, but future
     * attempts may succeed if the problem is fixed.
     * </p>
     * 
     * <p class="caution">
     * Database upgrade may take a long time, you should not call this method
     * from the application main thread, including from
     * {@link android.content.ContentProvider#onCreate
     * ContentProvider.onCreate()}.
     * 
     * @throws SQLiteException
     *             if the database cannot be opened for writing
     * @return a read/write database object valid until {@link #close} is called
     */
    public SQLiteDatabase getWritableDatabase() {
        return null;
    }
    
    /**
     * Create and/or open a database. This will be the same object returned by
     * {@link #getWritableDatabase} unless some problem, such as a full disk,
     * requires the database to be opened read-only. In that case, a read-only
     * database object will be returned. If the problem is fixed, a future call
     * to {@link #getWritableDatabase} may succeed, in which case the read-only
     * database object will be closed and the read/write object will be returned
     * in the future.
     * 
     * <p class="caution">
     * Like {@link #getWritableDatabase}, this method may take a long time to
     * return, so you should not call it from the application main thread,
     * including from {@link android.content.ContentProvider#onCreate
     * ContentProvider.onCreate()}.
     * 
     * @throws SQLiteException
     *             if the database cannot be opened
     * @return a database object valid until {@link #getWritableDatabase} or
     *         {@link #close} is called.
     */
    public SQLiteDatabase getReadableDatabase() {
        return null;
    }
    
}
