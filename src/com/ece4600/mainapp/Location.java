package com.ece4600.mainapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.qozix.tileview.TileView;

import android.widget.ImageView;

public class Location extends Activity {

    TileView tileView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create our TileView
        tileView = new TileView(this);

        // Set the minimum parameters
        tileView.setSize(9362,6623);
        tileView.addDetailLevel(1f, "tiles/1000_%col%_%row%.png", "downsamples/map.png");
        tileView.addDetailLevel(0.5f, "tiles/500_%col%_%row%.png", "downsamples/map.png");
        tileView.addDetailLevel(0.25f, "tiles/250_%col%_%row%.png", "downsamples/map.png");
        tileView.addDetailLevel(0.125f, "tiles/125_%col%_%row%.png", "downsamples/map.png");
        // Add the view to display it
        setContentView(tileView);
        // use pixel coordinates to roughly center it
        // they are calculated against the "full" size of the mapView 
        // i.e., the largest zoom level as it would be rendered at a scale of 1.0f
        tileView.moveToAndCenter( 9362,6623 );
        tileView.slideToAndCenter( 9362,6623 );

        // Set the default zoom (zoom out by 4 => 1/4 = 0.25)
        tileView.setScale( 0.125 );
        
//        ImageView markerA = new ImageView(this);
//        markerA.setImageResource(R.drawable.maps_marker_blue_small);
//        markerA.setTag("Nice");

        ImageView markerB = new ImageView(this);
        markerB.setImageResource(R.drawable.maps_marker_blue_small);
        markerB.setTag("Paris");

  //      tileView.addMarker(markerA, 100, 200, -0.5f, -1.0f);
        tileView.addMarker(markerB, 3000, 3000, -0.5f, -1.0f);
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

