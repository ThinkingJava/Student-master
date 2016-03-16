package com.example.yangchenghuan.myFragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yangchenghuan.android_item_test4.R;
import com.example.yangchenghuan.mydata.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yangchenghuan
 * 类描述：
 * 创建日期：2015/11/13
 */
public class MessageDetailFragment extends Fragment {
    List<Student> list;
    public static Context context;
    public static MessageDetailFragment newInstance(List<Student> scorelist,Context con) {
        Bundle args = new Bundle();
        context=con;
        MessageDetailFragment fragment = new MessageDetailFragment();
        args.putParcelableArrayList("info", (ArrayList<? extends Parcelable>) scorelist);   //数据打包传送

   //     args.putString("info", scorelist);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messagemedetail, null);
         list=getArguments().getParcelableArrayList("info");
        ListView listview = (ListView) view.findViewById(R.id.remainTicketsList);
        listview.setAdapter(new myAdapter());

        return view;
    }

    class myAdapter extends BaseAdapter {

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
            Student student=list.get(position);
            View view = View.inflate(context, R.layout.simple_message_item, null);
         //   TextView text1=(TextView)view.findViewById(R.id.text1);

            TextView text2=(TextView)view.findViewById(R.id.text2);
            TextView text3=(TextView)view.findViewById(R.id.text3);
            TextView text4=(TextView)view.findViewById(R.id.text4);
            TextView text5=(TextView)view.findViewById(R.id.text5);
         //   TextView text6=(TextView)view.findViewById(R.id.text6);
          //  TextView text7=(TextView)view.findViewById(R.id.text7);
            TextView text8=(TextView)view.findViewById(R.id.text8);
            TextView text9=(TextView)view.findViewById(R.id.text9);
         //   TextView text10=(TextView)view.findViewById(R.id.text10);
        //    text1.setText(""+student.getTerm());
            text2.setText(""+ student.getCourseCode());
            text3.setText(""+student.getCourseName());
            text4.setText(""+student.getCredit());
            text5.setText(""+student.getAssessment());
         //   text6.setText(""+student.getFirstCourse());
       //     text7.setText(""+student.getRepairCourse());
            text8.setText(""+student.getSchoolTrem());
            text9.setText(""+student.getScore());
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
