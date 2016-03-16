package com.example.yangchenghuan.mydata;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者：yangchenghuan
 * 类描述：
 * 创建日期：2015/11/13
 */
public class Student implements Parcelable {
    private String term;   //学期
    public String getTerm() {
        return term;
    }
    public void setTerm(String term) {
        this.term = term;
    }
    public String getCourseCode() {
        return courseCode;
    }
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
    public String getCourseName() {
        return courseName;
    }
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    public String getCredit() {
        return credit;
    }
    public void setCredit(String credit) {
        this.credit = credit;
    }
    public String getAssessment() {
        return assessment;
    }
    public void setAssessment(String assessment) {
        this.assessment = assessment;
    }
    public String getFirstCourse() {
        return firstCourse;
    }
    public void setFirstCourse(String firstCourse) {
        this.firstCourse = firstCourse;
    }
    public String getRepairCourse() {
        return repairCourse;
    }
    public void setRepairCourse(String repairCourse) {
        this.repairCourse = repairCourse;
    }
    public String getSchoolTrem() {
        return schoolTrem;
    }
    public void setSchoolTrem(String schoolTrem) {
        this.schoolTrem = schoolTrem;
    }
    public String getScore() {
        return score;
    }
    public void setScore(String score) {
        this.score = score;
    }
    public String getObtain() {
        return obtain;
    }
    public void setObtain(String obtain) {
        this.obtain = obtain;
    }
    private String courseCode;  //课程代码
    private String courseName;//课程名称
    private String credit;  //学分
    private String assessment;//考核方式
    private String firstCourse;//先修课程
    private String repairCourse;//同修课程
    private String schoolTrem;//修读学年学期
    private String score;//成绩
    private String obtain;//已得学分

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(term);
        out.writeString(courseCode);
        out.writeString(courseName);
        out.writeString(credit);
        out.writeString(assessment);
        out.writeString(firstCourse);
        out.writeString(repairCourse);
        out.writeString(schoolTrem);
        out.writeString(score);
        out.writeString(obtain);
    }
    public Student(){

    }

   public Student(Parcel in){
       term=in.readString();
       courseCode=in.readString();
       courseName=in.readString();
       credit=in.readString();
       assessment=in.readString();
       firstCourse=in.readString();
       repairCourse=in.readString();
       schoolTrem=in.readString();
       score=in.readString();
       obtain=in.readString();
   }
    public static final Parcelable.Creator<Student> CREATOR=new Parcelable.Creator<Student>(){

        @Override
        public Student createFromParcel(Parcel source) {
            return new Student(source);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };
}

