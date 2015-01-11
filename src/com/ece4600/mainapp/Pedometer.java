package com.ece4600.mainapp;

import java.util.concurrent.TimeUnit;

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
	private double X = 0, Y = 0, Z = 0;
	private SensorManager sensorManager;
	private Sensor accelerometer;
	private double deltaXMax = 0;
	private double deltaYMax = 0;
	private double deltaZMax = 0;
	private double deltaX = 0, lastX = 0;
	private double deltaY = 0, lastY = 0;
	private double deltaZ = 0, lastZ = 0;
	private double test = 0;
	private double MaxX = 0, xoldvalue = 0,MaxY = 0, yoldvalue = 0, MaxZ = 0, zoldvalue = 0;
	private int stepnum = 0, i = 0, iteration = 500;
	public double xp = 0, yp = 0, zp = 0, xn = 0, yn = 0, zn = 0;
	private TextView currentX, currentY, currentZ, maxX, maxY, maxZ, step, speed;	
	Button reset, returnbutton, start, stop;
	public FFT[] sample;
	public FFT[] fft;
	


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
		speed = (TextView) findViewById(R.id.pedo_speednum);
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
		currentX.setText(Double.toString(X));
		currentY.setText(Double.toString(Y));
		currentZ.setText(Double.toString(Z));
		}

	// display the max x,y,z accelerometer values
	public void displayMaxValues() {
		if (deltaX > deltaXMax) {
			deltaXMax = deltaX;
			maxX.setText(Double.toString(deltaXMax));
			}
		if (deltaY > deltaYMax) {
			deltaYMax = deltaY;
			maxY.setText(Double.toString(deltaYMax));
			}
		if (deltaZ > deltaZMax) {
			deltaZMax = deltaZ;
			maxZ.setText(Double.toString(deltaZMax));
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
			speed.setText("0.0");
			break;
		default:
			break;
		}
	}
	
	private void countdowndisplay() {
		final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setMessage("00:05");
		alertDialog.show();  
		
		new CountDownTimer(5000, 1000) {
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
			test = Math.max(Math.abs(X), Math.max(Math.abs(Y), Math.abs(Z)));
			// if the change is below 1.5, it is just plain noise
			if ((deltaX < 1.5) && (deltaY < 1.5) && (deltaZ < 1.5)){
				deltaX = 0;
				deltaY = 0;
				deltaZ = 0; 
			}
			int N = 1024;          // size of FFT and sample window
			int Fs = 40;        // sample rate = 40 Hz
			sample = new dataSample[N];           // input PCM data buffer
			fft = new fftbuffer[N * 2];        // FFT complex buffer (interleaved real/imag)
					magnitude[N / 2]  // power spectrum

					capture audio in data[] buffer
					apply window function to data[]

					// copy real input data to complex FFT buffer
					for i = 0 to N - 1
					  fft[2*i] = data[i]
					  fft[2*i+1] = 0

					perform in-place complex-to-complex FFT on fft[] buffer

					// calculate power spectrum (magnitude) values from fft[]
					for i = 0 to N / 2 - 1
					  re = fft[2*i]
					  im = fft[2*i+1]
					  magnitude[i] = sqrt(re*re+im*im)

					// find largest peak in power spectrum
					max_magnitude = -INF
					max_index = -1
					for i = 0 to N / 2 - 1
					  if (magnitude[i] > max_magnitude){
					    max_magnitude = magnitude[i]；
					    max_index = i；
					  }

					// convert index of largest peak to frequency
					freq = max_index * Fs / N；
		}
		}
}
