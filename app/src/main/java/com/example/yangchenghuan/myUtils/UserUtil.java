package com.example.yangchenghuan.myUtils;

import android.content.Context;

import com.example.yangchenghuan.myuser.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * 作者：yangchenghuan
 * 类描述：
 * 创建日期：2015/11/13
 */
public class UserUtil {
    public static final String FILENAME = "userinfo.json"; // 用户保存文件名
    private static final String TAG = "Utils";
    public static final String STUDENT_DATA_FILE_NAME = "userData.json";// 保存拉取的用户信息
    public static final String COURSE_OBJECT_FILE_NAME = "courseObject.json";// 保存课程对象
    public static final String MORE_COURSE_OBJECT_FILE_NAME = "moreCourseObject.json";// 保存课程对象
    public static final String SCORE="score.json";   //保存成绩
    public static final String[] DAY_OF_WEEK = { "星期一", "星期二", "星期三", "星期四",
            "星期五", "星期六", "星期日" };
    public static final String STUDENT_INFO_FILE_NAME = "studentInfo.json";// 学生信息文件

    /* 保存学生课程信息 */
    public static void saveData(Context context, JSONObject jsonObject,
                                String filePath) {
        // Log.i(TAG, "保存学生信息");
        Writer writer = null;
        OutputStream outputStream = null;
        try {
            outputStream = context.openFileOutput(filePath,
                    Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(outputStream);
            writer.write(jsonObject.toString());
            writer.flush();
            // Log.i(TAG, "完成学生信息保存");
        } catch (Exception e) {
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* 读取学生课程信息 */
    public static JSONObject readData(Context context, String filePath) {
		/* 加载 */
        FileInputStream in = null;
        try {

            in = context.openFileInput(filePath);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            JSONObject jsonObject = new JSONObject(jsonString.toString());
            return jsonObject;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /* 保存用户登录信息列表 */
    public static void saveUserList(Context context, ArrayList<User> users)
            throws Exception {
		/* 保存 */
        // Log.i(TAG, "正在保存");
        Writer writer = null;
        OutputStream out = null;
        JSONArray array = new JSONArray();
        for (User user : users) {
            array.put(user.toJSON());
        }
        try {
            out = context.openFileOutput(FILENAME, Context.MODE_PRIVATE); // 覆盖
            writer = new OutputStreamWriter(out);
            // Log.i(TAG, "json的值:" + array.toString());
            writer.write(array.toString());
        } finally {
            if (writer != null)
                writer.close();
        }

    }

    /* 获取用户登录信息列表 */
    public static ArrayList<User> getUserList(Context context) {
		/* 加载 */
        FileInputStream in = null;
        ArrayList<User> users = new ArrayList<User>();
        try {

            in = context.openFileInput(FILENAME);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            JSONArray jsonArray = new JSONArray();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            // Log.i(TAG, jsonString.toString());
            jsonArray = (JSONArray) new JSONTokener(jsonString.toString())
                    .nextValue(); // 把字符串转换成JSONArray对象
            for (int i = 0; i < jsonArray.length(); i++) {
                User user = new User(jsonArray.getJSONObject(i));
                users.add(user);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }

}