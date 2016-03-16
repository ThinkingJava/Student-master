package com.example.yangchenghuan.android_item_test4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yangchenghuan.myApp.StudentApplication;
import com.example.yangchenghuan.mydata.Student;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {
    private ListView listView;
    private List<List<String>> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
       listView=(ListView)this.findViewById(R.id.listview);

            StudentApplication application=(StudentApplication)getApplication();
        JSONObject jsonObject=application.getTestTime();

         if(jsonObject!=null){
             try {
                 Log.d("TestActivity---<<<>>>",jsonObject.toString());
                 list=(ArrayList<List<String>>)jsonObject.get("TestTime");
                // list=new ArrayList<List<String>>();

                 listView.setAdapter(new MyAdapter());
                 Log.d("TestActivity", "----->>>>>>" + list.toString());
             } catch (JSONException e) {
                 e.printStackTrace();
             }

         }   else{
              list=new ArrayList<List<String>>();
         }


    }


    class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                List<String> myList=list.get(position);
            View view = View.inflate(TestActivity.this, R.layout.simple_test_item, null);
               TextView text1=(TextView)view.findViewById(R.id.text1);

            TextView text2=(TextView)view.findViewById(R.id.text2);
            TextView text3=(TextView)view.findViewById(R.id.text3);
            TextView text4=(TextView)view.findViewById(R.id.text4);
            TextView text5=(TextView)view.findViewById(R.id.text5);
            TextView text6=(TextView)view.findViewById(R.id.text6);
            TextView text7=(TextView)view.findViewById(R.id.text7);
            TextView text8=(TextView)view.findViewById(R.id.text8);
;
            //   TextView text10=(TextView)view.findViewById(R.id.text10);
            if(list!=null) {
                text1.setText("" + myList.get(0));
                text2.setText("" + myList.get(1));
                text3.setText("" + myList.get(2));
                text4.setText("" + myList.get(3));
                text5.setText("" + myList.get(4));
                text6.setText("" + myList.get(5));
                text7.setText("" + myList.get(6));
                text8.setText("" + myList.get(7));
            }else{
                text1.setText("暂无数据");
            }

            //     text10.setText(""+student.getObtain());
            /*

                private String courseCode;  //课程代码
    private String courseName;//课程名称
    private String credit;  //学分
    private String assessment;//考核方式
    private String firstCourse;//先修课程
    private String repairCourse;//同修课程
    private String schoolTrem;//修读学年学期
    private String score;//成绩
    private String obtain;//已得学分
             */
            return view;
        }
    }
}
