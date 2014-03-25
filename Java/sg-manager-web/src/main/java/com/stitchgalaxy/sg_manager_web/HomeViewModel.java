/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stitchgalaxy.sg_manager_web;

import com.stitchgalaxy.dto.CommandGetProducts;
import com.stitchgalaxy.dto.ProductInfo;
import com.stitchgalaxy.service.DomainDataService;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;


/**
 *
 * @author Administrator
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class HomeViewModel {

    @WireVariable
    DomainDataService dataservice;
            
    private Product selectedProduct;
    private List<Product> products = new ArrayList<Product>();

    /**
     * @return the selectedProduct
     */
    public Product getSelectedProduct() {
        return selectedProduct;
    }

    /**
     * @param selectedProduct the selectedProduct to set
     */
    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    /**
     * @return the products
     */
    public List<Product> getProducts() {
        return products;
    }
    
    @Init
    public void init(){
        List<ProductInfo> products = dataservice.getAllProducts(new CommandGetProducts());
        for (ProductInfo p : products)
        {
            Product product = new Product();
            product.setId(p.getId());
            product.setName(p.getName());
            product.setPrice(p.getPriceUsd());
            product.setDate(p.getDate().toString());
            this.products.add(product);
        }
    }
}