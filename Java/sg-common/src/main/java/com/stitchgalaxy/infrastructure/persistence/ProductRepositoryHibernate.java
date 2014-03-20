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
        return (Product) getSession().createQuery("from Product where id = :id").setParameter("id", productId).uniqueResult();
    }

    public List<Product> getProducts() {
        List<Product> result = getSession().createQuery("from Product").list();
        return result;
    }

    public void store(Product product) {
        getSession().saveOrUpdate(product);
    }

    public void delete(Product product) {
        getSession().delete(product);
    }
    
}
