package com.bn.sample.add;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.bn.sample.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/7/11.
 */
public class job_announcement extends Activity {
    Button button1;
    ListView webText;
    int scrollTime=1;
    //listview条目数
    int i=0;
    int clickTime=0;
    final List<String> listWeb=new ArrayList<String>();
    final List<String> listText=new ArrayList<String>();
    List<Map<String,Object>> sList=new ArrayList<Map<String, Object>>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_announcement);

        button1=(Button)findViewById(R.id.button1);

        webText=(ListView)findViewById(R.id.listView);
        //textView1=(TextView)findViewById(R.id.textView1);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskWrites().detectDiskReads().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
      /* button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent();
               intent.setClass(MainActivity.this,Two.class);
               startActivity(intent);
           }
       });*/

        button1.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v)
            {
                if(clickTime==0)
                {
                    clickTime++;
                    com.bn.sample.add.fetchWeb F=new com.bn.sample.add.fetchWeb();
                    String ss=F.fetch("http://jy.just.edu.cn/old/content/list/50.html");
                    String pattern="<div class=\"articlelist\">[\\s\\S]+</a></li>";
                    Matcher matcher= Pattern.compile(pattern).matcher(ss);
                    String result=new String();
                    while(matcher.find())
                    {
                        result=matcher.group();
                    }
                    String pattern2="<li><a href=\"([\\s\\S]+?.)\">([\\s\\S]+?)</a></li>";

                    Matcher matcher1=Pattern.compile(pattern2).matcher(result);
                    // final List<String> listWeb=new ArrayList<String>();
                    //final List<String> listText=new ArrayList<String>();


                    while(matcher1.find())
                    {
                        // resultWeb[i]=matcher1.group(1);
                        listWeb.add(matcher1.group(1));
                        //  resultText[i]=matcher1.group(2);
                        listText.add(matcher1.group(2));
                        i++;
                    }
                    i--;
                    String sss="http://jy.just.edu.cn";
                    String ssss="/old/content";

                    for(int k=0;k<=i;k++)
                    {
                        if (listWeb.get(k).startsWith(ssss, 0))
                        {
                            listWeb.set(k, sss.concat(listWeb.get(k)));
                        }
                        else{
                            String newhyperlink=listWeb.get(k).replace("http://jy.just.edu.cn","http://jy.just.edu.cn/old");
                            listWeb.set(k,newhyperlink);
                        }
                    }
                    for(int j=0;j<=i;j++)
                    {
                        Map<String,Object> map=new HashMap<String, Object>();
                        map.put("内容",listText.get(j));
                        //map.put("地址",resultWeb[j]);
                        sList.add(map);
                    }
                    ListAdapter listAdapter=new SimpleAdapter(job_announcement.this,sList,R.layout.listview_layout,new String[]{"内容"},new int[]{R.id.webText});
                    ((ListView)findViewById(R.id.listView)).setAdapter(listAdapter);


                    webText.setOnScrollListener(new AbsListView.OnScrollListener()
                    {
                        @Override
                        public void onScrollStateChanged(AbsListView view, int scrollState)
                        {
                            if(scrollState==AbsListView.OnScrollListener.SCROLL_STATE_IDLE)
                            {
                                if(view.getLastVisiblePosition()==view.getCount()-1)
                                {
                                    int position=webText.getFirstVisiblePosition();
                                    scrollTime++;
                                    String next_add="http://jy.just.edu.cn/old/content/list/50/";
                                    next_add=next_add+Integer.toString(scrollTime);
                                    com.bn.sample.add.fetchWeb newf=new com.bn.sample.add.fetchWeb();
                                    String ss=newf.fetch(next_add);
                                    String next_pattern="<div class=\"articlelist\">[\\s\\S]+</a></li>";
                                    Matcher next_matcher= Pattern.compile(next_pattern).matcher(ss);


                                    String result=new String();
                                    while(next_matcher.find())
                                    {
                                        result=next_matcher.group();
                                    }
                                    String pattern2="<li><a href=\"([\\s\\S]+?.)\">([\\s\\S]+?)</a></li>";

                                    Matcher matcher1=Pattern.compile(pattern2).matcher(result);

                                    // final String []resultWeb=new String [200];
                                    //final String []resultText=new String[200];
                                    // int i=0;
                                    int newi=i;
                                    while(matcher1.find())
                                    {
                                        // resultWeb[i]=matcher1.group(1);
                                        listWeb.add(matcher1.group(1));
                                        // resultText[i]=matcher1.group(2);
                                        listText.add(matcher1.group(2));
                                        i++;
                                    }
                                    i--;
                                    String sss="http://jy.just.edu.cn";
                                    String ssss="/old/content";
                                    for(int k=newi+1;k<=i;k++)
                                    {
                                        if( listWeb.get(k).startsWith(ssss,0))
                                        {
                                            listWeb.set(k,sss.concat(listWeb.get(k)));
                                        }
                                        else{
                                            String newhyperlink=listWeb.get(k).replace("http://jy.just.edu.cn","http://jy.just.edu.cn/old");
                                            listWeb.set(k,newhyperlink);
                                        }
                                    }
                                    for(int j=newi+1;j<=i;j++)
                                    {
                                        Map<String,Object> map=new HashMap<String, Object>();
                                        map.put("内容",listText.get(j));
                                        //map.put("地址",resultWeb[j]);
                                        sList.add(map);
                                    }
                                    ListAdapter listAdapter=new SimpleAdapter(job_announcement.this,sList,R.layout.listview_layout,new String[]{"内容"},new int[]{R.id.webText});
                                    ((ListView)findViewById(R.id.listView)).setAdapter(listAdapter);
                                    webText.setSelection(position+1);


                                }
                            }
                        }

                        @Override
                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                        }
                    });




                    webText.setOnItemClickListener(new AdapterView.OnItemClickListener()
                    {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent=new Intent();
                            intent.setClass(job_announcement.this, com.bn.sample.add.Two.class);
                            Bundle bundle=new Bundle();
                            // System.out.print(resultText[position]);
                            bundle.putString("data",listWeb.get(position));
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }

                    });
                    clickTime++;
                }


            }

        });




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
