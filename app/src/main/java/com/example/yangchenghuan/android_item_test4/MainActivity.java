package com.example.yangchenghuan.android_item_test4;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yangchenghuan.myView.ArcMenu;
import com.example.yangchenghuan.myView.DraggableGridViewPager;

public class MainActivity extends Activity {
//    private TextView studentId;
//    private TextView studentName;
//    private TextView  studentMajor;
//    private TextView studentHours;
    private static final String TAG = "MainActivity";
    ArcMenu arcMenu;
    private DraggableGridViewPager mDraggableGridViewPager;
//    private Button mAddButton;
//    private Button mRemoveButton;

    private ArrayAdapter<String> mAdapter;
    private ProgressDialog progressDialog = null;
//    private int mGridCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDraggableGridViewPager = (DraggableGridViewPager) findViewById(R.id.draggable_grid_view_pager);
        arcMenu = new ArcMenu(this);
        arcMenu = (ArcMenu) findViewById(R.id.id_menu);
        arcMenu.setOnMenuItemClickListener(new ArcMenu.onMenuItemClickListener() {

            @Override
            public void onclick(View view, int position) {
                // TODO Auto-generated method stub
                Toast.makeText(MainActivity.this,
                        position + ":" + view.getTag(), Toast.LENGTH_SHORT)
                        .show();
                Intent intent;
                switch (position){

                    case 1: intent=new Intent(MainActivity.this,LoginActivity.class);
                          startActivity(intent);
                         MainActivity.this.finish();
                        break;
                    case 2:
                        break;
                }
            }
        });



//        studentId=(TextView)this.findViewById(R.id.studentId);
//        studentName=(TextView)this.findViewById(R.id.studentName);
//        studentHours=(TextView)this.findViewById(R.id.studentHours);
//        studentMajor=(TextView)this.findViewById(R.id.studentMajor);
        setData();
    }

    private void setData(){


        String []test_title= new String[]{this.getString(R.string.string1), this.getString(R.string.string2),
                this.getString(R.string.string3), this.getString(R.string.string4),this.getString(R.string.string5),
                this.getString(R.string.string6),
                this.getString(R.string.string7),this.getString(R.string.string8)};
        mAdapter = new ArrayAdapter<String>(this, 0) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final String text = getItem(position);
                if (convertView == null) {
                    convertView = (TextView) getLayoutInflater().inflate(R.layout.list_layout_mian, null);
                }
                ((TextView) convertView).setText(text);
                ((TextView) convertView).setBackgroundColor(Color.rgb(100+position*10,50+position*20,150+position*2));
                return convertView;
            }

        };
        for(int i=0;i<test_title.length;i++){
            mAdapter.add(test_title[i]);  //添加textview
        }
        mDraggableGridViewPager.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();    //刷新一次
        mDraggableGridViewPager.setOnPageChangeListener(new DraggableGridViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.v(TAG, "onPageScrolled position=" + position + ", positionOffset=" + positionOffset
                        + ", positionOffsetPixels=" + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                Log.i(TAG, "onPageSelected position=" + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d(TAG, "onPageScrollStateChanged state=" + state);
            }
        });

        mDraggableGridViewPager.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    TextView textView=(TextView)view;
                    Intent intent;

                        switch (textView.getText().toString()) {

                                                case "个人信息":
                                                intent = new Intent(MainActivity.this, PersonalActivity.class);
                                                startActivity(intent);
                                                break;
                                                case "课程表":
                                                intent = new Intent(MainActivity.this, CoursesActivity.class);
                                                startActivity(intent);
                                                break;
                                                case "考试时间":
                                                intent = new Intent(MainActivity.this, TestActivity.class);
                                                startActivity(intent);
                                                break;
                                                case "考勤信息":   intent=new Intent(MainActivity.this, AttendActivity.class);
                                                   startActivity(intent);
                                                break;
                                                case "成绩查询":
                                                intent = new Intent(MainActivity.this, MessageActivity.class);
                                                startActivity(intent);

                                                break;
                                                case "奖惩情况":
                                                    intent = new Intent(MainActivity.this, RewardpunishActivity.class);
                                                    startActivity(intent);

                                                break;
                                                case "开设课程":
                                                    intent = new Intent(MainActivity.this, OffercoursesActivity.class);
                                                    startActivity(intent);

                                                break;
                                                case "晚归违规":
                                                    intent = new Intent(MainActivity.this, LaterActivity.class);
                                                    startActivity(intent);
                                                break;
                                            }

            }
        });
        mDraggableGridViewPager.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            //    showToast(((TextView) view).getText().toString() + " long clicked!!!");
                return true;
            }
        });

        mDraggableGridViewPager.setOnRearrangeListener(new DraggableGridViewPager.OnRearrangeListener() {
            @Override
            public void onRearrange(int oldIndex, int newIndex) {
                Log.d(TAG, "OnRearrangeListener.onRearrange from=" + oldIndex + ", to=" + newIndex);
                String item = mAdapter.getItem(oldIndex);
                mAdapter.setNotifyOnChange(false);
                mAdapter.remove(item);
                mAdapter.insert(item, newIndex);
                mAdapter.notifyDataSetChanged();
            }
        });
//
//        mAddButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mGridCount++;
//                mAdapter.add("Grid" + mGridCount);
//            }
//        });
//
//        mRemoveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mAdapter.getCount() > 0) {
//                    mAdapter.remove(mAdapter.getItem(mAdapter.getCount() - 1));
//                }
//            }
//        });


//        GridView gridview = (GridView) findViewById(R.id.gridview);
//        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,     Object>>();/*在数组中存放数据*/
//
//        for(int i=0;i<picture.length;i++)
//        {
//            HashMap<String, Object> map = new HashMap<String, Object>();
//            map.put("ItemImage", picture[i]);//加入图片
//            map.put("ItemTitle", test_title[i]);
//            listItem.add(map);
//        }
//        //生成适配器的ImageItem <====> 动态数组的元素，两者一一对应
//        SimpleAdapter saImageItems = new SimpleAdapter(this, //没什么解释
//                listItem,//数据来源
//                R.layout.list_layout_mian,//night_item的XML实现
//
//                //动态数组与ImageItem对应的子项
//                new String[] {"ItemImage","ItemTitle"},
//
//                //ImageItem的XML文件里面的一个ImageView,两个TextView ID
//                new int[] {R.id.ItemImage,R.id.ItemTitle});
//        //添加并且显示
//        gridview.setAdapter(saImageItems);
//        //添加消息处理
//        gridview.setOnItemClickListener(new ItemClickListener());

    }

//    //当AdapterView被单击(触摸屏或者键盘)，则返回的Item单击事件
//    class  ItemClickListener implements AdapterView.OnItemClickListener
//    {
//        public void onItemClick(AdapterView<?> arg0,//The AdapterView where the click happened点击发生在适配器视图
//                                View arg1,//The view within the AdapterView that was clicked  适配器的视图，在视图中单击
//                                int arg2,//The position of the view in the adapter视图的适配器中的位置
//                                long arg3//The row id of the item that was clicked  这是一项的行ID
//        ) {
//            //在本例中arg2=arg3
//            HashMap<String, Object> item=(HashMap<String, Object>) arg0.getItemAtPosition(arg2);
//            //显示所选Item的ItemText
//            //     setTitle((String) item.get("ItemText"));
//            Intent intent;
//            switch (arg2) {
//                case 0:    intent = new Intent(MainActivity.this, MessageActivity.class);
//                          startActivity(intent);
//                        break;
//                case 1:    intent = new Intent(MainActivity.this,CoursesActivity.class);
//                    startActivity(intent);       break;
//                case 2:   intent=new Intent(MainActivity.this,TestActivity.class);
//                    startActivity(intent);      break;
//                case 3: //  intent=new Intent(MainActivity.this, ChartActivity.class);
//                 //   startActivity(intent);
//                   break;
//            }
//        }
//
//
//
//    }


}
