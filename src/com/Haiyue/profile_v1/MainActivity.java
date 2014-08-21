package com.Haiyue.profile_v1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
//import android.widget.Toast;


public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        setupMessageButton1();
        setupMessageButton2();
        setupMessageButton3();
        setupMessageButton4();
        Intent Login_window = new Intent(this, Login.class); // adds the log in window here
        startActivity(Login_window);
    }

    private void setupMessageButton1(){
    	Button messageButton = (Button)findViewById(R.id.heart);
    	messageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Toast.makeText(MainActivity.this, "Heart rate", Toast.LENGTH_LONG).show();
				startActivity(new Intent(MainActivity.this, Heartrate.class));
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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
