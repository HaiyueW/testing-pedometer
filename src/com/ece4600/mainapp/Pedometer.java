package com.ece4600.mainapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Pedometer extends Activity implements SensorEventListener{
	private boolean startflag = false;
	private float lastX = 0, lastY = 0, lastZ = 0;
	private float X = 0, Y = 0, Z = 0;
	private SensorManager sensorManager;
	private Sensor accelerometer;
	private float deltaXMax = 0;
	private float deltaYMax = 0;
	private float deltaZMax = 0;
	private float deltaX = 0;
	private float deltaY = 0;
	private float deltaZ = 0;
	private float test = 0;
	private float MaxX = 0, xoldvalue = 0,MaxY = 0, yoldvalue = 0, MaxZ = 0, zoldvalue = 0;
	private int stepnum = 0, i = 0, xp = 0, yp = 0, zp = 0, xn = 0, yn = 0, zn = 0, iteration = 500;
	private TextView currentX, currentY, currentZ, maxX, maxY, maxZ, step;	
	Button reset, returnbutton, start, stop;
    private float LastStepDetection = 0;
    private float StepDetectionDelta = 3500;
    private double DifferenceDelta = 1.0;
    private double minPeak = 3.0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pedometer);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		initializeViews();
		reset = (Button)findViewById(R.id.pedo_reset);
		returnbutton = (Button)findViewById(R.id.returnpedo);
		start = (Button)findViewById(R.id.pedo_start);
		stop = (Button)findViewById(R.id.pedo_stop);
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
			accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
			// success! we have an accelerometer
			} else {
   				final Toast toast = Toast.makeText(getApplicationContext(), "Do not have Accelerometer", Toast.LENGTH_SHORT);
   			    toast.show();
   			    Handler handler = new Handler();
   			        handler.postDelayed(new Runnable() {
   			           @Override
   			           public void run() {
   			               toast.cancel(); 
   			           }
   			    }, 500);
   			        }
		
	}
	
	public void initializeViews() {
		currentX = (TextView) findViewById(R.id.pedo_xaxisdata);
		currentY = (TextView) findViewById(R.id.pedo_yaxisdata);
		currentZ = (TextView) findViewById(R.id.pedo_zaxisdata);
		maxX = (TextView) findViewById(R.id.pedo_accxmax);
		maxY = (TextView) findViewById(R.id.pedo_accymax);
		maxZ = (TextView) findViewById(R.id.pedo_acczmax);
		step = (TextView) findViewById(R.id.pedo_stepnum);
		}

	//onResume() register the accelerometer for listening the events
	protected void onResume() {
		super.onResume();
		sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
		}
	
	//onPause() unregister the accelerometer for stop listening the events
	protected void onPause() {
		super.onPause();
		sensorManager.unregisterListener(this);
		}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
	}
	
	// display the current x,y,z accelerometer values
	public void displayCurrentValues() {
		currentX.setText(Float.toString(X));
		currentY.setText(Float.toString(Y));
		currentZ.setText(Float.toString(Z));
		}

	// display the max x,y,z accelerometer values
	public void displayMaxValues() {
		if (deltaX > deltaXMax) {
			deltaXMax = deltaX;
			maxX.setText(Float.toString(deltaXMax));
			}
		if (deltaY > deltaYMax) {
			deltaYMax = deltaY;
			maxY.setText(Float.toString(deltaYMax));
			}
		if (deltaZ > deltaZMax) {
			deltaZMax = deltaZ;
			maxZ.setText(Float.toString(deltaZMax));
			}
		}
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pedo_start: 
			onResume();
			countdowndisplay();			
			break;
		case R.id.pedo_stop:
			onPause();
			startflag = false;
			break;
		case R.id.returnpedo:
			startActivity(new Intent(Pedometer.this, MainActivity.class));
			finish();
			break;
		case R.id.pedo_reset:
			deltaXMax = 0;
			deltaYMax = 0;
			deltaZMax = 0;
			deltaX = 0;
			deltaY = 0;
			deltaZ = 0;
			X = 0;
			Y = 0;
			Z = 0;
			lastX = 0;
			lastY = 0;
			lastZ = 0;
			i = 0;
			xp = 0;
			yp = 0;
			zp = 0;
			xn = 0;
			yn = 0;
			zn = 0;
			MaxX = 0;
			MaxY = 0;
			MaxZ = 0;
			iteration = 500;
			stepnum = 0;
			currentX.setText("0.0");
			currentY.setText("0.0");
			currentZ.setText("0.0");
			maxX.setText("0.0");
			maxY.setText("0.0");
			maxZ.setText("0.0");
			step.setText("0.0");
			break;
		default:
			break;
		}
	}
	
	private void countdowndisplay() {
		final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setMessage("00:10");
		alertDialog.show();  
		
		new CountDownTimer(10000, 1000) {
		    public void onTick(long millisUntilFinished) {
		       alertDialog.setMessage("00:"+ (millisUntilFinished/1000));
		    }

		    @Override
		    public void onFinish() {
		    	alertDialog.setMessage("Completed");
		    	alertDialog.dismiss();
		    	startflag = true;
		    }
		}.start();
	}

	public void onBackPressed() {
		// do something on back.return;		
		startActivity(new Intent(Pedometer.this, MainActivity.class));
		finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pedometer, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		super.onOptionsItemSelected(item);
    	switch(item.getItemId()){
    	case R.id.pedomenu_heart:
    		startActivity(new Intent(this, Heartrate.class));
    		finish();
    		break;
    	case R.id.pedomenu_loca:
    		startActivity(new Intent(this, Location.class));
    		finish();
    		break;
    	case R.id.pedomenu_post:
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
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		if(startflag == true){
			// display the current x,y,z accelerometer values
			displayCurrentValues();
			// display the max x,y,z accelerometer values
			displayMaxValues();
			// get the change of the x,y,z values of the accelerometer
			deltaX = event.values[0] - lastX;
			deltaY = event.values[1] - lastY;
			deltaZ = event.values[2] - lastZ;
			//get the current values of x,y,z axis of the accelerometer
			X = event.values[0];
			Y = event.values[1];
			Z = event.values[2];
//			Double vector = Math.sqrt(X * X + Y * Y + Z * Z);
//			double average = (Math.abs(X)+Math.abs(Y)+Math.abs(Z))/3;
	        float time = System.currentTimeMillis();
			float delta = time - LastStepDetection;
			test = Math.max(Math.abs(X), Math.max(Math.abs(Y), Math.abs(Z)));
			// if the change is below 1.5, it is just plain noise
			if ((deltaX < 1.5) && (deltaY < 1.5) && (deltaZ < 1.5)){
				deltaX = 0;
				deltaY = 0;
				deltaZ = 0; 
			}else if (iteration == 500){
				iteration  = 0;
				if ((test == Math.abs(X)) && (test > 1.5)){
					i = 1;
				}else if ((test == Math.abs(Y)) && (test > 1.5)){
					i = 2;
				}else if ((test == Math.abs(Z)) && (test > 1.5)){
					i = 3;
					}
				}

			switch(i){
			case 1:
				if (deltaX > 0){
					xp++;
					MaxX = Math.max(MaxX, Math.max(Math.abs(X), xoldvalue));
				}else if(xp > 2){
					if (deltaX < 0){
						xn++;
						if (xn > 1 && delta > StepDetectionDelta && MaxX - Math.abs(X) > minPeak){
							stepnum++;
							step.setText(Integer.toString(stepnum));
							iteration++;
							xp = 0;
							xn = 0;
							MaxX = 0;
						}
					}
				}
				xoldvalue = Math.abs(X);
				Log.i("Pedometer", "Step detected Xaxis" + stepnum + "time" + time);
//				if (vector - average > DifferenceDelta && delta > StepDetectionDelta && minPeak < vector) {
//		        	LastStepDetection = time;
//		        	stepnum++;
//		            step.setText(Long.toString(delta));
//		            iteration++;
//		        }
				break;
			case 2:
				if (deltaY > 0){
					yp++;
					MaxY = Math.max(MaxY, Math.max(Math.abs(Y), yoldvalue));
				}else if(yp > 2){
					if (deltaY < 0){
						yn++;
						if (yn > 1 && delta > StepDetectionDelta && MaxY - Math.abs(Y) > minPeak){
							stepnum++;
							step.setText(Integer.toString(stepnum));
							iteration++;
							yp = 0;
							yn = 0;
							MaxY = 0;
						}
					}
				}
				yoldvalue = Math.abs(Y);
				Log.i("Pedometer", "Step detected Yaxis " + stepnum + "time" + time);
//				if (vector - average > DifferenceDelta && delta > StepDetectionDelta && minPeak < vector) {
//		        	LastStepDetection = time;
//		        	stepnum++;
//		            step.setText(Long.toString(delta)); 
//		            iteration++;
//		        }
				break;
			case 3:
				if (deltaZ > 0){
					zp++;
					MaxZ = Math.max(MaxZ, Math.max(Math.abs(Z), zoldvalue));
				}else if(zp > 2){
					if (deltaZ < 0){
						zn++;
						if (zn > 1 && delta > StepDetectionDelta  && MaxZ - Math.abs(Z) > minPeak){
							stepnum++;
							step.setText(Integer.toString(stepnum));
							iteration++;
							zp = 0;
							zn = 0;
							MaxZ = 0;
						}
					}
				}
				zoldvalue = Math.abs(Z);
				Log.i("Pedometer", "Step detected Zaxis " + stepnum + "time" + time);
//				if (vector - average > DifferenceDelta && delta > StepDetectionDelta && minPeak < vector) {
//		        	LastStepDetection = time;
//		        	stepnum++;
//		            step.setText(Long.toString(delta));
//		            iteration++;
//		        }
				break;
			default:
				break;				
			}
			// set the last know values of x,y,z
			lastX = event.values[0];
			lastY = event.values[1];
			lastZ = event.values[2];

		}
		}

}
