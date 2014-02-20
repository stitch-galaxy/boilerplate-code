/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stitchgalaxy.domain;

import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
/**
 *
 * @author Administrator
 */
public class Category implements Entity<Category>{
    
    private static final char path_delimeter = '/';
    
    public Category(){
    }
    
    public Category(Category parent, String name)
    {
        this.name = name;
        if (parent != null)
        {
            this.path = parent.getFullPath();
            this.parent = parent;
            this.parent.childs.add(this);
        }
        else
        {
            this.path = "";
        }
    }
    
    private Long id;
    private String name;
    private String path;
    
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
    
        /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }
    
    public String getFullPath()
    {
        StringBuilder sb = new StringBuilder(path);
        sb.append(path_delimeter);
        sb.append(name);
        return sb.toString();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(name)
                .append(path)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        
        Category other = (Category) obj;
        return sameIdentityAs(other);
    }
    
    public boolean sameIdentityAs(Category other) {
        return other != null && new EqualsBuilder().
                append(this.name, other.name).
                append(this.path, other.path).
                isEquals();
    }
}
