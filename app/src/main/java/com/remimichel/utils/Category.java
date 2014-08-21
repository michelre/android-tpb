package com.remimichel.utils;

import java.util.Collection;
import java.util.List;

public class Category implements Comparable<Category>{

    @Override
    public String toString(){
        return this.name;
    }

    public Category(String name, List<Category> categories) {
        this.name = name;
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

    private String name;
    private List<Category> categories;

    @Override
    public int compareTo(Category category) {
        if(this.getCategories().size() == category.getCategories().size()){
            return this.name.compareTo(category.name);
        }
        return -1;
    }
}
