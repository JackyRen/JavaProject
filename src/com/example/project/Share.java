package com.example.project;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Share extends Activity{
	private EditText text;
	private Button share;
	private Button moments;
	private static final String APP_ID = "wx4b65e523e9c1bcc5";
	private IWXAPI api;
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		setContentView(R.layout.share);
		text = (EditText)findViewById(R.id.info);
		Bundle getBundle = getIntent().getExtras();
		if (getBundle.getString("HASINTERNET").equals(new String("YES"))){
			String mes = "I'm in " + getBundle.getString("CITY") + ". The weather is " + getBundle.getString("WEATHER") + 
				". The temperature is " + getBundle.getString("TEMP") + ". ";
			text.append(mes);
		}
		else{
			text.append("No Internet!");
		}
		share = (Button)findViewById(R.id.share);
		share.setOnClickListener(new View.OnClickListener() {  
            public void onClick(View v) {  
            	WXTextObject textObj = new WXTextObject();
            	textObj.text = text.getText().toString();
            	WXMediaMessage msg = new WXMediaMessage();
            	msg.mediaObject = textObj;
            	msg.description = "Weather";
            	SendMessageToWX.Req req = new SendMessageToWX.Req();
            	req.transaction = String.valueOf(System.currentTimeMillis());
            	req.message = msg;
            	api.sendReq(req);
            }  
        }  );
		moments = (Button)findViewById(R.id.friend);
		moments.setOnClickListener(new View.OnClickListener() {  
            public void onClick(View v) {  
            	WXTextObject textObj = new WXTextObject();
            	textObj.text = text.getText().toString();
            	WXMediaMessage msg = new WXMediaMessage();
            	msg.mediaObject = textObj;
            	msg.description = "Weather";
            	SendMessageToWX.Req req = new SendMessageToWX.Req();
            	req.scene = SendMessageToWX.Req.WXSceneTimeline;
            	req.transaction = String.valueOf(System.currentTimeMillis());
            	req.message = msg;
            	api.sendReq(req);
            }  
        }  );
		regToWx();
		
	}
	private void regToWx() {
		api = WXAPIFactory.createWXAPI(this, APP_ID, true);
		api.registerApp(APP_ID);
	}
}
