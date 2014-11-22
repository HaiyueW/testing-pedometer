package com.ece4600.mainapp;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class PostureService extends IntentService{
	public static dataArrayFloat[] array_10 = new dataArrayFloat[11];
	public static int i;
	public static String postureState, newPosture;
	public static dataArrayFloat[] array_2d = new dataArrayFloat[2];
	private MyThread pThread;
	private static boolean running;

	public PostureService() {
		super("PostureService");
		// TODO Auto-generated constructor stub
		i = 0;
		pThread = new MyThread();
		postureState = "VERTICAL";
	}

	
	//-----------------------------------
	// Where data is received
	//-----------------------------------
	@Override
	protected void onHandleIntent(Intent intent) {
		float xValue1 = intent.getFloatExtra("XVal1", 0.0f);
		float yValue1 = intent.getFloatExtra("YVal1", 0.0f);
		float zValue1 = intent.getFloatExtra("ZVal1", 0.0f);
		
		float xValue2 = intent.getFloatExtra("XVal2", 0.0f);
		float yValue2 = intent.getFloatExtra("YVal2", 0.0f);
		float zValue2 = intent.getFloatExtra("ZVal2", 0.0f);
		
		dataArrayFloat data = new dataArrayFloat(xValue1, yValue1, zValue1);
		array_10[i] = data;
		
		i++;
		
		if (i == 11) //i = 11 to get rid of null pointer exception
		{
			i = 0;
			running =true;
			pThread.run();
		}
		
	}// On Handle Intent End
	
	
	
	
	private void broadcastUpdate(String results){
		Log.v("PostureService", results);

		Intent intent = new Intent(this, Posture.class);
		intent.setAction("POSTURE_ACTION");
		intent.putExtra("POSTURE", results);
		sendBroadcast(intent);
		
	}

	private class MyThread implements Runnable{
		 
		
		 @Override
		 public void run() {
			 
			 //android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
			 while(running){	
		
					// insert algorithm here:
					float average = 0;
					float THRESHOLD_CONSTANT = (float) -0.85;
					
					for(int m = 1; m< 11;m++)
						average += array_10[m].yaxis;
						
						
					if ( (average/10) < THRESHOLD_CONSTANT)
						newPosture = "VERTICAL";
					else
						newPosture = "HORIZONTAL";
						
						    
					i = 0;

						
					    
					if (!newPosture.equals(postureState)){
						// Where data is sent to posture class
						// issue: sometimes this is not sent?
						Log.v("PostureService", newPosture);
						postureState = newPosture;
						Intent i = new Intent(PostureService.this,Posture.class);
						i.setAction("POSTURE_ACTION");
						i.putExtra("POSTURE", newPosture);
						sendBroadcast(i);
					}
					
				}	
			running = false; 
		 	
	}
		 
	}
	
	
}
