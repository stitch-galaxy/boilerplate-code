/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stitchgalaxy.sg_manager_web.data;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class Category {
    private Long id;
    private String Name;
    private Category Parent;
    private final List<Category> Childs = new LinkedList<Category>();

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
        return Name;
    }

    /**
     * @param Name the Name to set
     */
    public void setName(String Name) {
        this.Name = Name;
    }

    /**
     * @return the Parent
     */
    public Category getParent() {
        return Parent;
    }

    /**
     * @param Parent the Parent to set
     */
    public void setParent(Category Parent) {
        this.Parent = Parent;
    }

    /**
     * @return the Childs
     */
    public List<Category> getChilds() {
        return Childs;
    }
}