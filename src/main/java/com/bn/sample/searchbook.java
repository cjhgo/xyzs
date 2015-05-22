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

    private ProgressDialog progressDialog;
    private Handler mhandler;
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

        final String str =strText.getText().toString();
        if(str.length()==0)
        {
            Toast.makeText(getApplicationContext(), "请输入内容",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            final ProgressDialog progressDialog = new ProgressDialog(searchbook.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            (new Handler()).postDelayed(new Runnable() {
                @Override
                public void run()
                {
                    String opt;
                    int radioButtonID = radioGroup.getCheckedRadioButtonId();
                    RadioButton selButton=(RadioButton)findViewById(radioButtonID);
                    if(selButton==title)
                        opt="title";
                    else if(selButton==author)
                        opt="author";
                    else
                        opt="keyword";

                    Intent getres = new Intent(searchbook.this,bookresult.class);

                    String first_url="http://202.195.195.137:8080/opac/openlink.php?"+opt+"="+
                            java.net.URLEncoder.encode(str)+"&"+
                            "location=ALL&doctype=ALL&lang_code=ALL&match_flag=forward" +
                            "&displaypg=20&showmode=list&orderby=DESC&sort=CATA_DATE";

                    String first_htmldata= Htmlutil.getHtmlString(first_url);
                    Bundle postdata=new Bundle();

                    postdata.putString("url",first_url);
                    postdata.putString("data",first_htmldata);

                    getres.putExtras(postdata);

                    progressDialog.dismiss();

                    startActivity(getres);
                    return;

                }
            },2000);

        }

    }
}
