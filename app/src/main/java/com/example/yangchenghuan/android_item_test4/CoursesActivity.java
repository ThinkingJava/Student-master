package com.example.yangchenghuan.android_item_test4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yangchenghuan.myAction.LoginAction;
import com.example.yangchenghuan.myUtils.StudentInfo;
import com.example.yangchenghuan.myUtils.UserUtil;
import com.example.yangchenghuan.mytools.DataParser;
import com.example.yangchenghuan.myuser.User;

public class CoursesActivity extends AppCompatActivity {
    private LinearLayout linearLayout = null;
    private JSONObject jsonObject = null;
    private JSONObject moreJsonObject = null;
    private TextView weekNum = null;
    private TextView settingTextView = null;
    private int weekNumI = 0;
    private String weekS;
    private String schoolyear;

    private void setWeekS() {
        if (weekNumI == 0) {
            weekS = "0";
        } else {
            weekS = new StringBuilder("0").append(weekNumI).append(",")
                    .toString();
        }
    }

    private static final int[] dayID = { R.id.day01, R.id.day02, R.id.day03,
            R.id.day04, R.id.day05, R.id.day06, R.id.day07 };

    private static final int[] textViewBackground = {
            R.drawable.textview_border_course_1,
            R.drawable.textview_border_course_2,
            R.drawable.textview_border_course_3,
            R.drawable.textview_border_course_4,
            R.drawable.textview_border_course_5,
            R.drawable.textview_border_course_6, };

    private void drawCourses() {
        for (int i = 0; i < 7; i++) {
            drawCourses(dayID[i], UserUtil.DAY_OF_WEEK[i]);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        settingTextView = (TextView) findViewById(R.id.set_button);
        setUser();
        initData();

        // 为标题栏的周数设定监听器，点击后可以进行自定义周数或者显示别的学期的课表...
        // weekNum.setOnClickListener(new OnClickListener() {
        // @Override
        // public void onClick(View v) {
        // Intent intent = new Intent(CoursesActivity.this,
        // SchoolYearAndSemesterPicker.class);
        // intent.putExtra("schoolyear", schoolyear);
        // startActivityForResult(intent, 1);
        // }
        // });
        settingTextView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // Intent intent = new Intent(CoursesActivity.this,
                // SettingActivity.class);
                // startActivity(intent);
            }
        });

    }

    // 接收回传回来的值
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1) {
            int year = data.getIntExtra("year", 0);
            int semester = data.getIntExtra("semester", 0);
            new NetWorkTask().execute(new Integer[] { year, semester });
            Toast.makeText(CoursesActivity.this,
                    "year: " + year + " semester: " + semester,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void initData() {
        jsonObject = UserUtil.readData(CoursesActivity.this,
                UserUtil.COURSE_OBJECT_FILE_NAME);
        moreJsonObject = UserUtil.readData(CoursesActivity.this,
                UserUtil.MORE_COURSE_OBJECT_FILE_NAME);
        weekNum = (TextView) findViewById(R.id.weekNum);
        try {
            schoolyear = jsonObject.getString("schoolyear");
            weekNumI = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)
                    - jsonObject.getInt("dValue");
            setWeekS();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        weekNum.setText(new StringBuilder("第 ").append(weekNumI).append(" 周"));
        drawCourses();
    }

    private int seed = 0;

    @SuppressLint("NewApi")
    private void drawCourses(int dayID, String dayName) {
        linearLayout = (LinearLayout) findViewById(dayID);
        try {
            JSONArray day = jsonObject.getJSONArray(dayName);
            // Log.d("day", day.toString());

            JSONArray moreDay = moreJsonObject.getJSONArray(dayName);
            // Log.d("moreDay", moreDay.toString());
            for (int i = 0; i < 8; i++) {

                final TextView textView = new TextView(this);
                JSONArray time = day.getJSONArray(i);
                /***************************
                 *
                 *
                 *
                 * ********************************/
                // Log.d("iiiiiiii", time.toString());
                JSONArray mTime = moreDay.getJSONArray(i);
                if (time.length() > 0) {
                    // Log.d("iiiiiiii", "是的");
                    for (int j = 0; j < time.length(); j++) {
                        final JSONObject temp = mTime.getJSONObject(j);
                        // Log.d("iiiiiiii", temp.toString());
                        // Log.d("week", weekS);
                        // Log.d("boolean", weekS
                        // + " "
                        // + temp.getString("week").replace('[', '0')
                        // .replace(",", ",0").replace(']', ','));
                        if (temp.getString("week").replace('[', '0')
                                .replace(",", ",0").replace(']', ',')
                                .indexOf(weekS) != -1) {

                            // Log.d("tttt", time.get(j).toString());
                            textView.setText(time.get(j).toString());
                            textView.setTextSize(11f);
                            textView.setTextColor(Color.BLACK);
                            textView.setBackground(getResources().getDrawable(
                                    textViewBackground[seed++]));
                            if (seed > 5) {
                                seed = 0;
                            }
                            textView.setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(
                                            CoursesActivity.this,
                                            CoursesDetailActivity.class);
                                    intent.putExtra("data", temp.toString());
                                    startActivity(intent);
                                }
                            });
                            break;
                        }
                    }
                } else {
                    textView.setBackground(getResources().getDrawable(
                            R.drawable.textview_border_course));
                }

                textView.setHeight(160);
                // textView.setHeight(120);

                linearLayout.addView(textView);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // Log.i("keyDown", event.toString());
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private User user = null;

    // 读取保存的用户信息并初始化
    private void setUser() {
        List<User> users = UserUtil.getUserList(this);
        try {
            String studentNo = UserUtil.readData(this,
                    UserUtil.STUDENT_INFO_FILE_NAME).getString(StudentInfo.id);
            for (User user : users) {
                if (user.getId().equals(studentNo)) {
                    this.user = user;
                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    // 去网页重新加载新的数据
    private int reLoadData(int year, int semester) throws JSONException {
        LoginAction loginAction = new LoginAction(this.user.getId(),
                this.user.getPwd(),getApplication());
        switch (loginAction.doLogin()) {
            case 2:
                return 2;
            case 3:
                return 3;
            default:
                break;
        }
        JSONObject data = loginAction.getSchedules(year, semester);
        DataParser parser = new DataParser();
        parser.makeNewCoursesInfo(CoursesActivity.this, data);
        return 1;
    }

    class NetWorkTask extends AsyncTask<Integer, Void, Integer> {

        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                return reLoadData(params[0], params[1]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return -1;
        }

        @Override
        protected void onPostExecute(Integer result) {
            switch (result) {
                case 2:
                    Toast.makeText(CoursesActivity.this, "异常！！！",
                            Toast.LENGTH_SHORT).show();
                    return;
                case 3:
                    Toast.makeText(CoursesActivity.this,
                            "您在上次输入密码后在网页端修改过密码\n请清除数据后重新登陆", Toast.LENGTH_SHORT)
                            .show();
                    return;
                default:
                    initData();
                    // 清除之前的数据
                    for (int i = 0; i < 7; i++) {
                        LinearLayout layout = (LinearLayout) findViewById(dayID[i]);
                        layout.removeAllViewsInLayout();
                    }
                    drawCourses();
                    break;
            }

        }
    }

}
