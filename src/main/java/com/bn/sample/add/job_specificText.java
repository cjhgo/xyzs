package com.bn.sample.add;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bn.sample.R;

import org.apache.http.protocol.HTTP;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/6/4.
 */

//点击某一项具体的招聘信息标题后跳转到此页面显示招聘信息的详细信息,与job_specificText.xml相对应
public class job_specificText extends Activity {

    WebView job_specificText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_specifictext);

        job_specificText=(WebView)findViewById(R.id.job_webView_specificText);

        //获取前一个Activity传过来的数据，即具体的某条招聘信息的超链接
        Intent intent=this.getIntent();
        Bundle bundle=intent.getExtras();
        String job_itemUrl=bundle.getString("job_itemUrl");

        //通过每一条招聘信息的超链接获取招聘信息的具体内容text
        //获取网页内容
        HttpConnectionGet job_itemhttpConnectionGet=new HttpConnectionGet();
        job_itemhttpConnectionGet.setUrl(job_itemUrl);
        final String job_result=new String(job_itemhttpConnectionGet.getResponse());

        //运用正则表达式进行匹配
        String news_pattern="<header>[\\s\\S]*?</header>([\\s\\S]*?<article[\\s\\S]*?</article>)+";
        Matcher job_itemMatcher= Pattern.compile(news_pattern).matcher(job_result);
        String job_text=new String();
        while(job_itemMatcher.find()) {
            job_text = job_itemMatcher.group();
        }

        //job_text=job_text.replaceAll("<div class=\"job\">[\\s\\S]+?</ul>","<div class=\"job\">\n\t\t<ul><li class=\"at\">职位信息</li></ul>");
        //job_text=job_text.replaceAll("</td><td width=","</td></tr><tr><td width=");

        //改变job_text中的关于图片的部分内容，使其大小适应屏幕
        String job_patternPicture="<img([^\"]*\"){2}";
        Matcher job_itemMatcherPicture= Pattern.compile(job_patternPicture).matcher(job_text);
        while(job_itemMatcherPicture.find()){
            String news_textPicture=job_itemMatcherPicture.group();
            job_text=job_text.replace(news_textPicture,news_textPicture+" \" style=\"display:block;width:100%;\"");
        }

        //将招聘信息的内容放到网页控件job_specificText中，并且给其中的超连接的相对路径自动补充为绝对路径
        job_specificText.loadDataWithBaseURL("http://jy.just.edu.cn/",job_text, "text/html", HTTP.UTF_8,null);

        /*
        //设置点击webView所呈现出来的页面中的超链接时的响应事件（不再是单纯的使用浏览器打开）
        job_specificText.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                //把该超链接传递给job_specificText,并跳转到job_specifictext.xml
                Intent intent=new Intent();
                intent.setClass(job_specificText.this,job_specificText.class);
                Bundle bundle=new Bundle();
                bundle.putString("job_itemUrl",url);
                intent.putExtras(bundle);
                startActivity(intent);

                return true;
            }
        });
*/

    }
}
