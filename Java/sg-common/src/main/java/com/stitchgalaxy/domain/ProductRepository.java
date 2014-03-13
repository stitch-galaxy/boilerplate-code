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
public interface ProductRepository {
    
    Product find(long productId);
    
    List<Product> getProducts();
    
    void store(Category category);
    
    void delete(Category category);
}
