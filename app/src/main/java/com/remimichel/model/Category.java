package com.remimichel.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Category implements Comparable<Category>{

    @Override
    public String toString() {
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
        if (this.getCategories().size() == category.getCategories().size()) {
            return this.name.compareTo(category.name);
        }
        return -1;
    }
}
