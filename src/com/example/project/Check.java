package com.example.project;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Check extends Activity{
	private Button check;
	private TextView text;
	@Override
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		setContentView(R.layout.check);
		check = (Button)findViewById(R.id.check);
		check.setOnClickListener(new View.OnClickListener() {  
            public void onClick(View v) {  
            	SharedPreferences sharedata = getSharedPreferences("data", 0);  
            	String data = sharedata.getString("record", null);  
                text.append(data);
            }  
        }  );  
		text = (TextView)findViewById(R.id.s);
	}
}
