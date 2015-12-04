package com.bn.sample.lib;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.bn.sample.R;
import com.bn.sample.utils.Htmlutil;

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
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chen-pc on 2015/5/2.
 */
public class MyLib extends Activity
{
    private EditText username;
    private EditText password;
    private Button login;
    private TextView loginLockedTV;
    private TextView attemptsLeftTV;
    private TextView numberOfRemainingLoginAttemptsTV;
    int numberOfRemainingLoginAttempts = 3;
    private Map<String,String> postdata=new HashMap<String,String>();

//    private TextView content;
    private ArrayList<MybookInfo> mbi;
    private ListView showmybook;
    private String goonresponse;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mylib);
        setupVariables();

    }


    public void authenticateLogin(View view)
    {
        postdata.put("username",username.getText().toString());
        postdata.put("password",password.getText().toString());
//        postdata.put("username","1241901305");
//        postdata.put("password","195118");
        postdata.put("flag","getmybook");
        new showmylib(postdata).execute(postdata);
//        if (username.getText().toString().equals("admin") &&
//                password.getText().toString().equals("admin"))
//        {
//            Toast.makeText(getApplicationContext(), "Hello admin!",
//                    Toast.LENGTH_SHORT).show();
//        }
//        else
//        {
//            Toast.makeText(getApplicationContext(), "Seems like you 're not admin!",
//                    Toast.LENGTH_SHORT).show();
//            numberOfRemainingLoginAttempts--;
//            attemptsLeftTV.setVisibility(View.VISIBLE);
//            numberOfRemainingLoginAttemptsTV.setVisibility(View.VISIBLE);
//            numberOfRemainingLoginAttemptsTV.setText(Integer.toString(numberOfRemainingLoginAttempts));
//
//            if (numberOfRemainingLoginAttempts == 0) {
//                login.setEnabled(false);
//                loginLockedTV.setVisibility(View.VISIBLE);
//                loginLockedTV.setBackgroundColor(Color.RED);
//                loginLockedTV.setText("LOGIN LOCKED!!!");
//            }
//        }
    }
    private void setupVariables() {
        username = (EditText) findViewById(R.id.usernameET);
        password = (EditText) findViewById(R.id.passwordET);
        login = (Button) findViewById(R.id.loginBtn);
        loginLockedTV = (TextView) findViewById(R.id.loginLockedTV);
        attemptsLeftTV = (TextView) findViewById(R.id.attemptsLeftTV);
        numberOfRemainingLoginAttemptsTV = (TextView) findViewById(R.id.numberOfRemainingLoginAttemptsTV);
        numberOfRemainingLoginAttemptsTV.setText(Integer.toString(numberOfRemainingLoginAttempts));

//        content=(TextView)findViewById(R.id.content);
        mbi=new ArrayList<MybookInfo>();
        showmybook=(ListView)findViewById(R.id.mylib);
    }

    class showmylib extends AsyncTask<Map<String,String>,Integer,String>
    {
        private Map<String,String> data;
        String flag;
        public showmylib(Map<String,String> data)
        {
            this.data=data;
            flag=data.get("flag");
        }
        @Override
        protected String doInBackground(Map<String,String>... params)
        {
//            number=1241901305&passwd=195118&select=cert_no&returnUrl=
            try
            {
                HttpClient httpClient=new DefaultHttpClient();
                String posturl="http://202.195.195.137:8080/reader/redr_verify.php";
                HttpPost httpPost=new HttpPost(posturl);

                Map<String,String> postdata=params[0];
                List<NameValuePair> parameters=new ArrayList<NameValuePair>();
                parameters.add(new BasicNameValuePair("number",postdata.get("username")));
                parameters.add(new BasicNameValuePair("passwd",postdata.get("password")));
                parameters.add(new BasicNameValuePair("select","cert_no"));
                parameters.add(new BasicNameValuePair("returnUrl",""));

                httpPost.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS, false);
                httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
                httpPost.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
                HttpResponse response=httpClient.execute(httpPost);

                List<Cookie> cookies=((AbstractHttpClient) httpClient).getCookieStore().getCookies();
                String location =response.getFirstHeader("Location").getValue();
                location="http://202.195.195.137:8080/reader/"+location;
                String name=cookies.get(0).getName();
                String value=cookies.get(0).getValue();
                String cookie=name+"="+value;


                if(flag.equals("getmybook"))
                {
                    location="http://202.195.195.137:8080/reader/book_lst.php";
                    HttpGet get = new HttpGet(location);


                    get.addHeader("Cookie", cookie);
                    get.addHeader("Referer","http://202.195.195.137:8080/reader/login.php");
                    HttpResponse httpResponse = new DefaultHttpClient().execute(get);
                    HttpEntity entity = httpResponse.getEntity();
                    String res= EntityUtils.toString(entity);
                    res=new String(res.getBytes("ISO-8859-1"), "UTF-8");
                    return  res;
                }
                else if(flag.equals("goonmybook"))
                {
                    location="http://202.195.195.137:8080/reader/ajax_renew.php";
                    //?bar_code=10791627&time=1430672299665"
                    HttpPost post=new HttpPost(location);

                    List<NameValuePair> para=new ArrayList<NameValuePair>();
                    para.add(new BasicNameValuePair("bar_code",postdata.get("bar_code")));
                    String time=String.valueOf(System.currentTimeMillis());
                    para.add(new BasicNameValuePair("time",time));

                    post.setEntity(new UrlEncodedFormEntity(para, "UTF-8"));
                    post.addHeader("Cookie", cookie);

                    HttpResponse httpResponse = new DefaultHttpClient().execute(post);
                    HttpEntity entity = httpResponse.getEntity();
                    String res= EntityUtils.toString(entity);

//                    android.os.Debug.waitForDebugger();

                    res=res.replaceAll("[\\r\\n\\t]","").replaceAll("<link(.*?)red>","");
                    res=res.replace("</font>","");
                    res=new String(res.getBytes("ISO-8859-1"), "UTF-8");
                    return  res;
                }
            }
            catch (Exception e)
            {
                System.out.println(e);
            }

            return "blank!!!!!!!!!!!";
        }


        @Override
        protected void onPostExecute(String res)
        {
            super.onPostExecute(res);
//            android.os.Debug.waitForDebugger();

//            username.setText(res);

            if(flag.equals("getmybook"))
            {
                res=res.replaceAll("[\\t\\r\\n]","");

                String regular="<tr><td(.*?)</td></tr>";
                Matcher myMatcher=Pattern.compile(regular).matcher(res);
                getMybookInfo(myMatcher);
//            content.setText(res);
//                            android.os.Debug.waitForDebugger();
                mbi.remove(0);
                showmybook.setVisibility(View.VISIBLE);
                Mylib_adapter adapter=new Mylib_adapter(MyLib.this,mbi);
                showmybook.setAdapter(adapter);

                login.setVisibility(View.GONE);
            }
            else if(flag.equals("goonmybook"))
            {
                MyLib.this.goonresponse=res;
//                goon.setText(book_i.get(0)+":"+goonresponse);
            }


        }
        private void getMybookInfo(Matcher matcher)
        {
            for(;;)
            {
                if(!matcher.find())
                {
                    return;
                }
                String raw=matcher.group().toString();
                mbi.add(getAbookInfo(raw));

            }
//            mbi.add(new MybookInfo());
        }
        private MybookInfo getAbookInfo(String raw)
        {
            MybookInfo abi=new MybookInfo();
            Matcher matcher=Pattern.compile("<td.*?</td>").matcher(raw);
            int i=0;
            for(;;)
            {
                if (!matcher.find()||i > 5)
                {
                    break;
                }

                String res=matcher.group();
                res=res.replaceAll("<td(.*?)>","");
                res=res.replaceAll("</td>","");
                if(i == 1)
                {
                    //get the address
                    abi.set(6,"");
                    res=res.replaceAll("<a(.*?)>","");
                    res=res.replaceAll("</a>","");
                    res= Htmlutil.decodeUnicode(res).toString();
                }
                if(i == 3)
                {
                    res=res.replaceAll("<font(.*?)>","").replaceAll("</font>","");
                }
                abi.set(i,res);
//                if(!matcher.find())
//                {
//                    break;
//                }
//                matcher.find();
                i++;
            }
            return abi;
        }
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }
    }

    class Mylib_adapter extends ArrayAdapter<MybookInfo>
    {
        private final Context context;
        private ArrayList<MybookInfo> mybooklist;
        public Button goon;
        public Mylib_adapter(Context context,ArrayList<MybookInfo> objects)
        {

            super(context, R.layout.bookrow, objects);
            this.context = context;
            this.mybooklist=objects;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View mybookrow = inflater.inflate(R.layout.mybookrow, parent, false);

            final Button goon=(Button)mybookrow.findViewById(R.id.goon);
            TextView name=(TextView)mybookrow.findViewById(R.id.name);
            TextView lendtime=(TextView)mybookrow.findViewById(R.id.lendtime);
            TextView giveback=(TextView)mybookrow.findViewById(R.id.giveback);
            TextView address=(TextView)mybookrow.findViewById(R.id.address);
            TextView keepcount=(TextView)mybookrow.findViewById(R.id.keepcount);

            final MybookInfo book_i=mybooklist.get(position);

            name.setText(book_i.get(1));
            lendtime.append(book_i.get(2));
            giveback.append(book_i.get(3));
            keepcount.append(book_i.get(4));
            address.append(book_i.get(5));
            goon.append(book_i.get(0)+"(条码号)");


            goon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    postdata.put("username","1241901305");
                    postdata.put("password","195118");
                    postdata.put("flag","goonmybook");
                    postdata.put("bar_code",book_i.get(0));
                    try
                    {
                        String response=new showmylib(postdata).execute(postdata).get();
                        goon.setText(book_i.get(0)+":"+response);

                    }
                    catch (Exception e)
                    {

                    }
//                    android.os.Debug.waitForDebugger();
                    System.out.print(1);
                }
            });
            return mybookrow;
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
    private void showMybook()
    {
        mbi.remove(0);
        Mylib_adapter adapter=new Mylib_adapter(this,mbi);
        showmybook.setAdapter(adapter);

    }
    public static final class MybookInfo
    {
        public String	id;
        public String	name;
        public String	lendtime;
        public String	giveback;
        public String	keepcount;
        public String	address;
        public String	link;

        public void set(int i,String value)
        {
            switch (i)
            {
                case 0:
                    id=value;
                    break;
                case 1:
                    name=value;
                    break;
                case 2:
                    lendtime=value;
                    break;
                case 3:
                    giveback=value;
                    break;
                case 4:
                    keepcount=value;
                    break;
                case 5:
                    address=value;
                    break;
                case 6:
//                    link=value;
                    link="http://www.baidu.com";
                    break;
                default:break;
            }
        }
        public String get(int i)
        {
            String value="";
            switch (i)
            {
                case 0:
                    value=id;
                    break;
                case 1:
                    value=name;
                    break;
                case 2:
                    value=lendtime;
                    break;
                case 3:
                    value=giveback;
                    break;
                case 4:
                    value=keepcount;
                    break;
                case 5:
                    value=address;
                    break;
                case 6:
                    //link="http://www.baidu.com";
                    value=link;
                    break;
                default:break;
            }
            return value;
        }

    }
}
