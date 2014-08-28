package com.remimichel.utils;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Categories extends ArrayList<Category> implements Parcelable{


    public Categories(List<Category> categories) {
        super(categories);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeTypedList(this);
    }

    public static final Parcelable.Creator<Categories> CREATOR
            = new Parcelable.Creator<Categories>() {
        public Categories createFromParcel(Parcel in) {
            return new Categories(in);
        }

        public Categories[] newArray(int size) {
            return new Categories[size];
        }
    };

    private Categories(Parcel in) {
    //    mData = in.readInt();
    }
}
