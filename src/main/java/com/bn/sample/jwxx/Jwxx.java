package com.bn.sample.jwxx;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

import com.bn.sample.R;

/**
 * Created by chen-pc on 2015/5/23.
 */
public class Jwxx extends Activity
{
    private EditText edit;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jwxx);
        edit=(EditText)findViewById(R.id.jwxxstr);
        Info.init("1241901305","195118");
        String res=Info.getInstance().getInfo(1);
        edit.setText(res);
    }
}
