package com.bn.sample.add;

import android.os.StrictMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by judy on 2015/5/30.
 */
public class fetchWeb {
    String fetch(String s)
    {
        String resultData=null;
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskWrites().detectDiskReads().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());

        try
        {

            URL url=new URL(s);
            HttpURLConnection conn=(HttpURLConnection)url.openConnection();
            InputStreamReader in=new InputStreamReader(conn.getInputStream());
            BufferedReader buffer=new BufferedReader(in);
            String inputLine=null;

            while(((inputLine=buffer.readLine())!=null))
            {
                resultData+=inputLine+"\n";
            }
            in.close();
            conn.disconnect();





        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

    return  resultData;
    }
}
