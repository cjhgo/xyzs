package com.bn.sample.add;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.webkit.WebView;
import android.widget.TextView;

import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.bn.sample.R;
/**
 * Created by ZouYunHe on 2015/5/30.
 */
public class song extends Activity{
    WebView webview;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songtext);

        webview=(WebView)findViewById(R.id.song);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskWrites().detectDiskReads().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        try{
            URL Url=new URL("http://www.just.edu.cn/list/anthem.html");
            HttpURLConnection conn= (HttpURLConnection)Url.openConnection();
            InputStreamReader in =new InputStreamReader(conn.getInputStream());
            BufferedReader buffer= new BufferedReader(in);

            String inputline=null;
            String result= null;

            while(((inputline= buffer.readLine())!=null)){
                result += inputline + "\n";
            }
            in.close();
            conn.disconnect();
            String olds= "<img src=\"/data/attachment/image/2013/09/2_f1_0833.png\" alt=\"\" />";
            String news= "<img src=\"/data/attachment/image/2013/09/2_f1_0833.png\" alt=\"\" style=\"display:block;width:100%;\" />";
            result=result.replace(olds,news);
            String pattern="<div class=\"cont_title[\\s\\S]+?javascript\">";
            Matcher matcher= Pattern.compile(pattern).matcher(result);
            String net=null;
            while (matcher.find()){
                net= matcher.group();
            }
            System.out.print(net);
            webview.loadDataWithBaseURL("http://www.just.edu.cn" ,net, "text/html", HTTP.UTF_8,null);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
