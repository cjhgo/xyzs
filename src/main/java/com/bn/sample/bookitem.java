package com.bn.sample;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.bn.sample.utils.Htmlutil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by chen-pc on 2015/3/14.
 */
public class bookitem extends Activity
{
    private WebView bookitem_web;
    private TextView bookitem_txt;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookitem);

        bookitem_web=(WebView)findViewById(R.id.bookitem_web);
        bookitem_txt=(TextView)findViewById(R.id.bookitem_txt);

        Bundle bundle = this.getIntent().getExtras();
//        String itemurl = bundle.getString("url");
        String bookitemdata=bundle.getString("data");
        bookitem_web.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        try
        {


//            Document bookitem= Jsoup.connect(itemurl).get();

            Document bookitem = Jsoup.parse(bookitemdata);
            String div_fl=bookitem.select("div[style=\"float:left;\"]").html();

            String tableinfo=bookitem.select("table").html();
            tableinfo="<table border=\"0\" align=\"center\" cellpadding=\"2\"" +
                    "cellspacing=\"1\" bgcolor=\"#d2d2d2\">"
                    +tableinfo+"</table>";


            bookitem_web.loadDataWithBaseURL("http://202.195.195.137:8080/opac/",
                    tableinfo+div_fl, "text/html", "UTF-8", "");
            bookitem_web.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view,
                                                        String url)
                {
//                    view.loadUrl(url);
                    return true;
                }
            });
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

    }
//    @Override
//    public boolean onKeyDown(int keyCoder,KeyEvent event){
//        if(bookitem_web.canGoBack() && keyCoder == KeyEvent.KEYCODE_BACK){
//            bookitem_web.goBack();   //goBack()表示返回webView的上一页面
//            bookitem_web.loadUrl("http://www.baidu.com");
//            return true;
//        }
//        return false;
//    }
}
//class wvBook extends WebViewClient
//{
//    @Override
//    public boolean shouldOverrideUrlLoading(WebView view,
//                                            String url)
//    {
//        return false;
//    }
//}