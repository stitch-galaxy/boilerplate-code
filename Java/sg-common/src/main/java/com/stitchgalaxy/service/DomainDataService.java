package com.stitchgalaxy.service;

import com.stitchgalaxy.domain.Category;
import com.stitchgalaxy.domain.CategoryRepository;
import com.stitchgalaxy.domain.Design;
import com.stitchgalaxy.domain.Partner;
import com.stitchgalaxy.domain.Product;
import com.stitchgalaxy.domain.ProductLocalization;
import com.stitchgalaxy.dto.CategoryInfoDTO;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import org.joda.time.LocalDate;
import org.springframework.transaction.annotation.Transactional;

/**
 * Hello world!
 *
 */
@Transactional
public class DomainDataService 
{
    private DataMapper dataMapper;
    private CategoryRepository categoryRepository;
    
    public void setDataMapper(DataMapper dataMapper){
        this.dataMapper = dataMapper;
    }
    
    public void setCategoryRepository(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    //Localization
    //TODO: add localization id attribute
    public static void removeProductLocalization(Long productId, String locale) {
    }
    
    public void storeProductLocalization(ProductLocalization localization) {
    }

    public void addProductLocalization(Long productId, String locale) {
    }
    
    public ProductLocalization getProductLocalization(Long productId, String locale) {
        return TestData.createProductLocalization();
    }

    public List<CategoryInfoDTO> getRootCategories() {
        List<Category> categories = categoryRepository.getTopLeveCategories();
        List<CategoryInfoDTO> result = new LinkedList<CategoryInfoDTO>();
        for(Category c : categories)
        {
            result.add(dataMapper.getCategoryInfoDTO(c));
        }
        return result;
    }
    
    public void createTopLevelСategory(String name) {
        Category topLevelCategory = new Category(null, name);
        categoryRepository.store(topLevelCategory);
    }
    
    public CategoryInfoDTO getCategoryById(Long categoryId) {
        return dataMapper.getCategoryInfoDTO(categoryRepository.find(categoryId));
    }

    public void createSubcategory(Long categoryId, String name) {
        Category parent = categoryRepository.find(categoryId);
        Category child = new Category(parent, name);
        categoryRepository.store(parent);
    }
    
    public void removeSubcategory(Long categoryId, Long subCategoryId) {
        Category parent = categoryRepository.find(categoryId);
        Category child = categoryRepository.find(subCategoryId);
        parent.getChilds().remove(child);
        categoryRepository.delete(child);
        categoryRepository.store(parent);
    }
    
    public void removeTopLevelCategory(Long categoryId)
    {
        Category topLevel = categoryRepository.find(categoryId);
        categoryRepository.delete(topLevel);
    }
    
    //Partners
    public void addPartner(String name, String uri) {
    }
    
    public List<Partner> getAllPartners() {
        return TestData.createPartnersList();
    }
    
    //Design
    public void createDesign(Long productId) {
    }

    public Design getDesignById(Long designId) {
        return TestData.createProductDesign();
    }

    public void storeDesignData(Design design) {
    }
    
    public void uploadDesignFile(Long designId, InputStream filecontent) {
    }

    public void uploadDesignThumbnail(Long designId, InputStream filecontent) {
    }
    
    public void removeDesignFile(Long designId) {
    }
    
    public void removeDesignThumbnail(Long designId) {
    }
    
    //Product
    
    public List<Product> getAllProducts() {
        return TestData.createProductsList();
    }
    
    public Product getProductById(Long productId) {
        return TestData.createProductData();
    }
    
    public void createNewProduct(String name, LocalDate date, BigDecimal price) {
    }
    
    public void storeProductData(Product product) {
    }
    
    public void addProductToCategory(Long categoryId, Long productId) {
    }

    public void removeProductFromCategory(Long categoryId, Long productId) {
    }
    
    public void removeProductDesign(Long productId, Long designId) {
    }

    public void productAssignTranslator(Long productId, Long partnerId) {
    }

    public void removeProductTranslator(Long productId) {
    }
    
    public void productAssignAuthor(Long productId, Long authorId) {
    }

    public void removeProductAuthor(Long productId) {
    }

    public void uploadProductCompleteProduct(Long productId, InputStream filecontent) {
    }

    public void uploadProductLargeImage(Long productId, InputStream filecontent) {
    }

    public void uploadProductPrototype(Long productId, InputStream filecontent) {
    }

    public void uploadProductThumbnail(Long productId, InputStream filecontent) {
    }
    
    public void removeProductCompleteProduct(Long productId) {
    }

    public void removeProductLargeImage(Long productId) {
    }

    public void removeProductPrototype(Long productId) {
    }

    public void removeProductThumbnail(Long productId) {
    }
    
}
