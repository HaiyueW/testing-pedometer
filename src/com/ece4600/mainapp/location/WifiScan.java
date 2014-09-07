package com.ece4600.mainapp.location;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

public class WifiScan {
	private Context context;
	private WifiManager wifiManager;
	private WifiScanResultReceiver scanResultReceiver;
	
	private List<WifiScanListener> listeners;
	private boolean scanning = false;
	
	public WifiScan(Context context) {
		this.context = context;
		this.listeners = new ArrayList<WifiScanListener>();
		
		initWifiManager();
	}
	
	private void initWifiManager() {
		wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		
		scanResultReceiver = new WifiScanResultReceiver();
	}
	
	public void getWifiData(WifiScanListener listener) {
		listeners.add(listener);
		
		if (!scanning) {
			wifiManager.startScan();
			scanning = true;
		}
	}
	
	public void unregisterReceiver() {
		context.unregisterReceiver(scanResultReceiver);
	}
	
	public void registerReceiver() {
		context.registerReceiver(scanResultReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
	}
	
	private void onWifiScanResults()  {
		List<ScanResult> scanResults = wifiManager.getScanResults();
		
		for (WifiScanListener listener : listeners) {
			listener.onWifiScanResult(scanResults);
		}
		
		scanning = false;
		listeners.clear();
	}
	
	class WifiScanResultReceiver extends BroadcastReceiver {
    	public void onReceive(Context context, Intent intent) {
    		onWifiScanResults();
    	}
    }
}
