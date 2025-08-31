package com.paulomelila.donaldsmarket;

import android.os.Parcel;
import android.os.Parcelable;

public class Location implements Parcelable {
    private String mName;
    private String mAddress;
    private String mPhone;
    private String mParkingInfo;

    public Location(String name, String address, String phone, String parkingInfo) {
        this.mName = name;
        this.mAddress = address;
        this.mPhone = phone;
        this.mParkingInfo = parkingInfo;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        this.mAddress = address;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        this.mPhone = phone;
    }

    public String getParkingInfo() { return mParkingInfo; }

    public void setParkingInfo(String parkingInfo) { mParkingInfo = parkingInfo; }

    private Location (Parcel in) {
        mName = in.readString();
        mAddress = in.readString();
        mPhone = in.readString();
        mParkingInfo = in.readString();
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mName);
        parcel.writeString(mAddress);
        parcel.writeString(mPhone);
        parcel.writeString(mParkingInfo);
    }
}