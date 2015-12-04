package com.bn.sample.add;

/**
 * Created by Administrator on 2015/8/16.
 */

//成绩类，用来存储某一课成绩的详细信息
public class GradeStorage {
    String courseNum;//课程号
    String courseName;//课程名
    double credit;//学分
    String score;//分数
    String year;//该课程所在学年，形式为“2012-2013”
    int term;//该课程所在学年的学期
    String attribute;//该门课程的属性：必选/任选/限选/...
    String properties;//该门课的性质
    double GPA;//该门课的绩点


    public void Set(String courseNum,String courseName,double credit,String score,String year,int term,String attribute){
        this.courseNum=courseNum;
        this.courseName=courseName;
        this.credit=credit;
        this.score=score;
        this.year=year;
        this.term=term;
        this.attribute=attribute;
    }

    public String getCourseNum() {
        return courseNum;
    }

    public void setCourseNum(String courseNum) {
        this.courseNum = courseNum;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public double getGPA() {
        int mark;
        if(score.equals("优"))
            mark=95;
        else if(score.equals("良"))
            mark=85;
        else if(score.equals("中"))
            mark=75;
        else if(score.equals("及格"))
            mark=65;
        else if(score.equals("不及格"))
            mark=59;
        else if(score.equals("通过"))
            mark=75;
        else if(score.equals("不通过"))
            mark=59;
        else
            mark=Integer.valueOf(score).intValue();

        if(mark<60)
            this.GPA=0;
        else
            this.GPA=(double)(mark-50)/10;

        return GPA;
    }

}
