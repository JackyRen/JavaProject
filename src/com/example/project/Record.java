package com.example.project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Record extends Activity{
	private TextView gps;
	private TextView temp;
	private TextView wifi;
	private TextView time;
	private Button take;
	private Button send;
	private ImageView image;  
    private File mPhotoFile;  
    private String mPhotoPath;  
    //private String dataPath = "mnt/sdcard/JavaProject.txt";
    public final static int CAMERA_RESULT=8888;  
    public final static String TAG="xx";  
	@Override
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		setContentView(R.layout.record);
		gps = (TextView)findViewById(R.id.gps);
		temp = (TextView)findViewById(R.id.temp2);
		wifi = (TextView)findViewById(R.id.wifi);
		time = (TextView)findViewById(R.id.time);
		
		Bundle getBundle = getIntent().getExtras();
		temp.append(getBundle.getString("temp"));
		
		send = (Button)findViewById(R.id.send);  
		send.setOnClickListener(new View.OnClickListener() {  
            public void onClick(View v) {  
                String s = gps.getText().toString() + " " + temp.getText().toString() + " " + wifi.getText().toString() + 
                		" " + time.getText().toString() + " " + mPhotoPath;
                SharedPreferences.Editor sharedata = getSharedPreferences("data", 0).edit();  
                sharedata.putString("record",s);  
                sharedata.commit();
                
            }  
        }  );  
		take = (Button) findViewById(R.id.takephoto);  
        take.setOnClickListener(new View.OnClickListener() {  
            public void onClick(View v) {  
                try {  
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");  
                    mPhotoPath="mnt/sdcard/DCIM/Camera/"+getPhotoFileName();  
                    mPhotoFile = new File(mPhotoPath);  
                    if (!mPhotoFile.exists()) {  
                        mPhotoFile.createNewFile();  
                    }  
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));  
                    startActivityForResult(intent,CAMERA_RESULT);  
                } catch (Exception e) {  
                }  
            }  
        }  );  
        image = (ImageView) findViewById(R.id.imageView);  
		
		gps.setTextSize(20);
		temp.setTextSize(20);
		wifi.setTextSize(20);
		time.setTextSize(20);
		
		
		Calendar c = Calendar.getInstance();
		String t = "Time : ";
		t += c.get(Calendar.YEAR) + "/";
		t += c.get(Calendar.MONTH) + 1 + "/";
		t += c.get(Calendar.DAY_OF_MONTH) + " ";
		t += transfer(c.get(Calendar.HOUR_OF_DAY)) + ":";
		t += transfer(c.get(Calendar.MINUTE)) + ":";
		t += transfer(c.get(Calendar.SECOND));
		time.setText(t);
		getLocation();
		getWifi();
	}
	private String getPhotoFileName() {  
	    Date date = new Date(System.currentTimeMillis());  
	    SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");  
	    return dateFormat.format(date) + ".jpg";  
	}  
	private String transfer(int value){
		String ret = "";
		if (value < 10){
			ret += "0";
		}
		ret += String.valueOf(value);
		return ret;
	}
	private void getWifi(){
		try{
		WifiManager wifi_service = (WifiManager)getSystemService(WIFI_SERVICE); 
		WifiInfo wifiInfo = wifi_service.getConnectionInfo(); 
		List<ScanResult> scanResults = wifi_service.getScanResults();
		String best = "";
		int best_level = -1000;

	    for (ScanResult scanResult : scanResults) {
	    	if (scanResult.level < 0 && scanResult.level > best_level){
	    		best_level = scanResult.level;
	    		best = scanResult.SSID;
	    	}
	    }
	    wifi.setText("WIFI : " + best);
		}
		catch (Exception e){
			wifi.setText("WIFI : No WIFI");
		}
	    
	}
	private void getLocation(){
		try{
		LocationManager locationManager  = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);//
		criteria.setAltitudeRequired(false);//
		criteria.setBearingRequired(false);//
		criteria.setPowerRequirement(Criteria.POWER_LOW);//
		String provider = locationManager.getBestProvider(criteria, true); 

		Location location = locationManager.getLastKnownLocation(provider);
		String loc = "GPS : ";
		if (location != null) {       
            //Log.i("log", "Location changed : Lat: "  + arg0.getLatitude() + " Lng: " + arg0.getLongitude());   
            gps.setText(loc + location.getLongitude() + " " + location.getLatitude());  
        } else {  
            //Log.i("log", "Location changed : Lat: "  + "NULL" + " Lng: " + "NULL");    
            gps.setText(loc + "No GPS information!");  
        } 
		
		}
		catch (Exception e){
			wifi.setText(e.toString());
		}
	}
	@Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        super.onActivityResult(requestCode, resultCode, data);  
        if (requestCode==CAMERA_RESULT) {  
             Bitmap bitmap = BitmapFactory.decodeFile(mPhotoPath, null);    
             image.setImageBitmap(bitmap);  
        }  
    }  
}
