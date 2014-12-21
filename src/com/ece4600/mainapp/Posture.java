package com.ece4600.mainapp;


import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
//import android.widget.Toast;

import android.widget.ImageView;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;


public class Posture extends Activity {
	
	private SharedPreferences mSharedPrefs;
	private BluetoothAdapter myBluetoothAdapter;

	// main code
	
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private TextView axisX1, axisY1, axisZ1,axisX2, axisY2, axisZ2;
	public TextView timenano,x_avg,threshold;
	public dataSample[] array_10 = new dataSample[10];
	int i = 0;
	public ImageView img;
	
	//public ImageView img = new ImageView(this);
	
	
	Bitmap posture_states[] = new Bitmap[2];
	
	
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		//bluetooth stuff starts here
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_posture);
		myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		bluetoothTest();
		setupMessageButton();
		//bluetooth stuff ends here;

		
		//main code starts here
		axisX1 = (TextView) findViewById(R.id.acc_x1);
		axisY1 = (TextView) findViewById(R.id.acc_y1);
		axisZ1 = (TextView) findViewById(R.id.acc_z1);
		
		axisX2 = (TextView) findViewById(R.id.acc_x2);
		axisY2 = (TextView) findViewById(R.id.acc_y2);
		axisZ2 = (TextView) findViewById(R.id.acc_z2);
		
		img = (ImageView) findViewById(R.id.displayIMG); 
		
		
		posture_states[0] = BitmapFactory.decodeResource(getResources(), R.drawable.lyingdown1);
		posture_states[1] = BitmapFactory.decodeResource(getResources(), R.drawable.stand1);
		
	
        IntentFilter intentFilter = new IntentFilter("POSTURE_EVENT");
        registerReceiver(broadcastRx, intentFilter);
	}
	
	
	
	 @Override
	 protected void onDestroy() {
	  super.onDestroy();
	  //un-register BroadcastReceiver
	  unregisterReceiver(broadcastRx);
	 }

	

	@Override
	
	protected void onResume() {
	super.onResume();
	
    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction("POSTURE_ACTION");
    registerReceiver(broadcastRx, intentFilter);
	}
	
	@Override
	protected void onPause() {
	super.onPause();

    LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);   
    bManager.unregisterReceiver(broadcastRx);
	}

	
	public void bluetoothTest(){
		int state = myBluetoothAdapter.getState();
		if (state == 10){
			AlertDialog.Builder alertDialogHint = new AlertDialog.Builder(this);
			alertDialogHint.setMessage("Bluetooth is OFF! Connection Fail!");
			alertDialogHint.setPositiveButton("Bluetooth Setting",
			new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent i = new Intent(Posture.this,Bluetooth.class);
					startActivity(i);
					finish();
				}
			});
			alertDialogHint.setNegativeButton("Cancel", 
			new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			AlertDialog alertDialog = alertDialogHint.create();
			alertDialog.show();
		}
	}

	private void setupMessageButton(){
    	Button messageButton = (Button)findViewById(R.id.returnpost);
    	messageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(Posture.this, MainActivity.class));
				finish();
			}
		});	
    }
	
	public void onBackPressed() {
		// do something on back.return;		
		startActivity(new Intent(Posture.this, MainActivity.class));
		finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.posture, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		super.onOptionsItemSelected(item);
    	switch(item.getItemId()){
    	case R.id.postmenu_pedo:
    		startActivity(new Intent(this, Pedometer.class));
    		finish();
    		break;
    	case R.id.postmenu_loca:
    		startActivity(new Intent(this, Location.class));
    		finish();
    		break;
    	case R.id.postmenu_heart:
    		startActivity(new Intent(this, Heartrate.class));
    		finish();
    		break;
    	}
        int id = item.getItemId();
        if (id == R.id.action_settings) {
        	startActivity(new Intent(this, Bluetooth.class));
    		finish();
            return true;
        }
        return true; 
	}
	
	
// Broadcast reciever
// Recieves updates from postureService
	

	
	private BroadcastReceiver broadcastRx = new BroadcastReceiver() {
	    @Override
	    public void onReceive(Context context, Intent intent) {
	        
	        	String posture = intent.getStringExtra("POSTURE");
	        	float avgX1  = intent.getFloatExtra("avgX1", 0.0f);
	        	float avgY1  = intent.getFloatExtra("avgY1", 0.0f);
	        	float avgZ1  = intent.getFloatExtra("avgZ1", 0.0f);
	        	
	        	float avgX2  = intent.getFloatExtra("avgX2", 0.0f);
	        	float avgY2  = intent.getFloatExtra("avgY2", 0.0f);
	        	float avgZ2  = intent.getFloatExtra("avgZ2", 0.0f);
	        	
	        	
	        	
	        	if (avgX1 != 0.0f){
	    		axisX1.setText("X: "+avgX1);
	    		axisY1.setText("Y: "+avgY1);
	    		axisZ1.setText("Z: "+avgZ1);
	    		
	    		axisX2.setText("X: "+avgX2);
	    		axisY2.setText("Y: "+avgY2);
	    		axisZ2.setText("Z: "+avgZ2);
	        	}
	        	
	        	if (posture != null){
	        	if (posture.equals("VERTICAL"))
	        	{
	        		img.setImageBitmap(posture_states[1]);
	        	}
	        	else if(posture.equals("HORIZONTAL")){
	        		img.setImageBitmap(posture_states[0]);
	        	}
	        	}
	        
	    }
	};
	
	
	
	
}

	
