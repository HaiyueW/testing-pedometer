package com.ece4600.mainapp;

import com.ece4600.mainapp.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
//import android.widget.Toast;

public class Posture extends Activity {
	
	private BluetoothAdapter myBluetoothAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_posture);
		myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		bluetoothTest();
		setupMessageButton();
	}
	
	public void bluetoothTest(){
		int state = myBluetoothAdapter.getState();
		if (state == 10){
			AlertDialog.Builder alertDialogHint = new AlertDialog.Builder(this);
			alertDialogHint.setMessage("Bluetooth is OFF! Connection Fail!");
			alertDialogHint.setPositiveButton("Bluetooth Setting",
			new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent i = new Intent(Posture.this,Bluetooth.class);
					startActivity(i);
					finish();
				}
			});
			alertDialogHint.setNegativeButton("Cancel", 
			new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			AlertDialog alertDialog = alertDialogHint.create();
			alertDialog.show();
		}
	}

	private void setupMessageButton(){
    	Button messageButton = (Button)findViewById(R.id.returnpost);
    	messageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(Posture.this, MainActivity.class));
				finish();
			}
		});	
    }
	
	public void onBackPressed() {
		// do something on back.return;		
		startActivity(new Intent(Posture.this, MainActivity.class));
		finish();
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
        	startActivity(new Intent(this, Bluetooth.class));
    		finish();
            return true;
        }
        return true; 
	}
}