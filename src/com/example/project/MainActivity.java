package com.example.project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private TextView city;
	private TextView weather;
	private TextView temp;
	private TextView wind;
	private Button settings;
	private Button record;
	private Button share;
	private Button check;
	private String user = "";
	private String pass = "";
	private String server = "";
	private String temperature = "";
	private final int SETTING_CODE = 1;
	private int id = 3;
	//String imageUrl = "http://l.yimg.com/a/i/us/we/52/32.gif";
	Bitmap bmImg;    
	ImageView imView;
	/*ImageGetter imageGetter = new ImageGetter() {

			  @Override
			  public Drawable getDrawable(String source) {
			   int id = Integer.parseInt(source);
			   Drawable drawable = getResources().getDrawable(id);
			   drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
			   return drawable;
			  }
			 };*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath().build());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		city = (TextView)findViewById(R.id.city);
		weather = (TextView)findViewById(R.id.weather);
		temp = (TextView)findViewById(R.id.temp);
		wind = (TextView)findViewById(R.id.wind);
		check = (Button)findViewById(R.id.check);
		check.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
	    		intent.setClass(MainActivity.this,Check.class);
	    		//Bundle bundle=new Bundle();
	    		//intent.putExtras(bundle);
	    		startActivity(intent);
			}
			
		});
		share = (Button)findViewById(R.id.share);
		share.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
	    		intent.setClass(MainActivity.this,Share.class);
	    		Bundle bundle=new Bundle();
	    		if (city.getText().toString().equals(new String("No Internet"))){
	    			bundle.putString("HASINTERNET", "NO");
	    		}
	    		else{
	    			bundle.putString("HASINTERNET", "YES");
	    		}
	    		bundle.putString("CITY", city.getText().toString());
	    		bundle.putString("WEATHER", weather.getText().toString());
	    		bundle.putString("TEMP", temp.getText().toString());
	    		intent.putExtras(bundle);
	    		startActivity(intent);
			}
			
		});
		settings = (Button)findViewById(R.id.settings);
		settings.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
	    		intent.setClass(MainActivity.this,Settings.class);
	    		Bundle bundle=new Bundle();
	    		bundle.putString("USER", user);
	    		bundle.putString("PASS", pass);
	    		bundle.putString("SERVER", server);
	    		bundle.putString("ID", String.valueOf(id));
	    		intent.putExtras(bundle);
	    		startActivityForResult(intent, SETTING_CODE);
			}
			
		});
		record = (Button)findViewById(R.id.record);
		record.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
	    		intent.setClass(MainActivity.this,Record.class);
	    		Bundle bundle=new Bundle();
	    		bundle.putString("temp", temperature);
	    		intent.putExtras(bundle);
	    		//startActivityForResult(intent, SETTING_CODE);
	    		startActivity(intent);
			}
			
		});
		city.setTextSize(30);
		weather.setTextSize(25);
		temp.setTextSize(20);
		wind.setTextSize(20);
		imView = (ImageView) findViewById(R.id.imview);    
		setWeather(get("http://xml.weather.yahoo.com/forecastrss?w=2151330&u=c"));//beijing
		//setWeather(get("http://xml.weather.yahoo.com/forecastrss?w=2163866&u=c"));
		//imView.setImageBitmap(returnBitMap(imageUrl));
			  
			 

		
		//weather.append(Html.fromHtml("<img src='"+R.drawable.laucher+"'/>", imageGetter, null));


		/*context = (TextView)findViewById(R.id.textView1);
		url = (EditText)findViewById(R.id.edit_message);
		but1 = (Button)findViewById(R.id.button1);
		but1.setOnClickListener(new OnClickListener (){
	    	public void onClick(View v){
	    		context.setText("");
	    		try{
	    			context.append(get("http://xml.weather.yahoo.com/forecastrss?w=2163866&u=c"));
	    			//context.append("finish");
	    		}
	    		catch (Exception e){
	    			context.append("Exception");
	    		}
	        }
		});*/
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == SETTING_CODE){
			if (resultCode != RESULT_CANCELED){
				user = data.getStringExtra("USER");
				pass = data.getStringExtra("PASS");
				server = data.getStringExtra("SERVER");
				id = Integer.valueOf(data.getStringExtra("ID"));
				//wind.setText(user+"\r\n"+pass+"\r\n"+server+"\r\n");
			}
		}
	}
	
	public void setWeather(String xml){
		Pattern p = Pattern.compile("code=\"[^\"]+");
		Matcher m = p.matcher(xml);
		String picUrl = "";
		if (m.find()){
			m.group();
			if (m.find()){
				String pic = m.group();
				pic = pic.replaceAll("code=\"", "");
				picUrl = "http://l.yimg.com/a/i/us/we/52/"+ pic +".gif";
				imView.setImageBitmap(returnBitMap(picUrl));
			}
			
		}
		p = Pattern.compile("city=\"[^\"]+");
		m = p.matcher(xml);
		if (m.find()){
			String c = m.group();
			c = c.replaceAll("city=\"", "");
			city.setText(c);
		}
		p = Pattern.compile("text=\"[^\"]+");
		m = p.matcher(xml);
		if (m.find()){
			m.group();
			if (m.find()){
				String w = m.group();
				w = w.replaceAll("text=\"", "");
				weather.setText(w);
			}
			
		}
		String t = "";
		p = Pattern.compile("low=\"[^\"]+");
		m = p.matcher(xml);
		if (m.find()){
			String l = m.group();
			l = l.replaceAll("low=\"", "");
			t += l + "℃ ~ ";
		}
		p = Pattern.compile("high=\"[^\"]+");
		m = p.matcher(xml);
		if (m.find()){
			String h = m.group();
			h = h.replaceAll("high=\"", "");
			t += h + "℃";
		}
		temp.setText(t);
		p = Pattern.compile("temp=\"[^\"]+");
		m = p.matcher(xml);
		if (m.find()){
			temperature = m.group();
			temperature = temperature.replaceAll("temp=\"", "");
			temperature += "℃";
		}
	}
	
	public Bitmap returnBitMap(String url) {    
		URL myFileUrl = null;    
		Bitmap bitmap = null;    
		try {    
			myFileUrl = new URL(url);    
		} catch (MalformedURLException e) {    
			e.printStackTrace();    
		}    
		try {    
			HttpURLConnection conn = (HttpURLConnection)myFileUrl.openConnection();    
			conn.setDoInput(true);    
			conn.connect();    
			InputStream is = conn.getInputStream();    
			bitmap = BitmapFactory.decodeStream(is);    
			is.close();    
		} catch (IOException e) {    
			e.printStackTrace();    
		}    
		return bitmap;    
	}
	
	public static String get(String uri) {
		BufferedReader reader = null;
		StringBuffer sb = null;
		String result = "";
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(uri);
		try {
			// 发送请求，得到响应
			HttpResponse response = client.execute(request);

			// 请求成功
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				reader = new BufferedReader(new InputStreamReader(response
						.getEntity().getContent()));
				sb = new StringBuffer();
				String line = "";
				// String NL = System.getProperty("line.separator");
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
			}
		} catch (ClientProtocolException e) {
			//android.util.Log.d("", "protocol");
			e.printStackTrace();
		} catch (IOException e) {
			//android.util.Log.d("", "io1");
			e.printStackTrace();
		} catch (Exception e) {
			//android.util.Log.d("", "io1");
			e.printStackTrace();
		} 
		finally {
			try {
				if (null != reader) {
					reader.close();
					reader = null;
				}
			} catch (IOException e) {
				//android.util.Log.d("", "io2");
				e.printStackTrace();
			}
		}
		if (null != sb) {
			result = sb.toString();
		}
		return result;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
