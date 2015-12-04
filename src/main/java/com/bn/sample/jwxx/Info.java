package com.bn.sample.jwxx;

import android.os.StrictMode;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by chen-pc on 2015/5/23.
 */
public class Info
{
    private static Info info_instance=null;
    private static int status = 0;
    private DefaultHttpClient httpClient = null;
    private HttpParams httpParams = null;
    private HashMap<String, String> list = new HashMap();



    private Info(String paramString1, String paramString2)
    {
//        HttpConnectionParams.setConnectionTimeout(this.httpParams, 999999);
//        HttpConnectionParams.setSoTimeout(this.httpParams, 999999);
        this.httpClient = new DefaultHttpClient(this.httpParams);
        this.list.put("username", paramString1);
        this.list.put("password", paramString2);
        getSession();
        if (status == 0)
        {
            this.list.put("currentScore", "");
            this.list.put("olderScore", "");
            this.list.put("schedule", "");
        }
    }
    private void getSession()
    {
        HttpGet localHttpGet = new HttpGet("http://info.just.edu.cn:81/");
        try
        {
            if (this.httpClient.execute(localHttpGet).getStatusLine().getStatusCode() == 200) {
                login();
            }
            return;
        }
        catch (ClientProtocolException localClientProtocolException)
        {
            localClientProtocolException.printStackTrace();
            return;
        }
        catch (IOException localIOException)
        {
            localIOException.printStackTrace();
        }
    }

    private void login()
    {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new BasicNameValuePair("userName", (String)this.list.get("username")));
        localArrayList.add(new BasicNameValuePair("userPass", (String)this.list.get("password")));
        HttpPost localHttpPost = new HttpPost("http://info.just.edu.cn:81/loginAction.do");
        localHttpPost.addHeader("Referer", "http://info.just.edu.cn:81/");
        try
        {
            localHttpPost.setEntity(new UrlEncodedFormEntity(localArrayList, "gb2312"));
            this.httpClient.setRedirectHandler(null);
            HttpResponse localHttpResponse = this.httpClient.execute(localHttpPost);
            if (localHttpResponse.getStatusLine().getStatusCode() == 200)
            {
                String str = new String(EntityUtils.toByteArray(localHttpResponse.getEntity()), "gb2312");
                if (Pattern.compile("(错误的)").matcher(str).find())
                {
                    status = 0;
                    return;
                }
                loginJWXX();
                return;
            }
        }
        catch (ClientProtocolException localClientProtocolException)
        {
            localClientProtocolException.printStackTrace();
            return;
        }
        catch (IOException localIOException)
        {
            localIOException.printStackTrace();
        }
    }

    private void loginJWXX()
    {
        HttpGet localHttpGet = new HttpGet("http://info.just.edu.cn:81/roamingAction.do?appId=BKS_CJCX");
        try
        {
            if (this.httpClient.execute(localHttpGet).getStatusLine().getStatusCode() == 200) {
                status = 1;
            }
            return;
        }
        catch (ClientProtocolException localClientProtocolException)
        {
            localClientProtocolException.printStackTrace();
            return;
        }
        catch (IOException localIOException)
        {
            localIOException.printStackTrace();
        }
    }


    public static final Info init(String paramString1, String paramString2)
    {
        if ((info_instance == null) || (status == 0))
        {
            info_instance = new Info(paramString1, paramString2);
        }
        return info_instance;

//        throw new RuntimeException("info_instance already exist!");
    }

    public int getStatus()
    {
        return status;
    }

    public static final Info getInstance()
    {
        return info_instance;
    }

    public String getInfo(int opt)
    {
//        return getOlderScore();
        return getSchedule();
    }

    private String getCurrentScore()
    {
        HttpGet localHttpGet = new HttpGet("http://202.195.195.86:7777/pls/wwwbks/bkscjcx.curscopre");
        try
        {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath().build());
            HttpResponse localHttpResponse = this.httpClient.execute(localHttpGet);
            if (localHttpResponse.getStatusLine().getStatusCode() == 200)
            {
                String str = new String(EntityUtils.toByteArray(localHttpResponse.getEntity()), "gb2312");
                return str;
            }
        }
        catch (ClientProtocolException localClientProtocolException)
        {
            localClientProtocolException.printStackTrace();
            return "";
        }
        catch (IOException localIOException)
        {

            {
                localIOException.printStackTrace();
            }
            return "";
        }
//        return getCurrentScore();
        return "";
    }

    private String getExpulsion()
    {
        HttpGet localHttpGet = new HttpGet("http://202.195.195.86:7777/pls/wwwbks/bks_xj.xjcx");
        try
        {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath().build());
            HttpResponse localHttpResponse = this.httpClient.execute(localHttpGet);
            if (localHttpResponse.getStatusLine().getStatusCode() == 200)
            {
                String str = new String(EntityUtils.toByteArray(localHttpResponse.getEntity()), "gb2312");
                return str;
            }
        }
        catch (ClientProtocolException localClientProtocolException)
        {
            localClientProtocolException.printStackTrace();
            return "";
        }
        catch (IOException localIOException)
        {

            localIOException.printStackTrace();
            return "";
        }
        return "";

    }



    private String getOlderScore()
    {
        HttpGet localHttpGet = new HttpGet("http://202.195.195.86:7777/pls/wwwbks/bkscjcx.yxkc");
        try
        {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath().build());
            HttpResponse localHttpResponse = this.httpClient.execute(localHttpGet);
            if (localHttpResponse.getStatusLine().getStatusCode() == 200)
            {
                String str = new String(EntityUtils.toByteArray(localHttpResponse.getEntity()), "gb2312");
                return str;
            }
        }
        catch (ClientProtocolException localClientProtocolException)
        {
            localClientProtocolException.printStackTrace();
            return "";
        }
        catch (IOException localIOException)
        {

            localIOException.printStackTrace();
            return "";

        }
        return "";

    }

    private String getSchedule()
    {
        HttpGet localHttpGet = new HttpGet("http://202.195.195.86:7777/pls/wwwbks/xk.CourseView");
        try
        {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath().build());
            HttpResponse localHttpResponse = this.httpClient.execute(localHttpGet);
            if (localHttpResponse.getStatusLine().getStatusCode() == 200)
            {
                String str = new String(EntityUtils.toByteArray(localHttpResponse.getEntity()), "gb2312");
                return str;
            }
        }
        catch (ClientProtocolException localClientProtocolException)
        {
            localClientProtocolException.printStackTrace();
            return "";
        }
        catch (IOException localIOException)
        {
            localIOException.printStackTrace();
            return "";
        }
        return "";

    }

}
