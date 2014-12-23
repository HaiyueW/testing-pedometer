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
	public static int i,counter;
	public static String postureState, newPosture, nowPosture,changePosture;
	public static dataArrayFloat[] array_2d = new dataArrayFloat[2];
	
	private static float avgX1, avgY1, avgZ1, avgX2, avgY2, avgZ2;
	private double roll_1, roll_2,pitch_1,pitch_2 , roll_1_comp, roll_2_comp;
	private double probBack, probFront, probLeft, probRight;
	
	
	//CONSTANTS
	private static double threshold_1_roll = (float)30.0;
	private static double threshold_2_roll = (float) 30.0;
	private static float THRESHOLD_CONSTANT = (float) 0.85;
	private static int threshold_counterPosture = (int) 3;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override 
	public void onCreate(){
		/* Called when service is first created
		 * one time setup. Not called if already running*/
		probBack = 0.0;
		probFront = 0.0;
		probLeft = 0.0;
		probRight = 0.0;
		
		i = 0;
		postureState = "STAND";
		newPosture = "STAND";
		changePosture = "STAND";
		nowPosture = "STAND";
		
		Handler h = new Handler(Looper.getMainLooper()); //handler to delay the scan, if can't connect, then stop attempts to scan
		h.postDelayed(resendPosture, 1000);	
		
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
		
		avgX1 = avgX1 / 10.0f;
		avgY1 = avgY1 / 10.0f;
		avgZ1 = avgZ1 / 10.0f;
		
		avgX2 = avgX2 / 10.0f;
		avgY2 = avgY2 / 10.0f;
		avgZ2 = avgZ2 / 10.0f;
		
	
		
		Handler hAvg = new Handler(Looper.getMainLooper());
		hAvg.post(new Runnable(){
			@Override
			public void run(){
				Intent i = new Intent("POSTURE_EVENT");
				
				i.putExtra("avgX1", avgX1);
				i.putExtra("avgY1", avgY1);
				i.putExtra("avgZ1", avgZ1);
				
				i.putExtra("avgX2", avgX2);
				i.putExtra("avgY2", avgY2);
				i.putExtra("avgZ2", avgZ2);
				
				sendBroadcast(i);
			}
		});
		
		
		
		/*if ( Math.abs(avgY1/10) > THRESHOLD_CONSTANT )
			newPosture = "VERTICAL";
		else
			newPosture = "HORIZONTAL";*/
		
		// Roll angle may be wrong
		
		roll_1 =   Math.atan2((double)  avgZ1,(double) -1.0* avgY1) * 180 / Math.PI;
		pitch_1 = Math.atan2((double)avgZ1,(double)avgY1) * 180 / Math.PI;

		roll_2 = Math.atan2((double)  avgZ2,(double)-1.0*avgY2) * 180 / Math.PI;
		
		pitch_2 = Math.atan2((double)avgZ2,(double)avgY2) * 180 / Math.PI;
		
		//roll_1 = atan3((double)  avgZ1,(double) -1.0* avgY1);
		//roll_2 = atan3((double)  avgZ2,(double) -1.0* avgY2);
		
		String dataAngles = "roll_1:" + roll_1+ ",roll_2:" +roll_2;
		Log.i("postureService", dataAngles);
		
		// for sitting standing lying down and bending

		roll_1_comp = Double.compare(Math.abs(roll_1), threshold_1_roll); // if greater than 0 , output greater than threshold
		roll_2_comp = Double.compare(Math.abs(roll_2), threshold_2_roll);
		
		String dataCompare = "roll_1_comp:" + roll_1_comp+ ",roll_2:" +roll_2_comp;
		Log.i("postureService", dataCompare);
		
		if ((roll_1_comp  <= 0 )&& (roll_2_comp  < 0) && (Math.abs(avgY1) >= 0.8) && (Math.abs(avgY1) <= 1.3)) {
			// standing condition  
			nowPosture = "STAND";
			}
			
		else if ((roll_1_comp  <= 0) && (roll_2_comp  > 0 )) {
			// sitting condition
			nowPosture = "SIT";
			}
		else if ((roll_1_comp  >= 0) && (roll_2_comp  < 0) ) {
			// bending condition
			nowPosture = "BEND";
			}
		else {// lying down position 
			nowPosture = lieDownPosture((double) avgX1, (double) avgY1, (double) avgZ1);	
			// conditions for lying down, can be written as mutually exclusive list. 
			
		 
		}
		
		
		if (!nowPosture.equals(changePosture)){
			changePosture = nowPosture;
		}
		
		if (nowPosture.equals(changePosture))
		{
			counter++;
		}
		
		
		if (counter == threshold_counterPosture){
			newPosture = changePosture;
			counter =0;
		}
		
		if (!newPosture.equals(postureState)){
			// Where data is sent to posture class
		
			Log.e("PostureService", newPosture);
			postureState = newPosture;

			Intent i = new Intent("POSTURE_EVENT");
			i.putExtra("POSTURE", newPosture);
			sendBroadcast(i);
			
			Handler h = new Handler(Looper.getMainLooper());
			h.post(new Runnable(){
				@Override
				public void run(){
					//Log.i(DEBUG, "Connection successful, Getting Services");
					Toast.makeText( PostureService.this, postureState, Toast.LENGTH_SHORT).show();
					
				}
			});
		}
		else{
			//Log.e("PostureService", newPosture);
		}
	}// End of calculate Posture
	
	
	private Runnable resendPosture = new Runnable() {
		   @Override
		   public void run() {
			 
			  Intent i = new Intent("POSTURE_EVENT");
			  i.putExtra("POSTURE", newPosture);
			  sendBroadcast(i);
			 
			  
			  Handler h = new Handler(Looper.getMainLooper()); //handler to delay the scan, if can't connect, then stop attempts to scan
			  h.postDelayed(this, 1000);
			  
		   }};
		   
	private String lieDownPosture(double X, double Y, double Z){
		String liePosture = "LIE";
		
		if (Z < -0.6){
			if( (Z > -1) && (Z< -0.6)){
				probBack = -2.5 * Z - 1.5;
			}
			else{
		 		probBack = 1.0;
			}

		}
		else if (Z > 0.6){

			if( (Z > 0.6) && (Z< 1.0)){
				probFront = -2.5 * Z - 1.5;
			}
			else{
				 probFront = 1.0;
			}
		}

		else if(X< -0.6){

			if( (X > -1) && (X< -0.6)){
				probRight = -2.5 * Z - 1.5;
			}
			else{
		 		probRight = 1.0;
			}
		}

		else if(X > 0.6){

			if( (X > 0.6) && (X< 1.0)){
				probLeft = -2.5 * Z - 1.5;
			}
			else{
				 probLeft = 1.0;
			}
		}
		
		
		
		if ((probBack > probFront)&&(probBack > probLeft) && (probBack > probRight)){
			liePosture = "LIEBACK";
		}
		else if ((probFront > probBack)&&(probFront > probLeft) && (probFront > probRight)){
			liePosture = "LIEFRONT";
		}
		else if ((probRight > probFront)&&(probRight > probLeft) && (probRight > probBack)){
			liePosture = "LIERIGHT";
		}
		else{
			liePosture = "LIELEFT";
		}
		
		probBack = 0.0;
		probFront = 0.0;
		probRight = 0.0;
		probLeft = 0.0;
		return liePosture;
	}

	
}
