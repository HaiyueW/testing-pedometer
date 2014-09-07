package com.ece4600.mainapp;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ece4600.mainapp.location.WifiScan;
import com.ece4600.mainapp.location.WifiScanListener;

public class DisplayWifi extends Activity implements WifiScanListener {
	private TextView wifiInfo;
	private TextView status;
	
	private WifiScan wifi;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_wifi);
        setupMessageButtonreturn();
        
        wifi = new WifiScan(this);
        wifiInfo = (TextView) findViewById(R.id.wifi_info);
        status = (TextView) findViewById(R.id.wifi_status);
        
		
		refreshAccessPoints();
    }
    
    private void setupMessageButtonreturn() {
		// TODO Auto-generated method stub
    	Button messageButton = (Button)findViewById(R.id.return_location);
    	messageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Toast.makeText(Location.this, "Return to profile", Toast.LENGTH_LONG).show();
				startActivity(new Intent(DisplayWifi.this, Location.class));
				finish();
			}
		});	
	}

	public void refreshAccessPoints(View view) {
    	refreshAccessPoints();
    }
    
    public void refreshAccessPoints() {
		wifi.getWifiData(this);
		
		wifiInfo.setText(R.string.loading);
	}
    
   
    
    
    public void onWifiScanResult(List<ScanResult> scanResults) {
    	String result = "";
    	
		for (ScanResult scanResult : scanResults) {
			result += scanResult.SSID + " (" + scanResult.BSSID + ") RSSI: " + scanResult.level;
			
			result += "\n";
		}
		
		wifiInfo.setText(result);
	}

   
    
    @Override
	protected void onPause() {
    	wifi.unregisterReceiver();
        super.onPause();
    }

	@Override
    protected void onResume() {
		wifi.registerReceiver();
        super.onResume();
    }
    
   

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
