package com.example.project;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Settings extends Activity{
	private EditText user;
	private EditText pass;
	private EditText server;
	private Button confirm;
	//private Button open;
	//private Button close;
	private RadioGroup alarmgroup; 
	private RadioButton alarmbut[] = new RadioButton[4];
	private int id = 3;
	@Override
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		setContentView(R.layout.settings);
		user = (EditText)findViewById(R.id.eusername);
		pass = (EditText)findViewById(R.id.epassword);
		server = (EditText)findViewById(R.id.eserver);
		alarmbut[0] = (RadioButton)findViewById(R.id.alarm1);
		alarmbut[1] = (RadioButton)findViewById(R.id.alarm2);
		alarmbut[2] = (RadioButton)findViewById(R.id.alarm3);
		alarmbut[3] = (RadioButton)findViewById(R.id.alarm4);
		Bundle getBundle = getIntent().getExtras();
		user.setText(getBundle.getString("USER"));
		pass.setText(getBundle.getString("PASS"));
		server.setText(getBundle.getString("SERVER"));
		id = Integer.valueOf(getBundle.getString("ID"));
		confirm = (Button)findViewById(R.id.confirm);
		confirm.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				try{
				Intent backIntent = new Intent();
				Bundle backBundle = new Bundle();
				backBundle.putString("USER", user.getText().toString());
				backBundle.putString("PASS", pass.getText().toString());
				backBundle.putString("SERVER", server.getText().toString());
				backBundle.putString("ID", String.valueOf(id));
				backIntent.putExtras(backBundle);
				setResult(RESULT_OK, backIntent);
				Settings.this.finish();
				}
				catch (Exception e){
					Log.d("!!!!!", e.toString());
				}
			}
			
		});
		alarmgroup = (RadioGroup)this.findViewById(R.id.alarmgroup);
		alarmgroup.check(alarmbut[id].getId());
		alarmgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {    
            @Override 
            public void onCheckedChanged(RadioGroup group, int checkedId) { 
            	//server.append(new Integer(checkedId).toString());
            	if (checkedId == R.id.alarm1){
            		id = 0;
            		setAlarm(30*60);
            	}
            	else if (checkedId == R.id.alarm2){
            		id = 1;
            		setAlarm(60*60);
            	}
            	else if (checkedId == R.id.alarm3){
            		id = 2;
            		setAlarm(120*60);
            	}
            	else if (checkedId == R.id.alarm4){
            		id = 3;
            		clearAlarm();
            		Toast.makeText(Settings.this, "Alarm canceled!", Toast.LENGTH_SHORT).show();
            	}
            } 
		}); 
		
		/*open = (Button)findViewById(R.id.open);
		open.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(Settings.this,AlarmReceiver.class);   
	            PendingIntent pendingIntent = PendingIntent.getBroadcast(Settings.this, 0, intent, 0);   
	            //获取闹钟管理器   
	            AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);   
	            //设置闹钟   
	            long time = System.currentTimeMillis();
	            alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);   
	            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, 10*1000, pendingIntent);   
			}
			
		});
		close = (Button)findViewById(R.id.close);
		close.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Settings.this,AlarmReceiver.class);   
                PendingIntent pendingIntent = PendingIntent.getBroadcast(Settings.this, 0, intent, 0);   
                //获取闹钟管理器   
                AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);   
                alarmManager.cancel(pendingIntent);
			}
			
		});*/
	}
	
	private void setAlarm(int t){
		clearAlarm();
		Intent intent = new Intent(Settings.this,AlarmReceiver.class);   
        PendingIntent pendingIntent = PendingIntent.getBroadcast(Settings.this, 0, intent, 0);   
        //获取闹钟管理器   
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);   
        //设置闹钟   
        long time = System.currentTimeMillis();
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);   
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, t*1000, pendingIntent);
        Toast.makeText(Settings.this, "Alarm set!", Toast.LENGTH_SHORT).show();
	}
	
	private void clearAlarm(){
		Intent intent = new Intent(Settings.this,AlarmReceiver.class);   
        PendingIntent pendingIntent = PendingIntent.getBroadcast(Settings.this, 0, intent, 0);   
        //获取闹钟管理器   
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);   
        alarmManager.cancel(pendingIntent);
	}
}
