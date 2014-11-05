/**
 * 
 */
package cn.ieclipse.aorm.example;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import cn.ieclipse.aorm.example.bean.Course;
import cn.ieclipse.aorm.example.bean.Grade;
import cn.ieclipse.aorm.example.bean.Student;

/**
 * @author Jamling
 * 
 */
public class StudentGradeDetailActivity extends DetailActivity {
    
    private Student student;
    private Grade grade;
    private EditText etName;
    private Spinner spnCourse;
    private EditText etScore;
    private List<Course> courseList;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        etIndex = (EditText) findViewById(R.id.et_no);
        etName = (EditText) findViewById(R.id.et_name);
        etScore = (EditText) findViewById(R.id.et_score);
        spnCourse = (Spinner) findViewById(R.id.spn_course);
        session = ExampleContentProvider.getSession();
        courseList = session.list(Course.class);
        spnCourse.setAdapter(new CourseAdapter(this, courseList));
        
        if (student != null) {
            etName.setText(student.getName());
        }
        
        grade = session.get(Grade.class, id);
        if (grade != null) {
            int i = 0;
            for (Course c : courseList) {
                if (c.getId() == grade.getCid()) {
                    spnCourse.setSelection(i);
                    break;
                }
                i++;
            }
            etScore.setText(String.valueOf(grade.getScore()));
            
            student = (Student) session.get(Student.class, grade.getSid());
            etName.setText(student.getName());
        }
    }
    
    @Override
    protected void initIntent(Intent intent, Bundle savedInstanceState) {
        super.initIntent(intent, savedInstanceState);
        long sid = intent.getLongExtra("sid", 0);
        if (sid > 0) {
            student = ExampleContentProvider.getSession().get(Student.class,
                    sid);
        }
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see cn.ieclipse.aorm.example.DetailActivity#getLayout()
     */
    @Override
    protected int getLayout() {
        return R.layout.student_grade_detail;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see cn.ieclipse.aorm.example.DetailActivity#delete()
     */
    @Override
    protected boolean delete() {
        int count = session.deleteById(Grade.class, id);
        return count > 0;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see cn.ieclipse.aorm.example.DetailActivity#edit()
     */
    @Override
    protected boolean edit() {
        if (grade != null) {
            Course c = (Course) spnCourse.getSelectedItem();
            grade.setCid(c.getId());
            float score = 0f;
            try {
                score = Float.valueOf(etScore.getText().toString());
            } catch (Exception e) {
                
            }
            grade.setScore(score);
            int count = session.update(grade);
            return count > 0;
        }
        return false;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see cn.ieclipse.aorm.example.DetailActivity#save()
     */
    @Override
    protected boolean save() {
        Grade grade = new Grade();
        Course c = (Course) spnCourse.getSelectedItem();
        grade.setCid(c.getId());
        float score = 0f;
        try {
            score = Float.valueOf(etScore.getText().toString());
        } catch (Exception e) {
            
        }
        grade.setScore(score);
        grade.setSid(student.getId());
        id = session.insert(grade);
        if (id > 0) {
            this.grade = grade;
        }
        return id > 0;
    }
    
    private static class CourseAdapter extends AbstractBaseAdapter<Course> {
        
        public CourseAdapter(Context context, List<Course> dataList) {
            super(context, dataList);
        }
        
        @Override
        protected int getLayout() {
            return android.R.layout.simple_dropdown_item_1line;
        }
        
        @Override
        protected void onUpdateView(View view, int position) {
            Course s = getItem(position);
            TextView tvName = (TextView) view.findViewById(android.R.id.text1);
            tvName.setText(s.getName());
        }
        
    }
}
