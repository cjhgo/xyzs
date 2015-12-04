package com.bn.sample.add;

import android.os.StrictMode;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by Administrator on 2015/5/26.
 */

//用来传入超连接到类的变量url后通过连网获取网页信息，返回到类的变量response中
public class HttpConnectionGet {
    String url;
    String response;

    //有参数的构造函数
    public void HttpConnectionGet(String url){
        this.url=url;
    }

    public void setUrl(String url){
        this.url=url;
    }

    public String getUrl(){
        return this.url;
    }

    //通过HttpGet访问url中的网址，获取网页信息，存储到response
    public String getResponse(){

//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskWrites().detectDiskReads().detectNetwork().penaltyLog().build());
//        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());

        if(this.response==null) {
            try {
                HttpGet httpGet = new HttpGet(this.url);

                //HttpParams httpParams=new BasicHttpParams();
                //HttpConnectionParams.setConnectionTimeout(httpParams,30000);
                //HttpClient httpClient=new DefaultHttpClient(httpParams);
                HttpClient httpClient = new DefaultHttpClient();

                //HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),3000);
                //HttpConnectionParams.setSoTimeout(httpClient.getParams(),3000);

                HttpResponse hResponse;
                hResponse = httpClient.execute(httpGet);
                if (hResponse.getStatusLine().getStatusCode() == 200) {
                    response = EntityUtils.toString(hResponse.getEntity());
                }
            }catch(ConnectTimeoutException e1){
                return null;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return this.response;
        }
        else
            return this.response;
    }
}
