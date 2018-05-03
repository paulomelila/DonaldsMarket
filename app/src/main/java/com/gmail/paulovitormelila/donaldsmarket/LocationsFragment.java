package com.gmail.paulovitormelila.donaldsmarket;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;
import static java.lang.String.valueOf;

public class LocationsFragment extends Fragment {
    private View mView;
    private SupportMapFragment mMapFragment;
    private GoogleMap mMap;

    private List<Location> mLocations = new ArrayList<>();
    private List<String> mAddresses = new ArrayList<>();
    private List<LatLng> mCoordinatesList = new ArrayList<>();

    private LatLng mUserLocation;

    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestUserPermission();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.locations_fragment, container, false);

        getUserLocation();
        instantiateWidgets();
        createLocations();
        getLocationAddresses();
        getCoordinatesOfLocations();
        loadMap();
        setupListView();

        return mView;
    }

    private void instantiateWidgets() {
        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
    }

    private void createLocations() {
        Location hastings = new Location(getString(R.string.hastings_name), getString(R.string.hastings_address), getString(R.string.hastings_phone), getString(R.string.hastings_parking_info));
        Location commercial = new Location(getString(R.string.commercial_name), getString(R.string.commercial_address), getString(R.string.commercial_phone), getString(R.string.commercial_parking_info));
        Location new_west = new Location(getString(R.string.newwest_name), getString(R.string.newwest_address), getString(R.string.newwest_phone), getString(R.string.newwest_parking_info));
        Location po_co = new Location(getString(R.string.poco_name), getString(R.string.poco_address), getString(R.string.poco_phone), getString(R.string.poco_parking_info));

        mLocations.add(hastings);
        mLocations.add(commercial);
        mLocations.add(new_west);
        mLocations.add(po_co);
    }

    private void getLocationAddresses() {
        for (int i = 0; i < mLocations.size(); i++) {
            mAddresses.add(mLocations.get(i).getAddress());
        }
    }

    private void getCoordinatesOfLocations() {
        List<Address> hastings = new ArrayList<>();
        List<Address> commercial = new ArrayList<>();
        List<Address> new_west = new ArrayList<>();
        List<Address> poco = new ArrayList<>();

        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

        try {
            hastings = geocoder.getFromLocationName(mAddresses.get(0), 1);
            while (hastings.isEmpty()) {
                hastings = geocoder.getFromLocationName(mAddresses.get(0), 1);
            }

            commercial = geocoder.getFromLocationName(mAddresses.get(1), 1);
            while (commercial.isEmpty()) {
                commercial = geocoder.getFromLocationName(mAddresses.get(1), 1);
            }

            new_west = geocoder.getFromLocationName(mAddresses.get(2), 1);
            while (new_west.isEmpty()) {
                new_west = geocoder.getFromLocationName(mAddresses.get(2), 1);
            }

            poco = geocoder.getFromLocationName(mAddresses.get(3), 1);
            while (poco.isEmpty()) {
                poco = geocoder.getFromLocationName(mAddresses.get(3), 1);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        mCoordinatesList.add(new LatLng(hastings.get(0).getLatitude(), hastings.get(0).getLongitude()));
        mCoordinatesList.add(new LatLng(commercial.get(0).getLatitude(), commercial.get(0).getLongitude()));
        mCoordinatesList.add(new LatLng(new_west.get(0).getLatitude(), new_west.get(0).getLongitude()));
        mCoordinatesList.add(new LatLng(poco.get(0).getLatitude(), poco.get(0).getLongitude()));
    }

    private void loadMap() {
        ViewGroup.LayoutParams params = mMapFragment.getView().getLayoutParams();
        params.height = (int) ((Resources.getSystem().getDisplayMetrics().widthPixels) / 1.77);

        mMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                mMap.getUiSettings().setZoomControlsEnabled(true);

                int height = 110;
                int width = 80;
                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.map_marker);
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap marker = Bitmap.createScaledBitmap(b, width, height, false);

                for (int i = 0; i < mLocations.size(); i++) {
                    mMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromBitmap(marker))
                            .position(mCoordinatesList.get(i))
                            .title(getString(R.string.app_name))
                            .snippet(mLocations.get(i).getName()));
                }

                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(49.273211, -122.924573)));
            }
        });
    }

    private void setupListView() {
        ListAdapter adapter = new LocationsAdapter(getContext(), mLocations);

        final ListView listView = mView.findViewById(R.id.locations_listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Location location = (Location) listView.getItemAtPosition(position);

                Intent intent = new Intent(getContext(), LocationDetailActivity.class);
                intent.putExtra("Location", location);

                startActivity(intent);
            }
        });
    }

    private class LocationsAdapter extends ArrayAdapter {

        public LocationsAdapter(Context context, List<Location> locationsList) {
            super(context, R.layout.locations_list_item, locationsList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.locations_list_item, parent, false);

            Location location = (Location) getItem(position);

            TextView locationName = view.findViewById(R.id.location_name);

            locationName.setText(location.getName());

            return view;
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            for (int i = 0; i<permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                switch (permission) {
                    case Manifest.permission.ACCESS_FINE_LOCATION: {
                        if (grantResult == PackageManager.PERMISSION_GRANTED) {
                            getUserLocation();
                        }
                    }

                    case Manifest.permission.ACCESS_COARSE_LOCATION: {
                        if (grantResult == PackageManager.PERMISSION_GRANTED) {
                            getUserLocation();
                        }
                    }
                }
            }
        }
    }

    private void requestUserPermission() {
        if (checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
    }

    @SuppressLint("MissingPermission")
    private void getUserLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<android.location.Location>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(android.location.Location location) {
                        mUserLocation = new LatLng(location.getLatitude(), location.getLongitude());

                        mMap.moveCamera(CameraUpdateFactory.newLatLng(mUserLocation));
                        mMap.setMyLocationEnabled(true);
                    }
                });
    }
}
