package cn.ieclipse.aorm.example;

import android.content.Intent;
import android.view.View;

public class MainActivity extends BaseActivity {
    
    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }
    
    @Override
    public void onClick(View v) {
        if (R.id.btn_student_mgr == v.getId()) {
            Intent intent = new Intent(this, StudentListActivity.class);
            startActivity(intent);
        }
        else if (R.id.btn_course_mgr == v.getId()) {
            Intent intent = new Intent(this, CourseListActivity.class);
            startActivity(intent);
        }
    }
}
