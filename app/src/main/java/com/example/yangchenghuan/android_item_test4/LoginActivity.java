package com.example.yangchenghuan.android_item_test4;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yangchenghuan.myAction.LoginAction;
import com.example.yangchenghuan.myApp.StudentApplication;
import com.example.yangchenghuan.myUtils.UserUtil;
import com.example.yangchenghuan.myView.SplashView;
import com.example.yangchenghuan.mytools.DataParser;
import com.example.yangchenghuan.myuser.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginActivity extends Activity implements View.OnClickListener,
        AdapterView.OnItemClickListener, PopupWindow.OnDismissListener {



    protected static final String TAG = "LoginActivity";
    private LinearLayout mLoginLinearLayout; // 登录内容的容器
    private LinearLayout mUserIdLinearLayout; // 将下拉弹出窗口在此容器下方显示
    private Animation mTranslate; // 位移动画
    private Dialog mLoginingDlg; // 显示正在登录的Dialog
    private EditText mIdEditText; // 登录ID编辑框
    private EditText mPwdEditText; // 登录密码编辑框
    private ImageView mMoreUser; // 下拉图标
    private Button mLoginButton; // 登录按钮
    private ImageView mLoginMoreUserView; // 弹出下拉弹出窗的按钮
    private String mIdString;
    private String mPwdString;
    private ArrayList<User> mUsers; // 用户列表
    private ListView mUserIdListView; // 下拉弹出窗显示的ListView对象
    private MyAapter mAdapter; // ListView的监听器
    private PopupWindow mPop; // 下拉弹出窗
    private FrameLayout frameLayout;
    private SplashView splashView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		/*
		简单粗暴的方法在主线程中链接网络
		 */
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        setContentView(R.layout.activity_login);
        initView();
        setListener();
        mLoginLinearLayout.startAnimation(mTranslate); // Y轴水平移动

		/* 获取已经保存好的用户密码 */
        mUsers = UserUtil.getUserList(LoginActivity.this);

        if (mUsers.size() > 0) {
			/* 将列表中的第一个user显示在编辑框 */
            mIdEditText.setText(mUsers.get(0).getId());
            mPwdEditText.setText(mUsers.get(0).getPwd());
        }

        LinearLayout parent = (LinearLayout) getLayoutInflater().inflate(
                R.layout.userinfo_listview, null);
        mUserIdListView = (ListView) parent.findViewById(android.R.id.list);
        parent.removeView(mUserIdListView); // 必须脱离父子关系,不然会报错
        mUserIdListView.setOnItemClickListener(this); // 设置点击事
        mAdapter = new MyAapter(mUsers);
        mUserIdListView.setAdapter(mAdapter);

    }

    /* ListView的适配器 */
    class MyAapter extends ArrayAdapter<User> {

        public MyAapter(ArrayList<User> users) {
            super(LoginActivity.this, 0, users);
        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(
                        R.layout.listview_item, null);
            }

            TextView userIdText = (TextView) convertView
                    .findViewById(R.id.listview_userid);
            userIdText.setText(getItem(position).getId());

            ImageView deleteUser = (ImageView) convertView
                    .findViewById(R.id.login_delete_user);
            deleteUser.setOnClickListener(new OnClickListener() {
                // 点击删除deleteUser时,在mUsers中删除选中的元素
                @Override
                public void onClick(View v) {

                    if (getItem(position).getId().equals(mIdString)) {
                        // 如果要删除的用户Id和Id编辑框当前值相等，则清空
                        mIdString = "";
                        mPwdString = "";
                        mIdEditText.setText(mIdString);
                        mPwdEditText.setText(mPwdString);
                    }
                    mUsers.remove(getItem(position));
                    mAdapter.notifyDataSetChanged(); // 更新ListView
                }
            });
            return convertView;
        }

    }

    private void setListener() {
        mIdEditText.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                mIdString = s.toString();
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        mPwdEditText.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                mPwdString = s.toString();
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        mLoginButton.setOnClickListener(this);
        mLoginMoreUserView.setOnClickListener(this);
    }

    private void initView() {
        mIdEditText = (EditText) findViewById(R.id.login_edtId);
        mPwdEditText = (EditText) findViewById(R.id.login_edtPwd);
        mMoreUser = (ImageView) findViewById(R.id.login_more_user);
        mLoginButton = (Button) findViewById(R.id.login_btnLogin);
        mLoginMoreUserView = (ImageView) findViewById(R.id.login_more_user);
        mLoginLinearLayout = (LinearLayout) findViewById(R.id.login_linearLayout);
        mUserIdLinearLayout = (LinearLayout) findViewById(R.id.userId_LinearLayout);
        mTranslate = AnimationUtils.loadAnimation(this, R.anim.my_translate); // 初始化动画对象
        initLoginingDlg();
    }

    public void initPop() {
        int width = mUserIdLinearLayout.getWidth() - 4;
        int height = LayoutParams.WRAP_CONTENT;
        mPop = new PopupWindow(mUserIdListView, width, height, true);
        mPop.setOnDismissListener(this);// 设置弹出窗口消失时监听器

        // 注意要加这句代码，点击弹出窗口其它区域才会让窗口消失
        mPop.setBackgroundDrawable(new ColorDrawable(0xffffffff));

    }

    /* 初始化正在登录对话框 */
    private void initLoginingDlg() {

        frameLayout = new FrameLayout(this);

        // 再添加SplashView
        splashView = new SplashView(this);
        frameLayout.addView(splashView);



    //    setContentView(frameLayout);
        // 开启Splash动画 --- 模拟后台加载数据

        mLoginingDlg = new Dialog(this, R.style.loginingDlg);
  //      mLoginingDlg.setContentView(R.layout.logining_dlg);

       Window window = mLoginingDlg.getWindow();
       WindowManager.LayoutParams params = window.getAttributes();
        // 获取和mLoginingDlg关联的当前窗口的属性，从而设置它在屏幕中显示的位置


        mLoginingDlg.addContentView(frameLayout,params);
        mLoginingDlg.setCanceledOnTouchOutside(true); // 设置点击Dialog外部任意区域关闭Dialog
    }

    /*
    *显示加载进度条
     */
    private void startLoad() {
        splashView.splashAndDisapper();
    }

    /* 显示正在登录对话框 */
    private void showLoginingDlg() {
        if (mLoginingDlg != null)
            mLoginingDlg.show();
    }

    /* 关闭正在登录对话框 */
    private void closeLoginingDlg() {
        if (mLoginingDlg != null && mLoginingDlg.isShowing())
            mLoginingDlg.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btnLogin:
                // 启动登录
                // showLoginingDlg(); //
                // Log.i(TAG, mIdString + "  " + mPwdString);
                if (mIdString == null || mIdString.equals("")) { // 账号为空时
                    Toast.makeText(LoginActivity.this, "请输入账号", Toast.LENGTH_SHORT)
                            .show();
                } else if (mPwdString == null || mPwdString.equals("")) {// 密码为空时
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT)
                            .show();
                } else {// 账号和密码都不为空时
                    boolean mIsSave = true;
                    showLoginingDlg(); // 显示"正在登录"对话框
                    try {
                        /***
                         * 此处准备执行登录
                         */
                        new DoInBackgroundLogin().execute(new User(mIdString,
                                mPwdString));

                        // Log.i(TAG, "保存用户列表");
                        for (User user : mUsers) { // 判断本地文档是否有此ID用户
                            if (user.getId().equals(mIdString)) {
                                mIsSave = false;
                                break;
                            }
                        }
                        if (mIsSave) { // 将新用户加入users
                            User user = new User(mIdString, mPwdString);
                            mUsers.add(user);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.login_more_user: // 当点击下拉栏
                if (mPop == null) {
                    initPop();
                }
                if (!mPop.isShowing() && mUsers.size() > 0) {
                    // // Log.i(TAG, "切换为角向上图标");
                    mMoreUser.setImageResource(R.drawable.load_more_down); // 切换图标
                    mPop.showAsDropDown(mUserIdLinearLayout, 2, 1); // 显示弹出窗口
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        mIdEditText.setText(mUsers.get(position).getId());
        mPwdEditText.setText(mUsers.get(position).getPwd());
        mPop.dismiss();
    }

    /* PopupWindow对象dismiss时的事件 */
    @Override
    public void onDismiss() {
        // // Log.i(TAG, "切换为角向下图标");
        mMoreUser.setImageResource(R.drawable.load_more_up);
    }

    /* 退出此Activity时保存users */
    @Override
    public void onPause() {
        super.onPause();
        try {
            UserUtil.saveUserList(LoginActivity.this, mUsers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class DoInBackgroundLogin extends AsyncTask<User, android.R.integer, String> {
        @Override
        protected String doInBackground(User... params) {
            String msg = "";
            LoginAction loginAction = new LoginAction(params[0].getId(),
                    params[0].getPwd(),getApplication());
            StudentApplication studentApplication=(StudentApplication)getApplication();
            int flag=0;
            try {
                    flag = loginAction.doLogin();
               }catch (Exception e){
                Intent intent=new Intent(LoginActivity.this,CoursesActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
               }
            // Log.i("flagCode", String.valueOf(flag));
            if (flag == 1) {
                JSONObject object = loginAction.getSchedules();
                JSONObject score=loginAction.getObliScore();  //获取成绩
                JSONObject attend=loginAction.getAttend();  //考勤
                JSONObject later=loginAction.getlatemessage();//晚归
                JSONObject rewrad=loginAction.getrewardpunish();//奖罚
                JSONObject offer=loginAction.getoffercourse();//开设课程
                // Utils.saveData(getApplicationContext(), object,
                // Utils.STUDENT_DATA_FILE_NAME);
                Log.i(TAG, "login success	写入用户信息" + object.toString());

                studentApplication.setTestTime(loginAction.getTesttime());   //考试时间存在app生命中
          //      studentApplication.setStudentScore(score);
                studentApplication.setAttendMessage(attend);
                studentApplication.setLaterMessage(later);
                studentApplication.setRewardMessage(rewrad);
                studentApplication.setOfferMessage(offer);
                Log.d("LogActivity","----<<<<<"+studentApplication.getTestTime().toString());

                DataParser dataParser = new DataParser();
                try {
                    dataParser.makeNewCoursesInfo(LoginActivity.this, object);
                    dataParser.writeStudentInfoToFile(LoginActivity.this,
                            loginAction.getStudentInfo());
                    dataParser.saveScore(LoginActivity.this,score); //保存成绩
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                msg = "登陆成功";
            } else if (flag == 2) {
                msg = "学号错误";
            } else if (flag == 3) {
                msg = "密码错误";
            }
            closeLoginingDlg();
            return msg;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT)
                    .show();
            if ("登陆成功".equals(result)) {
                SharedPreferences sharedPreferences = getSharedPreferences(
                        "info", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isFirst", false);
                editor.commit();
                Intent intent = new Intent(LoginActivity.this,
                        MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                // Log.i(TAG, "跳转。。。");
            }else{

            }

            /***
             * 后续...
             */
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

        }
        return true;
    }

}

