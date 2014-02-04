/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stitchgalaxy.dto;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tarasev
 */
public class CategoryInfoDTO {
    
    private CategoryInfo parent;
    private List<CategoryInfo> childs = new ArrayList<CategoryInfo>();
    private CategoryInfo current;

    /**
     * @return the parent
     */
    public CategoryInfo getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(CategoryInfo parent) {
        this.parent = parent;
    }

    /**
     * @return the childs
     */
    public List<CategoryInfo> getChilds() {
        return childs;
    }

    /**
     * @param childs the childs to set
     */
    public void setChilds(List<CategoryInfo> childs) {
        this.childs = childs;
    }

    /**
     * @return the current
     */
    public CategoryInfo getCurrent() {
        return current;
    }

    /**
     * @param current the current to set
     */
    public void setCurrent(CategoryInfo current) {
        this.current = current;
    }
}
