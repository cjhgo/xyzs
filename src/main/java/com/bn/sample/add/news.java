package com.bn.sample.add;

import android.app.Activity;
import android.content.Intent;
import android.os.*;
import android.view.View;
import android.widget.*;

import com.bn.sample.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Administrator on 2015/5/26.
 */

//关于新闻信息的主界面,与news.xml相对应
public class news extends Activity {

    ListView news_newsItem;
    TabHost news_tabHost;
    int news_index=1;//加载新闻时的页码数
    String newsUrl;
    ListAdapter listAdapter;//用来绑定的适配器

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news);

        final List<NewsStorage> list=new ArrayList<NewsStorage>();//用来存储所得到的每一条新闻的各项内容

        news_newsItem=(ListView)findViewById(R.id.news_listView_newsItem);
        news_tabHost=(TabHost)findViewById(R.id.news_TabHost_showNews);
        news_tabHost.setup();

        //设置各个面板的标题与内容
        news_tabHost.addTab(news_tabHost.newTabSpec("tab0").setIndicator("科大要闻").setContent(R.id.news_listView_newsItem));
        news_tabHost.addTab(news_tabHost.newTabSpec("tab1").setIndicator("媒体科大").setContent(R.id.news_listView_newsItem));

        //设置改变标签时的响应事件
        news_tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                //对“科大要闻”设置响应事件：罗列出所有新闻的标题
                if(tabId.equals("tab0")){
                    list.clear();//清空原list中的项目
                    news_index=1;//重置页码为1

                    //连网获取学校新闻界面信息，存储到result中
                    HttpConnectionGet httpConnectionGet=new HttpConnectionGet();
                    newsUrl="http://hbd.just.edu.cn/news/c/xxxw";
                    httpConnectionGet.setUrl(newsUrl);
                    final String result=new String(httpConnectionGet.getResponse());

                    //通过匹配得到每一条新闻的日期date、标题title和超链接hyperlink
                    String pattern="<td valign=\"top\">[^h]*href=\"([^\"]*)[^>]*>([^<]*)([^>]*>){4}([^<]*)</span>";
                    Matcher matcher=Pattern.compile(pattern).matcher(result);

                    //通过循环把得到每一条新闻的日期date、标题title和超链接hyperlink放到一个NewsStorage类的对象中，再放入集合list中
                    while(matcher.find()){
                        String news_hyperlink="http://hbd.just.edu.cn"+matcher.group(1);
                        String news_title=matcher.group(2);
                        String news_date=matcher.group(4);
                        NewsStorage newsStorage=new NewsStorage();
                        newsStorage.Set(news_date,news_hyperlink,news_title);

                        //把该条新闻项目添加到集合list中
                        list.add(newsStorage);
                    }

                    //利用for循环，将每一项的title的内容加到sList中
                    List<Map<String,Object>> sList=new ArrayList<Map<String, Object>>();
                    for(int i=0;i<list.size();i++){
                        Map<String,Object> map=new HashMap<String, Object>();

                        //定义显示在listView的条目中的内容为:[新闻的时间]新闻的内容
                        String news_show="["+list.get(i).getDate()+"]"+list.get(i).getTitle();
                        map.put("news_title",news_show);
                        sList.add(map);
                    }

                    //声明适配器，并将其绑定到ListView控件
                    listAdapter=new SimpleAdapter(news.this,sList,
                            R.layout.news_titleitem,new String[]{"news_title"},
                            new int[]{R.id.news_textView_newsItem});
                    news_newsItem.setAdapter(listAdapter);
                }
                //对“媒体科大”设置响应事件：罗列出所有新闻的标题
                else{
                    list.clear();//清空原list中的项目
                    news_index=1;//重置页码为1

                    //连网获取学校新闻界面信息，存储到result中
                    HttpConnectionGet httpConnectionGet=new HttpConnectionGet();
                    newsUrl="http://hbd.just.edu.cn/news/c/mtkd";
                    httpConnectionGet.setUrl(newsUrl);
                    final String result=new String(httpConnectionGet.getResponse());

                    //通过匹配得到每一条新闻的日期date、标题title和超链接hyperlink
                    String pattern="<td valign=\"top\">[^h]*href=\"([^\"]*)[^>]*>([^<]*)([^>]*>){4}([^<]*)</span>";
                    Matcher matcher=Pattern.compile(pattern).matcher(result);

                    //通过循环把得到每一条新闻的日期date、标题title和超链接hyperlink放到一个NewsStorage类的对象中，再放入集合list中
                    while(matcher.find()){
                        String news_hyperlink="http://hbd.just.edu.cn"+matcher.group(1);
                        String news_title=matcher.group(2);
                        String news_date=matcher.group(4);
                        NewsStorage newsStorage=new NewsStorage();
                        newsStorage.Set(news_date,news_hyperlink,news_title);

                        //把该条新闻项目添加到集合list中
                        list.add(newsStorage);
                    }

                    //利用for循环，将每一项的title的内容加到sList中
                    List<Map<String,Object>> sList=new ArrayList<Map<String, Object>>();
                    for(int i=0;i<list.size();i++){
                        Map<String,Object> map=new HashMap<String, Object>();

                        //定义显示在listView的条目中的内容为:[新闻的时间]新闻的内容
                        String news_show="["+list.get(i).getDate()+"]"+list.get(i).getTitle();
                        map.put("news_title",news_show);
                        sList.add(map);
                    }

                    //声明适配器，并将其绑定到ListView控件
                    listAdapter=new SimpleAdapter(news.this,sList,
                            R.layout.news_titleitem,new String[]{"news_title"},
                            new int[]{R.id.news_textView_newsItem});
                    news_newsItem.setAdapter(listAdapter);
                }
            }
        });

        //设置默认显示的面板的index
        news_tabHost.setCurrentTab(1);//这句的作用在于经过这一步的变化，使其效果为先默认加载出“科大要闻”的第一页，因为这一句到下一句有一个标签的变化，会调用onTabChanged函数
        news_tabHost.setCurrentTab(0);



        //为ListView添加点击每一项时的响应事件：跳转到该条新闻的具体内容
        news_newsItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //得到该条新闻的超连接
                String news_itemUrl=list.get(position).getHyperlink();

                //把该超链接传递给news_specificText,并跳转到news_specifictext.xml
                Intent intent=new Intent();
                intent.setClass(news.this, com.bn.sample.add.news_specificText.class);
                Bundle bundle=new Bundle();
                bundle.putString("news_itemUrl",news_itemUrl);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        //给listView添加进度条滚动的响应函数
        news_newsItem.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 当不滚动时
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    //判断是否滚动到底部
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        //修改页码数以及首页超链接后缀
                        news_index++;
                        String newsUrlChanged=newsUrl+"?p="+Integer.toString(news_index);

                        //获取当前listView的进度条的位置，后面改变listView的内容后再恢复它的位置信息
                        int position = news_newsItem.getFirstVisiblePosition();

                        //连网获取学校新闻界面信息，存储到result中
                        HttpConnectionGet httpConnectionGet=new HttpConnectionGet();
                        httpConnectionGet.setUrl(newsUrlChanged);
                        final String result=new String(httpConnectionGet.getResponse());

                        //通过匹配得到每一条新闻的日期date、标题title和超链接hyperlink
                        String pattern="<td valign=\"top\">[^h]*href=\"([^\"]*)[^>]*>([^<]*)([^>]*>){4}([^<]*)</span>";
                        Matcher matcher=Pattern.compile(pattern).matcher(result);

                        //通过循环把得到每一条新闻的日期date、标题title和超链接hyperlink放到一个NewsStorage类的对象中，再加入到集合list中
                        while(matcher.find()){
                            String news_hyperlink="http://hbd.just.edu.cn"+matcher.group(1);
                            String news_title=matcher.group(2);
                            String news_date=matcher.group(4);
                            NewsStorage newsStorage=new NewsStorage();
                            newsStorage.Set(news_date,news_hyperlink,news_title);

                            //把该条新闻项目添加到集合list中
                            list.add(newsStorage);
                        }

                        //利用for循环，将每一项的title的内容加到sList中
                        List<Map<String,Object>> sList=new ArrayList<Map<String, Object>>();
                        for(int i=0;i<list.size();i++){
                            Map<String,Object> map=new HashMap<String, Object>();

                            //定义显示在listView的条目中的内容为:[新闻的时间]新闻的内容
                            String news_show="["+list.get(i).getDate()+"]"+list.get(i).getTitle();
                            map.put("news_title",news_show);
                            sList.add(map);
                        }


                        //声明适配器，并将其绑定到ListView控件
                        listAdapter=new SimpleAdapter(news.this,sList,
                                R.layout.news_titleitem,new String[]{"news_title"},
                                new int[]{R.id.news_textView_newsItem});
                        news_newsItem.setAdapter(listAdapter);

                        //重新设置当前的滚动位置为之前的位置
                        news_newsItem.setSelection(position+1);

                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

    }
}
