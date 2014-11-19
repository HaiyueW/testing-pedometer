package com.ece4600.mainapp;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class Bluetooth extends Activity{
	private BluetoothAdapter myBluetoothAdapter;
	private ListView listpaired;
	Button blueon, blueoff, bluecancel, bluesearch;
    
    //For toast messages:
    Context context;
    private Handler handler = new Handler();
    
    //TI SensorTag device info
    private final String device1_MAC = "90:59:AF:0B:82:F4";
    private final String device2_MAC = "90:59:AF:0B:82:D9";
    
    
    //--------------------------------------------------------------------
    // ON CREATE function
    //--------------------------------------------------------------------
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth);
		myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
		blueon = (Button)findViewById(R.id.blueon);
		blueoff = (Button)findViewById(R.id.blueoff);
		bluecancel = (Button)findViewById(R.id.bluecancel);
		bluesearch =(Button)findViewById(R.id.bluesearch);
		
		initButtons();
		
		context = this;

		
	}
	
	 protected void onDestroy() {
	        super.onDestroy();
	    }



	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bluetooth, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onClick(View v) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		listpaired.setAdapter(adapter);
		switch (v.getId()) {
		case R.id.blueon:
			startActivity(new Intent(Bluetooth.this, MainActivity.class));
			//finish();
			break;
		case R.id.blueoff:
			//myBluetoothAdapter.disable();
			Toast.makeText(getApplicationContext(), "Bluetooth Turned OFF", Toast.LENGTH_SHORT).show();
			break;
		case R.id.bluecancel:
			//poll();
			startActivity(new Intent(Bluetooth.this, MainActivity.class));
			finish();
		default:
			break;
		}		     
	}

	
	
	public void initButtons(){
		bluecancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(Bluetooth.this, MainActivity.class));
			}
		});
		
	    
		bluesearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				 if(myBluetoothAdapter!=null){
		        Intent intent = new Intent(Bluetooth.this, bleService.class);
		        startService(intent);
				 }
			}     
	    });
		
		blueon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				 if(myBluetoothAdapter==null){
					 Toast.makeText(getApplicationContext(), "Bluetooth service not available in the device", Toast.LENGTH_SHORT).show();
	             }
				 else{
					 if(!myBluetoothAdapter.isEnabled()){
							Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
							startActivityForResult(turnOn, 0);
							Toast.makeText(getApplicationContext(), "Bluetooth turned ON", Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(getApplicationContext(), "Bluetooth is already ON", Toast.LENGTH_SHORT).show();
						}
					 }	
			}     
	    });
		
	}
	}

 