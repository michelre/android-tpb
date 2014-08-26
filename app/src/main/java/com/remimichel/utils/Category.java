package com.remimichel.utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Collection;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Category implements Comparable<Category>, Parcelable{

    @Override
    public String toString(){
        return this.name;
    }

    public Category(String path, Boolean hasChildren, String name, List<Category> categories) {
        this.path = path;
        this.name = name;
        this.hasChildren = hasChildren;
        this.categories = categories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(Boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    @SerializedName("has_children")
    private Boolean hasChildren;

    @SerializedName("path")
    private String path;

    @SerializedName("name")
    private String name;

    @SerializedName("categories")
    private List<Category> categories;

    @Override
    public int compareTo(Category category) {
        if(this.getCategories().size() == category.getCategories().size()){
            return this.name.compareTo(category.name);
        }
        return -1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(this.path);
        out.writeByte((byte) (this.hasChildren ? 1 : 0));
        out.writeString(this.name);
        out.writeTypedList(this.categories);
    }

    public static final Parcelable.Creator<Category> CREATOR
            = new Parcelable.Creator<Category>() {
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    private Category(Parcel in) {
        this.path = in.readString();
        this.hasChildren = in.readByte() != 0;
        this.name = in.readString();
        in.readTypedList(this.categories, Category.CREATOR);
    }
}
