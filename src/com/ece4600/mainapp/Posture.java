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

public class Posture extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_posture);
		setupMessageButton();
	}

	private void setupMessageButton(){
    	Button messageButton = (Button)findViewById(R.id.returnpost);
    	messageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Toast.makeText(Posture.this, "Return to profile", Toast.LENGTH_LONG).show();
				startActivity(new Intent(Posture.this, MainActivity.class));
				finish();
			}
		});	
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
            return true;
        }
        return true; 
	}
}
