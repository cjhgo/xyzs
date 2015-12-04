package com.bn.sample.add;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bn.sample.R;

import org.apache.http.protocol.HTTP;

/**
 * Created by Administrator on 2015/11/13.
 */
public class fleamarket extends Activity {

    WebView fleamarket_specificText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fleamarket);

        Intent intent=this.getIntent();
        Bundle bundle=intent.getExtras();
        String fleamarket_Url=bundle.getString("fleamarket_Url");

        fleamarket_specificText=(WebView)findViewById(R.id.fleamarket_webView_specificText);

        HttpConnectionGet fleamarket_itemhttpConnectionGet=new HttpConnectionGet();
        fleamarket_itemhttpConnectionGet.setUrl(fleamarket_Url);
        final String fleamarket_result=new String(fleamarket_itemhttpConnectionGet.getResponse());

        fleamarket_specificText.loadDataWithBaseURL("http://justershou.sinaapp.com/market/", fleamarket_result, "text/html", HTTP.UTF_8, null);

        //设置点击webView所呈现出来的页面中的超链接时的响应事件（不再是单纯的使用浏览器打开）
        fleamarket_specificText.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                Intent intent=new Intent();
                intent.setClass(fleamarket.this,fleamarket.class);
                Bundle bundle=new Bundle();
                bundle.putString("fleamarket_Url",url);
                intent.putExtras(bundle);
                startActivity(intent);

                return true;
            }
        });
    }

}
