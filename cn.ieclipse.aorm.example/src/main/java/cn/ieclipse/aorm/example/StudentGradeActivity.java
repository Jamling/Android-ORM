/**
 * 
 */
package cn.ieclipse.aorm.example;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TextView;
import cn.ieclipse.aorm.Criteria;
import cn.ieclipse.aorm.Restrictions;
import cn.ieclipse.aorm.example.bean.Course;
import cn.ieclipse.aorm.example.bean.Grade;
import cn.ieclipse.aorm.example.bean.Student;

/**
 * @author Jamling
 * 
 */
public class StudentGradeActivity extends ListActivity {
    
    private long sid = 0;
    private StudentAdapter adapter;
    
    @Override
    protected void initIntent(Intent intent, Bundle savedInstanceState) {
        super.initIntent(intent, savedInstanceState);
        sid = intent.getLongExtra(Intent.EXTRA_UID, 0);
    }
    
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
        Object[] o = adapter.getItem(position);
        Grade g = (Grade) o[1];
        Intent intent = new Intent(this, StudentGradeDetailActivity.class);
        intent.putExtra(Intent.EXTRA_UID, g.getId());
        startActivity(intent);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see cn.ieclipse.aorm.example.ListActivity#add()
     */
    @Override
    protected void add() {
        Intent intent = new Intent(this, StudentGradeDetailActivity.class);
        intent.putExtra("sid", sid);
        startActivity(intent);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see cn.ieclipse.aorm.example.ListActivity#initAdapter()
     */
    @Override
    protected ListAdapter initAdapter() {
        if (adapter == null) {
            adapter = new StudentAdapter(this, null);
        }
        return adapter;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see cn.ieclipse.aorm.example.ListActivity#onUpdateAdapter()
     */
    @Override
    protected ListAdapter onUpdateAdapter() {
        Cursor c = ExampleContentProvider.getSession().query(
                Criteria.create(Grade.class).add(Restrictions.eq("sid", sid)));
        int len = c.getColumnCount();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            for (int i = 0; i < len; i++) {
                System.out.println(c.getColumnName(i) + "=" + c.getString(i));
            }
        }
        Criteria criteria = Criteria
                .create(Student.class, "s")
                .addChild(Grade.class, "g", Criteria.LEFT_JOIN,
                        Restrictions.eqProperty("s.id", "g.sid"))
                .setProjection(true)
                .addChild(Course.class, "c", Criteria.LEFT_JOIN,
                        Restrictions.eqProperty("g.cid", "c.id"))
                .setProjection(true).add(Restrictions.eq("g.sid", sid));
        
        List<Object[]> list = ExampleContentProvider.getSession().listAll(
                criteria);
        adapter.setDataList(list);
        adapter.notifyDataSetChanged();
        return adapter;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see cn.ieclipse.aorm.example.BaseActivity#getLayout()
     */
    @Override
    protected int getLayout() {
        return R.layout.student_grade_activity;
    }
    
    private class StudentAdapter extends AbstractBaseAdapter<Object[]> {
        
        public StudentAdapter(Context context, List<Object[]> dataList) {
            super(context, dataList);
        }
        
        @Override
        protected int getLayout() {
            return R.layout.student_grade_list_item;
        }
        
        @Override
        protected void onUpdateView(View view, int position) {
            Object[] o = getItem(position);
            TextView tvIndex = (TextView) view.findViewById(R.id.tv_index);
            TextView tvName = (TextView) view.findViewById(R.id.tv_name);
            TextView tvCourse = (TextView) view.findViewById(R.id.tv_course);
            TextView tvScore = (TextView) view.findViewById(R.id.tv_score);
            TextView tvPass = (TextView) view.findViewById(R.id.tv_pass);
            // TextView tvAddress = (TextView)
            // view.findViewById(R.id.tv_address);
            Grade g = (Grade) o[1];
            Student s = (Student) o[0];
            Course c = (Course) o[2];
            
            tvIndex.setText(String.valueOf(g.getId()));
            tvName.setText(String.valueOf(s.getName()));
            tvCourse.setText(String.valueOf(c.getName()));
            tvScore.setText(String.valueOf(g.getScore()));
            tvPass.setText(mContext.getResources().getString(
                    g.isPass() ? R.string.pass_ok : R.string.pass_not));
        }
    }
}
