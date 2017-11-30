package com.goer.view.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.goer.view.CamActivity;
import com.goer.view.EventDetailActivity;
import com.goer.R;
import com.goer.controller.EventController;
import com.goer.model.Event;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class NearbyFragment extends GoerFragment implements OnMapReadyCallback {
    private static final int WIKITUDE_PERMISSIONS_REQUEST_CAMERA = 1;
    private static final int WIKITUDE_PERMISSIONS_REQUEST_GPS = 2;
    private Button btn_launch_AR;
    private GoogleMap map;
    private MapView mapView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_nearby, container, false);
        btn_launch_AR = (Button) v.findViewById(R.id.btn_launch_AR);
        btn_launch_AR.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( ContextCompat.checkSelfPermission(NearbyFragment.this.getContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions(NearbyFragment.this.getActivity(), new String[]{android.Manifest.permission.CAMERA}, WIKITUDE_PERMISSIONS_REQUEST_CAMERA);
                } else {
                    if ( ContextCompat.checkSelfPermission(NearbyFragment.this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                        ActivityCompat.requestPermissions(NearbyFragment.this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, WIKITUDE_PERMISSIONS_REQUEST_GPS);
                    } else {
                        launchArchitectCam();
                    }
                }
            }
        });

        mapView = (MapView) v.findViewById(R.id.show_event_on_map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        UiSettings uiSettings = map.getUiSettings();
        uiSettings.setTiltGesturesEnabled(true);
        uiSettings.setZoomControlsEnabled(true);

        getCurrentLocation();
        setMarker();
    }

    public void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, WIKITUDE_PERMISSIONS_REQUEST_GPS);
            return;
        }

        LocationManager locationManager = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);

        List<String> providers = locationManager.getProviders(true);
        Location location = null;
        for (String provider : providers) {
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (location == null || l.getAccuracy() < location.getAccuracy()) {
                location = l;
            }
        }

        if (location != null) {
            double dLatitude = location.getLatitude();
            double dLongitude = location.getLongitude();
            LatLng location_latlng = new LatLng(dLatitude, dLongitude);
            Marker m = map.addMarker(new MarkerOptions().position(location_latlng));
            m.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_user));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location_latlng, 10));

        }
        map.setMyLocationEnabled(true);

    }

    public void setMarker(){
        try {
            ArrayList<Event> events = EventController.getAllEvents(getArguments().getString("user_id"));
            if (events != null) {
                for(Event e : events) {
                    Marker m = map.addMarker(new MarkerOptions().position(new LatLng(e.lat, e.lng)).title(e.title).snippet("Click for detail..."));
                    m.setTag(e);
                    m.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_flag));
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Event e = (Event)(marker.getTag());
                Intent intent = new Intent(getContext(), EventDetailActivity.class);
                intent.putExtra("EventModel", e);
                getContext().startActivity(intent);
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case WIKITUDE_PERMISSIONS_REQUEST_CAMERA: {
                if ( grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                    if ( ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                        ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, WIKITUDE_PERMISSIONS_REQUEST_GPS);
                    } else {
                        launchArchitectCam();
                    }
                } else {
                    Toast.makeText(this.getContext(), "Sorry, augmented reality doesn't work without reality.\n\nPlease grant camera permission.", Toast.LENGTH_LONG).show();
                }
                return;
            }
            case WIKITUDE_PERMISSIONS_REQUEST_GPS: {
                if ( grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                    launchArchitectCam();
                } else {
                    Toast.makeText(this.getContext(), "Sorry, this example requires access to your location in order to work properly.\n\nPlease grant location permission.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void launchArchitectCam() {

        Intent architectIntent = new Intent( this.getActivity(), CamActivity.class);
        architectIntent.putExtra("user_id", getArguments().getString("user_id"));
        startActivity( architectIntent );
    }
    @Override
    public Boolean isAddEventButtonVisible() {
        return false;
    }

}
