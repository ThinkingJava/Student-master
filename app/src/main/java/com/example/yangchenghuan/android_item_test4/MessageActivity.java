package com.example.yangchenghuan.android_item_test4;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.yangchenghuan.myFragment.MessageDetailFragment;
import com.example.yangchenghuan.myUtils.UserUtil;
import com.example.yangchenghuan.mydata.Student;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends FragmentActivity {
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });

                    CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
                    collapsingToolbar.setTitle("成绩");
                    mViewPager = (ViewPager) findViewById(R.id.viewpager);
                    setupViewPager(mViewPager);
                    TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
                    tabLayout.addTab(tabLayout.newTab().setText("必修课程"));
                    tabLayout.addTab(tabLayout.newTab().setText("选修课程"));
                    tabLayout.setupWithViewPager(mViewPager);

    }

    private void setupViewPager(ViewPager mViewPager) {


        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());

        JSONObject score1= UserUtil.readData(MessageActivity.this,UserUtil.SCORE);
        Log.d("MessageActivity",score1.toString());
        try {
            Gson gson=new Gson();
            Log.d("111",score1.get("obligatory").toString());
            Log.d("222",score1.get("elective").toString());
            List<Student> obligatory1=gson.fromJson(score1.get("obligatory").toString(),new TypeToken<List<Student>>(){}.getType());
            List<Student> elective1=gson.fromJson(score1.get("elective").toString(),new TypeToken<List<Student>>(){}.getType());

            Log.d("mmmmm",obligatory1.toString());
            Log.d("bbbb",elective1.toString());

            adapter.addFragment(MessageDetailFragment.newInstance(obligatory1, MessageActivity.this), "必修课程");
            adapter.addFragment(MessageDetailFragment.newInstance(elective1, MessageActivity.this), "选修课程");
        }catch (JSONException e){
            e.printStackTrace();
        }

        mViewPager.setAdapter(adapter);

    }

    static class MyPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
