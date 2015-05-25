package com.example.admin.altusmapstest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.crashlytics.android.Crashlytics;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;
import us.ba3.me.LightingType;
import us.ba3.me.Location;
import us.ba3.me.LocationType;
import us.ba3.me.markers.DynamicMarker;
import us.ba3.me.markers.DynamicMarkerMapDelegate;
import us.ba3.me.markers.DynamicMarkerMapInfo;


public class MainActivity extends ActionBarActivity implements DynamicMarkerMapDelegate {

    public final String MAP_NAME = "Map";
    public final String MARKERS_1 = "Markers1";
    public final String MARKERS_2 = "Markers2";
    private RelativeLayout layout;
    protected MyMapView mapView;
    private ArrayList<Vehicle> vehicles;
    private ArrayList<Location> locations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (BuildConfig.USE_CRASHLYTICS) {
            Fabric.with(this, new Crashlytics());
//        }
        setContentView(R.layout.activity_main);

        layout = (RelativeLayout) findViewById(R.id.layout);

        //Add map view.
        mapView = new MyMapView(getApplication());

        //If you want to disable the BA3 watermark, set your license key.
        mapView.setLicenseKey("AAAAAAAA-AAAA-AAAA-AAAA-AAAAAAAAAAAA");

        //Set lighting type to classic
        mapView.setLightingType(LightingType.kLightingTypeClassic);

        //Set sun relative to observer
        mapView.setSunLocation(new Location(0, 0), LocationType.kLocationTypeRelative);

        mapView.setBackgroundColor(Color.BLACK);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);

        layout.addView(mapView, params);

        findViewById(R.id.floating_button).bringToFront();

//        mapView.addStreamingTerrainMap(MAP_NAME,
//                "http://dev1.ba3.us/maps/terrain/pngtiles/{z}/{x}/{y}.png", //Center tiles
//                "http://dev1.ba3.us/maps/terrain/pngtiles/altn/{id}.png", //North tiles
//                "http://dev1.ba3.us/maps/terrain/pngtiles/alts/{id}.png", //South tiles
//                "", 		//Subdomains
//                11,			//Max Level
//                2,			//zOrder
//                3,			//Number of simultaneous downloads
//                true		//Use cache
//                );

        mapView.addStreamingRasterMap(this.MAP_NAME,
                "http://{s}.tiles.mapbox.com/v3/dxjacob.ho6k3ag9/{z}/{x}/{y}.jpg",
                "", //North tiles
                "", //South tiles
                "a,b,c,d",    //Subdomains
                20,            //Max Level
                2,            //zOrder
                3,            //Number of simultaneous downloads
                true,        //Use cache
                false        //No alpha
        );

        //Add dynamic marker layer
        DynamicMarkerMapInfo mapInfo = new DynamicMarkerMapInfo();
        mapInfo.zOrder = 2000;
        mapInfo.name = this.MARKERS_1;
        mapInfo.hitTestingEnabled = true;
        mapInfo.delegate = this;
        mapView.addMapUsingMapInfo(mapInfo);

        this.vehicles = new ArrayList<Vehicle>();

        Vehicle Car = new Vehicle("Car", Vehicle.Type.CAR, new Location(31.323364, 34.919594));
        Vehicle Bus = new Vehicle("Bus", Vehicle.Type.BUS, new Location(31.320, 34.919));
        Vehicle Tank = new Vehicle("Tank", Vehicle.Type.TANK, new Location(31.324, 34.912));
        Vehicle Tank1 = new Vehicle("Tank1", Vehicle.Type.TANK, new Location(31, 34));
        Vehicle Tank2 = new Vehicle("Tank2", Vehicle.Type.TANK, new Location(31.00, 32.012));
        Vehicle Tank3 = new Vehicle("Tank3", Vehicle.Type.TANK, new Location(32.324, 33.912));


        this.vehicles.add(Car);
        this.vehicles.add(Bus);
        this.vehicles.add(Tank);
        this.vehicles.add(Tank1);
        this.vehicles.add(Tank2);
        this.vehicles.add(Tank3);

        for (Vehicle vehicle : vehicles) {
            DynamicMarker marker = new DynamicMarker();
            marker.name = vehicle.getName();
            marker.location = vehicle.getLocation();

//            PicassoMarker target = new PicassoMarker(marker);

            Bitmap bitmap;

            switch (vehicle.getType()) {
                case CAR:
//                    Picasso.with(this).load(R.mipmap.car).into(target);
                    bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.car);
                    marker.setImage(bitmap, false);
                    break;
                case BUS:
//                    Picasso.with(this).load(R.mipmap.bus).into(target);
                    bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.bus);
                    marker.setImage(bitmap, false);
                    break;
                case TANK:
//                    Picasso.with(this).load(R.mipmap.tank).into(target);
                    bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.tank);
                    marker.setImage(bitmap, false);
                    break;
                default:
                    bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            }

            marker.anchorPoint = new PointF(bitmap.getWidth() / 2, bitmap.getHeight() / 2);

            if (vehicle.getType() == Vehicle.Type.TANK) {
                mapView.addDynamicMarkerToMap(this.MARKERS_1, marker);
            } else {
                mapView.addDynamicMarkerToMap(this.MARKERS_2, marker);
            }

            locations.add(vehicle.getLocation());
        }

        mapView.setLocationThatFitsCoordinates(locations.toArray(new Location[0]), 10, 10);
//        mapView.performZoomIn();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //region Location
    protected boolean locationServicesAvailable() {
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            return false;
        }
        return true;
    }

    protected void promptToEnableLocationServices() {
        if (!locationServicesAvailable()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Location Services Not Active");
            builder.setMessage("Please enable Location Services and GPS");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Show location settings when the user acknowledges the alert dialog
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            Dialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }
    }
    //endregion

    @Override
    public void tapOnMarker(
            String mapName,
            String markerName,
            PointF screenPoint,
            PointF markerPoint) {

        Log.w("tapOnMarker", "map: " + mapName + ", marker: " + markerName + ", screenPoint: (" + screenPoint.x + ", " + screenPoint.y + "), markerPoint: (" + markerPoint.x + ", " + markerPoint.y + ")");
    }
}

class Vehicle {

    public enum Type {
        CAR,
        BUS,
        TANK
    }

    private String name;
    private Type type;
    private Location location;

    public Vehicle(String name, Type type, Location location) {
        this.name = name;
        this.type = type;
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {

        return name;
    }

    public Type getType() {
        return type;
    }

    public Location getLocation() {
        return location;
    }


}

class PicassoMarker implements Target {

    DynamicMarker mMarker;

    PicassoMarker(DynamicMarker marker) {
        mMarker = marker;
    }

    @Override
    public int hashCode() {
        return mMarker.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof PicassoMarker) {
            DynamicMarker marker = ((PicassoMarker) o).mMarker;
            return mMarker.equals(marker);
        } else {
            return false;
        }
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        mMarker.setImage(bitmap, false);
        mMarker.anchorPoint = new PointF(bitmap.getWidth() / 2, bitmap.getHeight() / 2);
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {
    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {
    }

}

