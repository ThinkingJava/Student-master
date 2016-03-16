package com.example.yangchenghuan.mytools;

import android.content.Context;
import android.util.Log;

import com.example.yangchenghuan.myUtils.UserUtil;
import com.example.yangchenghuan.mydata.CoursesInfo;
import com.example.yangchenghuan.mydata.Student;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 作者：yangchenghuan
 * 类描述：把json对象转换为java对象
 * 创建日期：2015/11/13
 */
public class DataParser {
    /**
     * 把json对象转换为java对象
     *
     * @param src
     * @return
     */
    List<Student> obligatory;//必修成绩
    List<Student> elective;//选修成绩
    public CoursesInfo getCourseObject(String src) {
        // String[] strings = src.split("#");// [0]==>timeInfo:JSONArray;
        // [1]==>courseInfo:JSONArray

        // JSONObject tTime =

    //    Log.d("******", src);
        CoursesInfo coursesInfo = new CoursesInfo();
        String time = src.substring(src.indexOf(">") + 1, src.indexOf("#"))
                .substring(0,5);
        String courseName = src.substring(src.indexOf("#") + 1,
                src.indexOf("("));
        String otherInfo = src.substring(src.indexOf("("));
        String[] split = otherInfo.split(" ");
        String className = split[0].substring(split[0].indexOf("(") + 1);
        String teacherName = split[1];
        int splitLength = split.length;
        String classRoom = split[splitLength - 1].substring(
                split[splitLength - 1].indexOf("[") + 1,
                split[splitLength - 1].length() - 2);
        Integer[] week = new Integer[splitLength - 3];
        for (int i = 0; i < split.length - 4; i++) {
            week[i] = Integer.valueOf(split[i + 2]);
        }
        week[splitLength - 4] = Integer.valueOf(split[splitLength - 2]
                .substring(0, split[splitLength - 2].length() - 1));
        coursesInfo.setClassName(className);
        coursesInfo.setClassRoom(classRoom);
        coursesInfo.setFlag(className + classRoom + time);
        coursesInfo.setCourseName(courseName);
        coursesInfo.setStartTime(time);
        coursesInfo.setTeacherName(teacherName);
        coursesInfo.setWeek(week);
        return coursesInfo;
    }

    /**
     * 获取CourseObjectArray
     *
     * @param context
     * @return
     * @throws JSONException
     */
    private Map<String, CoursesInfo> getCourseObjectArray(Context context,
                                                          JSONObject jsonObject) throws JSONException {
        Set<String> set = new HashSet<String>();
        JSONArray timeInfo = jsonObject.getJSONArray("&nbsp;");
        for (String string : UserUtil.DAY_OF_WEEK) {
            JSONArray dayInfo = jsonObject.getJSONArray(string);
            for (int i = 0; i < 8; i++) {
                JSONArray coursesArray = dayInfo.getJSONArray(i);
                // Log.d("coursesArray", coursesArray.toString());
                if (!coursesArray.toString().equals("[\"&nbsp;\"]")) {
                    // 放到set里面是为了去重
                    // Log.d("dayInfo", dayInfo.get(i).toString());
                    for (int j = 0; j < coursesArray.length(); j++) {
                        set.add(timeInfo.getJSONArray(i).getString(0) + "#"
                                + coursesArray.getString(j));
                    }

                }
            }
        }
        Map<String, CoursesInfo> map = new HashMap<String, CoursesInfo>();
        CoursesInfo info = null;
        for (String string : set) {
        //    Log.d("dayInfo", string);
            info = getCourseObject(string);
        //    Log.d("info.toString()", info.toString());
            map.put(info.getFlag(), info);
        }
        return map;
    }

    public void makeNewCoursesInfo(Context context, JSONObject jsonObject)
            throws JSONException {
  //      Log.d("jsonObject", jsonObject.toString());
        Map<String, CoursesInfo> coursesInfos = getCourseObjectArray(context,
                jsonObject);
  //      Log.d("--------------",coursesInfos.toString());
        JSONObject targetJsonObject = new JSONObject();
        JSONObject moreInfoTargetJsonObject = new JSONObject();
        JSONArray timeInfo = jsonObject.getJSONArray("&nbsp;");
        // 19:00 - 20:20
        for (String string : UserUtil.DAY_OF_WEEK) {
            JSONArray dayInfo = jsonObject.getJSONArray(string);
            JSONArray array = new JSONArray();
            JSONArray moreInfoArray = new JSONArray();
            for (int i = 0; i < 8; i++) {
                JSONArray courseArray = dayInfo.getJSONArray(i);
                JSONArray sameTime = new JSONArray();
                JSONArray moreSameTime = new JSONArray();
                for (int j = 0; j < courseArray.length(); j++) {
                    String src = courseArray.get(j).toString();

                    if (src.equals("&nbsp;")) {
                        // array.put(sameTime);
                        // moreInfoArray.put(moreSameTime);
                    } else {
                        // Log.d("ssrrcc", src);
                        // name + room
                        //Android网络编程(IE 潘正军 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17周 )
                        // [B201])
                        String[] split = src.substring(src.indexOf("(")).split(
                                " ");
                        String courseName = split[0].substring(1);
                        // src.substring(src.indexOf("("));
                        String classRoom = src.substring(src.indexOf("[") + 1,
                                src.indexOf("]"));
                        /******************************************************************/
                        String time = timeInfo.getString(i);
                        // Log.d("time", time);
                        int length = time.length();
                        String startTime = time.substring(length - 15,
                                length - 10);
                        Log.d("startTime", startTime);
                        //	String flag = courseName + classRoom + startTime;
                        String flag = courseName + classRoom+startTime;
                        //	 Log.d("coursesInfos", coursesInfos.toString());
                        //	 Log.d("coursesInfos", flag);
                        CoursesInfo info = coursesInfos.get(flag);
                        Log.d("11111", ""+flag);
                        StringBuilder builder = new StringBuilder();
                        builder.append(info.getCourseName());
                        builder.append("\n");
                        builder.append(info.getTeacherName());
                        builder.append("\n@ ");
                        builder.append(info.getClassRoom());
                        sameTime.put(builder.toString());
                        // array.put(builder.toString());

                        JSONObject tempObject = new JSONObject();
                        tempObject.put("className", info.getClassName());
                        tempObject.put("classRoom", info.getClassRoom());
                        tempObject.put("courseName", info.getCourseName());
                        tempObject.put("startTime", info.getStartTime());
                        tempObject.put("teacherName", info.getTeacherName());
                        JSONArray week = new JSONArray();
                        for (Integer tmp : info.getWeek()) {
                            week.put(tmp);
                        }
                        tempObject.put("week", week);
                        moreSameTime.put(tempObject);
                        // moreInfoArray.put(tempObject);
                    }
                }
                array.put(sameTime);
                moreInfoArray.put(moreSameTime);

            }
            targetJsonObject.put(string, array);
            moreInfoTargetJsonObject.put(string, moreInfoArray);
        }
        targetJsonObject.put("dValue", jsonObject.get("dValue"));
        targetJsonObject.put("schoolyear", jsonObject.get("schoolyear"));
//		moreInfoTargetJsonObject
//				.put("schoolyear", jsonObject.get("schoolyear"));
        moreInfoTargetJsonObject.put("dValue", jsonObject.get("dValue"));

        UserUtil.saveData(context, targetJsonObject, UserUtil.COURSE_OBJECT_FILE_NAME);
        UserUtil.saveData(context, moreInfoTargetJsonObject,
                UserUtil.MORE_COURSE_OBJECT_FILE_NAME);
    }

    // 将学生信息写入文件
    public void writeStudentInfoToFile(Context context, JSONObject jsonObject)
            throws JSONException {
        UserUtil.saveData(context, jsonObject, UserUtil.STUDENT_INFO_FILE_NAME);
    }

    /*
    *设置成绩
     */
    public void setScore(JSONObject jsonObject){
        try {
            obligatory = (List<Student>) jsonObject.get("obligatory");
             elective=(List<Student>)jsonObject.get("elective");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public List<Student> getScore(int i){
       if(i==1){
           return   obligatory;
       }else{
           return elective;
       }
    }
    public void saveScore(Context context,JSONObject jsonObject){  //保存成绩
        UserUtil.saveData(context, jsonObject, UserUtil.SCORE);
    }
}

