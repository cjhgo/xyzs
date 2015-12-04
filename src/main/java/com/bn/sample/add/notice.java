package com.bn.sample.add;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import com.bn.sample.R;
/**
 * Created by Zhao on 2015/5/28.
 */
public class notice extends ListActivity {
    private ArrayAdapter<String> adapter;
    //static String[] data=new String[10];
    static List<String> data=new ArrayList<String>();
    List<String> urls=new ArrayList<String>();
    ListView list;
    String url="";
    String indirect_title="";
    String indirect_clickTimes="";
    String body="";

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview2);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskWrites().detectDiskReads().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());

        list=(ListView)findViewById(android.R.id.list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                url = urls.get(position);  //得到点击项的url;
                System.out.println(url);
                try {
                    indirect_title=getNtcTitle(url); //获取文章Title;
                    indirect_clickTimes=getclickTimes(url);  //获取点击次数及发布日期;
                    body=getBody(url);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Intent intent=new Intent();
                intent.setClass(notice.this, notice_result.class);
                //System.out.println(urls.get(position));
                intent.putExtra("title",indirect_title);
                intent.putExtra("clickTimes",indirect_clickTimes);
                intent.putExtra("body",body);
                startActivity(intent);
            }
        });

        List<String> postTitleList=new ArrayList<String>();
        try{
            postTitleList=getMyTitle(); //通过HtmlParser获取题目列表,并存放于postTitleList中;
            adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
            setListAdapter(adapter);
        }
        catch(ParseException e){
            e.printStackTrace();
        }

    }

    private List<String> getMyTitle() throws ParseException {
        final String home_Url="http://www.just.edu.cn/";
        ArrayList<String> pTitleList=new ArrayList<String>();
        try {
            Parser htmlparser = new Parser(home_Url);
            htmlparser.setEncoding("UTF-8");
            String postTitle = "";

            NodeList div = htmlparser.extractAllNodesThatMatch(new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("class", "left-con fn-clear")));  //获取指定的div节点;

            if(div!=null && div.size()>0){
                NodeList itemLiList=div.elementAt(0).getChildren().extractAllNodesThatMatch(new TagNameFilter("li"), true);    //获取div中的li节点

                if(itemLiList!=null && itemLiList.size()>0){
                    for(int i=0;i<itemLiList.size();i++){
                        NodeList linkItem=itemLiList.elementAt(i).getChildren().extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class), true); //获取li中的link节点

                        if(linkItem!=null && linkItem.size()>0){
                            postTitle=((LinkTag)linkItem.elementAt(0)).getLinkText(); //获取Link节点的Text,即为要获取的题目文字;
                            System.out.println(postTitle);
                            urls.add(((LinkTag) linkItem.elementAt(0)).getLink());
                            pTitleList.add(postTitle);
                            //data[i]=postTitle;
                            data.add(postTitle);
                        }
                    }
                }
            }
        }
        catch(Throwable e){
            e.printStackTrace();
        }
        return pTitleList;
    }

    public String getNtcTitle(String url) throws ParseException {
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
                    transport=pseudo_clickTimes.replaceAll("&nbsp[;]?", ""); //将取得的字符串中的"&nbsp;"去除;
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
            if(nodeL1!=null && nodeL1.size()>0){ //匹配到class-cont_content节点
                NodeList nodeL2=nodeL1.extractAllNodesThatMatch(new TagNameFilter("p"),true);
                if(nodeL2!=null && nodeL2.size()>0){
                    for(int i=0;i<nodeL2.size();i++){
                        content.append(nodeL2.elementAt(i).getChildren().toHtml());
                    }
                }
//                else{
                if(!(nodeL2!=null && nodeL2.size()>0)) {
                    nodeL2 = nodeL1.extractAllNodesThatMatch(new TagNameFilter("a"), true); //否则确定是否为<a>标签,若是则获取文本和附件地址
                    if(nodeL2!=null && nodeL2.size()>0){
                        content.append(nodeL2.elementAt(0).toHtml());
                        content.append('\n');
                        content.append("附件地址:\n");
                        content.append(((LinkTag) nodeL2.elementAt(0)).getLink());
                    }
                    else{
                        content.append(nodeL1.elementAt(0).getChildren().toHtml());
                    }
                }
            }
            transport=content.toString();
            transport=transport.replace("\t","");
            transport=transport.replaceAll("&nbsp[;]?", " ");
            transport=transport.replaceAll("</?\\w*[^>]*>", " ");
            System.out.println(transport);
        }
        catch(Throwable e){
            e.printStackTrace();
        }
        return transport;
    }
}
