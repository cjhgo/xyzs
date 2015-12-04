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
public class corporation extends Activity {
    WebView webview;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.corporationtext);

        webview=(WebView)findViewById(R.id.corporation);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskWrites().detectDiskReads().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        try{
            URL Url=new URL("http://www.just.edu.cn/list/87.html");
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
            String imgaddress= "<img([^\"]*\"){2}";
            Matcher matcher1= Pattern.compile(imgaddress).matcher(result);
            String img=null;
            while(matcher1.find()){
                img=matcher1.group();
                result=result.replace(img,img + " style=\"display:block;width:100%;\"");
            }
            String pattern="<div class=\"cont_title[\\s\\S]+?<div id=\"jcbutton\"";
            Matcher matcher= Pattern.compile(pattern).matcher(result);
            String net=null;
            while (matcher.find()){
                net= matcher.group();
            }
            webview.loadDataWithBaseURL("http://www.just.edu.cn" ,net, "text/html", HTTP.UTF_8,null);
            System.out.print(result);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
