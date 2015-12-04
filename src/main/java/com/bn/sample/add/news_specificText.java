package com.bn.sample.add;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bn.sample.R;

import org.apache.http.protocol.HTTP;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/5/29.
 */

//点击某一项具体的新闻标题后跳转到此页面显示新闻的详细信息,与news_specificText.xml相对应
public class news_specificText extends Activity {

    WebView news_specificText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_specifictext);

        news_specificText=(WebView)findViewById(R.id.news_webView_specificText);

        //String news_formerText="正在加载...";
        //news_specificText.loadData(news_formerText, "text/html", HTTP.UTF_8);

        //获取前一个Activity传过来的数据，即具体的某条新闻的超链接
        Intent intent=this.getIntent();
        Bundle bundle=intent.getExtras();
        String news_itemUrl=bundle.getString("news_itemUrl");

        //通过每一条新闻的超链接获取新闻的具体内容text
        //获取网页内容
        HttpConnectionGet news_itemhttpConnectionGet=new HttpConnectionGet();
        news_itemhttpConnectionGet.setUrl(news_itemUrl);
        final String news_result=new String(news_itemhttpConnectionGet.getResponse());
        //运用正则表达式进行匹配
        String news_pattern="<div class=\"txt f14px newsContent\">(([\\s\\S]+?)<div class=\"l left\">[\\s\\S]+?</div>)";
        Matcher news_itemMatcher= Pattern.compile(news_pattern).matcher(news_result);
        String news_text=new String();
        while(news_itemMatcher.find()) {
            news_text = news_itemMatcher.group(1);
        }

        //改变news_text中的关于图片的部分内容，使其大小适应屏幕
        String news_patternPicture="<img([^\"]*\"){2}";
        Matcher news_itemMatcherPicture= Pattern.compile(news_patternPicture).matcher(news_text);
        while(news_itemMatcherPicture.find()){
            String news_textPicture=news_itemMatcherPicture.group();
            news_text=news_text.replace(news_textPicture,news_textPicture+" \" style=\"display:block;width:100%;\"");
        }

        //将新闻内容放到网页控件news_specificText中，并且给其中的超连接的相对路径自动补充为绝对路径
        news_specificText.loadDataWithBaseURL("http://hbd.just.edu.cn/",news_text, "text/html", HTTP.UTF_8,null);
        //直接将新闻内容放到网页控件news_specificText中，不修改其中超连接的相对路径
        //news_specificText.loadData(news_text, "text/html", HTTP.UTF_8);

        //让webView控件自适应屏幕的大小
        //news_specificText.getSettings().setUseWideViewPort(true);
        //news_specificText.getSettings().setLoadWithOverviewMode(true);
    }
}
