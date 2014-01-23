package com.stitchgalaxy.dao;

import com.stitchgalaxy.domain.Category;
import com.stitchgalaxy.domain.Design;
import com.stitchgalaxy.domain.Partner;
import com.stitchgalaxy.domain.Product;
import com.stitchgalaxy.domain.ProductLocalization;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import org.joda.time.LocalDate;

/**
 * Hello world!
 *
 */
public final class DomainDataService 
{
    private static volatile DomainDataService instance;
    
    public static DomainDataService getInstance()
    {
        DomainDataService localInstance = instance;
        if (localInstance == null)
        {
            synchronized (DomainDataService.class)
            {
                localInstance = instance;
                if (localInstance == null)
                {
                    instance = localInstance = new DomainDataService();
                }
            }
        }
        return localInstance;
    }

    public static void removeProductLocalization(Long productId, String locale) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Category getCategoryById(Long categoryId) {
        return TestData.createProductCategory();
    }

    public void createSubcategory(Long categoryId, String name) {
    }

    public List<Product> getAllProducts() {
        return TestData.createProductsList();
    }

    public void productAssignAuthor(Long productId, Long authorId) {
    }

    public void removeProductAuthor(Long productId) {
    }

    public void addProductToCategory(Long categoryId, Long productId) {
    }

    public void removeProductFromCategory(Long categoryId, Long productId) {
    }

    public void createDesign(Long productId) {
    }

    public Design getDesignById(Long designId) {
        return TestData.createProductDesign();
    }

    public void storeDesignData(Design design) {
    }

    public void removeDesignFile(Long designId) {
    }

    public void removeProductDesign(Long productId, Long designId) {
    }

    public void removeDesignThumbnail(Long designId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void uploadDesignFile(Long designId, InputStream filecontent) {
    }

    public void uploadDesignThumbnail(Long designId, InputStream filecontent) {
    }

    public Product getProductById(Long productId) {
        return TestData.createProductData();
    }

    public void storeProductData(Product product) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ProductLocalization getProductLocalization(Long productId, String locale) {
        return TestData.createProductLocalization();
    }

    public void storeProductLocalization(ProductLocalization localization) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void addProductLocalization(Long productId, String locale) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void createNewProduct(String name, LocalDate date, BigDecimal price) {
    }

    public List<Partner> getAllPartners() {
        return TestData.createPartnersList();
    }

    public void addPartner(String name, String uri) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void removeProductCompleteProduct(Long productId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void removeProductLargeImage(Long productId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void removeProductPrototype(Long productId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void removeProductThumbnail(Long productId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void productAssignTranslator(Long productId, Long partnerId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void removeProductTranslator(Long productId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void uploadProductCompleteProduct(Long productId, InputStream filecontent) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void uploadProductLargeImage(Long productId, InputStream filecontent) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void uploadProductPrototype(Long productId, InputStream filecontent) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void uploadProductThumbnail(Long productId, InputStream filecontent) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void removeSubcategory(Long categoryId, Long subCategoryId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
