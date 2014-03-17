/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stitchgalaxy.infrastructure.persistence;

import com.stitchgalaxy.domain.Product;
import com.stitchgalaxy.domain.ProductRepository;
import java.util.List;

/**
 *
 * @author tarasev
 */
public class ProductRepositoryHibernate extends HibernateRepository implements ProductRepository {

    public Product find(long productId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<Product> getProducts() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void store(Product product) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void delete(Product product) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
