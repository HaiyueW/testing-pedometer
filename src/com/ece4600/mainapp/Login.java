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
import android.widget.EditText;
import android.widget.TextView;

public class Login extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setupLoginButton();
		setupClearButton();
	}

	private void setupClearButton()
	{
		Button messageButton = (Button)findViewById(R.id.clear);
    	messageButton.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
		
		
		EditText id=(EditText)findViewById(R.id.userID);
		EditText password=(EditText)findViewById(R.id.userPass);
		id.setText("");
		password.setText("");
		TextView info=(TextView)findViewById(R.id.login_info);
		info.setText("");
		
		
		}
		});
	}
    	
    	
    	
	private void setupLoginButton(){
    	Button messageButton = (Button)findViewById(R.id.login);
    	messageButton.setOnClickListener(new View.OnClickListener() {
			@Override
		public void onClick(View v) {
				//Toast.makeText(Posture.this, "Return to profile", Toast.LENGTH_LONG).show();
				//startActivity(new Intent(Heartrate.this, MainActivity.class));
		EditText id=(EditText)findViewById(R.id.userID);
		EditText password=(EditText)findViewById(R.id.userPass);
		String id_string = id.getText().toString();
		String id_pass = password.getText().toString();
		
		if(id_string.equalsIgnoreCase("admin") && id_pass.equals("password")) {
		TextView info=(TextView)findViewById(R.id.login_info);
		
		info.setText("Correct Password");
		finish();
		}
		
		else{
			
		
		TextView info=(TextView)findViewById(R.id.login_info);
		
		info.setText("Wrong Password");
		//String password=person.getText().toString();	
	    }	
			
				
		
			}
		});	
    }
	
	
//    private void setupMessageButton(){
//    	Button messageButton = (Button)findViewById(R.id.heart);
//    	messageButton.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				//Toast.makeText(MainActivity.this, "Heart rate", Toast.LENGTH_LONG).show();
//				startActivity(new Intent(MainActivity.this, Heartrate.class));
//			}
//		});	
//    }
//	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.posture, menu);
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
