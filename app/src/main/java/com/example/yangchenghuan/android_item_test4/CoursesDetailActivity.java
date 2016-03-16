package com.example.yangchenghuan.android_item_test4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class CoursesDetailActivity extends Activity {
    private TextView course;
    private TextView Room;
    private TextView studentclass;
    private TextView teacher;
    private TextView time;
    private TextView datas;
    private Button returnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses_detail);
        course = (TextView) this.findViewById(R.id.studentCourse);
        Room = (TextView) this.findViewById(R.id.studentRoom);
        studentclass = (TextView) this.findViewById(R.id.studentClass);
        teacher = (TextView) this.findViewById(R.id.studentTeacher);
        time = (TextView) this.findViewById(R.id.studentTime);
        datas = (TextView) this.findViewById(R.id.studentData);
        returnButton =(Button)this.findViewById(R.id.return1);
        returnButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CoursesDetailActivity.this,CoursesActivity.class);
                startActivity(intent);
                CoursesDetailActivity.this.finish();
            }
        });
        setData();
    }

    private void setData() {
        String data = getIntent().getExtras().getString("data");
        // Log.d("data", data);
        JSONObject object = null;
        try {
            object = new JSONObject(data);
            course.setText(object.getString("courseName"));
            Room.setText(object.getString("classRoom"));
            teacher.setText(object.getString("teacherName"));
            studentclass.setText(object.getString("className"));
            time.setText(object.getString("startTime"));
            datas.setText(object.getString("week").replace("[", "")
                    .replace("]", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
