package com.example.project;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {   
	  
    /* (non-Javadoc)  
     * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)  
     */  
    @Override  
    public void onReceive(Context arg0, Intent data) {   
        Log.d("Settings", "the time is up,start the alarm...");
        Toast.makeText(arg0, "It's time to record your life!", Toast.LENGTH_LONG).show();
        //Toast.makeText(arg0, "该记录生活了！", Toast.LENGTH_SHORT).show();
    }   
}  
