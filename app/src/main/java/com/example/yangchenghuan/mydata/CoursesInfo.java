package com.example.yangchenghuan.mydata;

import java.util.Arrays;

/**
 * 作者：yangchenghuan
 * 类描述：
 * 创建日期：2015/11/13
 */
public class CoursesInfo {
    private String flag; // 区分不同教室不同班级的标记
    private Integer[] week;
    private String classRoom;
    private String teacherName;
    private String className;
    private String courseName;
    private String startTime;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Integer[] getWeek() {
        return week;
    }

    public void setWeek(Integer[] week) {
        this.week = week;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return "CoursesInfo [flag=" + flag + ", week=" + Arrays.toString(week)
                + ", classRoom=" + classRoom + ", teacherName=" + teacherName
                + ", className=" + className + ", courseName=" + courseName
                + ", startTime=" + startTime + "]";
    }
}