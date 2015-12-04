package com.bn.sample.add;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;

import com.bn.sample.JobStorage;
import com.bn.sample.R;
import com.bn.sample.add.HttpConnectionGet;
import com.bn.sample.add.job_specificText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/6/3.
 */

//关于工作信息的主界面,与job.xml相对应
public class job extends Activity{

    TabHost job_tabHost;
    ListView job_jobItem;
    int job_index=1;//加载招聘信息时的页码数
    String jobUrl;
    List<Map<String,Object>> sList=new ArrayList<Map<String, Object>>();
    ListAdapter listAdapter;//用来绑定的适配器

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job);

        job_jobItem=(ListView)findViewById(R.id.job_listView_jobItem);
        job_tabHost=(TabHost)findViewById(R.id.job_TabHost_showJob);
        job_tabHost.setup();

        final List<JobStorage> list=new ArrayList<JobStorage>();//用来存储所得到的每一条招聘信息的各项内容

        //设置各个面板的标题与内容
        job_tabHost.addTab(job_tabHost.newTabSpec("tab0").setIndicator("招聘公告").setContent(R.id.job_listView_jobItem));
        job_tabHost.addTab(job_tabHost.newTabSpec("tab1").setIndicator("招聘会").setContent(R.id.job_listView_jobItem));
        job_tabHost.addTab(job_tabHost.newTabSpec("tab2").setIndicator("宣讲会").setContent(R.id.job_listView_jobItem));
        job_tabHost.addTab(job_tabHost.newTabSpec("tab3").setIndicator("全职岗位").setContent(R.id.job_listView_jobItem));
        job_tabHost.addTab(job_tabHost.newTabSpec("tab4").setIndicator("实习岗位").setContent(R.id.job_listView_jobItem));

        //设置改变标签时的响应事件
        job_tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                //招聘公告
                if(tabId.equals("tab0")){
                    list.clear();//清空原list中的项目
                    sList.clear();//清空原sList中的项目
                    job_index=1;//重置页码为1

                    //连网获取学校就业信息网招聘界面信息，存储到result中
                    HttpConnectionGet httpConnectionGet=new HttpConnectionGet();
                    jobUrl="http://just.91job.gov.cn/campus";
                    httpConnectionGet.setUrl(jobUrl);
                    final String result=new String(httpConnectionGet.getResponse());

                    //通过匹配得到每一条招聘项目的详细信息
                    //String pattern="<ul class=\"infoList\"[\\s\\S]+?a href=\"([^\"]*)[^>]*>([^<]*)([^>]*>){3}([^<]*)([^>]*>){2}([^<]*)";
                    String pattern="<li>[\\s\\S]*?<a href=\"([^\"]*)\">([^<]*)[\\s\\S]*?<li class=\"css-begintime\">([^<]*)</li>";
                    Matcher matcher= Pattern.compile(pattern).matcher(result);

                    //通过循环把得到每一条招聘条目的具体信息放到一个JobStorage类的对象中，再放入集合list中
                    while(matcher.find()){
                        String job_hyperlink="http://just.91job.gov.cn/"+matcher.group(1);
                        String job_companyName=matcher.group(2);
                        String job_time=matcher.group(3);

                        JobStorage jobStorage=new JobStorage();
                        jobStorage.setHyperlink(job_hyperlink);
                        jobStorage.setCompanyName(job_companyName);
                        jobStorage.setTime(job_time);

                        //把该条招聘项目添加到集合list中
                        list.add(jobStorage);
                    }

                    //利用for循环，将每一项的title的内容加到sList中
                    for(int i=0;i<list.size();i++){
                        Map<String,Object> map=new HashMap<String, Object>();

                        //定义显示在listView的条目中的内容格式
                        String job_show=list.get(i).getCompanyName()+"\n"+list.get(i).getTime();
                        map.put("job_title",job_show);
                        sList.add(map);
                    }

                    //声明适配器，并将其绑定到ListView控件
                    listAdapter=new SimpleAdapter(job.this,sList,
                            R.layout.job_titleitem,new String[]{"job_title"},
                            new int[]{R.id.job_textView_jobItem});
                    job_jobItem.setAdapter(listAdapter);
                }

                //招聘会
                else if(tabId.equals("tab1")){
                    list.clear();//清空原list中的项目
                    sList.clear();//清空原sList中的项目
                    job_index=1;//重置页码为1

                    //连网获取学校就业信息网招聘会界面信息，存储到result中
                    HttpConnectionGet httpConnectionGet=new HttpConnectionGet();
                    jobUrl="http://just.91job.gov.cn/jobfair";
                    httpConnectionGet.setUrl(jobUrl);
                    final String result=new String(httpConnectionGet.getResponse());

                    //通过匹配得到每一条招聘会项目的详细信息
                    //String pattern="<ul class=\"infoList jobfairList\">[\\s\\S]+?<a href=\"([^\"]*)[^>]*>([^<]*)" +
                     //               "[^=]*[^>]*>([^<]*)[^=]*[^>]*>([^<]*)[^=]*[^>]*>([^<]*)[^=]*[^>]*>([^<]*)</li>";
                    String pattern="<li>[\\s\\S]*?<a href=\"([^\"]*)\">([^<]*)[\\s\\S]*?<li class=\"css-begintime\">([^<]*)</li>[\\s\\S]*?<li class=\"css-begintime\">([^<]*)</li>";
                    Matcher matcher= Pattern.compile(pattern).matcher(result);

                    //通过循环把得到每一条招聘会条目的具体信息放到一个JobStorage类的对象中，再放入集合list中
                    while(matcher.find()){
                        String job_hyperlink="http://just.91job.gov.cn/"+matcher.group(1);
                        String job_companyName=matcher.group(2);
                        String job_workplace=matcher.group(4);
                        String job_time=matcher.group(3);

                        JobStorage jobStorage=new JobStorage();
                        jobStorage.setHyperlink(job_hyperlink);
                        jobStorage.setCompanyName(job_companyName);
                        jobStorage.setWorkPlace(job_workplace);
                        jobStorage.setTime(job_time);

                        //把该条招聘会项目添加到集合list中
                        list.add(jobStorage);
                    }

                    //利用for循环，将每一项的title的内容加到sList中
                    List<Map<String,Object>> sList=new ArrayList<Map<String, Object>>();
                    for(int i=0;i<list.size();i++){
                        Map<String,Object> map=new HashMap<String, Object>();

                        //定义显示在listView的条目中的内容格式
                        String job_show=list.get(i).getCompanyName()+"\n"+list.get(i).getWorkPlace()+"\n"+list.get(i).getTime();
                        map.put("job_title",job_show);
                        sList.add(map);
                    }

                    //声明适配器，并将其绑定到ListView控件
                    listAdapter=new SimpleAdapter(job.this,sList,
                            R.layout.job_titleitem,new String[]{"job_title"},
                            new int[]{R.id.job_textView_jobItem});
                    job_jobItem.setAdapter(listAdapter);
                }


                //宣讲会
                else if(tabId.equals("tab2")){
                    list.clear();//清空原list中的项目
                    sList.clear();//清空原sList中的项目
                    job_index=1;//重置页码为1

                    //连网获取学校就业信息网宣讲会界面信息，存储到result中
                    HttpConnectionGet httpConnectionGet=new HttpConnectionGet();
                    jobUrl="http://just.91job.gov.cn/teachin";
                    httpConnectionGet.setUrl(jobUrl);
                    final String result=new String(httpConnectionGet.getResponse());

                    //通过匹配得到每一条宣讲会项目的详细信息
                    //String pattern="<ul class=\"infoList teachinList\">[\\s\\S]+?<a href=\"([^\"]*)[^>]*>([^<]*)" +
                    //        "[^=]*[^>]*>([^<]*)[^=]*[^>]*>([^<]*)[^=]*[^>]*>([^<]*)[^=]*[^>]*>([^<]*)</li>";
                    String pattern="<li>[\\s\\S]*?<a href=\"([^\"]*)\">([^<]*)[\\s\\S]*?<li class=\"css-begintime\">([^<]*)</li>[\\s\\S]*?<li class=\"css-begintime\">([^<]*)</li>";
                    Matcher matcher= Pattern.compile(pattern).matcher(result);

                    //通过循环把得到每一条宣讲会条目的具体信息放到一个JobStorage类的对象中，再放入集合list中
                    while(matcher.find()){
                        String job_hyperlink="http://just.91job.gov.cn/"+matcher.group(1);
                        String job_companyName=matcher.group(2);
                        String job_workplace=matcher.group(4);
                        String job_time=matcher.group(3);

                        JobStorage jobStorage=new JobStorage();
                        jobStorage.setHyperlink(job_hyperlink);
                        jobStorage.setCompanyName(job_companyName);
                        jobStorage.setWorkPlace(job_workplace);
                        jobStorage.setTime(job_time);

                        //把该条宣讲会项目添加到集合list中
                        list.add(jobStorage);
                    }

                    //利用for循环，将每一项的title的内容加到sList中
                    List<Map<String,Object>> sList=new ArrayList<Map<String, Object>>();
                    for(int i=0;i<list.size();i++){
                        Map<String,Object> map=new HashMap<String, Object>();

                        //定义显示在listView的条目中的内容格式
                        String job_show=list.get(i).getCompanyName()+"\n"+list.get(i).getWorkPlace()+"\n"+list.get(i).getTime();
                        map.put("job_title",job_show);
                        sList.add(map);
                    }

                    //声明适配器，并将其绑定到ListView控件
                    listAdapter=new SimpleAdapter(job.this,sList,
                            R.layout.job_titleitem,new String[]{"job_title"},
                            new int[]{R.id.job_textView_jobItem});
                    job_jobItem.setAdapter(listAdapter);
                }


                //全职岗位
                else if(tabId.equals("tab3")){
                    list.clear();//清空原list中的项目
                    sList.clear();//清空原sList中的项目
                    job_index=1;//重置页码为1

                    //连网获取学校就业信息网全职岗位界面信息，存储到result中
                    HttpConnectionGet httpConnectionGet=new HttpConnectionGet();
                    jobUrl="http://just.91job.gov.cn/job/search";
                    httpConnectionGet.setUrl(jobUrl);
                    final String result=new String(httpConnectionGet.getResponse());

                    //通过匹配得到每一条全职岗位项目的详细信息
                    //String pattern="<ul class=\"infoList\">[\\s\\S]+?<a href=\"([^\"]*)[^>]*>([^<]*)" +
                    //       "([^=]*=){2}[^>]*>([^<]*)[^=]*[^>]*>([^<]*)[^=]*[^>]*>([^<]*)</li>";
                    String pattern="<li>[\\s\\S]*?<a href=\"([^\"]*)\">([^<]*)[\\s\\S]*?<li class=\"css-begintime\">([^<]*)</li>[\\s\\S]*?<li class=\"css-begintime\">([^<]*)</li>";
                    Matcher matcher= Pattern.compile(pattern).matcher(result);

                    //通过循环把得到每一条全职岗位条目的具体信息放到一个JobStorage类的对象中，再放入集合list中
                    while(matcher.find()){
                        String job_hyperlink="http://just.91job.gov.cn/"+matcher.group(1);
                        String job_positionName=matcher.group(2);
                        String job_companyName=matcher.group(3);
                        String job_time=matcher.group(4);

                        JobStorage jobStorage=new JobStorage();
                        jobStorage.setHyperlink(job_hyperlink);
                        jobStorage.setPositionName(job_positionName);
                        jobStorage.setCompanyName(job_companyName);
                        jobStorage.setTime(job_time);

                        //把该条全职岗位项目添加到集合list中
                        list.add(jobStorage);
                    }

                    //利用for循环，将每一项的title的内容加到sList中
                    List<Map<String,Object>> sList=new ArrayList<Map<String, Object>>();
                    for(int i=0;i<list.size();i++){
                        Map<String,Object> map=new HashMap<String, Object>();

                        //定义显示在listView的条目中的内容格式
                        String job_show=list.get(i).getPositionName()+"\n"+list.get(i).getCompanyName()+"\n"+list.get(i).getTime();
                        map.put("job_title",job_show);
                        sList.add(map);
                    }

                    //声明适配器，并将其绑定到ListView控件
                    listAdapter=new SimpleAdapter(job.this,sList,
                            R.layout.job_titleitem,new String[]{"job_title"},
                            new int[]{R.id.job_textView_jobItem});
                    job_jobItem.setAdapter(listAdapter);
                }


                //实习岗位
                else if(tabId.equals("tab4")){
                    list.clear();//清空原list中的项目
                    sList.clear();//清空原sList中的项目
                    job_index=1;//重置页码为1

                    //连网获取学校就业信息网实习岗位界面信息，存储到result中
                    HttpConnectionGet httpConnectionGet=new HttpConnectionGet();
                    jobUrl="http://just.91job.gov.cn/job/search/d_category%5B0%5D/0/d_category%5B1%5D/101/d_category%5B2%5D/102";
                    httpConnectionGet.setUrl(jobUrl);
                    final String result=new String(httpConnectionGet.getResponse());

                    //通过匹配得到每一条实习岗位项目的详细信息
                    //String pattern="<ul class=\"infoList\">[\\s\\S]+?<a href=\"([^\"]*)[^>]*>([^<]*)" +
                    //        "([^=]*=){2}[^>]*>([^<]*)[^=]*[^>]*>([^<]*)[^=]*[^>]*>([^<]*)</li>";
                    String pattern="<li>[\\s\\S]*?<a href=\"([^\"]*)\">([^<]*)[\\s\\S]*?<li class=\"css-begintime\">([^<]*)</li>[\\s\\S]*?<li class=\"css-begintime\">([^<]*)</li>";
                    Matcher matcher= Pattern.compile(pattern).matcher(result);

                    //通过循环把得到每一条实习岗位条目的具体信息放到一个JobStorage类的对象中，再放入集合list中
                    while(matcher.find()){
                        String job_hyperlink="http://just.91job.gov.cn/"+matcher.group(1);
                        String job_positionName=matcher.group(2);
                        String job_companyName=matcher.group(3);
                        String job_time=matcher.group(4);

                        JobStorage jobStorage=new JobStorage();
                        jobStorage.setHyperlink(job_hyperlink);
                        jobStorage.setPositionName(job_positionName);
                        jobStorage.setCompanyName(job_companyName);
                        jobStorage.setTime(job_time);

                        //把该条实习岗位项目添加到集合list中
                        list.add(jobStorage);
                    }

                    //利用for循环，将每一项的title的内容加到sList中
                    List<Map<String,Object>> sList=new ArrayList<Map<String, Object>>();
                    for(int i=0;i<list.size();i++){
                        Map<String,Object> map=new HashMap<String, Object>();

                        //定义显示在listView的条目中的内容格式
                        String job_show=list.get(i).getPositionName()+"\n"+list.get(i).getCompanyName()+"\n"+list.get(i).getTime();
                        map.put("job_title",job_show);
                        sList.add(map);
                    }

                    //声明适配器，并将其绑定到ListView控件
                    listAdapter=new SimpleAdapter(job.this,sList,
                            R.layout.job_titleitem,new String[]{"job_title"},
                            new int[]{R.id.job_textView_jobItem});
                    job_jobItem.setAdapter(listAdapter);
                }
            }
        });

        //设置默认显示的面板的index
        job_tabHost.setCurrentTab(1);
        job_tabHost.setCurrentTab(0);


        //为ListView添加点击每一项时的响应事件：跳转到该条工作信息的具体内容
        job_jobItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //得到该条工作信息的超连接
                String job_itemUrl=list.get(position).getHyperlink();

                //把该超链接传递给job_specificText,并跳转到job_specifictext.xml
                Intent intent=new Intent();
                intent.setClass(job.this,job_specificText.class);
                Bundle bundle=new Bundle();
                bundle.putString("job_itemUrl",job_itemUrl);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        //给listView添加进度条滚动的响应函数
        job_jobItem.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 当不滚动时
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    //判断是否滚动到底部
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        //修改页码数以及首页超链接后缀
                        job_index++;

                        //获取当前listView的进度条的位置，后面改变listView的内容后再恢复它的位置信息
                        int position = job_jobItem.getFirstVisiblePosition();

                        //对每个tab进行不同的加载
                        String jobUrlChanged="";

                        if(job_tabHost.getCurrentTab()==0){
                            jobUrlChanged=jobUrl+"/index?page="+Integer.toString(job_index);

                            //连网获取学校就业信息网招聘界面信息，存储到result中
                            HttpConnectionGet httpConnectionGet=new HttpConnectionGet();
                            httpConnectionGet.setUrl(jobUrlChanged);
                            final String result=new String(httpConnectionGet.getResponse());

                            //通过匹配得到每一条招聘项目的详细信息
                            //String pattern="<ul class=\"infoList\"[\\s\\S]+?a href=\"([^\"]*)[^>]*([^<]*)([^>]*>){3}([^<]*)([^>]*>){2}([^<]*)";
                            String pattern="<li>[\\s\\S]*?<a href=\"([^\"]*)\">([^<]*)[\\s\\S]*?<li class=\"css-begintime\">([^<]*)</li>";
                            Matcher matcher= Pattern.compile(pattern).matcher(result);

                            //通过循环把得到每一条招聘条目的具体信息放到一个JobStorage类的对象中，再放入集合list中
                            while(matcher.find()){
                                String job_hyperlink="http://just.91job.gov.cn/"+matcher.group(1);
                                String job_companyName=matcher.group(2);
                                String job_time=matcher.group(3);

                                JobStorage jobStorage=new JobStorage();
                                jobStorage.setHyperlink(job_hyperlink);
                                jobStorage.setCompanyName(job_companyName);
                                jobStorage.setTime(job_time);

                                //把该条招聘项目添加到集合list中
                                list.add(jobStorage);
                            }

                            //利用for循环，将每一项的title的内容加到sList中
                            sList.clear();
                            for(int i=0;i<list.size();i++) {
                                Map<String, Object> map = new HashMap<String, Object>();

                                //定义显示在listView的条目中的内容为：招聘信息的职位名称（公司名称）
                                String job_show=list.get(i).getCompanyName()+"\n"+list.get(i).getTime();
                                map.put("job_title",job_show);
                                sList.add(map);
                            }

                            //声明适配器，并将其绑定到ListView控件
                            listAdapter=new SimpleAdapter(job.this,sList,
                                    R.layout.job_titleitem,new String[]{"job_title"},
                                    new int[]{R.id.job_textView_jobItem});
                            job_jobItem.setAdapter(listAdapter);

                            //重新设置当前的滚动位置为之前的位置
                            job_jobItem.setSelection(position+1);
                        }
                        else if(job_tabHost.getCurrentTab()==1||job_tabHost.getCurrentTab()==2){ }
                        else if(job_tabHost.getCurrentTab()==3){
                            jobUrlChanged="http://just.91job.gov.cn/job/search?d_category%5B0%5D=0&d_category%5B1%5D=100&page="+job_index;

                            //连网获取学校就业信息网全职岗位界面信息，存储到result中
                            HttpConnectionGet httpConnectionGet=new HttpConnectionGet();
                            httpConnectionGet.setUrl(jobUrlChanged);
                            final String result=new String(httpConnectionGet.getResponse());

                            //通过匹配得到每一条全职岗位项目的详细信息
                            //String pattern="<ul class=\"infoList\">[\\s\\S]+?<a href=\"([^\"]*)[^>]*>([^<]*)" +
                            //       "([^=]*=){2}[^>]*>([^<]*)[^=]*[^>]*>([^<]*)[^=]*[^>]*>([^<]*)</li>";
                            String pattern="<li>[\\s\\S]*?<a href=\"([^\"]*)\">([^<]*)[\\s\\S]*?<li class=\"css-begintime\">([^<]*)</li>[\\s\\S]*?<li class=\"css-begintime\">([^<]*)</li>";
                            Matcher matcher= Pattern.compile(pattern).matcher(result);

                            //通过循环把得到每一条全职岗位条目的具体信息放到一个JobStorage类的对象中，再放入集合list中
                            while(matcher.find()){
                                String job_hyperlink="http://just.91job.gov.cn/"+matcher.group(1);
                                String job_positionName=matcher.group(2);
                                String job_companyName=matcher.group(3);
                                String job_time=matcher.group(4);

                                JobStorage jobStorage=new JobStorage();
                                jobStorage.setHyperlink(job_hyperlink);
                                jobStorage.setPositionName(job_positionName);
                                jobStorage.setCompanyName(job_companyName);
                                jobStorage.setTime(job_time);

                                //把该条全职岗位项目添加到集合list中
                                list.add(jobStorage);
                            }

                            //利用for循环，将每一项的title的内容加到sList中
                            List<Map<String,Object>> sList=new ArrayList<Map<String, Object>>();
                            for(int i=0;i<list.size();i++){
                                Map<String,Object> map=new HashMap<String, Object>();

                                //定义显示在listView的条目中的内容格式
                                String job_show=list.get(i).getPositionName()+"\n"+list.get(i).getCompanyName()+"\n"+list.get(i).getTime();
                                map.put("job_title",job_show);
                                sList.add(map);
                            }

                            //声明适配器，并将其绑定到ListView控件
                            listAdapter=new SimpleAdapter(job.this,sList,
                                    R.layout.job_titleitem,new String[]{"job_title"},
                                    new int[]{R.id.job_textView_jobItem});
                            job_jobItem.setAdapter(listAdapter);

                            //重新设置当前的滚动位置为之前的位置
                            job_jobItem.setSelection(position+1);
                        }

                        else if(job_tabHost.getCurrentTab()==4) {
                            jobUrlChanged = "http://just.91job.gov.cn/job/search?d_category%5B0%5D=0&d_category%5B1%5D=101&d_category%5B2%5D=102&page=" + job_index;

                            //连网获取学校就业信息网实习岗位界面信息，存储到result中
                            HttpConnectionGet httpConnectionGet=new HttpConnectionGet();
                            httpConnectionGet.setUrl(jobUrlChanged);
                            final String result=new String(httpConnectionGet.getResponse());

                            //通过匹配得到每一条实习岗位项目的详细信息
                            //String pattern="<ul class=\"infoList\">[\\s\\S]+?<a href=\"([^\"]*)[^>]*>([^<]*)" +
                            //        "([^=]*=){2}[^>]*>([^<]*)[^=]*[^>]*>([^<]*)[^=]*[^>]*>([^<]*)</li>";
                            String pattern="<li>[\\s\\S]*?<a href=\"([^\"]*)\">([^<]*)[\\s\\S]*?<li class=\"css-begintime\">([^<]*)</li>[\\s\\S]*?<li class=\"css-begintime\">([^<]*)</li>";
                            Matcher matcher= Pattern.compile(pattern).matcher(result);

                            //通过循环把得到每一条实习岗位条目的具体信息放到一个JobStorage类的对象中，再放入集合list中
                            while(matcher.find()){
                                String job_hyperlink="http://just.91job.gov.cn/"+matcher.group(1);
                                String job_positionName=matcher.group(2);
                                String job_companyName=matcher.group(3);
                                String job_time=matcher.group(4);

                                JobStorage jobStorage=new JobStorage();
                                jobStorage.setHyperlink(job_hyperlink);
                                jobStorage.setPositionName(job_positionName);
                                jobStorage.setCompanyName(job_companyName);
                                jobStorage.setTime(job_time);

                                //把该条实习岗位项目添加到集合list中
                                list.add(jobStorage);
                            }

                            //利用for循环，将每一项的title的内容加到sList中
                            List<Map<String,Object>> sList=new ArrayList<Map<String, Object>>();
                            for(int i=0;i<list.size();i++){
                                Map<String,Object> map=new HashMap<String, Object>();

                                //定义显示在listView的条目中的内容格式
                                String job_show=list.get(i).getPositionName()+"\n"+list.get(i).getCompanyName()+"\n"+list.get(i).getTime();
                                map.put("job_title",job_show);
                                sList.add(map);
                            }

                            //声明适配器，并将其绑定到ListView控件
                            listAdapter=new SimpleAdapter(job.this,sList,
                                    R.layout.job_titleitem,new String[]{"job_title"},
                                    new int[]{R.id.job_textView_jobItem});
                            job_jobItem.setAdapter(listAdapter);

                            //重新设置当前的滚动位置为之前的位置
                            job_jobItem.setSelection(position+1);
                        }

                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

    }

}
