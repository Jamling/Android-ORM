/**
 * 
 */
package cn.ieclipse.aorm.example;

import android.os.Bundle;
import android.widget.EditText;
import cn.ieclipse.aorm.example.bean.Course;

/**
 * @author Jamling
 * 
 */
public class CourseDetailActivity extends DetailActivity {
    
    private Course course;
    
    private EditText etName;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        etIndex = (EditText) findViewById(R.id.et_no);
        etName = (EditText) findViewById(R.id.et_name);
        session = ExampleContentProvider.getSession();
        course = (Course) session.get(Course.class, id);
        if (course != null) {
            etIndex.setText(String.valueOf(course.getId()));
            etName.setText(String.valueOf(course.getName()));
        }
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see cn.ieclipse.aorm.example.DetailActivity#getLayout()
     */
    @Override
    protected int getLayout() {
        return R.layout.course_detail;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see cn.ieclipse.aorm.example.DetailActivity#delete()
     */
    @Override
    protected boolean delete() {
        int count = session.deleteById(Course.class, id);
        return count > 0;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see cn.ieclipse.aorm.example.DetailActivity#edit()
     */
    @Override
    protected boolean edit() {
        if (course != null) {
            course.setName(etName.getText().toString());
            int count = session.update(course);
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
        Course course = new Course();
        course.setName(etName.getText().toString());
        id = session.insert(course);
        if (id > 0) {
            this.course = course;
        }
        return id > 0;
    }
    
}
