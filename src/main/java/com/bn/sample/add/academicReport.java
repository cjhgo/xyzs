package com.bn.sample.add;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.ParagraphTag;
import org.htmlparser.util.NodeList;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.bn.sample.R;
/**
 * Created by Zhaor on 2015/5/27.
 */
public class academicReport extends ListActivity {
    private ArrayAdapter<String> adapter;
    static List<String> data = new ArrayList<String>();
    String url;
    String indirect_title="";
    String indirect_clickTimes="";
    String body="";
    String[] content=new String[10];
    List<String> urls = new ArrayList<String>();
    ListView list;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskWrites().detectDiskReads().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());

        List<String> postTitleList = new ArrayList<String>();
        try {
            postTitleList = getMyTitle(); //通过HtmlParser获取题目列表,并存放于postTitleList中;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
        setListAdapter(adapter);

        list = (ListView) findViewById(android.R.id.list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                url = urls.get(position);  //得到点击项的url;
                try {
                    indirect_title=getArTitle(url); //获取文章Title;
                    indirect_clickTimes=getclickTimes(url);  //获取点击次数及发布日期;
                    body=getBody(url);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent();
                intent.setClass(academicReport.this, academic_report_Result.class);
                intent.putExtra("url",indirect_title);
                intent.putExtra("clickTimes",indirect_clickTimes);
                intent.putExtra("report_title",content[0]);
                intent.putExtra("reporter",content[1]);
                intent.putExtra("reporter_detail",content[2]);
                intent.putExtra("body",body);
                startActivity(intent);
            }
        });

    }

    private List<String> getMyTitle() throws ParseException {
        final String home_Url = "http://www.just.edu.cn/";
        //final String home_Url="file:///G:/00.html";
        ArrayList<String> pTitleList = new ArrayList<String>();
        try {
            Parser htmlparser = new Parser(home_Url);
            htmlparser.setEncoding("UTF-8");
            String postTitle = "";

            NodeList div = htmlparser.extractAllNodesThatMatch(new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("class", "left-con fn-clear fn-hide")));  //获取指定的div节点;

            if (div != null && div.size() > 0) {
                NodeList itemLiList = div.elementAt(0).getChildren().extractAllNodesThatMatch(new TagNameFilter("li"), true);    //获取div中的li节点

                if (itemLiList != null && itemLiList.size() > 0) {
                    for (int i = 0; i < itemLiList.size(); i++) {
                        NodeList linkItem = itemLiList.elementAt(i).getChildren().extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class), true); //获取li中的link节点

                        if (linkItem != null && linkItem.size() > 0) {
                            postTitle = ((LinkTag) linkItem.elementAt(0)).getLinkText(); //获取Link节点的Text,即为要获取的题目文字;
                            System.out.println(postTitle);
                            urls.add(((LinkTag) linkItem.elementAt(0)).getLink());
                            pTitleList.add(postTitle);
                            //data[1]=postTitle;
                            data.add(postTitle);
                            //System.out.println(data.get(i));
                        }
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return pTitleList;
    }

    public String getArTitle(String url) throws ParseException {
        String pseudo_title = "";
        try {
            Parser htmlparser = new Parser(url);
            htmlparser.setEncoding("UTF-8");
            NodeList divL1 = htmlparser.extractAllNodesThatMatch(new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("class", "mainright")));
            if(divL1!=null && divL1.size()>0){
               NodeList divL2 = divL1.elementAt(0).getChildren().extractAllNodesThatMatch(new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("class", "cont_title")));
               if (divL2 != null && divL2.size() > 0) {
                   pseudo_title =divL2.elementAt(0).getChildren().toHtml();
                   }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return pseudo_title;
    }

    public String getclickTimes(String url) throws ParseException {
        String pseudo_clickTimes = "";
        String transport="";
        try {
            Parser htmlparser = new Parser(url);
            htmlparser.setEncoding("UTF-8");
            NodeList divL1 = htmlparser.extractAllNodesThatMatch(new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("class", "mainright")));
            if(divL1!=null && divL1.size()>0){
                NodeList divL2 = divL1.elementAt(0).getChildren().extractAllNodesThatMatch(new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("class", "cont_desc")));
                if (divL2 != null && divL2.size() > 0) {
                    pseudo_clickTimes =divL2.elementAt(0).getChildren().toHtml();
                    //System.out.print(pseudo_clickTimes);
                    transport=pseudo_clickTimes.replace("&nbsp;",""); //将取得的字符串中的"&nbsp;"去除;
                    transport=transport.replace("  "," ");
                    System.out.println(transport);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return transport;
    }

    public String getBody(String url) throws ParseException {
        String transport="";
        StringBuffer content=new StringBuffer();
        try{
            Parser parser=new Parser(url);
            parser.setEncoding("UTF-8");
            NodeList nodeL1=parser.extractAllNodesThatMatch(new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("class", "cont_content")));
            if(nodeL1!=null && nodeL1.size()>0){
                NodeList nodeL2=nodeL1.extractAllNodesThatMatch(new TagNameFilter("p"),true);
                if(nodeL2!=null && nodeL2.size()>0){
                    for(int i=0;i<nodeL2.size();i++){
                        content.append(nodeL2.elementAt(i).getChildren().toHtml());
                    }
                }
            }
            transport=content.toString();
            transport=transport.replaceAll("&nbsp[;]?", " ");
            transport=transport.replaceAll("</?\\w*[^>]*>", " ");

        }
        catch(Throwable e){
            e.printStackTrace();
        }
        return transport;
    }
}
