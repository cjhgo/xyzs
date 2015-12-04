package com.bn.sample;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.*;
import android.widget.TextView;
import android.widget.Toast;

import com.bn.sample.add.fleamarket;
import com.bn.sample.add.job;
import com.bn.sample.add.news;
import com.bn.sample.add.others;
import com.bn.sample.jwxx.Jwxx;

import org.w3c.dom.Text;

public class Sample1_1_Activity extends Activity {
    /** Called when the activity is first created. */
    View home,message,app,setting;
    View[] tabwid=new View[]{home,message,app,setting};
    int[] tabsId=new int[]{R.layout.home,R.layout.message,R.layout.app,R.layout.setting};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        String s = (<caret is here> 
        setContentView(R.layout.main);

        TabHost tab=(TabHost)findViewById(R.id.tabhost);
        LocalActivityManager mLocalActivityManager = new LocalActivityManager(this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);
        tab.setup(mLocalActivityManager);

        Resources resources = this.getResources();
        Drawable[]tabwid_bg=new Drawable[]{
                resources.getDrawable(R.drawable.tabwid_home),
                resources.getDrawable(R.drawable.tabwid_messaage),
                resources.getDrawable(R.drawable.tabwid_app),
                resources.getDrawable(R.drawable.tabwid_setting)};

        for(int i=0;i<tabwid.length;i++)
        {
            tabwid[i]=LayoutInflater.from(this).inflate(R.layout.tabwid,null);
            TextView tv=(TextView)tabwid[i].findViewById(R.id.tab_label);

            tv.setBackgroundDrawable(tabwid_bg[i]);


            if(i == 0)
                tabwid[i].setBackgroundDrawable(resources.getDrawable(R.drawable.tab_l_select));
            else if(i==3)
                tabwid[i].setBackgroundDrawable(resources.getDrawable(R.drawable.tab_r_select));
            else
                tabwid[i].setBackgroundDrawable(resources.getDrawable(R.drawable.tab_c_select));


            final View tabhost=LayoutInflater.from(this).inflate(tabsId[i], null);
            TabSpec setContent = tab.newTabSpec("zhe").setIndicator(tabwid[i]).setContent(
                    new TabContentFactory() {
                        public View createTabContent(String tag) {
                            return tabhost;
                        }
                    });
            tab.addTab(setContent);

//            final Intent mainIntent=new Intent(this,home.class);

//
//            final Intent webInt=new Intent(this,home_web.class);
//            Button btn2=(Button)tabhost.findViewById(R.id.button2);
//            btn2.setOnClickListener(new View.OnClickListener() {
//                // Start new list activity
//                public void onClick(View v) {
//
//                    startActivity(webInt);
//                }
//            });

            if(i ==0)
            {

                final Intent to_fleamarket=new Intent(this,fleamarket.class);
                Button button_to_fleamarket=(Button)tabhost.findViewById(R.id.button_to_fleamarket);
                button_to_fleamarket.setOnClickListener(new View.OnClickListener() {
                    // Start new list activity
                    public void onClick(View v) {

                        Bundle bundle=new Bundle();
                        bundle.putString("fleamarket_Url", "http://justershou.sinaapp.com/market/home.php");
                        to_fleamarket.putExtras(bundle);
                        startActivity(to_fleamarket);

                    }
                });

                final Intent to_news=new Intent(this,news.class);
                Button button_to_news=(Button)tabhost.findViewById(R.id.button_to_news);
                button_to_news.setOnClickListener(new View.OnClickListener() {
                    // Start new list activity
                    public void onClick(View v) {

                        startActivity(to_news);
//                        startActivity(bookInt);
                    }
                });


                final Intent to_job = new Intent(this, job.class);
                Button button_to_job=(Button)tabhost.findViewById(R.id.button_to_job);
                button_to_job.setOnClickListener(new View.OnClickListener() {
                    // Start new list activity
                    public void onClick(View v) {

                        startActivity(to_job);
//                        startActivity(bookInt);
                    }
                });

                final Intent to_others = new Intent(this, others.class);
                Button button_to_other=(Button)tabhost.findViewById(R.id.button_to_others);
                button_to_other.setOnClickListener(new View.OnClickListener() {
                    // Start new list activity
                    public void onClick(View v) {

                        startActivity(to_others);
//                        startActivity(bookInt);
                    }
                });

                final Intent bookInt=new Intent(this,searchbook.class);
                Button lib=(Button)tabhost.findViewById(R.id.button3);
                lib.setOnClickListener(new View.OnClickListener() {
                    // Start new list activity
                    public void onClick(View v) {

                        startActivity(bookInt);
                    }
                });

//                final Intent jwxxx=new Intent(this, Jwxx.class);
//                Button jwxx=(Button)tabhost.findViewById(R.id.button1);
//                jwxx.setOnClickListener(new View.OnClickListener() {
//                    // Start new list activity
//                    public void onClick(View v)
//                    {
//
//                        Toast.makeText(Sample1_1_Activity.this,"gun",Toast.LENGTH_LONG).show();
//                        startActivity(jwxxx);
////                        httpcli.login("1241901305","195118jkd");
//                    }
//                });
            }
        }



    }
}