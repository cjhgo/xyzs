package com.bn.sample.add;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import com.bn.sample.R;
/**
 * Created by Zhao on 2015/5/29.
 */
public class academic_report_Result extends Activity {
    TextView ar_title;
    TextView ar_clickTimes;
    TextView ar_body;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.academic_report_detail);
        setTitle("学术报告");

        Intent intent=getIntent();
        String url=intent.getStringExtra("url");
        String mclickTiems=intent.getStringExtra("clickTimes");
        String body=intent.getStringExtra("body");

        ar_title=(TextView)findViewById(R.id.ar_title);
        ar_clickTimes=(TextView)findViewById(R.id.ar_clickTimes_date);
        ar_body=(TextView)findViewById(R.id.ar_body);

        ar_title.setText(url);
        ar_clickTimes.setText(mclickTiems);
        ar_body.setText(body);
        ar_body.setMovementMethod(ScrollingMovementMethod.getInstance());
    }
}
