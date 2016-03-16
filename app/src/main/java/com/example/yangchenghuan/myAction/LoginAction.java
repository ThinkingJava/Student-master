package com.example.yangchenghuan.myAction;

import android.app.Application;
import android.util.Log;

import com.example.yangchenghuan.myApp.StudentApplication;
import com.example.yangchenghuan.myUtils.StudentInfo;
import com.example.yangchenghuan.myUtils.StudentURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者：yangchenghuan
 * 类描述：
 * 创建日期：2015/11/13
 */
public class LoginAction {
    private String userName;
    private String passwd;
    private static Connection connection;
    private Application context;   //当前对象
    StudentApplication app;
    public LoginAction() {
    }
    public LoginAction(Application context){
        this.context=context;
        connection=Jsoup.connect("http://www.sise.com.cn/");
    }

    /**获取url后的参数
     * @param strURL
     * @throws IOException
     */
    public static String UrlPage(String strURL)
    {
        String strPage=null;
        String[] arrSplit=null;


        arrSplit=strURL.split("[?]");
        if(strURL.length()>0)
        {
            if(arrSplit.length>1)
            {
                if(arrSplit[arrSplit.length-1]!=null)
                {
                    strPage=arrSplit[arrSplit.length-1];
                }
            }
        }

        return strPage;
    }


    private Map<String, String> getHiddentFieldData() {
        Map<String, String> map = new HashMap<String, String>();
        connection = Jsoup.connect(StudentURL.LOGIN);
        try {
            Document document = connection.get();
            Elements inputField = document.getElementsByTag("form");
            map.put("name", inputField.get(0).childNode(1).attr("name"));
            map.put("value", inputField.get(0).childNode(1).attr("value"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Log.i("hidentData", map.toString());
        return map;
    }

    public LoginAction(String userName, String passwd,Application context) {
        this.userName = userName;
        this.passwd = passwd;
        this.context=context;
    }

    /**
     *
     * param   userName
     *            学号
     * param passwd
     *            密码
     * @return 登录结果
     */
    public int doLogin() {
        Map<String, String> hiddent = getHiddentFieldData();

		/*
		 * flag ==> 1 (成功) ==> 2 (学号错误) ==> 3 (密码错误)
		 */
        int flag = 0;
        try {
            Map<String, String> postData = new HashMap<String, String>();
            postData.put(hiddent.get("name"), hiddent.get("value"));
            postData.put("username", userName);
            postData.put("password", passwd);
            connection.cookies(connection.response().cookies());
            /*
            *保存cookies
             */
            app = (StudentApplication) getApplication();
            app.setSession(connection.response().cookies());

            connection.url(StudentURL.LOGIN_CHECK);
            connection.data(postData);
            // Log.i("postData", postData.toString());
            Document document = connection.post();
            // Log.i("document", document.data());
            if (document.data().indexOf("系统数据库中找不到你的数据哦") != -1) {
                flag = 2;
            }
            if (document.data().indexOf("密码错误") != -1) {
                flag = 3;
            }
            if (document.data().indexOf("index.jsp") != -1) {
                flag = 1;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 获取课程表信息
     *
     * @return 保存课程信息的二维数组
     */
    public JSONObject getSchedules() {
        return getSchedules(0, 0);
    }

    /**
     * 获取课程表信息
     *
     * @param year
     *            年份
     * @param semester
     *            学期数
     * @return 保存课程信息的二维数组
     */
    public JSONObject getSchedules(int year, int semester) {
        if (year != 0 && semester != 0) {
            Map<String, String> para = new HashMap<String, String>();
            para.put("schoolyear", String.valueOf(year));
            para.put("semester", String.valueOf(semester));
            connection.data(para);
        }
        connection.url(StudentURL.SCHEDULAR).cookies(connection.response().cookies());// 传入会话session
        try {
            Document document = connection.get();
            if (isAlive(document.data())) {
                Element body = document.body();

                // 获得学年范围：eg: 2012 2013 2014
                String schoolyear = body.getElementsByTag("select").get(0)
                        .getElementsByTag("option").html().replace("\n", " ");
                // Log.d("schoolyear", schoolyear);
                // Log.d("loginAction", body.html());
				/*
				 * 计算当前周跟上课周的差值开始
				 */
                String week = body.getElementsByTag("table").get(5)
                        .getElementsByTag("tr").get(0).child(1)
                        .getElementsByTag("span").get(1).html();
                Pattern p = Pattern.compile("\\d+");
                Matcher m = p.matcher(week);
                int weekNum = 0;
                if (m.find()) {
                    weekNum = Integer.valueOf(m.group());
                }

                Log.d(" weekNum", String.valueOf(weekNum));

                Calendar calendar = Calendar.getInstance();
                int nowWeek = calendar.get(Calendar.WEEK_OF_YEAR);
                int dValue = nowWeek - weekNum;
				/*
				 * 周数差值计算结束
				 */

                Elements trs = body.getElementsByTag("table").get(6)
                        .getElementsByTag("tr");
                // tdCount ==> 表格列数 trCount ==> 表格行数
                int tdCount = trs.get(0).children().size();
                int trCount = trs.size();
                JSONObject all = new JSONObject();
                for (int i = 0; i < tdCount; i++) {
                    JSONArray everyDay = new JSONArray();
                    for (int j = 0; j < trCount - 1; j++) {
                        String data = trs.get(j + 1).child(i).html();
                        // Log.d("data", data);
                        JSONArray dataArray = new JSONArray();
                        if (data.indexOf(",") != -1) {
                            String[] spli = data.split(", ");
                            for (String string : spli) {
                                dataArray.put(string.replace("<strong>", "")
                                        .replace("</strong>", "")
                                        .replace("<br>", ""));
                            }
                            everyDay.put(dataArray);
                        } else {

                            dataArray.put(data.replace("<strong>", "")
                                    .replace("</strong>", "")
                                    .replace("<br>", ""));
                            everyDay.put(dataArray);
                        }

                    }
                    all.put(trs.get(0).child(i).html().replace("<strong>", "")
                            .replace("</strong>", ""), everyDay);
                }
                all.put("dValue", dValue);
                all.put("schoolyear", schoolyear);
                // Log.d("LoginAction", all.toString());
                return all;
            } else {
                if (doLogin() == 1) {
                    getSchedules(year, semester);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取学生的ID
     *
     * @return
     */
    public String getStudentID() {

        connection.url(StudentURL.MAIN).cookies(connection.response().cookies());
        try {
            Document document = connection.get();
            if (isAlive(document.data())) {
                Element body = document.body();
                Elements tables = body.getElementsByClass("tablehead");
                Elements tds = tables.get(0).getElementsByAttribute("onclick");
                String src = tds.toString();
                String result = src.substring(src.indexOf("studentid") + 10,
                        src.indexOf("onmouseover") - 3);
                return result;
            } else {
                if (doLogin() == 1) {
                    getStudentID();
                }
            }
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return null;
    }


    /*
    *获取学生个人信息
    * int i=0  个人信息
    *   i=1  课表
    *    i=2  考试时间表
    *    i=3  考勤信息
    *    i=4  平时信息
    *    i=5  奖惩记录
     */
    public String getStudentID(StudentApplication application,int number) {

        connection.url(StudentURL.MAIN).cookies(application.getSession());
        try {
            Document document = connection.get();
            if (isAlive(document.data())) {
                Element body = document.body();
                Elements tables = body.getElementsByClass("tablehead");
                Elements tds = tables.get(number).getElementsByAttribute("onclick");
                String src = tds.toString();
                String result = src.substring(src.indexOf("studentid") + 10,
                        src.indexOf("onmouseover") - 3);
                return result;
            } else {
                if (doLogin() == 1) {
                    getStudentID();
                }
            }
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return null;
    }

    /**
     * 获取学生个人信息
     *
     * @return
     */
    public JSONObject getStudentInfo() {
        JSONObject allInfo = new JSONObject();
        String studengID = getStudentID();
        connection.url(StudentURL.STUDENT_INFO).cookies(
                connection.response().cookies());
        Map<String, String> data = new HashMap<String, String>();
        data.put("method", "doMain");
        data.put("studentid", studengID);
        connection.data(data);
        try {
            Document document = connection.get();
            if (isAlive(document.data())) {
                Element body = document.body();
                // Elements table1 = body.getElementsByClass("table1");
                // System.out.println(table1.toString());
                // 获取学生信息开始

                Elements class_td_left = body.getElementsByClass("td_left");
                List<String> studentInfos = new ArrayList<String>();
                for (Element element : class_td_left) {
                    String divData = element.getElementsByTag("div").html();
                    if (divData.length() > 1) {
                        studentInfos.add(divData);
                    }
                }
                allInfo.put(StudentInfo.id, studentInfos.get(0));
                allInfo.put(StudentInfo.name, studentInfos.get(1));
                allInfo.put(StudentInfo.grade, studentInfos.get(2));
                allInfo.put(StudentInfo.specialty, studentInfos.get(3));
                allInfo.put(StudentInfo.cardID, studentInfos.get(4));
                allInfo.put(StudentInfo.mail, studentInfos.get(5));
                allInfo.put(StudentInfo.teacher, studentInfos.get(6));
                allInfo.put(StudentInfo.instructor, studentInfos.get(7));
                // 获取学生信息结束

                // 获取必修课的总学分开始
                String requiredCourseSum = body
                        .getElementsContainingOwnText("必修课的总学分").get(0)
                        .parent().parent().parent().parent()
                        .getElementsByAttributeValue("align", "left").html();
                allInfo.put(StudentInfo.requiredCourseSum, requiredCourseSum);
                // 获取必修课的总学分结束

                // 获取必修课的已修学分开始
                String requiredCourseHas = body
                        .getElementsContainingOwnText("必修课已修学分").get(0)
                        .parent().parent().parent().parent()
                        .getElementsByAttributeValue("align", "left").get(0)
                        .html();
                allInfo.put(StudentInfo.requiredCourseHas, requiredCourseHas);
                // 获取必修课的已修学分结束

                // 获取选修课已修学分开始
                String selectiveCourseHas = body
                        .getElementsContainingOwnText("选修课已修学分").get(0)
                        .parent().parent().parent().parent()
                        .getElementsByAttributeValue("align", "left").get(0)
                        .html();
                allInfo.put(StudentInfo.selectiveCourseHas, selectiveCourseHas);
                // 结束

                // 累计已修学分
                allInfo.put(StudentInfo.hasCredit,
                        String.valueOf(Float.valueOf(allInfo
                                .getString("requiredCourseHas"))
                                + Float.valueOf(allInfo
                                .getString("selectiveCourseHas"))));

                // 累计在读课程学分
                String crediting = body
                        .getElementsContainingOwnText("累计在读课程学分").get(0)
                        .parent().parent().parent().parent()
                        .getElementsByTag("font").get(1).html();
                allInfo.put(StudentInfo.crediting, crediting);

                // 预期获得学分
                String willCredit = body.getElementsContainingOwnText("预期获得学分")
                        .get(0).parent().parent().parent().parent()
                        .getElementsByTag("font").get(1).html();
                allInfo.put(StudentInfo.willCredit, willCredit);

                // 平均学分绩点
                String point = body.getElementsContainingOwnText("平均学分绩点")
                        .get(0).parent().parent().parent().parent()
                        .getElementsByTag("font").get(1).html();
                allInfo.put(StudentInfo.point, point);
                return allInfo;
                // 获取已修课程

            } else {
                if (doLogin() == 1) {
                    getStudentInfo();
                }
            }
        } catch (Exception e) {
            // Log.e("Exception", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取学生成绩
     */
    public JSONObject getObliScore(){
        JSONObject allstudentscore=new JSONObject();
        StudentApplication application=(StudentApplication)getApplication();
    //    Log.d("LogAction","dddd------------"+application.getSession());


//        connection.url(StudentURL.STUDENT_INFO).cookies(
//                application.getSession());   //重新获取cookies

        String studengID = getStudentID(application,0);
        Map<String, String> data = new HashMap<String, String>();
        data.put("method", "doMain");
        data.put("studentid", studengID);
    //    connection.data(data);
        try {
            connection=Jsoup.connect(StudentURL.STUDENT_INFO)
                    .data(data)
                    .cookies(application.getSession())
                    .timeout(2000);
            Document document = connection.get();
           if( isAlive(document.data())) {
            //   Element body = document.body();
               // Elements table1 = body.getElementsByClass("table1");
               // System.out.println(table1.toString());
               // 获取学生信息开始
                          String item[]={"term","courseCode","courseName","credit","assessment","firstCourse","repairCourse","schoolTrem","score","obtain"};
               Elements th1=   document.select("table").get(6).select("tr");
               List<JSONObject> studentsInfos= new ArrayList<JSONObject>();
          //     List<String> studentInfos = new ArrayList<String>();
               for(int s= 1;s<th1.size();s++){
//                   Student student=new Student();
//                   List<JSONObject> list=new ArrayList<>();
                    JSONObject jsonObject=new JSONObject();
                   Elements td1 = th1.get(s).select("td");
                   for(int i=0;i<td1.size();i++){

                       jsonObject.put(item[i], td1.get(i).text());
                   }
//                       student.setTerm( td1.get(0).text());//学期
//                       student.setCourseCode(td1.get(1).text());  //课程代码
//                       student.setCourseName(td1.get(2).text());  //课程名
//                       student.setCredit(td1.get(3).text());  //学分
//                       student.setAssessment(td1.get(4).text());  //考核方式
//                        student.setFirstCourse(td1.get(5).text());  //先修课程
//                      student.setRepairCourse(td1.get(6).text());  //同修课程
//                     student.setSchoolTrem(td1.get(7).text());  //修读学年
//                     student.setScore(td1.get(8).text());  //成绩
//                     student.setObtain(td1.get(9).text());  //已得学分
                   studentsInfos.add(jsonObject);
               }


               Elements th2=   document.select("table").get(9).select("tr");
              // List<Student> studentsInfos1= new ArrayList<Student>();
                    List<JSONObject> studentInfos1 = new ArrayList<JSONObject>();
               for(int s= 1;s<th2.size();s++){
                     JSONObject jsonObject=new JSONObject();
                   Elements td1 = th2.get(s).select("td");
                   for(int i=0;i<td1.size();i++){

                       jsonObject.put(item[i+1], td1.get(i).text());
                   }
                   studentInfos1.add(jsonObject);
                 //  student.setTerm( td1.get(0).text());//学期
//                   student.setCourseCode(td1.get(0).text());  //课程代码
//                   student.setCourseName(td1.get(1).text());  //课程名
//                   student.setCredit(td1.get(2).text());  //学分
//                   student.setAssessment(td1.get(3).text());  //考核方式
//                   student.setFirstCourse(td1.get(4).text());  //先修课程
//                   student.setRepairCourse(td1.get(5).text());  //同修课程
//                   student.setSchoolTrem(td1.get(6).text());  //修读学年
//                   student.setScore(td1.get(7).text());  //成绩
//                   student.setObtain(td1.get(8).text());  //已得学分
                //   studentsInfos1.add(student);
               }
                allstudentscore.put("obligatory",studentsInfos);  //必修
                allstudentscore.put("elective",studentInfos1);  //选修
               Log.d("选修",studentInfos1.toString());
               return allstudentscore;
           }else{
               if (doLogin() == 1) {
                   getStudentInfo();
               }
           }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }
    /*
    *获取考试时间表
     */
    public  JSONObject getTesttime(){
        JSONObject alltesttime=new JSONObject();

        connection.url(StudentURL.MAIN).cookies(connection.response().cookies());
        try {
            Document document = connection.get();
            if (isAlive(document.data())) {
                Element body = document.body();
                Elements tables = body.getElementsByClass("tablehead");
                Elements tds = tables.get(2).getElementsByAttribute("onclick");
                String src = tds.toString();
       StudentApplication application=(StudentApplication)getApplication();
               String studentid=getStudentID(application,2);
                Map<String, String> mapRequest= new HashMap<String,String>();
                mapRequest.put("method", "doMain");
                mapRequest.put("studentid", studentid);
                Log.d("----------LoginAction","---"+mapRequest);

//                  String strRequestKeyAndValues="";
//                  for(String strRequestKey: mapRequest.keySet()) {
//           String strRequestValue=mapRequest.get(strRequestKey);
//              strRequestKeyAndValues+="key:"+strRequestKey+",Value:"+strRequestValue+";";
//             }
//                Log.d("LogAction", strRequestKeyAndValues);

                connection.url(StudentURL.TEST).cookies(connection.response().cookies()).data(mapRequest).timeout(2000);
                Document document1=connection.get();
                Elements alltimeelements=document1.getElementsByClass("table").select("tr");

                List<List<String>> list1=new ArrayList<List<String>>();
                if(alltimeelements==null){
                    return null;
                }else {
                    for(int i = 1;i<alltimeelements.size();i++){
	            Elements td = alltimeelements.get(i).select("td");
                        List<String> list=new ArrayList<String>();
	            for(int j = 0;j<td.size();j++){
	                String text = td.get(j).text();
	                list.add(text);
	            }
                 list1.add(list);
	        }
                    Log.d("LogAction","--<<<>>>"+list1.toString());
                    alltesttime.put("TestTime",list1);
                    return  alltesttime;
                }
                /*
                找出考试时间
                 */
            } else {
                if (doLogin() == 1) {
                    getStudentID();
                }
            }
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return null;
    }
    /*
    *考勤信息
      */

    public JSONObject getAttend(){
        JSONObject allAttend=new JSONObject();
        connection.url(StudentURL.MAIN).cookies(connection.response().cookies());
        try {
            Document document = connection.get();
            Elements links1 = document.select("td[onclick]");
            String myStr="";
            for(Element link : links1) {
                if(link.text().equals("考勤信息")) {
                    myStr=link.attr("onclick").toString();
              String     path=UrlPage(myStr.substring(0, myStr.length() - 1));
                    path=StudentURL.ATTEND+"?"+path;
                    Document doctext=Jsoup.connect(path)
                            .cookies(connection.response().cookies())
                            .method(Connection.Method.GET)
                            .get();

                    Elements th1=   doctext.getElementsByClass("table").select("tr") ;
                    List<List<String>> list1=new ArrayList<List<String>>();

                    for(int s= 1;s<th1.size();s++){
                        Elements td1 = th1.get(s).select("td");
                        List<String> cjList=new ArrayList<String>();
                        for(int j = 0;j<td1.size();j++){
                            String text = td1.get(j).text();
                            cjList.add(text);
                        }
                        list1.add(cjList);
                    }
                    allAttend.put("reward",list1);
                }
            }
            return allAttend;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
    晚归信息获取
     */

    public JSONObject getlatemessage()
    {
        JSONObject allLater=new JSONObject();
        connection.url(StudentURL.MAIN).cookies(connection.response().cookies());
        try {
            Document document = connection.get();
            Elements links1 = document.select("td[onclick]");
            String myStr="";
            for(Element link : links1) {
                if(link.text().equals("晚归、违规用电记录")) {
                    myStr=link.attr("onclick").toString();
                    String     path=UrlPage(myStr.substring(0, myStr.length() - 1));
                    path=StudentURL.LATER+"?"+path;
                    Document doctext=Jsoup.connect(path)
                            .cookies(connection.response().cookies())
                            .method(Connection.Method.GET)
                            .get();

                    Elements th1=   doctext.getElementsByClass("table").select("tr") ;
                    List<List<String>> list1=new ArrayList<List<String>>();

                    for(int s= 1;s<th1.size();s++){
                        Elements td1 = th1.get(s).select("td");
                        List<String> cjList=new ArrayList<String>();
                        for(int j = 0;j<td1.size();j++){
                            String text = td1.get(j).text();
                            cjList.add(text);
                        }
                        list1.add(cjList);
                    }
                    allLater.put("later",list1);
                }
            }
            return allLater;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /*
    奖罚情况信息
     */
    public JSONObject getrewardpunish()
    {
        JSONObject allReward=new JSONObject();
        connection.url(StudentURL.MAIN).cookies(connection.response().cookies());
        try {
            Document document = connection.get();
            Elements links1 = document.select("td[onclick]");
            String myStr="";
       //     Log.d("-------<<<<<<<<<<","查看奖惩记录"+links1.toString());
            for(Element link : links1) {
                if(link.text().equals("奖惩记录")) {
                    myStr=link.attr("onclick").toString();
                    String  path=UrlPage(myStr.substring(0, myStr.length() - 1));
                    path=StudentURL.REWRAD+"?"+path;
             //       Log.d("-------<<<<<<<<<<", "查看奖惩记录" + path);
                    connection.url(path);
                    connection.cookies(connection.response().cookies());
                     Document doctext=  connection.get();
                    Elements th1=   doctext.getElementsByClass("table1").get(1).select("tr") ;
             //       Log.d("-------<<<<<<<<<<","查看奖惩记录"+doctext.toString());
                    List<List<String>> list1=new ArrayList<List<String>>();

                    for(int s= 1;s<th1.size();s++){
                        Elements td1 = th1.get(s).select("td");
                        List<String> cjList=new ArrayList<String>();
                        for(int j = 0;j<td1.size();j++){
                            String text = td1.get(j).text();
                            cjList.add(text);
                        }
                    //    Log.d("<<<<---->>>11",cjList.toString());
                        list1.add(cjList);
                    }
                    Elements th2=doctext.getElementsByClass("table1").last().select("tr");
                    for(int s= 1;s<th2.size();s++){
                        Elements td1 = th2.get(s).select("td");
                        List<String> cjList=new ArrayList<String>();
                        for(int j = 0;j<td1.size();j++){
                            String text = td1.get(j).text();
                            cjList.add(text);
                       //     Log.d("<<<<---->>>22", cjList.toString());
                        }
                        list1.add(cjList);
                    }
                    allReward.put("reward", list1);
                //    Log.d("-------<<<<<<<<<<","查看奖惩记录"+list1.toString());
                }
            }
            return allReward;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    /*
   开设课程情况信息
    */
    public JSONObject getoffercourse()
    {
        JSONObject allOffer=new JSONObject();
        connection.url(StudentURL.MAIN).cookies(connection.response().cookies());
        try {
            Document document = connection.get();
            Elements links1 = document.select("td[onclick]");
            String myStr="";
            //     Log.d("-------<<<<<<<<<<","查看奖惩记录"+links1.toString());
            for(Element link : links1) {
                if(link.text().equals("查看开设课程")) {
                    myStr=link.attr("onclick").toString();
                    String  path=UrlPage(myStr.substring(0, myStr.length() - 1));
                    path=StudentURL.OFFER+"?"+path;

                    connection.url(path);
                    connection.cookies(connection.response().cookies());
                    Document doctext=  connection.get();
                    List<List<String>> list1=new ArrayList<List<String>>();

                    Elements th1=   doctext.getElementsByClass("table1").get(1).select("tr") ;

                    for(int s= 1;s<th1.size();s++){
                        Elements td1 = th1.get(s).select("td");
                        List<String> cjList=new ArrayList<String>();
                        for(int j = 0;j<td1.size();j++){
                            String text = td1.get(j).text();
                            cjList.add(text);

                        }

                        list1.add(cjList);
                    }

                    Elements th2=doctext.getElementsByClass("table1").get(2).select("tr");
                    for(int s= 2;s<th2.size();s++){
                        Elements td1 = th2.get(s).select("td");
                        List<String> cjList=new ArrayList<String>();
                        for(int j = 0;j<td1.size();j++){
                            String text = td1.get(j).text();
                            cjList.add(text);

                        }
                        list1.add(cjList);
                    }

                    Elements th3=doctext.getElementsByClass("table1").last().select("tr");
                    for(int s= 2;s<th3.size();s++){
                        Elements td1 = th3.get(s).select("td");
                        List<String> cjList=new ArrayList<String>();
                        for(int j = 0;j<td1.size();j++){
                            String text = td1.get(j).text();
                            cjList.add(text);
                            //     Log.d("<<<<---->>>22", cjList.toString());
                        }
                        list1.add(cjList);
                    }


                    Log.d("qqqqqqqqqq",list1.toString());
                    allOffer.put("offer", list1);

                }
            }
            return allOffer;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }



    /**
     * 判断是否还在会话
     *
     * @return
     */
    private boolean isAlive(String responds) {
        if (responds.indexOf("login.jsp") != -1) {
            return false;
        }
        return true;
    }


    public Application getApplication() {
        return this.context;
    }
}
