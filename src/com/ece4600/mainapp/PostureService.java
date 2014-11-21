package com.ece4600.mainapp;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class PostureService extends IntentService{
	public static dataArrayFloat[] array_10 = new dataArrayFloat[10];
	public static int i;
	public static String postureState, newPosture;
	public static dataArrayFloat[] array_2d = new dataArrayFloat[2];
	private Thread pThread;
	

	public PostureService() {
		super("PostureService");
		// TODO Auto-generated constructor stub
		i = 0;
		postureState = "VERTICAL";
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		float xValue1 = intent.getFloatExtra("XVal1", 0.0f);
		float yValue1 = intent.getFloatExtra("YVal1", 0.0f);
		float zValue1 = intent.getFloatExtra("ZVal1", 0.0f);
		
		float xValue2 = intent.getFloatExtra("XVal2", 0.0f);
		float yValue2 = intent.getFloatExtra("YVal2", 0.0f);
		float zValue2 = intent.getFloatExtra("ZVal2", 0.0f);
		
		
		
		/*dataArrayFloat data = new dataArrayFloat(xValue1, yValue1, zValue1);
		array_2d[0] = data;
		//pThread = new MyThread();
	    //pThread.start();
	    
		if (!newPosture.equals(postureState)){
			Log.v("PostureService", newPosture);
			Intent i = new Intent(PostureService.this, Posture.class);
			i.setAction("POSTURE_ACTION");
			i.putExtra("POSTURE", newPosture);
			sendBroadcast(intent);
		}*/
		
		/*
		TimeStamp timer_value = new TimeStamp();
		dataSample first_data = new dataSample(xValue1, yValue1, zValue1, timer_value.returntime());
		array_10[i] = first_data;
		
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
				
				if (Math.abs(average)/10 < THRESHOLD_CONSTANT)
				{
					postureState = "HORIZONTAL";
					
				}
				else
				{	
					postureState = "VERTICAL";
				}
				    
				i = 0;
				
				//Broadcast posture update
				broadcastUpdate(postureState);
			}	*/
		
	}
	
	/*public static void broadcastUpdate(String results){
		Log.v("PostureService", results);

		Intent intent = new Intent(this, Posture.class);
		intent.setAction("POSTURE_ACTION");
		intent.putExtra("POSTURE", results);
		sendBroadcast(intent);
		
	}*/

	static public class MyThread extends Thread{
		 
		
		 @Override
		 public void run() {
			 
			 
			 array_10[i] = array_2d[0];
				
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
						
						if (Math.abs(average)/10 < THRESHOLD_CONSTANT)
						{
							newPosture = "HORIZONTAL";
							
						}
						else
						{	
							newPosture = "VERTICAL";
						}
						    
						i = 0;

					}	
			 
			 
		 }
		 
	}
	
	
}
