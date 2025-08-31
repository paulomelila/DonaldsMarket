package com.paulomelila.donaldsmarket;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LocationDetailFragment extends Fragment {
    private View mView;
    private TextView mName;
    private TextView mParkInfo;
    private ListView mLocationDetailsListView;
    private Location mLocation;
    private List<LocationDetails> mLocationDetails = new ArrayList<>();

    // TODO: Make the layout scrollable
    // TODO: Make LocationDetailActivity child of MainActivity and add back button in toolbar

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getIntent();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_location_detail, container, false);

        instantiateWidgets();

        createLocationDetails();

        setInfoForLocation();

        return mView;
    }

    private void instantiateWidgets() {
        mName = mView.findViewById(R.id.location_name);
        mParkInfo = mView.findViewById(R.id.parking_info_content);
        mLocationDetailsListView = mView.findViewById(R.id.location_detail_listView);
    }

    private void getIntent() {

        if (getActivity().getIntent() != null) {
            mLocation = getActivity().getIntent().getParcelableExtra("Location", Location.class);
        }
    }

    private void createLocationDetails() {
        mLocationDetails.add(new LocationDetails(mLocation.getAddress(), android.R.drawable.ic_menu_directions));
        mLocationDetails.add(new LocationDetails(mLocation.getPhone(), android.R.drawable.ic_menu_call));
    }

    private void setInfoForLocation() {
        mName.setText(mLocation.getName());
        mParkInfo.setText(mLocation.getParkingInfo());

        ListAdapter adapter = new LocationDetailAdapter(getContext(), mLocationDetails);

        mLocationDetailsListView = mView.findViewById(R.id.location_detail_listView);
        mLocationDetailsListView.setAdapter(adapter);

        mLocationDetailsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch(position) {
                    case 0: {
                        navigateToLocation(mLocation.getAddress());
                        break;
                    }

                    case 1: {
                        contactLocation(mLocation.getPhone());
                        break;
                    }
                }
            }
        });
    }

    private class LocationDetailAdapter extends ArrayAdapter<LocationDetails> {

        public LocationDetailAdapter(Context context, List<LocationDetails> locationDetails) {
            super(context, R.layout.location_detail_list_item, locationDetails);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.location_detail_list_item, parent, false);

            LocationDetails locationDetails = getItem(position);

            TextView text = view.findViewById(R.id.list_text);
            ImageView icon = view.findViewById(R.id.list_icon);

            text.setText(locationDetails.getText());
            icon.setImageResource(locationDetails.getImageDrawableId());

            return view;
        }
    }

    private void navigateToLocation(String address) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
        Uri.parse("https://www.google.com/maps/dir/?api=1&destination=" + address));
        startActivity(intent);
    }

    private void contactLocation(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    private class LocationDetails {
        private String text;
        private int imageDrawableId;

        public LocationDetails(String text, int imageDrawableId) {
            this.text = text;
            this.imageDrawableId = imageDrawableId;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getImageDrawableId() {
            return imageDrawableId;
        }

        public void setImageDrawableId(int imageDrawableId) {
            this.imageDrawableId = imageDrawableId;
        }
    }


}
