package com.bn.sample.add;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import com.bn.sample.R;
/**
 * Created by Administrator on 2015/9/3.
 */

//成绩登录界面，与grade_login.xml对应
public class grade_login extends Activity {
    EditText grade_loginUserName,grade_loginPassword;
    Button grade_login;
    List<Cookie> cookies;
    String sessionID="";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grade_login);


        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskWrites().detectDiskReads().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());


        grade_loginUserName=(EditText)findViewById(R.id.grade_EditText_loginUserName);
        grade_loginPassword=(EditText)findViewById(R.id.grade_EditText_loginPassword);
        grade_login=(Button)findViewById(R.id.grade_Button_gradeLogin);
        grade_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName=grade_loginUserName.getText().toString();
                String password=grade_loginPassword.getText().toString();

                //使用用户名和密码进行登录
                if(userName.length()==0||password.length()==0)
                    Toast.makeText(grade_login.this,"请输入用户名或密码",Toast.LENGTH_LONG).show();
                else{
                    //HttpPost连接对象
                    //http://202.195.206.35:8080/jsxsd/
                    HttpPost httpPost=new HttpPost("http://jwgl.just.edu.cn:8080/jsxsd/");
                    //使用NameValuePair保存要传递的参数
                    List<NameValuePair> params=new ArrayList<NameValuePair>();
                    //添加要传递的Post参数
                    params.add(new BasicNameValuePair("USERNAME",userName));
                    params.add(new BasicNameValuePair("PASSWORD",password));

                    try{
                        //httpPost.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS, false);

                        //设置字符集
                        HttpEntity httpEntity=new UrlEncodedFormEntity(params, HTTP.UTF_8);
                        //请求HttpRequest
                        httpPost.setEntity(httpEntity);
                        //取得默认的HttpClient
                        HttpClient httpClient=new DefaultHttpClient();
                        //获取HttpResponse
                        HttpResponse httpResponse=httpClient.execute(httpPost);

                        //如果连接成功
                        if(httpResponse.getStatusLine().getStatusCode()==200){
                            cookies=((AbstractHttpClient) httpClient).getCookieStore().getCookies();
                            for(int i=0;i<cookies.size();i++){
                                sessionID=cookies.get(i).getValue();
                            }

                            String onecookie=httpResponse.getFirstHeader("Set-Cookie").getValue();
                            String result="";
                            HttpGet httpGet=new HttpGet("http://jwgl.just.edu.cn:8080/jsxsd/kscj/cjcx_list");
                            if(onecookie!="")
                                httpGet.setHeader("Cookie",cookies.get(0).getValue());
                            //HttpResponse newhttpResponse =new DefaultHttpClient().execute(httpGet);
                            HttpResponse newhttpResponse =httpClient.execute(httpGet);
                            if (newhttpResponse.getStatusLine().getStatusCode() == 200) {
                                result= EntityUtils.toString(newhttpResponse.getEntity());
                            }

                            //Activity跳转，同时传递result的值
                            Intent intent=new Intent();
                            Bundle bundle=new Bundle();
                            bundle.putString("data",result);
                            intent.putExtras(bundle);
                            intent.setClass(grade_login.this,grade.class);
                            startActivity(intent);
                        }
                        else
                            Toast.makeText(grade_login.this,"用户名或密码错误",Toast.LENGTH_LONG).show();

                    }catch(Exception e){
                        e.printStackTrace();
                    }


                }
            }
        });



    }
}
