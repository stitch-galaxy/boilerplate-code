/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stitchgalaxy.domain;

import java.util.List;

/**
 *
 * @author Administrator
 */
public interface CategoryRepository {
    
    Category find(long categoryId);
    
    List<Category> getTopLeveCategories();
    
    void store(Category category);
    
    void delete(Category category);
}
