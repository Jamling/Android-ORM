/**
 * 
 */
package cn.ieclipse.aorm.example;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TextView;
import cn.ieclipse.aorm.example.bean.Course;

/**
 * @author Jamling
 * 
 */
public class CourseListActivity extends ListActivity {
    
    private CourseAdapter adapter;
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        Course s = adapter.getItem(position);
        Intent intent = new Intent(this, CourseDetailActivity.class);
        intent.putExtra(Intent.EXTRA_UID, s.getId());
        startActivity(intent);
    }
    
    @Override
    protected void add() {
        Intent intent = new Intent(this, CourseDetailActivity.class);
        startActivity(intent);
    }
    
    @Override
    protected ListAdapter initAdapter() {
        if (adapter == null) {
            adapter = new CourseAdapter(this, null);
        }
        return adapter;
    }
    
    @Override
    protected ListAdapter onUpdateAdapter() {
        List<Course> list = ExampleContentProvider.getSession().list(
                Course.class);
        adapter.setDataList(list);
        adapter.notifyDataSetChanged();
        return adapter;
    }
    
    @Override
    protected int getLayout() {
        return R.layout.course_activity;
    }
    
    private static class CourseAdapter extends AbstractBaseAdapter<Course> {
        
        public CourseAdapter(Context context, List<Course> dataList) {
            super(context, dataList);
        }
        
        @Override
        protected int getLayout() {
            return R.layout.course_list_item;
        }
        
        @Override
        protected void onUpdateView(View view, int position) {
            Course s = getItem(position);
            TextView tvIndex = (TextView) view.findViewById(R.id.tv_index);
            TextView tvName = (TextView) view.findViewById(R.id.tv_name);
            
            tvIndex.setText(String.valueOf(s.getId()));
            tvName.setText(String.valueOf(s.getName()));
        }
        
    }
    
}
