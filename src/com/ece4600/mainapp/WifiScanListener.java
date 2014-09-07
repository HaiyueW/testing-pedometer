package com.ece4600.mainapp;

import java.util.List;

import android.net.wifi.ScanResult;

public interface WifiScanListener {
	public void onWifiScanResult(List<ScanResult> scanResults);
}
