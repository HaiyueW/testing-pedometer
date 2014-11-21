package com.ece4600.mainapp;

import com.ece4600.mainapp.R;

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


public class Posture extends Activity implements SensorEventListener{
	
	private SharedPreferences mSharedPrefs;
	private BluetoothAdapter myBluetoothAdapter;

	// main code
	
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private TextView axisX, axisY, axisZ;
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
		axisX = (TextView) findViewById(R.id.acc_x);
		axisY = (TextView) findViewById(R.id.acc_y);
		axisZ = (TextView) findViewById(R.id.acc_z);
		timenano = (TextView) findViewById(R.id.timenano);
		x_avg = (TextView) findViewById(R.id.x_avg);
		threshold = (TextView) findViewById(R.id.threshold);
		
		img = (ImageView) findViewById(R.id.displayIMG); 
		
		
		posture_states[0] = BitmapFactory.decodeResource(getResources(), R.drawable.lyingdown1);
		posture_states[1] = BitmapFactory.decodeResource(getResources(), R.drawable.stand1);
		
	
		
        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("POSTURE_ACTION");
        registerReceiver(broadcastRx, intentFilter);
	}
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	//mSensorManager.registerListener(this, mAccelerometer, 100000);
	// Do something here if sensor accuracy changes.
	}
	@Override
	public void onSensorChanged(SensorEvent event) {
	if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
		
		//ImageView myImage = (ImageView) findViewById(R.id.displayIMG);
		
		
		
		
		TimeStamp timer_value = new TimeStamp();
		dataSample first_data = new dataSample(0.0f, 0.0f, 0.0f, timer_value.returntime());
		array_10[i] = first_data;
		axisX.setText("X: "+array_10[i].xaxis);
		axisY.setText("Y: "+array_10[i].yaxis);
		axisZ.setText("Z: "+array_10[i].zaxis);
		timenano.setText("Time: "+ array_10[i].timestamp);
		
		
		
		i++;
		
		if (i == 10)
		{
	// insert algorithm here:
			double average = 0;
			double THRESHOLD_CONSTANT = 5;
			for(int m = 0; m< 10;m++)
			{
				average += array_10[m].yaxis;
			}
				x_avg.setText("y average: "+ average/10);
	//		
				if (Math.abs(average)/10 < THRESHOLD_CONSTANT)
				{
					threshold.setText("horizontal");
				
			//		img.setImageResource(0);
			//		img.setImageResource(R.drawable.lyingdown1);
			//		img.destroyDrawingCache();
			
												
					img.setImageBitmap(posture_states[0]);
					
				}
				else
				{	
					threshold.setText("vertical");
			//		img = (ImageView) findViewById(R.id.displayIMG);
					//img.setImageResource(0);
			//		img.setImageResource(R.drawable.stand1);
			//		img.destroyDrawingCache();
				    img.setImageBitmap(posture_states[1]);
				}
				    
				i = 0;
				
			}	
		}
	}
	@Override
	
	protected void onResume() {
	super.onResume();
	//mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
	}
	
	@Override
		protected void onPause() {
	super.onPause();
	//mSensorManager.unregisterListener(this);
	}
	
	

	
	//bluetooth check starts here;
	
	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_posture);
//		myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//		bluetoothTest();
//		setupMessageButton();
//	}
//	
	
	
	
	
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
	        if(intent.getAction().equals("POSTURE_ACTION")) {
	        	String posture = intent.getStringExtra("POSTURE");
	        	Log.v("PostureActivity",posture);
	        }
	    }
	};
	
	
	
	
}

	
