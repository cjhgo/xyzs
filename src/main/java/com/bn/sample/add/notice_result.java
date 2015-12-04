package com.bn.sample.add;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import com.bn.sample.R;
/**
 * Created by Zhao on 2015/6/3.
 */
public class notice_result extends Activity {
    TextView ntc_title;
    TextView ntc_clickTimes_date;
    TextView ntc_body;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_detail);
        setTitle("通知公告");

        Intent intent=getIntent();
        String title=intent.getStringExtra("title");
        String clickTimes=intent.getStringExtra("clickTimes");
        String body=intent.getStringExtra("body");

        ntc_title=(TextView)findViewById(R.id.ntc_title);
        ntc_clickTimes_date=(TextView)findViewById(R.id.ntc_clickTimes_date);
        ntc_body=(TextView)findViewById(R.id.ntc_body);

        ntc_title.setText(title);
        ntc_clickTimes_date.setText(clickTimes);
        ntc_body.setText(body);
        ntc_body.setMovementMethod(ScrollingMovementMethod.getInstance());
    }
}
