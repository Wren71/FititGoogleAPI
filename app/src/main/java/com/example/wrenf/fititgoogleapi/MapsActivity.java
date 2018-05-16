package com.example.wrenf.fititgoogleapi;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.gms.maps.model.SquareCap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.util.Log.e;


/**
 * An activity that displays a map showing the place at the device's current location.
 */

public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleMap.OnPolylineClickListener,
        GoogleMap.OnPolygonClickListener, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    private static final String TAG = MapsActivity.class.getSimpleName();
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;


    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    static final LatLng UTS = new LatLng(-33.883997, 151.199156);


    private static final int COLOR_BLACK_ARGB = 0xff000000;
    private static final int COLOR_WHITE_ARGB = 0xffffffff;
    private static final int COLOR_GREEN_ARGB = 0xff388E3C;
    private static final int COLOR_PURPLE_ARGB = 0xff81C784;
    private static final int COLOR_ORANGE_ARGB = 0xffF57F17;
    private static final int COLOR_BLUE_ARGB = 0xffF9A825;

    private static final int POLYLINE_STROKE_WIDTH_PX = 12;
    private static final int POLYGON_STROKE_WIDTH_PX = 8;
    private static final int PATTERN_DASH_LENGTH_PX = 20;
    private static final int PATTERN_GAP_LENGTH_PX = 20;
    private static final PatternItem DOT = new Dot();
    private static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);
    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);

    // Create a stroke pattern of a gap followed by a dot.
    private static final List<PatternItem> PATTERN_POLYLINE_DOTTED = Arrays.asList(GAP, DOT);

    // Create a stroke pattern of a gap followed by a dash.
    private static final List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH);

    // Create a stroke pattern of a dot followed by a gap, a dash, and another gap.
    private static final List<PatternItem> PATTERN_POLYGON_BETA =
            Arrays.asList(DOT, GAP, DASH, GAP);





    private PolylineOptions polylineOptions;
    private ArrayList<LatLng> arrayPoints;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.addMarker(new MarkerOptions()
                .position(UTS)
                .draggable(true));



        getLocationPermission();
        // Turn on the My Location layer and the related control on the map.
       updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();



        // see the methods below for descriptions of the method use.
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);

        this.init();



          mMap.setOnPolylineClickListener(this);



        Polyline polyline1 = mMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(-33.884019, 151.199831),
                        new LatLng(-33.883105, 151.199360),
                        new LatLng(-33.881483, 151.198429),
                        new LatLng(-33.881073, 151.198729)));

        polyline1.setTag("Relaxed Run");
        stylePolyline(polyline1);



        Polyline polyline2 = mMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(37.421955, -122.084112),
                        new LatLng(37.421976, -122.084321),
                        new LatLng(37.421904, -122.084343),
                        new LatLng(37.421763, -122.084938)));

        polyline2.setTag("Short Run");
        stylePolyline(polyline2);

        Polyline polyline3 = mMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(37.422093, -122.082729),
                        new LatLng(37.422198, -122.082717),
                        new LatLng(37.422259, -122.082621),
                        new LatLng(37.422580, -122.081682)));

        polyline3.setTag("Relaxed Run");
        stylePolyline(polyline3);



        Polyline polyline4 = mMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(37.422097, -122.082733),
                        new LatLng(37.422095, -122.082440),
                        new LatLng(37.422099, -122.082405),
                        new LatLng(37.422231, -122.082501),
                        new LatLng(37.422423, -122.082673),
                        new LatLng(37.422698, -122.082720),
                        new LatLng(37.422873, -122.082866),
                        new LatLng(37.422984, -122.081404)




                ));

        polyline4.setTag("Long Run");
        stylePolyline(polyline4);



         mMap.setOnPolygonClickListener(this);


         // Add polygons to indicate areas on the map.
        Polygon polygon1 = mMap.addPolygon(new com.google.android.gms.maps.model.PolygonOptions()
                .clickable(true)
                .add(
                        new LatLng(37.424779, -122.090553),
                        new LatLng(37.424575, -122.086798),
                        new LatLng(37.423621, -122.086862),
                        new LatLng(37.423962, -122.090016)));
        // Store a data object with the polygon, used here to indicate an arbitrary type.
        polygon1.setTag("Relaxed Area");
        // Style the polygon.
        stylePolygon(polygon1);

        Polygon polygon2 = mMap.addPolygon(new com.google.android.gms.maps.model.PolygonOptions()
                .clickable(true)
                .add(
                        new LatLng(37.428203, -122.093104),
                        new LatLng(37.428340, -122.087023),
                        new LatLng(37.425103, -122.086916),
                        new LatLng(37.426599, -122.093267)));
        polygon2.setTag("Intense Area");
        stylePolygon(polygon2);






    }






    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }








    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }

            }
        }
updateLocationUI();
    }

    /*


    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);

            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            e("Exception: %s", e.getMessage());
        }
    }


    @Override
    public void onPolygonClick(Polygon polygon) {

         // Flip the values of the red, green, and blue components of the polygon's color.
        int color = polygon.getStrokeColor(); //^ 0x00ffffff;
        polygon.setStrokeColor(color);
        color = polygon.getFillColor();
        polygon.setFillColor(color);

        Toast.makeText(this, "Area type " + polygon.getTag().toString(), Toast.LENGTH_SHORT).show();
    }





    @Override
    public void onPolylineClick(Polyline polyline) {
        // Flip from solid stroke to dotted stroke pattern.
        if ((polyline.getPattern() == null) || (!polyline.getPattern().contains(DOT))) {
            polyline.setPattern(PATTERN_POLYLINE_DOTTED);
        } else {
            // The default pattern is a solid stroke.
            polyline.setPattern(null);
        }

        Toast.makeText(this, "Route type " + polyline.getTag().toString(),
                Toast.LENGTH_SHORT).show();
    }



    private void stylePolyline(Polyline polyline) {

         String type = "";
    // Get the data object stored with the polyline.
    if (polyline.getTag() != null) {
        type = polyline.getTag().toString();
    }

    switch (type) {
        // If no type is given, allow the API to use the default.
        case "Relaxed Run":


        polyline.setStartCap(new RoundCap());
        polyline.setEndCap(new SquareCap());
        polyline.setWidth(POLYLINE_STROKE_WIDTH_PX);
        polyline.setColor(COLOR_GREEN_ARGB);
        polyline.setJointType(JointType.ROUND);

            break;

        case "Long Run":
            polyline.setStartCap(new RoundCap());
        polyline.setEndCap(new SquareCap());
        polyline.setWidth(POLYLINE_STROKE_WIDTH_PX);
        polyline.setColor(Color.RED);
        polyline.setJointType(JointType.ROUND);


            break;

            case "Short Run":

        polyline.setStartCap(new RoundCap());
        polyline.setEndCap(new SquareCap());
        polyline.setWidth(POLYLINE_STROKE_WIDTH_PX);
        polyline.setColor(COLOR_ORANGE_ARGB );
        polyline.setJointType(JointType.ROUND);


            break;










    }

    }







    //adds marker with each short click and adds a polyline between each marker. This allows the user to create their own running routes.
    @Override
    public void onMapClick(LatLng latLng) {
        MarkerOptions marker = new MarkerOptions();
        marker.position(latLng);
        mMap.addMarker(marker);

        polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.BLACK);
        polylineOptions.width(5);
        arrayPoints.add(latLng);
        polylineOptions.addAll(arrayPoints);
        mMap.addPolyline(polylineOptions);

    }


    //deletes routes if user has a long click
    @Override
    public void onMapLongClick(LatLng latLng) {
       // mMap.clear();

        arrayPoints.clear();


    }

    // sets array points of coordinates of user clicks.
    private void init() {

        String coordinates[] = { "33.8523341", "151.2106085" };
        double lat = Double.parseDouble(coordinates[0]);
        double lng = Double.parseDouble(coordinates[1]);

        LatLng position = new LatLng(lat, lng);
        //GooglePlayServicesUtil.isGooglePlayServicesAvailable(
          //      MapsActivity.this);


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));

        arrayPoints = new ArrayList<>();
    }

/**
     * Styles the polygon, based on type.
     * @param polygon The polygon object that needs styling.
     */
    private void stylePolygon(Polygon polygon) {
        String type = "";
        // Get the data object stored with the polygon.
        if (polygon.getTag() != null) {
            type = polygon.getTag().toString();
        }

        List<PatternItem> pattern = null;
        int strokeColor = COLOR_BLACK_ARGB;
        int fillColor = COLOR_WHITE_ARGB;

        switch (type) {
            // If no type is given, allow the API to use the default.
            case "Relaxed Area":
                // Apply a stroke pattern to render a dashed line, and define colors.
                pattern = PATTERN_POLYGON_ALPHA;
                strokeColor = COLOR_GREEN_ARGB;
                fillColor = COLOR_GREEN_ARGB;
                break;
            case "Intense Area":
                // Apply a stroke pattern to render a line of dots and dashes, and define colors.
                pattern = PATTERN_POLYGON_BETA;
                strokeColor = Color.RED;
                fillColor = Color.RED;
                break;


              case "Short Area":
                // Apply a stroke pattern to render a line of dots and dashes, and define colors.
                pattern = PATTERN_POLYGON_BETA;
                strokeColor = COLOR_ORANGE_ARGB;
                fillColor = COLOR_ORANGE_ARGB;
                break;
        }

        polygon.setStrokePattern(pattern);
        polygon.setStrokeWidth(POLYGON_STROKE_WIDTH_PX);
        polygon.setStrokeColor(strokeColor);
        polygon.setFillColor(fillColor);
    }




}





