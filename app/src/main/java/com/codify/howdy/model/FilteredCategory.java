package com.codify.howdy.model;

import java.io.Serializable;
import java.util.ArrayList;

public final class FilteredCategory implements Serializable {

    public ArrayList<Category> topCategories;
    public ArrayList<Activity> activities;
}