package com.ece4600.mainapp;

public class dataSample {
	
	public double xaxis;
	public double yaxis;
	public double zaxis;
	public long timestamp;
	
	public dataSample(double Xvalue, double Yvalue, double Zvalue, long Time_stamp) {
	
		xaxis = Xvalue;
		yaxis = Yvalue;
		zaxis = Zvalue;
		timestamp = Time_stamp;
	}
}