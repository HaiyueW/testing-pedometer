package com.ece4600.mainapp;


import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.text.format.Time;

public class ECGLine {

	//Just for one line
		private TimeSeries series = new TimeSeries("Line");
		private XYSeriesRenderer renderer = new XYSeriesRenderer();
		
		//contains all lines information
		private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
		private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer(); 
		
		//counter to have graph to only have 50 points
		private int count;
		private long interval,now ;
		
		//private static final int textSize = 12;
		
		private GraphicalView mChartView;
		private Time nowTxt = new Time(Time.getCurrentTimezone());
		
		@SuppressWarnings("deprecation")
		public void stop(){
			series.clear();
			mDataset.removeSeries(series);
			mRenderer.removeAllRenderers();
			mRenderer.clearXTextLabels();
			
		}
		
		public void initialize(){
			//int[] margins = {100, 25, 250, 250};
			// initializes class:
			// Add single dataset to multiple dataset
			mDataset.addSeries(series);
			
			// Customization time for line 1!
			renderer.setColor(Color.RED);
			renderer.setPointStyle(PointStyle.SQUARE);
			renderer.setFillPoints(true);
			
					
			// Enable Zoom
			mRenderer.setApplyBackgroundColor(true);
			mRenderer.setBackgroundColor(Color.BLACK);
			//mRenderer.setZoomButtonsVisible(true);
			//mRenderer.setMargins(margins);
			mRenderer.setChartTitle("ECG Measurement");
			mRenderer.setChartTitleTextSize(50);
			mRenderer.setXTitle("");
			
			mRenderer.setYTitle("");
			
			
			mRenderer.setXLabels(0);		
			// Add single renderer to multiple renderer
			mRenderer.addSeriesRenderer(renderer);	
			mRenderer.setLabelsTextSize(25);
			mRenderer.setXLabelsAngle(-45);
			//mRenderer.set
			
			count =0;
			interval =0;
			nowTxt.setToNow();
			now = nowTxt.toMillis(false);
			
			
		}
		
		public void addPoint(double x, double y){
			series.add(x, y);
			
			nowTxt.setToNow();
			interval = nowTxt.toMillis(false) - now;
			
			if (interval == 1000){ // 1 second passed
			mRenderer.addXTextLabel(x,nowTxt.format("%k:%M:%S"));
			now = nowTxt.toMillis(false);
			}
			else
			{
				mRenderer.addXTextLabel(x,"");
			}
			
			
			if (count < 50){
				count++;
			}
			else{
				series.remove(0);
			}
			
			
		}
		
		public GraphicalView getView(Context context){
			mChartView = ChartFactory.getLineChartView(context, mDataset, mRenderer);
			return mChartView;
		}
}
