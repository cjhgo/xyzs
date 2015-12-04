package com.bn.sample.add;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.bn.sample.R;
import com.bn.sample.Sample1_1_Activity;

/**
 * Created by Administrator on 2015/8/4.
 */

//关于成绩及绩点查询的主界面,与grade.xml相对应
public class grade extends Activity {
    TabHost grade_tabHost;
    TextView grade_showGrade0,grade_showGrade1,grade_showGrade2,grade_showGrade3,grade_showGrade4;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grade);

        final List<GradeStorage> list=new ArrayList<GradeStorage>();//用来存储所得到的每一门课程的具体信息


        //接收传递过来的数据
        final Intent intent=this.getIntent();
        Bundle bundle=intent.getExtras();
        String result=bundle.getString("data");



        String pattern="<table id=\"dataList\"[\\s\\S]+?(<tr>[\\s\\S]+</tr>)[\\s\\S]+?</table>";
        Matcher matcher=Pattern.compile(pattern).matcher(result);
        String newresult="";
        while(matcher.find()){
            newresult=matcher.group(1);
        }

        String newpattern="<tr>[^<]*<td>[^<]*</td>[^<]*<td>([^<]*)</td>" +
                "[^<]*<td align=\"left\">([^<]*)</td>[^<]*<td align=\"left\">([^<]*)</td>" +
                "[^<]*<td style=\" \">([^<]*)</td>[^<]*<td>([^<]*)</td>[^<]*<td>([^<]*)</td>" +
                "[^<]*<td>([^<]*)</td>[^<]*<td>([^<]*)</td>[^<]*<td>([^<]*)</td>([^<]*)</tr>";
        Matcher newmatcher=Pattern.compile(newpattern).matcher(newresult);
        while(newmatcher.find()){
            GradeStorage gradeStorage=new GradeStorage();
            String courseTime=newmatcher.group(1);
            gradeStorage.setYear(courseTime.substring(0,9));
            gradeStorage.setTerm(courseTime.charAt(10)-'0');
            gradeStorage.setCourseNum(newmatcher.group(2));
            gradeStorage.setCourseName(newmatcher.group(3));
            gradeStorage.setScore(newmatcher.group(4));
            gradeStorage.setCredit(Double.parseDouble(newmatcher.group(5)));
            gradeStorage.setAttribute(newmatcher.group(8));
            gradeStorage.setProperties(newmatcher.group(9));

            list.add(gradeStorage);
        }

        grade_tabHost=(TabHost)findViewById(R.id.grade_TabHost_showGrade);
        grade_tabHost.setup();

        grade_showGrade0=(TextView)findViewById(R.id.grade_textView_showGrade0);
        grade_showGrade1=(TextView)findViewById(R.id.grade_textView_showGrade1);
        grade_showGrade2=(TextView)findViewById(R.id.grade_textView_showGrade2);
        grade_showGrade3=(TextView)findViewById(R.id.grade_textView_showGrade3);
        grade_showGrade4=(TextView)findViewById(R.id.grade_textView_showGrade4);

        //计算绩点，并把绩点和成绩显示在对于位置
        String showGrade="您目前可查询的成绩以及绩点情况如下：";
        String showGradeLeft="";
        if(list.size()==0)
            showGrade="您目前尚无可查询成绩";
        else{
            double sumScore=0;//总的各科绩点与学分的成绩之和
            double sumCredit=0;//总的学分数

            for(int year=1;year<=4;year++){
                String yearShowGrade="";
                double yearSumScore=0;//本学年的各科绩点与学分的成绩之和
                double yearSumCredit=0;//本学年的总的学分数
                boolean yearIsEmpty=true;//判断本学年成绩是否为空，默认为空
                String termShowGrade="";

                for(int term=1;term<=2;term++){
                    double termSumScore=0;//本学期的各科绩点与学分的成绩之和
                    double termSumCredit=0;//本学期的总的学分数
                    boolean termIsEmpty=true;//判断本学期成绩是否为空，默认为空
                    String termShowGradeLeft="";

                    //设置三个变量用于计算年份
                    String courseYear1=list.get(0).getYear();
                    String courseYear2=list.get(0).getYear();
                    int yearCount=1;

                    Iterator<GradeStorage> iterator=list.iterator();
                    while(iterator.hasNext()){
                        GradeStorage gradeStorage= iterator.next();
                        //判断学年并对courseYear1和courseYear2做对应更改
                        courseYear1=courseYear2;
                        courseYear2=gradeStorage.getYear();
                        if(courseYear1.equals(courseYear2)==false)
                            yearCount++;

                        if(yearCount>year)
                            break;
                        if(yearCount>=year&&gradeStorage.getTerm()>term)
                            break;
                        if(yearCount==year&&gradeStorage.getTerm()==term){
                            //置判断成绩是否为空的标记为非空
                            yearIsEmpty=false;
                            termIsEmpty=false;
                            //显示该门课成绩
                            termShowGradeLeft=termShowGradeLeft+"\n"+gradeStorage.getCourseName()+":"+gradeStorage.getScore();
                            //体育课不算在绩点内
                            if(gradeStorage.getCourseName().contains("体育"))
                                termShowGradeLeft=termShowGradeLeft+"(不在绩点计算范围内)";
                            else if(gradeStorage.getAttribute().equals("任选"))
                                termShowGradeLeft=termShowGradeLeft+"(不在绩点计算范围内)";
                            else{
                                //把该门课的绩点与学分分别加到该学期的绩点和与学分和上
                                termSumScore+=(gradeStorage.getGPA()*gradeStorage.getCredit());
                                termSumCredit+=gradeStorage.getCredit();
                            }
                        }
                    }

                    //把该学期的绩点和与学分和分别加到该学年的绩点和与学分和上
                    yearSumScore+=termSumScore;
                    yearSumCredit+=termSumCredit;

                    //计算该学期的平均绩点
                    double termGPA=0;
                    if(termSumCredit!=0)
                        termGPA=termSumScore/termSumCredit;
                    if(termIsEmpty==false)
                        termShowGrade=termShowGrade+"\n\n第"+term+"学期绩点："+termGPA+"\n各科成绩为："+termShowGradeLeft;
                    else
                        termShowGrade=termShowGrade+"\n\n第"+term+"学期暂无可查询成绩";
                }

                //把该学年的绩点和与学分和分别加到总的绩点和与学分和上
                sumScore+=yearSumScore;
                sumCredit+=yearSumCredit;

                //计算该学年的平均绩点
                double yearGPA=0;
                if(yearSumCredit!=0)
                    yearGPA=yearSumScore/yearSumCredit;
                if(yearIsEmpty==false)
                    yearShowGrade="本学年绩点："+yearGPA+termShowGrade;

                if(yearShowGrade.equals(""))
                    yearShowGrade="当前学年暂无可查询成绩！";

                //根据学年把要显示的字符串添加到相应的文本框内
                switch(year){
                    case 1:grade_showGrade1.setText(yearShowGrade);showGradeLeft+="\n大一";break;
                    case 2:grade_showGrade2.setText(yearShowGrade);showGradeLeft+="\n大二";break;
                    case 3:grade_showGrade3.setText(yearShowGrade);showGradeLeft+="\n大三";break;
                    case 4:grade_showGrade4.setText(yearShowGrade);showGradeLeft+="\n大四";break;
                }
                showGradeLeft=showGradeLeft+"学年的绩点："+yearGPA;
            }

            //计算总的平均绩点
            double GPA=0;
            if(sumCredit!=0)
                GPA=sumScore/sumCredit;
            showGrade+="\n\n总绩点："+GPA+showGradeLeft+"\n(各学年的具体成绩情况详见对应面板)";
        }
        grade_showGrade0.setText(showGrade);


        //设置各个面板的标题与内容
        grade_tabHost.addTab(grade_tabHost.newTabSpec("tab0").setIndicator("总成绩").setContent(R.id.grade_textView_showGrade0));
        grade_tabHost.addTab(grade_tabHost.newTabSpec("tab1").setIndicator("大一").setContent(R.id.grade_textView_showGrade1));
        grade_tabHost.addTab(grade_tabHost.newTabSpec("tab2").setIndicator("大二").setContent(R.id.grade_textView_showGrade2));
        grade_tabHost.addTab(grade_tabHost.newTabSpec("tab3").setIndicator("大三").setContent(R.id.grade_textView_showGrade3));
        grade_tabHost.addTab(grade_tabHost.newTabSpec("tab4").setIndicator("大四").setContent(R.id.grade_textView_showGrade4));
        //设置默认显示的面板的index
        grade_tabHost.setCurrentTab(0);
    }

    //设置点击返回按钮时的响应事件为返回到主界面
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK){
            finish();
            Intent intent=new Intent();
            intent.setClass(grade.this,Sample1_1_Activity.class);
            startActivity(intent);
        }
        return false;
    }

}
