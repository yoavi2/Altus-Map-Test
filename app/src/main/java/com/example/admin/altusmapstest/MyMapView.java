package com.example.admin.altusmapstest;

import android.content.Context;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;

import us.ba3.me.DoubleReceiver;
import us.ba3.me.Location;
import us.ba3.me.PointReceiver;

public class MyMapView extends us.ba3.me.MapView {

	public MyMapView(Context context) {
		super(context);
	}

	//This is how you capture touch events....
	@Override
	public boolean onTouchEvent(MotionEvent event) {
	
		//This is how to you get the distance from a screen point to the horizon
		Log.w("DistanceToHorizonFrom","("+event.getX()+"," + event.getY()+")");
		super.getDistanceToHorizonFromPoint(new PointF(event.getX(), event.getY()), new DoubleReceiver(){
			@Override
			public void receiveDouble(double normalizedDistance) {
				Log.w("DistanceToHorizon:",""+normalizedDistance);
			}});
		
		//This is how you look up the geographic location of a touch event
		/*
		super.getLocationForPoint(new PointF(event.getX(), event.getY()), new LocationReceiver(){
			@Override
			public void receiveLocation(Location loc) {
				Log.w("onTouch", "lon:" + loc.longitude + " lat:" + loc.latitude);
			}});
		*/
		
		//This is how you convert a location to a screen point.
		//Determine where on the screen Mt. Ranier is:
		Location ranierLocation = new Location(46.852189, -121.757604);
		super.convertCoordinate(ranierLocation, new PointReceiver(){
			@Override
			public void receivePoint(PointF screenPoint){
				Log.w("screen point (", + screenPoint.x + ", " + screenPoint.y + ")");
			}});

//		Crashlytics.getInstance().crash();

		//Always pass to base class....
		return super.onTouchEvent(event);
	}

}
