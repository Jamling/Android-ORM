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
package cn.ieclipse.aorm.test;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;

/**
 * @author Jamling
 *         
 */
public class MockCursor implements Cursor {
    private String[] mColumns;
    private int rowIdx = 0;
    private ArrayList<Object[]> data = new ArrayList<Object[]>();
    
    public MockCursor(String[] colomns) {
        this.mColumns = colomns;
    }
    
    public void addRow(Object[] rowData) {
        data.add(rowData);
    }
    
    public void setRowIndex(int j) {
        if (j >= 0 && j < getRowSize()) {
            this.rowIdx = j;
        }
    }
    
    // public void insertRow(Object[] rowData) {
    // int pkv = getCount();
    //
    // }
    
    public int getRowSize() {
        return data == null ? 0 : data.size();
    }
    
    public int getColumnCount() {
        return mColumns.length;
    }
    
    public String[] getColumnNames() {
        return mColumns;
    }
    
    public int getColumnIndex(String columnName) {
        for (int i = 0; i < mColumns.length; i++) {
            if (columnName.equals(mColumns[i])) {
                return i;
            }
        }
        return -1;
    }
    
    public boolean moveToFirst() {
        rowIdx = 0;
        if (getRowSize() > 0) {
            return true;
        }
        else {
            return false;
        }
    }
    
    public boolean isAfterLast() {
        return rowIdx + 1 > data.size();
    }
    
    public boolean moveToNext() {
        if (isAfterLast()) {
            return false;
        }
        rowIdx++;
        return true;
    }
    
    public byte[] getBlob(int columnIndex) {
        Object[] obj = data.get(rowIdx);
        return (byte[]) obj[columnIndex];
    }
    
    public double getDouble(int columnIndex) {
        Object[] obj = data.get(rowIdx);
        return Double.valueOf(obj[columnIndex].toString());
    }
    
    public float getFloat(int columnIndex) {
        Object[] obj = data.get(rowIdx);
        return Float.valueOf(obj[columnIndex].toString());
    }
    
    public int getInt(int columnIndex) {
        Object[] obj = data.get(rowIdx);
        return Integer.valueOf(obj[columnIndex].toString());
    }
    
    public long getLong(int columnIndex) {
        Object[] obj = data.get(rowIdx);
        return Long.parseLong(obj[columnIndex].toString());
    }
    
    public short getShort(int columnIndex) {
        Object[] obj = data.get(rowIdx);
        return Short.valueOf(obj[columnIndex].toString());
    }
    
    public String getString(int columnIndex) {
        Object[] obj = data.get(rowIdx);
        return obj[columnIndex] == null ? null : obj[columnIndex].toString();
    }
    
    public int getCount() {
        return data == null ? 0 : data.size();
    }
    
    public int getPosition() {
        return rowIdx;
    }
    
    public boolean move(int offset) {
        if (offset < getCount()) {
            rowIdx = offset;
            return true;
        }
        return false;
    }
    
    public boolean moveToPosition(int position) {
        if (position < getCount()) {
            rowIdx = position;
            return true;
        }
        return false;
    }
    
    public boolean moveToLast() {
        if (rowIdx < getCount()) {
            rowIdx = getCount() - 1;
            return true;
        }
        return false;
    }
    
    public boolean moveToPrevious() {
        if (rowIdx > 0) {
            rowIdx--;
            return true;
        }
        return false;
    }
    
    public boolean isFirst() {
        if (rowIdx == 0) {
            return true;
        }
        return false;
    }
    
    public boolean isLast() {
        if (rowIdx == getCount() - 1) {
            return true;
        }
        return false;
    }
    
    public boolean isBeforeFirst() {
        if (rowIdx < 0) {
            return true;
        }
        return false;
    }
    
    public int getColumnIndexOrThrow(String columnName)
            throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public String getColumnName(int columnIndex) {
        return mColumns[columnIndex];
    }
    
    public boolean isNull(int columnIndex) {
        return data.get(rowIdx)[columnIndex] == null;
    }
    
    public void deactivate() {
        // TODO Auto-generated method stub
        
    }
    
    public boolean requery() {
        // TODO Auto-generated method stub
        return false;
    }
    
    public void close() {
        // TODO Auto-generated method stub
        
    }
    
    public boolean isClosed() {
        // TODO Auto-generated method stub
        return false;
    }
    
    public void registerContentObserver(ContentObserver observer) {
        // TODO Auto-generated method stub
        
    }
    
    public void unregisterContentObserver(ContentObserver observer) {
        // TODO Auto-generated method stub
        
    }
    
    public void setNotificationUri(ContentResolver cr, Uri uri) {
        // TODO Auto-generated method stub
        
    }
    
    public boolean getWantsAllOnMoveCalls() {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public int getType(int columnIndex) {
        // TODO Auto-generated method stub
        return 0;
    }
    
    @Override
    public Uri getNotificationUri() {
        // TODO Auto-generated method stub
        return null;
    }
}
