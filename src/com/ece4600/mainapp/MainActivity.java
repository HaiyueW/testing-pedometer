package com.ece4600.mainapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); 
        setupMessageButton1();
        setupMessageButton2();
        setupMessageButton3();
        setupMessageButton4();
        //Intent Login_window = new Intent(this, Login.class); // adds the log in window here
        //startActivity(Login_window);
    }

    private void setupMessageButton1(){
    	Button messageButton = (Button)findViewById(R.id.heart);
    	messageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Toast.makeText(MainActivity.this, "Heart rate", Toast.LENGTH_LONG).show();
				startActivity(new Intent(MainActivity.this, Heartrate.class));
				finish();
			}
		});	
    }
    
    private void setupMessageButton2(){
    	Button messageButton = (Button)findViewById(R.id.pedo);
    	messageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Toast.makeText(MainActivity.this, "Pedometer", Toast.LENGTH_LONG).show();
				startActivity(new Intent(MainActivity.this, Pedometer.class));
				finish();
			}
		});	
    }
    
    private void setupMessageButton3(){
    	Button messageButton = (Button)findViewById(R.id.loca);
    	messageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Toast.makeText(MainActivity.this, "Location", Toast.LENGTH_LONG).show();
				startActivity(new Intent(MainActivity.this, Location.class));
				finish();
			}
		});	
    }
    
    private void setupMessageButton4(){
    	Button messageButton = (Button)findViewById(R.id.post);
    	messageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Toast.makeText(MainActivity.this, "Posture", Toast.LENGTH_LONG).show();
				startActivity(new Intent(MainActivity.this, Posture.class));
				finish();
			}
		});	
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
   
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
    	super.onOptionsItemSelected(item);
    	switch(item.getItemId()){
    	case R.id.mainmenu_heart:
    		startActivity(new Intent(this, Heartrate.class));
    		finish();
    		break;
    	case R.id.mainmenu_pedo:
    		startActivity(new Intent(this, Pedometer.class));
    		finish();
    		break;
    	case R.id.mainmenu_loca:
    		startActivity(new Intent(this, Location.class));
    		finish();
    		break;
    	case R.id.mainmenu_post:
    		startActivity(new Intent(this, Posture.class));
    		finish();
    		break;
    	case R.id.mainmenu_logout:
    		PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().clear().commit();
    		startActivity(new Intent(this, Login.class));
    		finish();
    		break;
    	}
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return true; 
    }
}
