/**
 * 
 */
package cn.ieclipse.aorm.example;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import cn.ieclipse.aorm.Criteria;
import cn.ieclipse.aorm.Restrictions;
import cn.ieclipse.aorm.example.bean.Grade;
import cn.ieclipse.aorm.example.bean.Student;

/**
 * @author Jamling
 * 
 */
public class StudentDetailActivity extends DetailActivity {
    private Student student;
    
    private EditText etName;
    private EditText etPhone;
    private EditText etAge;
    private EditText etAddress;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        etIndex = (EditText) findViewById(R.id.et_no);
        etName = (EditText) findViewById(R.id.et_name);
        etPhone = (EditText) findViewById(R.id.et_phone);
        etAge = (EditText) findViewById(R.id.et_age);
        etAddress = (EditText) findViewById(R.id.et_address);
        
        session = ExampleContentProvider.getSession();
        student = session.get(Student.class, id);
        if (student != null) {
            etAddress.setText(student.getAddress());
            etName.setText(student.getName());
            etIndex.setText(String.valueOf(student.getId()));
            etAge.setText(String.valueOf(student.getAge()));
            etPhone.setText(String.valueOf(student.getPhone()));
        }
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see cn.ieclipse.aorm.example.DetailActivity#getLayout()
     */
    @Override
    protected int getLayout() {
        return R.layout.student_detail;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see cn.ieclipse.aorm.example.DetailActivity#delete()
     */
    @Override
    protected boolean delete() {
        int count = session.deleteById(Student.class, id);
        Criteria criteria = Criteria.create(Grade.class).add(
                Restrictions.eq("sid", id));
        int num = session.delete(criteria);
        String text = getString(R.string.delete_grade_info, num,
                student.getName());
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        return count > 0;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see cn.ieclipse.aorm.example.DetailActivity#edit()
     */
    @Override
    protected boolean edit() {
        boolean ret = false;
        if (student != null) {
            student.setAddress(etAddress.getText().toString());
            int age = 0;
            try {
                age = Integer.parseInt(etAge.getText().toString());
            } catch (NumberFormatException e) {
                age = 0;
            }
            student.setAge(age);
            student.setPhone(etPhone.getText().toString());
            student.setName(etName.getText().toString());
            int count = session.update(student);
            if (count > 0) {
                ret = true;
            }
        }
        return ret;
    }
    
    @Override
    protected boolean save() {
        boolean ret = false;
        Student student = new Student();
        student.setAddress(etAddress.getText().toString());
        int age = 0;
        try {
            age = Integer.parseInt(etAge.getText().toString());
        } catch (NumberFormatException e) {
            age = 0;
        }
        student.setAge(age);
        student.setPhone(etPhone.getText().toString());
        student.setName(etName.getText().toString());
        id = session.insert(student);
        if (id > 0) {
            ret = true;
            this.student = student;
        }
        return ret;
    }
}
