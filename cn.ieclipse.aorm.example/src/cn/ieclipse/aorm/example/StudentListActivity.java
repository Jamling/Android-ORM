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
import cn.ieclipse.aorm.Criteria;
import cn.ieclipse.aorm.Order;
import cn.ieclipse.aorm.example.bean.Student;

/**
 * @author Jamling
 * 
 */
public class StudentListActivity extends ListActivity {
    
    private StudentAdapter adapter;
    private int offset = 0;
    private int pageCount = 10;
    
    private View btnPrev;
    private View btnNext;
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
     * .AdapterView, android.view.View, int, long)
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        Student s = adapter.getItem(position);
        Intent intent = new Intent(this, StudentDetailActivity.class);
        intent.putExtra(Intent.EXTRA_UID, s.getId());
        startActivity(intent);
    }
    
    @Override
    public void onClick(View v) {
        if (R.id.btn_next == v.getId()) {
            offset = offset + pageCount;
            onUpdateAdapter();
        }
        else if (R.id.btn_prev == v.getId()) {
            offset = offset - pageCount;
            onUpdateAdapter();
        }
        else if (R.id.btn_view == v.getId()) {
            Student s = (Student) v.getTag();
            if (s != null) {
                Intent intent = new Intent(this, StudentGradeActivity.class);
                intent.putExtra(Intent.EXTRA_UID, s.getId());
                startActivity(intent);
            }
        }
    }
    
    @Override
    protected int getLayout() {
        return R.layout.student_activity;
    }
    
    @Override
    protected void add() {
        Intent intent = new Intent(this, StudentDetailActivity.class);
        startActivity(intent);
    }
    
    @Override
    protected ListAdapter initAdapter() {
        if (adapter == null) {
            adapter = new StudentAdapter(this, null);
        }
        return adapter;
    }
    
    @Override
    protected ListAdapter onUpdateAdapter() {
        Criteria criteria = Criteria.create(Student.class);
        int total = ExampleContentProvider.getSession().count(criteria);
        int size = Math.min(pageCount, total - offset);
        TextView tv = (TextView) findViewById(R.id.tv_total);
        tv.setText(getString(R.string.page_info, offset, offset + size, total));
        
        if (size == pageCount && total > offset + size) {
            findViewById(R.id.btn_next).setVisibility(View.VISIBLE);
        }
        else {
            findViewById(R.id.btn_next).setVisibility(View.INVISIBLE);
        }
        
        if (offset > pageCount) {
            findViewById(R.id.btn_prev).setVisibility(View.VISIBLE);
        }
        else {
            findViewById(R.id.btn_prev).setVisibility(View.INVISIBLE);
        }
        
        criteria.addOrder(Order.desc("id"));
        criteria.setLimit(offset, size);
        
        List<Student> list = ExampleContentProvider.getSession().list(
                Student.class);
        adapter.setDataList(list);
        adapter.notifyDataSetChanged();
        return adapter;
    }
    
    private class StudentAdapter extends AbstractBaseAdapter<Student> {
        
        public StudentAdapter(Context context, List<Student> dataList) {
            super(context, dataList);
        }
        
        @Override
        protected int getLayout() {
            return R.layout.student_list_item;
        }
        
        @Override
        protected void onUpdateView(View view, int position) {
            Student s = getItem(position);
            TextView tvIndex = (TextView) view.findViewById(R.id.tv_index);
            TextView tvName = (TextView) view.findViewById(R.id.tv_name);
            TextView tvPhone = (TextView) view.findViewById(R.id.tv_phone);
            TextView tvAge = (TextView) view.findViewById(R.id.tv_age);
            View btn = view.findViewById(R.id.btn_view);
            btn.setOnClickListener(StudentListActivity.this);
            btn.setTag(s);
            // TextView tvAddress = (TextView)
            // view.findViewById(R.id.tv_address);
            tvIndex.setText(String.valueOf(s.getId()));
            tvName.setText(String.valueOf(s.getName()));
            tvPhone.setText(String.valueOf(s.getPhone()));
            tvAge.setText(String.valueOf(s.getAge()));
        }
        
    }
}
