package com.bn.sample;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.cookie.Cookie;
import org.apache.http.ParseException;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen-pc on 2015/3/19.
 */
public class httpcli
{
    private static List<Cookie> cookies;
    private static String location = "";
    private static String onecookie;
    static String sessionId="";
    public static void login(String username,String password)
    {
        try
        {

            HttpClient httpClient=new DefaultHttpClient();

                String path="http://202.195.206.14/xk/LoginToXk";

            HttpPost httpPost=new HttpPost(path);

            List<NameValuePair> parameters=new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("USERNAME", username));
            parameters.add(new BasicNameValuePair("PASSWORD", password));

            httpPost.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS, false);
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
            HttpResponse response=httpClient.execute(httpPost);
            cookies = ((AbstractHttpClient) httpClient).getCookieStore().getCookies();

            for (int i = 0; i < cookies.size(); i++){
                sessionId = cookies.get(i).getValue();
            }
            System.out.println(sessionId);

            onecookie = response.getFirstHeader("Set-Cookie").getValue();
            System.out.println("onecookie= " + onecookie);

            location = response.getFirstHeader("Location").getValue();
            System.out.println("location " + location);
//            System.out.println(getHtml(location));
            System.out.println(getHtml("http://202.195.206.14/kscj/cjcx_list"));
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

    }
    private static String getHtml(String url)throws ParseException,
            IOException
    {
        HttpGet get = new HttpGet(url);
        if ("" != onecookie) {
            get.addHeader("Cookie", onecookie);
        }
        HttpResponse httpResponse = new DefaultHttpClient().execute(get);
        HttpEntity entity = httpResponse.getEntity();
        return EntityUtils.toString(entity);
    }
    private static String getHtml_v1(String url)throws ParseException,
            IOException
    {
//        String mycookie="JSESSIONID=A435E1135CC4E8E9C2741500FBB0CF8C";
        String mycookie="JSESSIONID=6571E134A2EB159545F2FE2AD7BCB744";
        HttpGet get = new HttpGet(url);
        if ("" != mycookie) {
            get.addHeader("Cookie", mycookie);
        }
        HttpResponse httpResponse = new DefaultHttpClient().execute(get);
        HttpEntity entity = httpResponse.getEntity();
        return EntityUtils.toString(entity);
    }
    private static String postHtml(String url)throws ParseException,
            IOException
    {
        HttpPost post=new HttpPost(url);
        if(""!=onecookie)
        {
            post.addHeader("Cookie",onecookie);
        }
        List<NameValuePair> parameters=new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("kksj",""));
        parameters.add(new BasicNameValuePair("kcxz",""));
        parameters.add(new BasicNameValuePair("kcmc",""));
        parameters.add(new BasicNameValuePair("xsfs","all"));
        post.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
        HttpResponse httpResponse = new DefaultHttpClient().execute(post);
        HttpEntity entity = httpResponse.getEntity();
        return EntityUtils.toString(entity);


    }
    public static void main(String[] argv)
    {
//        login("1241901305","195118jkd");
        try
        {
        System.out.println(getHtml_v1("http://202.195.206.14/kscj/cjcx_list"));
        }
        catch (Exception e)
        {

        }
    }
}
