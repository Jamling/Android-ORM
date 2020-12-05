/*
 * Copyright 2013 E6Home inc. All rights reserved. 
 * created date: 2013年9月12日
 */
package cn.ieclipse.aorm.example;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 
 * @author Jamling
 * 
 */
public abstract class AbstractBaseAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected List<T> dataList;
    
    public AbstractBaseAdapter(Context context, List<T> dataList) {
        this.mContext = context;
        this.dataList = dataList;
    }
    
    public List<T> getDataList() {
        return dataList;
    }
    
    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
    
    @Override
    public int getCount() {
        return dataList == null ? 0 : dataList.size();
    }
    
    @Override
    public T getItem(int position) {
        return dataList.get(position);
    }
    
    @Override
    public long getItemId(int position) {
        return 0;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup root) {
        if (convertView == null) {
            convertView = View.inflate(mContext, getLayout(), null);
        }
        onUpdateView(convertView, position);
        return convertView;
    }
    
    protected abstract int getLayout();
    
    protected abstract void onUpdateView(View view, int position);
}
