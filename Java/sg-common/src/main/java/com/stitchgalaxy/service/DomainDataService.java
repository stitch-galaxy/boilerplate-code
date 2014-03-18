package com.stitchgalaxy.service;

import com.stitchgalaxy.domain.Category;
import com.stitchgalaxy.domain.CategoryRepository;
import com.stitchgalaxy.domain.Design;
import com.stitchgalaxy.domain.Partner;
import com.stitchgalaxy.domain.Product;
import com.stitchgalaxy.domain.ProductLocalization;
import com.stitchgalaxy.domain.ProductRepository;
import com.stitchgalaxy.dto.CategoryInfoDTO;
import com.stitchgalaxy.dto.CommandCreateSubcategory;
import com.stitchgalaxy.dto.CommandCreateTopLevelCategory;
import com.stitchgalaxy.dto.CommandGetCategory;
import com.stitchgalaxy.dto.CommandGetProductLocalization;
import com.stitchgalaxy.dto.CommandGetRootCategories;
import com.stitchgalaxy.dto.CommandRemoveProductLocalization;
import com.stitchgalaxy.dto.CommandRemoveSubcategory;
import com.stitchgalaxy.dto.CommandRemoveTopLevelCategory;
import com.stitchgalaxy.dto.CommandStoreProductLocalization;
import com.stitchgalaxy.dto.ProductLocalizationInfo;
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
public class DomainDataService {

    private static final String ERROR_CATEGORY_NOT_FOUND = "Category not found";
    private static final String ERROR_PRODUCT_NOT_FOUND = "Product not found";
    private static final String ERROR_PRODUCT_LOCALIZATION_NOT_FOUND = "Localization not found";
    
    private DataMapper dataMapper;
    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;

    public void setDataMapper(DataMapper dataMapper) {
        this.dataMapper = dataMapper;
    }

    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void removeProductLocalization(CommandRemoveProductLocalization command) throws DomainDataServiceException {
        Product product = productRepository.find(command.getProductId());
        if (product == null) {
            throw new DomainDataServiceException(ERROR_PRODUCT_NOT_FOUND);
        }
        for (ProductLocalization localization : product.getLocalizations()) {
            if (localization.getLocale().equals(command.getLocale())) {
                product.getLocalizations().remove(localization);
                return;
            }
        }
        throw new DomainDataServiceException(ERROR_PRODUCT_LOCALIZATION_NOT_FOUND);
    }

    public void storeProductLocalization(CommandStoreProductLocalization command) throws DomainDataServiceException {
        Product product = productRepository.find(command.getProductId());
        if (product == null){
            throw new DomainDataServiceException(ERROR_PRODUCT_NOT_FOUND);
        }
        ProductLocalization localization = dataMapper.getProductLocalization(command.getProductLocalization());
        //How does it work ?
        product.getLocalizations().remove(localization);
        product.getLocalizations().add(localization);
    }

    public ProductLocalizationInfo getProductLocalization(CommandGetProductLocalization command) throws DomainDataServiceException {
        Product product = productRepository.find(command.getProductId());
        if (product == null){
            throw new DomainDataServiceException(ERROR_PRODUCT_NOT_FOUND);
        }
        for (ProductLocalization localization : product.getLocalizations()) {
            if (localization.getLocale().equals(command.getLocale()))
            {
                ProductLocalizationInfo dto = dataMapper.getProductLocalizationDTO(localization);
                return dto;
            }
        }
        throw new DomainDataServiceException(ERROR_PRODUCT_LOCALIZATION_NOT_FOUND);
    }

    public List<CategoryInfoDTO> getRootCategories(CommandGetRootCategories command) {
        List<Category> categories = categoryRepository.getTopLeveCategories();
        List<CategoryInfoDTO> result = new LinkedList<CategoryInfoDTO>();
        for (Category c : categories) {
            result.add(dataMapper.getCategoryInfoDTO(c));
        }
        return result;
    }

    public void createTopLevelСategory(CommandCreateTopLevelCategory command) {
        Category topLevelCategory = new Category(null, command.getName());
        categoryRepository.store(topLevelCategory);
    }

    public CategoryInfoDTO getCategoryById(CommandGetCategory command) throws DomainDataServiceException {
        Category category = categoryRepository.find(command.getCategoryId());
        if (category == null)
            throw new DomainDataServiceException(ERROR_CATEGORY_NOT_FOUND);
        return dataMapper.getCategoryInfoDTO(category);
    }
    
    public void createSubcategory(CommandCreateSubcategory command) throws DomainDataServiceException {
        Category parent = categoryRepository.find(command.getCategoryId());
        if (parent == null)
            throw new DomainDataServiceException(ERROR_CATEGORY_NOT_FOUND);
        Category child = new Category(parent, command.getName());
        categoryRepository.store(parent);
    }
    
    public void removeSubcategory(CommandRemoveSubcategory command) throws DomainDataServiceException{
        Category parent = categoryRepository.find(command.getCategoryId());
        if (parent == null)
            throw new DomainDataServiceException(ERROR_CATEGORY_NOT_FOUND);
        Category child = categoryRepository.find(command.getSubCategoryId());
        if (child == null)
            throw new DomainDataServiceException(ERROR_CATEGORY_NOT_FOUND);
        parent.getChilds().remove(child);
        categoryRepository.delete(child);
        categoryRepository.store(parent);
    }
    
    public void removeTopLevelCategory(CommandRemoveTopLevelCategory command) throws DomainDataServiceException {
        Category topLevel = categoryRepository.find(command.getCategoryId());
        if (command == null)
            throw new DomainDataServiceException(ERROR_CATEGORY_NOT_FOUND);
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

    public void createSubcategory(Long categoryId, String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
