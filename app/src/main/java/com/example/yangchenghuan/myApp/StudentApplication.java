package com.example.yangchenghuan.myApp;

import android.app.Application;

import org.json.JSONObject;

import java.util.Map;

/**
 * 作者：yangchenghuan
 * 类描述：
 * 创建日期：2015/11/13
 */
public class StudentApplication extends Application {

    private Map<String,String> session;   //保存cookies
    private JSONObject TestTime;   //考试时间
    private JSONObject AttendMessage;  //考勤
    private JSONObject LaterMessage;//晚归信息
private  JSONObject OfferMessage;//开设课程
private JSONObject RewardMessage;//奖罚
  //  private JSONObject StudentScore;  //考试成绩

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public Map<String, String> getSession() {
        return session;
    }

    public void setSession(Map<String, String> session) {
        this.session = session;
    }

    public JSONObject getTestTime() {
        return TestTime;
    }

    public void setTestTime(JSONObject testTime) {
        TestTime = testTime;
    }

//    public JSONObject getStudentScore() {
//        return StudentScore;
//    }
//
//    public void setStudentScore(JSONObject studentScore) {
//        StudentScore = studentScore;
//    }

    public JSONObject getAttendMessage() {
        return AttendMessage;
    }

    public void setAttendMessage(JSONObject attendMessage) {
        AttendMessage = attendMessage;
    }

    public JSONObject getLaterMessage() {
        return LaterMessage;
    }

    public void setLaterMessage(JSONObject laterMessage) {
        LaterMessage = laterMessage;
    }

    public JSONObject getRewardMessage(){return  RewardMessage;}

    public void setRewardMessage(JSONObject rewardMessage){RewardMessage=rewardMessage;}

    public  JSONObject getOfferMessage(){ return  OfferMessage;}

    public void setOfferMessage(JSONObject offerMessage){OfferMessage=offerMessage;}
}
