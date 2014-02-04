/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stitchgalaxy.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 *
 * @author Administrator
 */
public class Category {
    private Long id;
    private String name;
    
    private Category parent;
    
    private final Set<Category> childs = new HashSet<Category>();

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the Name
     */
    public String getName() {
        return name;
    }

    /**
     * @param Name the Name to set
     */
    public void setName(String Name) {
        this.name = Name;
    }

    /**
     * @return the Parent
     */
    public Category getParent() {
        return parent;
    }

    /**
     * @param Parent the Parent to set
     */
    public void setParent(Category Parent) {
        this.parent = Parent;
    }

    /**
     * @return the Childs
     */
    public Set<Category> getChilds() {
        return childs;
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Category other = (Category) obj;
        return other.id.equals(id);

    }
}
