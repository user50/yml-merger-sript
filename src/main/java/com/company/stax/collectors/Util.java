package com.company.stax.collectors;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Sets;

import java.util.*;

/**
 * Created by user50 on 11.07.2015.
 */
public class Util {
    ListMultimap<String, Category> map;

    public Util(ListMultimap<String, Category> map) {
        this.map = map;
    }

    public Set<Category> getDescendants(Category category){
        Set<Category> categories = Sets.newHashSet(category);

        if (!map.containsKey(category.getId()) || map.get(category.getId()).isEmpty()) {
            return categories;
        }

        for (Category child : map.get(category.getId())) {
            categories.addAll(getDescendants(child));
        }

        return categories;
    }

    public Set<Category> getDescendants(Set<Category> categories){
        Set<Category> results = new HashSet<>();

        for (Category category : categories) {
            results.addAll(getDescendants(category));
        }

        return results;
    }
}
