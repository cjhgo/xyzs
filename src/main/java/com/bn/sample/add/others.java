package com.bn.sample.add;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.bn.sample.R;
/**
 * Created by Administrator on 2015/7/11.
 */
public class others extends Activity {

    Button button_acdmRpt;
    Button button_ntc;
    Button button_schoolMotto;
    Button button_song;
    Button button_society;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.others);
        button_acdmRpt=(Button)findViewById(R.id.buttonReport);
        button_ntc=(Button)findViewById(R.id.buttonNotice);
        button_schoolMotto=(Button)findViewById(R.id.buttonSchoolMotto);
        button_song=(Button)findViewById(R.id.buttonSong);
        button_society=(Button)findViewById(R.id.buttonSociety);
        button_acdmRpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(others.this,academicReport.class);
                startActivity(intent);
            }
        });
        button_ntc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent();
                intent2.setClass(others.this, notice.class);
                startActivity(intent2);
            }
        });
        button_schoolMotto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent();
                intent3.setClass(others.this ,motto.class);
                startActivity(intent3);
            }
        });
        button_song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent();
                intent4.setClass(others.this ,song.class);
                startActivity(intent4);
            }
        });
        button_society.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent5 = new Intent();
                intent5.setClass(others.this , corporation.class);
                startActivity(intent5);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
