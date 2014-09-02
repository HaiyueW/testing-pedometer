package com.ece4600.mainapp;

import com.ece4600.mainapp.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
//import android.widget.Toast;

public class Pedometer extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pedometer);
		setupMessageButton();
	}

	private void setupMessageButton(){
    	Button messageButton = (Button)findViewById(R.id.returnpedo);
    	messageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Toast.makeText(Pedometer.this, "Return to profile", Toast.LENGTH_LONG).show();
				startActivity(new Intent(Pedometer.this, MainActivity.class));
				finish();
			}
		});	
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
}
