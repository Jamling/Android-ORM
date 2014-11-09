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

package android.database.sqlite;

import java.sql.SQLException;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Exposes methods to manage a SQLite database.
 * 
 * <p>
 * SQLiteDatabase has methods to create, delete, execute SQL commands, and
 * perform other common database management tasks.
 * </p>
 * <p>
 * See the Notepad sample application in the SDK for an example of creating and
 * managing a database.
 * </p>
 * <p>
 * Database names must be unique within an application, not across all
 * applications.
 * </p>
 * 
 * <h3>Localized Collation - ORDER BY</h3>
 * <p>
 * In addition to SQLite's default <code>BINARY</code> collator, Android
 * supplies two more, <code>LOCALIZED</code>, which changes with the system's
 * current locale, and <code>UNICODE</code>, which is the Unicode Collation
 * Algorithm and not tailored to the current locale.
 * </p>
 */
public abstract class SQLiteDatabase {
    private static final String TAG = "SQLiteDatabase";
    
    public SQLiteDatabase(String path) {
        
    }
    
    /**
     * Begins a transaction in EXCLUSIVE mode.
     * <p>
     * Transactions can be nested. When the outer transaction is ended all of
     * the work done in that transaction and all of the nested transactions will
     * be committed or rolled back. The changes will be rolled back if any
     * transaction is ended without being marked as clean (by calling
     * setTransactionSuccessful). Otherwise they will be committed.
     * </p>
     * <p>
     * Here is the standard idiom for transactions:
     * 
     * <pre>
     *   db.beginTransaction();
     *   try {
     *     ...
     *     db.setTransactionSuccessful();
     *   } finally {
     *     db.endTransaction();
     *   }
     * </pre>
     */
    public void beginTransaction() {
        
    }
    
    /**
     * Begins a transaction in IMMEDIATE mode. Transactions can be nested. When
     * the outer transaction is ended all of the work done in that transaction
     * and all of the nested transactions will be committed or rolled back. The
     * changes will be rolled back if any transaction is ended without being
     * marked as clean (by calling setTransactionSuccessful). Otherwise they
     * will be committed.
     * <p>
     * Here is the standard idiom for transactions:
     * 
     * <pre>
     *   db.beginTransactionNonExclusive();
     *   try {
     *     ...
     *     db.setTransactionSuccessful();
     *   } finally {
     *     db.endTransaction();
     *   }
     * </pre>
     */
    public void beginTransactionNonExclusive() {
        
    }
    
    /**
     * End a transaction. See beginTransaction for notes about how to use this
     * and when transactions are committed and rolled back.
     */
    public void endTransaction() {
    }
    
    /**
     * Query the given URL, returning a {@link Cursor} over the result set.
     * 
     * @param distinct
     *            true if you want each row to be unique, false otherwise.
     * @param table
     *            The table name to compile the query against.
     * @param columns
     *            A list of which columns to return. Passing null will return
     *            all columns, which is discouraged to prevent reading data from
     *            storage that isn't going to be used.
     * @param selection
     *            A filter declaring which rows to return, formatted as an SQL
     *            WHERE clause (excluding the WHERE itself). Passing null will
     *            return all rows for the given table.
     * @param selectionArgs
     *            You may include ?s in selection, which will be replaced by the
     *            values from selectionArgs, in order that they appear in the
     *            selection. The values will be bound as Strings.
     * @param groupBy
     *            A filter declaring how to group rows, formatted as an SQL
     *            GROUP BY clause (excluding the GROUP BY itself). Passing null
     *            will cause the rows to not be grouped.
     * @param having
     *            A filter declare which row groups to include in the cursor, if
     *            row grouping is being used, formatted as an SQL HAVING clause
     *            (excluding the HAVING itself). Passing null will cause all row
     *            groups to be included, and is required when row grouping is
     *            not being used.
     * @param orderBy
     *            How to order the rows, formatted as an SQL ORDER BY clause
     *            (excluding the ORDER BY itself). Passing null will use the
     *            default sort order, which may be unordered.
     * @param limit
     *            Limits the number of rows returned by the query, formatted as
     *            LIMIT clause. Passing null denotes no LIMIT clause.
     * @return A {@link Cursor} object, which is positioned before the first
     *         entry. Note that {@link Cursor}s are not synchronized, see the
     *         documentation for more details.
     * @see Cursor
     */
    public abstract Cursor query(boolean distinct, String table,
            String[] columns, String selection, String[] selectionArgs,
            String groupBy, String having, String orderBy, String limit);
    
    /**
     * Query the given table, returning a {@link Cursor} over the result set.
     * 
     * @param table
     *            The table name to compile the query against.
     * @param columns
     *            A list of which columns to return. Passing null will return
     *            all columns, which is discouraged to prevent reading data from
     *            storage that isn't going to be used.
     * @param selection
     *            A filter declaring which rows to return, formatted as an SQL
     *            WHERE clause (excluding the WHERE itself). Passing null will
     *            return all rows for the given table.
     * @param selectionArgs
     *            You may include ?s in selection, which will be replaced by the
     *            values from selectionArgs, in order that they appear in the
     *            selection. The values will be bound as Strings.
     * @param groupBy
     *            A filter declaring how to group rows, formatted as an SQL
     *            GROUP BY clause (excluding the GROUP BY itself). Passing null
     *            will cause the rows to not be grouped.
     * @param having
     *            A filter declare which row groups to include in the cursor, if
     *            row grouping is being used, formatted as an SQL HAVING clause
     *            (excluding the HAVING itself). Passing null will cause all row
     *            groups to be included, and is required when row grouping is
     *            not being used.
     * @param orderBy
     *            How to order the rows, formatted as an SQL ORDER BY clause
     *            (excluding the ORDER BY itself). Passing null will use the
     *            default sort order, which may be unordered.
     * @return A {@link Cursor} object, which is positioned before the first
     *         entry. Note that {@link Cursor}s are not synchronized, see the
     *         documentation for more details.
     * @see Cursor
     */
    public Cursor query(String table, String[] columns, String selection,
            String[] selectionArgs, String groupBy, String having,
            String orderBy) {
        
        return query(false, table, columns, selection, selectionArgs, groupBy,
                having, orderBy, null /* limit */);
    }
    
    /**
     * Query the given table, returning a {@link Cursor} over the result set.
     * 
     * @param table
     *            The table name to compile the query against.
     * @param columns
     *            A list of which columns to return. Passing null will return
     *            all columns, which is discouraged to prevent reading data from
     *            storage that isn't going to be used.
     * @param selection
     *            A filter declaring which rows to return, formatted as an SQL
     *            WHERE clause (excluding the WHERE itself). Passing null will
     *            return all rows for the given table.
     * @param selectionArgs
     *            You may include ?s in selection, which will be replaced by the
     *            values from selectionArgs, in order that they appear in the
     *            selection. The values will be bound as Strings.
     * @param groupBy
     *            A filter declaring how to group rows, formatted as an SQL
     *            GROUP BY clause (excluding the GROUP BY itself). Passing null
     *            will cause the rows to not be grouped.
     * @param having
     *            A filter declare which row groups to include in the cursor, if
     *            row grouping is being used, formatted as an SQL HAVING clause
     *            (excluding the HAVING itself). Passing null will cause all row
     *            groups to be included, and is required when row grouping is
     *            not being used.
     * @param orderBy
     *            How to order the rows, formatted as an SQL ORDER BY clause
     *            (excluding the ORDER BY itself). Passing null will use the
     *            default sort order, which may be unordered.
     * @param limit
     *            Limits the number of rows returned by the query, formatted as
     *            LIMIT clause. Passing null denotes no LIMIT clause.
     * @return A {@link Cursor} object, which is positioned before the first
     *         entry. Note that {@link Cursor}s are not synchronized, see the
     *         documentation for more details.
     * @see Cursor
     */
    public Cursor query(String table, String[] columns, String selection,
            String[] selectionArgs, String groupBy, String having,
            String orderBy, String limit) {
        
        return query(false, table, columns, selection, selectionArgs, groupBy,
                having, orderBy, limit);
    }
    
    /**
     * Runs the provided SQL and returns a {@link Cursor} over the result set.
     * 
     * @param sql
     *            the SQL query. The SQL string must not be ; terminated
     * @param selectionArgs
     *            You may include ?s in where clause in the query, which will be
     *            replaced by the values from selectionArgs. The values will be
     *            bound as Strings.
     * @return A {@link Cursor} object, which is positioned before the first
     *         entry. Note that {@link Cursor}s are not synchronized, see the
     *         documentation for more details.
     */
    public abstract Cursor rawQuery(String sql, String[] selectionArgs);
    
    /**
     * Convenience method for inserting a row into the database.
     * 
     * @param table
     *            the table to insert the row into
     * @param nullColumnHack
     *            optional; may be <code>null</code>. SQL doesn't allow
     *            inserting a completely empty row without naming at least one
     *            column name. If your provided <code>values</code> is empty, no
     *            column names are known and an empty row can't be inserted. If
     *            not set to null, the <code>nullColumnHack</code> parameter
     *            provides the name of nullable column name to explicitly insert
     *            a NULL into in the case where your <code>values</code> is
     *            empty.
     * @param values
     *            this map contains the initial column values for the row. The
     *            keys should be the column names and the values the column
     *            values
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    public abstract long insert(String table, String nullColumnHack,
            ContentValues values);
    
    /**
     * Convenience method for deleting rows in the database.
     * 
     * @param table
     *            the table to delete from
     * @param whereClause
     *            the optional WHERE clause to apply when deleting. Passing null
     *            will delete all rows.
     * @param whereArgs
     *            You may include ?s in the where clause, which will be replaced
     *            by the values from whereArgs. The values will be bound as
     *            Strings.
     * @return the number of rows affected if a whereClause is passed in, 0
     *         otherwise. To remove all rows and get a count pass "1" as the
     *         whereClause.
     */
    public abstract int delete(String table, String whereClause,
            String[] whereArgs);
    
    /**
     * Convenience method for updating rows in the database.
     * 
     * @param table
     *            the table to update in
     * @param values
     *            a map from column names to new column values. null is a valid
     *            value that will be translated to NULL.
     * @param whereClause
     *            the optional WHERE clause to apply when updating. Passing null
     *            will update all rows.
     * @param whereArgs
     *            You may include ?s in the where clause, which will be replaced
     *            by the values from whereArgs. The values will be bound as
     *            Strings.
     * @return the number of rows affected
     */
    public abstract int update(String table, ContentValues values,
            String whereClause, String[] whereArgs);
    
    /**
     * Execute a single SQL statement that is NOT a SELECT or any other SQL
     * statement that returns data.
     * <p>
     * It has no means to return any data (such as the number of affected rows).
     * Instead, you're encouraged to use
     * {@link #insert(String, String, ContentValues)},
     * {@link #update(String, ContentValues, String, String[])}, et al, when
     * possible.
     * </p>
     * <p>
     * When using {@link #enableWriteAheadLogging()}, journal_mode is
     * automatically managed by this class. So, do not set journal_mode using
     * "PRAGMA journal_mode'<value>" statement if your app is using
     * {@link #enableWriteAheadLogging()}
     * </p>
     * 
     * @param sql
     *            the SQL statement to be executed. Multiple statements
     *            separated by semicolons are not supported.
     * @throws SQLException
     *             if the SQL string is invalid
     */
    public void execSQL(String sql) {
        
    }
    
    /**
     * Execute a single SQL statement that is NOT a SELECT/INSERT/UPDATE/DELETE.
     * <p>
     * For INSERT statements, use any of the following instead.
     * <ul>
     * <li>{@link #insert(String, String, ContentValues)}</li>
     * <li>{@link #insertOrThrow(String, String, ContentValues)}</li>
     * <li>{@link #insertWithOnConflict(String, String, ContentValues, int)}</li>
     * </ul>
     * <p>
     * For UPDATE statements, use any of the following instead.
     * <ul>
     * <li>{@link #update(String, ContentValues, String, String[])}</li>
     * <li>
     * {@link #updateWithOnConflict(String, ContentValues, String, String[], int)}
     * </li>
     * </ul>
     * <p>
     * For DELETE statements, use any of the following instead.
     * <ul>
     * <li>{@link #delete(String, String, String[])}</li>
     * </ul>
     * <p>
     * For example, the following are good candidates for using this method:
     * <ul>
     * <li>ALTER TABLE</li>
     * <li>CREATE or DROP table / trigger / view / index / virtual table</li>
     * <li>REINDEX</li>
     * <li>RELEASE</li>
     * <li>SAVEPOINT</li>
     * <li>PRAGMA that returns no data</li>
     * </ul>
     * </p>
     * <p>
     * When using {@link #enableWriteAheadLogging()}, journal_mode is
     * automatically managed by this class. So, do not set journal_mode using
     * "PRAGMA journal_mode'<value>" statement if your app is using
     * {@link #enableWriteAheadLogging()}
     * </p>
     * 
     * @param sql
     *            the SQL statement to be executed. Multiple statements
     *            separated by semicolons are not supported.
     * @param bindArgs
     *            only byte[], String, Long and Double are supported in
     *            bindArgs.
     * @throws SQLException
     *             if the SQL string is invalid
     */
    public void execSQL(String sql, Object[] bindArgs) {
        
    }
}
