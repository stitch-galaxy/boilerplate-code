/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stitchgalaxy.sg_manager_web;

import com.stitchgalaxy.dto.CommandGetProduct;
import com.stitchgalaxy.dto.CommandGetProducts;
import com.stitchgalaxy.dto.ProductInfo;
import com.stitchgalaxy.service.DomainDataService;
import com.stitchgalaxy.service.DomainDataServiceException;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
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
    private DomainDataService dataservice;
            
    private ProductRef selectedProduct;
    private List<ProductRef> products = new ArrayList<ProductRef>();
    private ProductDetails product = null;

    private String productDetailsError = null;
    private static final String ERROR_LOAD_PRODUCT_DETAILS = "Cannot load product details";
    
    
    /**
     * @return the selectedProduct
     */
    public ProductRef getSelectedProduct() {
        return selectedProduct;
    }

    /**
     * @param selectedProduct the selectedProduct to set
     */
    public void setSelectedProduct(ProductRef selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    /**
     * @return the products
     */
    public List<ProductRef> getProducts() {
        return products;
    }
    
    @Init
    public void init(){
        List<ProductInfo> products = dataservice.getAllProducts(new CommandGetProducts());
        for (ProductInfo p : products)
        {
            ProductRef product = new ProductRef();
            product.setId(p.getId());
            product.setName(p.getName());
            product.setPrice(p.getPriceUsd());
            product.setDate(p.getDate().toString());
            this.products.add(product);
        }
    }
    
    @Command("productSelected")
    @NotifyChange("product")
    public void productSelected() {
        setProduct(null);
        setProductDetailsError(ERROR_LOAD_PRODUCT_DETAILS);
        
        Long selectedProductId = selectedProduct.getId();
        CommandGetProduct command = new CommandGetProduct();
        command.setProductId(selectedProductId);
        try
        {
            ProductInfo productInfo = dataservice.getProductById(command);
            setProduct(new ProductDetails());
            getProduct().setName(productInfo.getName());
            setProductDetailsError(null);
        }
        catch(Exception e)
        {
            //TODO: show exception
        }
    }

    /**
     * @return the product
     */
    public ProductDetails getProduct() {
        return product;
    }

    /**
     * @param product the product to set
     */
    public void setProduct(ProductDetails product) {
        this.product = product;
    }

    /**
     * @return the productDetailsError
     */
    public String getProductDetailsError() {
        return productDetailsError;
    }

    /**
     * @param productDetailsError the productDetailsError to set
     */
    public void setProductDetailsError(String productDetailsError) {
        this.productDetailsError = productDetailsError;
    }
}