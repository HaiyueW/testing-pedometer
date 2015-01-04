package com.ece4600.mainapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//edited by tianqi
public class Location extends Activity {

	private Button messageButton;
	private TextView allNetWork;
	private Button start;
	private Button stop;
	private Button check;
	private Button output;

	private WifiAdmin mWifiAdmin;
	private List<ScanResult> list;
	private ScanResult mScanResult;
	private StringBuffer sb = new StringBuffer();
	private EditText roomnm;
	private EditText spotnm;
	
	private String filename = "fingerprint.xml";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		setupMessageButton1();
		mWifiAdmin = new WifiAdmin(Location.this);
		init();
	}
	
	
	public void init() {
		messageButton = (Button)findViewById(R.id.scan);
		allNetWork = (TextView) findViewById(R.id.allNetWork);
		start = (Button) findViewById(R.id.start);
		stop = (Button) findViewById(R.id.stop);
		check = (Button) findViewById(R.id.check);
		output = (Button) findViewById(R.id.output);
		roomnm = (EditText) findViewById(R.id.roomnm);
		spotnm = (EditText) findViewById(R.id.spotnm);
		start.setOnClickListener(new MyListener());
		stop.setOnClickListener(new MyListener());
		check.setOnClickListener(new MyListener());
		output.setOnClickListener(new MyListener());
		messageButton.setOnClickListener(new MyListener());
	}

	
	
	private class MyListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.scan:
				getAllNetWorkList();
				break;
			case R.id.start:
				mWifiAdmin.openWifi();
				Toast.makeText(Location.this,
						"Current WiFi Status is on" + mWifiAdmin.checkState(),
						Toast.LENGTH_SHORT).show();
				break;
			case R.id.stop:
				mWifiAdmin.closeWifi();
				Toast.makeText(Location.this,
						"Current WiFi Status is off" + mWifiAdmin.checkState(),
						Toast.LENGTH_SHORT).show();
				break;
			case R.id.check:

				String room = roomnm.getText().toString();
				String spot = spotnm.getText().toString();
				if (list != null) {

					// we have to bind the new file with a FileOutputStream
					FileOutputStream fileos = null;
					try {
						fileos = openFileOutput("fingerprint.xml",
								Context.MODE_APPEND);
					} catch (FileNotFoundException e) {
						Log.e("FileNotFoundException",
								"can't create FileOutputStream");
					}
					// we create a XmlSerializer in order to write xml data
					XmlSerializer serializer = Xml.newSerializer();
					try {
						// we set the FileOutputStream as output for the
						// serializer, using UTF-8 encoding
						serializer.setOutput(fileos, "UTF-8");
						// Write <?xml declaration with encoding (if encoding
						// not null) and standalone flag (if standalone not
						// null)
						// serializer.startDocument(null,
						// Boolean.valueOf(true));
						// set indentation option
						serializer
								.setFeature(
										"http://xmlpull.org/v1/doc/features.html#indent-output",
										true);
						// start a tag called "root"
						serializer.startTag(null, "WifiInfo");
						// i indent code just to have a view similar to xml-tree

						serializer.startTag("", "Room");
						serializer.text(room);
						serializer.endTag("", "Room");
						serializer.startTag("", "Spot");
						serializer.text(spot);
						serializer.endTag("", "Spot");

						if (list != null) {
							for (int i = 0; i < list.size(); i++) {
								
								mScanResult = list.get(i);
								String mac = mScanResult.BSSID;
								String level = "" + mScanResult.level;
								serializer.startTag("", "BSSID");
								serializer.text(mac);
								serializer.endTag("", "BSSID");
								serializer.startTag("", "level");
								serializer.text(level);
								serializer.endTag("", "level");
							}
						}

						serializer.endTag(null, "WifiInfo");

						// serializer.endDocument();
						// write xml data into the FileOutputStream
						serializer.flush();
						// finally we close the file stream
						fileos.close();

					} catch (Exception e) {
						Log.e("Exception",
								"error occurred while creating xml file");
					}
					Toast.makeText(
							Location.this,
							"FingerPrint has been written into XML file"
									+ filename, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(
							Location.this,
							"There is no Fingerprint data available currently, please click the scan button",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.output:

				String oldfile = getFilesDir() + "/fingerprint.xml";
			
				File file = new File(oldfile);
				file.delete();
				break;
			default:
				break;
			}

		}
	
	}
	
	
	public void getAllNetWorkList() {
	
		if (sb != null) {
			sb = new StringBuffer();
		}
	
		mWifiAdmin.startScan();
		list = mWifiAdmin.getWifiList();
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				
				mScanResult = list.get(i);
				sb = sb.append(mScanResult.BSSID + "  ")
						.append(mScanResult.SSID + "   ")
						.append(mScanResult.capabilities + "   ")
						.append(mScanResult.frequency + "   ")
						.append(mScanResult.level + "\n\n");
			}
			allNetWork.setText("WiFi Scan Result\n" + sb.toString());
		}
	}

	
	
	private void setupMessageButton1(){
    	Button messageButton = (Button)findViewById(R.id.returnloca);
    	messageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Toast.makeText(Location.this, "Return to profile", Toast.LENGTH_LONG).show();
				startActivity(new Intent(Location.this, MainActivity.class));
				finish();
			}
		});	
    }
	
	public void onBackPressed() {
		// do something on back.return;		
		startActivity(new Intent(Location.this, MainActivity.class));
		finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.location, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		super.onOptionsItemSelected(item);
    	switch(item.getItemId()){
    	case R.id.locamenu_pedo:
    		startActivity(new Intent(this, Pedometer.class));
    		finish();
    		break;
    	case R.id.locamenu_heart:
    		startActivity(new Intent(this, Heartrate.class));
    		finish();
    		break;
    	case R.id.locamenu_post:
    		startActivity(new Intent(this, Posture.class));
    		finish();
    		break;
    	}
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return true; 
	}
}
