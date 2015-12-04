package com.bn.sample.add;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.method.ScrollingMovementMethod;
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
public class motto extends Activity {
   WebView webview;

   public void onCreate(Bundle savedInstanceState){
       super.onCreate(savedInstanceState);
       setContentView(R.layout.mottotext);

       webview=(WebView)findViewById(R.id.motto);

       StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskWrites().detectDiskReads().detectNetwork().penaltyLog().build());
       StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
       try{
           URL Url=new URL("http://www.just.edu.cn/list/standard.html");
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
           //>([^<]*)[\s\S]+?">([^<]*)[\s\S]+?<strong>([^<]*)[\s\S]+?:">([\s\S]+?)</p>[\s\S]+?
           String pattern="<div class=\"cont_title[\\s\\S]+?/UIS</a>";
           Matcher matcher= Pattern.compile(pattern).matcher(result);
           String net=null;
           while (matcher.find()){
               net= matcher.group();
           }
           webview.loadDataWithBaseURL("http://www.just.edu.cn" ,net, "text/html",HTTP.UTF_8,null);
           //System.out.print(net);
           //textview.setText(net);
           //textview.setMovementMethod(ScrollingMovementMethod.getInstance());
       }
       catch (IOException e){
           e.printStackTrace();
       }
   }
}
