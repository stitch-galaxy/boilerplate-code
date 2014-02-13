/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stitchgalaxy.domain;
/**
 *
 * @author Administrator
 */
public interface CategoryRepository {
    
    Category find(long categoryId);
    
    void store(Category category);
}
