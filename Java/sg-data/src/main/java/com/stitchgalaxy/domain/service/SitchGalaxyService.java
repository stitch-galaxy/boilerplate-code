/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stitchgalaxy.domain.service;

import com.stitchgalaxy.domain.entities.jpa.Product;
import com.stitchgalaxy.domain.entities.jpa.ProductRepository;
import java.nio.file.AccessDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

/**
 *
 * @author tarasev
 */
@Service
public class SitchGalaxyService {
    
    @Autowired
    ProductRepository productRepository;
    
    @Transactional(rollbackFor = Exception.class)
    public void addProduct()
    {
        Product p = new Product();
        p.setBlocked(Boolean.TRUE);
        productRepository.save(p);
    }
}
