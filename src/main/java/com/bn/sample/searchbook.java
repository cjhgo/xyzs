package com.bn.sample;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bn.sample.lib.MyLib;
import com.bn.sample.utils.Htmlutil;

/**
 * Created by chen-pc on 2015/3/13.
 */
public class searchbook extends Activity implements ImageButton.OnClickListener
{
    private RadioGroup radioGroup;
    private RadioButton title;
    private RadioButton author;
    private RadioButton keyword;
    private ImageButton searchbutton;
    private EditText strText;


    private Button login;

    private Handler mhandler;
    private int status;
    private ProgressDialog progressDialog;
    private String search_opt;
    private String search_str;
    private String bookres_str;
    private String first_url;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchbook);
        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
        title=(RadioButton)findViewById(R.id.radioButton1);
        author=(RadioButton)findViewById(R.id.radioButton2);
        keyword=(RadioButton)findViewById(R.id.radioButton3);

        searchbutton=(ImageButton)findViewById(R.id.searchButton);
        searchbutton.setOnClickListener(this);

        strText=(EditText)findViewById(R.id.editText);
        status=0;
        mhandler=new Handler();


        login=(Button)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginmylib=new Intent(searchbook.this, MyLib.class);
                startActivity(loginmylib);
            }
        });
    }
    @Override
    public void onClick(View view)
    {

        setSearch_str();
        if(search_str.length()==0)
        {
            Toast.makeText(getApplicationContext(), "请输入内容",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            progressDialog = new ProgressDialog(searchbook.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            getHtmlP();

        }

    }
    private void setSearch_str()
    {
        search_str =strText.getText().toString();
        int radioButtonID = radioGroup.getCheckedRadioButtonId();
        RadioButton selButton=(RadioButton)findViewById(radioButtonID);
        if(selButton==title)
            search_opt="title";
        else if(selButton==author)
            search_opt="author";
        else
            search_opt="keyword";
        first_url="http://202.195.195.137:8080/opac/openlink.php?"+search_opt+"="+
                java.net.URLEncoder.encode(search_str)+"&"+
                "location=ALL&doctype=ALL&lang_code=ALL&match_flag=forward" +
                "&displaypg=20&showmode=list&orderby=DESC&sort=CATA_DATE";
    }
    private void getHtmlP()
    {
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                bookres_str=Htmlutil.getHtmlString(first_url);
                if(bookres_str!="")
                {
                    status=1;
                }
                toRes();

            }
        }).start();
    }
    private void toRes()
    {
        this.mhandler.post(new Runnable() {
            @Override
            public void run()
            {


                if(searchbook.this.status==0)
                {
                    Toast.makeText(searchbook.this,R.string.searchbook_error,Toast.LENGTH_LONG).show();
                    return;
                }


                Intent getres = new Intent(searchbook.this,bookresult.class);

                Bundle postdata=new Bundle();
                postdata.putString("url",first_url);
                postdata.putString("data",bookres_str);

                getres.putExtras(postdata);


                startActivity(getres);
                progressDialog.dismiss();
            }
        });
    }

}
