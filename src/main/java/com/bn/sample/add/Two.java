package com.bn.sample.add;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

import com.bn.sample.R;

import org.apache.http.protocol.HTTP;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Two extends Activity {
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        webView=(WebView)findViewById(R.id.webView);
        String jobNew=new String();
       Intent intent=this.getIntent();
        Bundle bundle=intent.getExtras();
        String result=bundle.getString("data");
        com.bn.sample.add.fetchWeb f=new com.bn.sample.add.fetchWeb();
        String ss=f.fetch(result);
        String pattern="<div class=\"articleview\">([\\s\\S]+?)</div>";
        Matcher matcher= Pattern.compile(pattern).matcher(ss);
        while(matcher.find())
        {
            jobNew=matcher.group(1);
        }

        jobNew=jobNew.replaceAll("/data/attachment","http://jy.just.edu.cn/data/attachment");
        System.out.print(jobNew);
        webView.loadDataWithBaseURL(null,jobNew,"text/html", HTTP.UTF_8,null);
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
