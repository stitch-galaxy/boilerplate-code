package com.stitchgalaxy.service;

import com.stitchgalaxy.domain.Category;
import com.stitchgalaxy.domain.CategoryRepository;
import com.stitchgalaxy.domain.Partner;
import com.stitchgalaxy.domain.PartnerRepository;
import com.stitchgalaxy.domain.Product;
import com.stitchgalaxy.domain.ProductLocalization;
import com.stitchgalaxy.domain.ProductRepository;
import com.stitchgalaxy.dto.CategoryInfoDTO;
import com.stitchgalaxy.dto.CommandAttachProductToCategory;
import com.stitchgalaxy.dto.CommandAttachProductToPartner;
import com.stitchgalaxy.dto.CommandCreatePartner;
import com.stitchgalaxy.dto.CommandCreateProduct;
import com.stitchgalaxy.dto.CommandCreateSubcategory;
import com.stitchgalaxy.dto.CommandDetachProductFromCategory;
import com.stitchgalaxy.dto.CommandDetachProductFromPartner;
import com.stitchgalaxy.dto.CommandGetCategory;
import com.stitchgalaxy.dto.CommandGetPartners;
import com.stitchgalaxy.dto.CommandGetProduct;
import com.stitchgalaxy.dto.CommandGetProductLocalization;
import com.stitchgalaxy.dto.CommandGetProducts;
import com.stitchgalaxy.dto.CommandGetRootCategory;
import com.stitchgalaxy.dto.CommandRemoveProductFile;
import com.stitchgalaxy.dto.CommandRemoveProductLocalization;
import com.stitchgalaxy.dto.CommandRemoveSubcategory;
import com.stitchgalaxy.dto.CommandStoreProductLocalization;
import com.stitchgalaxy.dto.CommandUploadProductFile;
import com.stitchgalaxy.dto.PartnerInfo;
import com.stitchgalaxy.dto.ProductInfo;
import com.stitchgalaxy.dto.ProductLocalizationInfo;
import java.util.LinkedList;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 * Hello world!
 *
 */
@Transactional
public class DomainDataService {

    private static final String ERROR_CATEGORY_NOT_FOUND = "Category not found";
    private static final String ERROR_PRODUCT_NOT_FOUND = "Product not found";
    private static final String ERROR_PARTNER_NOT_FOUND = "Partner not found";
    private static final String ERROR_PRODUCT_LOCALIZATION_NOT_FOUND = "Localization not found";
    private static final String ERROR_PRODUCT_TRANSLATOR_NOT_ASSIGNED = "Translator not assigned";
    private static final String ERROR_PRODUCT_AUTHOR_NOT_ASSIGNED = "Author not assigned";
    private static final String ERROR_NO_SUCH_RESOURCE_TYPE = "Such resource does not exsist";

    private static final String URL_PATTERN_COMPLETE_PRODUCT = "%d-cp.png";
    private static final String URL_PATTERN_DESIGN = "%d-design";
    private static final String URL_PATTERN_IMAGE = "%d-image.pmg";
    private static final String URL_PATTERN_PROTOTYPE = "%d-prototype.png";
    private static final String URL_PATTERN_THUMBNAIL = "%d-thumbnail.png";

    private DataMapper dataMapper;
    private CategoryRepository categoryRepository;
    private PartnerRepository partnerRepository;
    private ProductRepository productRepository;

    public void setDataMapper(DataMapper dataMapper) {
        this.dataMapper = dataMapper;
    }

    public void setPartnerRepository(PartnerRepository partnerRepository) {
        this.partnerRepository = partnerRepository;
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
                productRepository.store(product);
                return;
            }
        }
        throw new DomainDataServiceException(ERROR_PRODUCT_LOCALIZATION_NOT_FOUND);
    }

    public void storeProductLocalization(CommandStoreProductLocalization command) throws DomainDataServiceException {
        Product product = productRepository.find(command.getProductId());
        if (product == null) {
            throw new DomainDataServiceException(ERROR_PRODUCT_NOT_FOUND);
        }
        ProductLocalization localization = dataMapper.getProductLocalization(command.getProductLocalization());
        for(ProductLocalization l : product.getLocalizations())
        {
            if (l.getLocale().equals(localization.getLocale()))
            {
                product.getLocalizations().remove(l);
                break;
            }
        }
        product.getLocalizations().add(localization);
        productRepository.store(product);
    }

    public ProductLocalizationInfo getProductLocalization(CommandGetProductLocalization command) throws DomainDataServiceException {
        Product product = productRepository.find(command.getProductId());
        if (product == null) {
            throw new DomainDataServiceException(ERROR_PRODUCT_NOT_FOUND);
        }
        for (ProductLocalization localization : product.getLocalizations()) {
            if (localization.getLocale().equals(command.getLocale())) {
                ProductLocalizationInfo dto = dataMapper.getProductLocalizationDTO(localization);
                return dto;
            }
        }
        throw new DomainDataServiceException(ERROR_PRODUCT_LOCALIZATION_NOT_FOUND);
    }

    public CategoryInfoDTO getRootCategory(CommandGetRootCategory command){
        Category root = categoryRepository.getRootCategory();
        if (root == null)
        {
            root = new Category(null, "Root");
            categoryRepository.store(root);
        }
        return dataMapper.getCategoryInfoDTO(root);
    }

    public CategoryInfoDTO getCategoryById(CommandGetCategory command) throws DomainDataServiceException {
        Category category = categoryRepository.find(command.getCategoryId());
        if (category == null) {
            throw new DomainDataServiceException(ERROR_CATEGORY_NOT_FOUND);
        }
        return dataMapper.getCategoryInfoDTO(category);
    }

    public void createSubcategory(CommandCreateSubcategory command) throws DomainDataServiceException {
        Category parent = categoryRepository.find(command.getCategoryId());
        if (parent == null) {
            throw new DomainDataServiceException(ERROR_CATEGORY_NOT_FOUND);
        }
        Category child = new Category(parent, command.getName());
        categoryRepository.store(parent);
    }

    public void removeSubcategory(CommandRemoveSubcategory command) throws DomainDataServiceException {
        Category parent = categoryRepository.find(command.getCategoryId());
        if (parent == null) {
            throw new DomainDataServiceException(ERROR_CATEGORY_NOT_FOUND);
        }
        Category child = categoryRepository.find(command.getSubCategoryId());
        if (child == null) {
            throw new DomainDataServiceException(ERROR_CATEGORY_NOT_FOUND);
        }
        parent.getChilds().remove(child);
        categoryRepository.delete(child);
        categoryRepository.store(parent);
    }

    public void addPartner(CommandCreatePartner command) {
        Partner partner = new Partner();
        partner.setUri(command.getUri());
        partner.setName(command.getName());
        partnerRepository.store(partner);
    }

    public List<PartnerInfo> getAllPartners(CommandGetPartners command) {
        List<Partner> partners = partnerRepository.getPartners();
        List<PartnerInfo> infos = new LinkedList<PartnerInfo>();
        for (Partner p : partners) {
            infos.add(dataMapper.getPartner(p));
        }
        return infos;
    }

    public void addProductToCategory(CommandAttachProductToCategory command) throws DomainDataServiceException {
        Product product = productRepository.find(command.getProductId());
        if (product == null) {
            throw new DomainDataServiceException(ERROR_PRODUCT_NOT_FOUND);
        }
        Category category = categoryRepository.find(command.getCategoryId());
        if (category == null) {
            throw new DomainDataServiceException(ERROR_CATEGORY_NOT_FOUND);
        }
        product.getCategories().add(category);
        productRepository.store(product);
    }

    public void removeProductFromCategory(CommandDetachProductFromCategory command) throws DomainDataServiceException {
        Product product = productRepository.find(command.getProductId());
        if (product == null) {
            throw new DomainDataServiceException(ERROR_PRODUCT_NOT_FOUND);
        }
        Category category = categoryRepository.find(command.getCategoryId());
        if (category == null) {
            throw new DomainDataServiceException(ERROR_CATEGORY_NOT_FOUND);
        }
        product.getCategories().remove(category);
        productRepository.store(product);
    }

    public void productAssignTranslator(CommandAttachProductToPartner command) throws DomainDataServiceException {
        Product product = productRepository.find(command.getProductId());
        if (product == null) {
            throw new DomainDataServiceException(ERROR_PRODUCT_NOT_FOUND);
        }
        Partner partner = partnerRepository.find(command.getPartnerId());
        if (partner == null) {
            throw new DomainDataServiceException(ERROR_PARTNER_NOT_FOUND);
        }
        product.setTranslator(partner);
        productRepository.store(product);
    }

    public void removeProductTranslator(CommandDetachProductFromPartner command) throws DomainDataServiceException {
        Product product = productRepository.find(command.getProductId());
        if (product == null) {
            throw new DomainDataServiceException(ERROR_PRODUCT_NOT_FOUND);
        }
        if (product.getTranslator() == null) {
            throw new DomainDataServiceException(ERROR_PRODUCT_TRANSLATOR_NOT_ASSIGNED);
        }
        product.setTranslator(null);
        productRepository.store(product);
    }

    public void productAssignAuthor(CommandAttachProductToPartner command) throws DomainDataServiceException {
        Product product = productRepository.find(command.getProductId());
        if (product == null) {
            throw new DomainDataServiceException(ERROR_PRODUCT_NOT_FOUND);
        }
        Partner partner = partnerRepository.find(command.getPartnerId());
        if (partner == null) {
            throw new DomainDataServiceException(ERROR_PARTNER_NOT_FOUND);
        }
        product.setAuthor(partner);
        productRepository.store(product);
    }

    public void removeProductAuthor(CommandDetachProductFromPartner command) throws DomainDataServiceException {
        Product product = productRepository.find(command.getProductId());
        if (product == null) {
            throw new DomainDataServiceException(ERROR_PRODUCT_NOT_FOUND);
        }
        if (product.getAuthor()== null) {
            throw new DomainDataServiceException(ERROR_PRODUCT_AUTHOR_NOT_ASSIGNED);
        }
        product.setAuthor(null);
        productRepository.store(product);
    }

    public void uploadProductFile(CommandUploadProductFile command) throws DomainDataServiceException {
        Product product = productRepository.find(command.getProductId());
        if (product == null) {
            throw new DomainDataServiceException(ERROR_PARTNER_NOT_FOUND);
        }
        String uri;
        //TODO: get container uri
        StringBuilder sb = new StringBuilder("CONTAINTERURI");
        switch (command.getFileType()) {
            case COMPLETE_PRODUCT:
                uri = String.format(URL_PATTERN_COMPLETE_PRODUCT, product.getId());
                product.setCompleteProductUri(sb.append(uri).toString());
                break;
            case DESIGN:
                uri = String.format(URL_PATTERN_DESIGN, product.getId());
                product.setFileUri(sb.append(uri).toString());
                break;
            case IMAGE:
                uri = String.format(URL_PATTERN_IMAGE, product.getId());
                product.setLargeImageUri(sb.append(uri).toString());
                break;
            case PROTOTYPE:
                uri = String.format(URL_PATTERN_PROTOTYPE, product.getId());
                product.setPrototypeUri(sb.append(uri).toString());
                break;
            case THUMBNAIL:
                uri = String.format(URL_PATTERN_THUMBNAIL, product.getId());
                product.setThumbnailUri(sb.append(uri).toString());
                break;
            default:
                throw new DomainDataServiceException(ERROR_NO_SUCH_RESOURCE_TYPE);
        }
        //TODO: add or update CDN content
        productRepository.store(product);
    }

    public void removeProductFile(CommandRemoveProductFile command) throws DomainDataServiceException {
        Product product = productRepository.find(command.getProductId());
        if (product == null) {
            throw new DomainDataServiceException(ERROR_PARTNER_NOT_FOUND);
        }
        String uri;
        //TODO: get container uri
        StringBuilder sb = new StringBuilder("CONTAINTERURI");
        switch (command.getFileType()) {
            case COMPLETE_PRODUCT:
                uri = String.format(URL_PATTERN_COMPLETE_PRODUCT, product.getId());
                product.setCompleteProductUri(null);
                break;
            case DESIGN:
                uri = String.format(URL_PATTERN_DESIGN, product.getId());
                product.setFileUri(null);
                break;
            case IMAGE:
                uri = String.format(URL_PATTERN_IMAGE, product.getId());
                product.setLargeImageUri(null);
                break;
            case PROTOTYPE:
                uri = String.format(URL_PATTERN_PROTOTYPE, product.getId());
                product.setPrototypeUri(null);
                break;
            case THUMBNAIL:
                uri = String.format(URL_PATTERN_THUMBNAIL, product.getId());
                product.setThumbnailUri(null);
                break;
            default:
                throw new DomainDataServiceException(ERROR_NO_SUCH_RESOURCE_TYPE);
        }
        sb.append(uri);
        //TODO: remove CDN content
        productRepository.store(product);
    }

    public List<ProductInfo> getAllProducts(CommandGetProducts command) {
        List<Product> products = productRepository.getProducts();
        List<ProductInfo> dtos = new LinkedList<ProductInfo>();
        for (Product p : products) {
            dtos.add(dataMapper.getProductInfoDto(p));
        }
        return dtos;
    }

    public ProductInfo getProductById(CommandGetProduct command) throws DomainDataServiceException {
        Product product = productRepository.find(command.getProductId());
        if (product == null) {
            throw new DomainDataServiceException(ERROR_PRODUCT_NOT_FOUND);
        }
        ProductInfo info = dataMapper.getProductInfoDto(product);
        return info;
    }

    public void createNewProduct(CommandCreateProduct command){
        Product product = new Product();
        product.setName(command.getName());
        product.setPriceUsd(command.getPrice());
        product.setDate(command.getDate());
        productRepository.store(product);
    }
    

    public void storeProductData(ProductInfo product) {
    }
}
