package com.ece4600.mainapp;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class PostureService extends Service{
	public static dataArrayFloat[] array_10_D1 = new dataArrayFloat[11];
	public static dataArrayFloat[] array_10_D2 = new dataArrayFloat[11];
	public static int i;
	public static String postureState, newPosture;
	public static dataArrayFloat[] array_2d = new dataArrayFloat[2];
	
	private static float avgX1, avgY1, avgZ1, avgX2, avgY2, avgZ2;
	private static float THRESHOLD_CONSTANT = (float) 0.85;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override 
	public void onCreate(){
		/* Called when service is first created
		 * one time setup. Not called if already running*/
		i = 0;
		postureState = "VERTICAL";
		super.onCreate();
	}

	@Override 
	public int onStartCommand(Intent intent, int flags, int startId){
		/*Called by the system when an app component requests that a 
		 * Service start using startService()
		 * Once started, it can run in the background indefinitely*/
		
		float xValue1 = intent.getFloatExtra("XVal1", 0.0f);
		float yValue1 = intent.getFloatExtra("YVal1", 0.0f);
		float zValue1 = intent.getFloatExtra("ZVal1", 0.0f);
		
		float xValue2 = intent.getFloatExtra("XVal2", 0.0f);
		float yValue2 = intent.getFloatExtra("YVal2", 0.0f);
		float zValue2 = intent.getFloatExtra("ZVal2", 0.0f);
		

		dataArrayFloat data = new dataArrayFloat(xValue1, yValue1, zValue1);
		array_10_D1[i] = data;
		
		dataArrayFloat data2 = new dataArrayFloat(xValue2, yValue2, zValue2);
		array_10_D2[i] = data2;
		
		Log.v("PostureService", "Recieved data");
		
		i++;
		
		if (i == (int)11) //i = 11 to get rid of null pointer exception
		{
			i = 0;
			//running =true;
			//pThread.run();
			calculatePosture();
		}
		
		return super.onStartCommand(intent,flags, startId);
	}//end of onStartCommand(...)

	@Override 
	public void onDestroy(){
		/*Called when a service no longer used and being destroyed*/
		super.onDestroy();
	}// End of onDestroy
	
	
	
	private void calculatePosture(){
		avgX1 = 0;
		avgY1 = 0;
		avgZ1 = 0;
		
		avgX2 = 0;
		avgY2 = 0;
		avgZ2 = 0;
		
		for(int m = 1; m< 11;m++){
			avgX1 +=  array_10_D1[m].xaxis;
			avgY1 += array_10_D1[m].yaxis;
			avgZ1 +=  array_10_D1[m].zaxis;
			
			avgX2 +=  array_10_D2[m].xaxis;
			avgY2 += array_10_D2[m].yaxis;
			avgZ2 +=  array_10_D2[m].zaxis;
			
		}
			
		Handler hAvg = new Handler(Looper.getMainLooper());
		hAvg.post(new Runnable(){
			@Override
			public void run(){
				Intent i = new Intent("POSTURE_EVENT");
				
				i.putExtra("avgX1", avgX1/10);
				i.putExtra("avgY1", avgY1/10);
				i.putExtra("avgZ1", avgZ1/10);
				
				i.putExtra("avgX2", avgX2/10);
				i.putExtra("avgY2", avgY2/10);
				i.putExtra("avgZ2", avgZ2/10);
				
				sendBroadcast(i);
			}
		});
			
		if ( Math.abs(avgY1/10) > THRESHOLD_CONSTANT )
			newPosture = "VERTICAL";
		else
			newPosture = "HORIZONTAL";
		    
		
		
		if (!newPosture.equals(postureState)){
			// Where data is sent to posture class
		
			Log.e("PostureService", newPosture);
			postureState = newPosture;

			
			Handler h = new Handler(Looper.getMainLooper());
			h.post(new Runnable(){
				@Override
				public void run(){
					//Log.i(DEBUG, "Connection successful, Getting Services");
					Toast.makeText( PostureService.this, postureState, Toast.LENGTH_SHORT).show();
					Intent i = new Intent("POSTURE_EVENT");
					i.putExtra("POSTURE", newPosture);
					
					sendBroadcast(i);
				}
			});
		}
		else{
			//Log.e("PostureService", newPosture);
		}
	}// End of calculate Posture
	
	
}
