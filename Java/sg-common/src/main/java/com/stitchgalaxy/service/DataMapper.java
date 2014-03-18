/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stitchgalaxy.service;

import com.stitchgalaxy.domain.Category;
import com.stitchgalaxy.domain.Partner;
import com.stitchgalaxy.domain.Product;
import com.stitchgalaxy.domain.ProductLocalization;
import com.stitchgalaxy.dto.CategoryInfo;
import com.stitchgalaxy.dto.CategoryInfoDTO;
import com.stitchgalaxy.dto.PartnerInfo;
import com.stitchgalaxy.dto.ProductInfo;
import com.stitchgalaxy.dto.ProductLocalizationInfo;
import java.util.List;
import org.dozer.DozerBeanMapper;

/**
 *
 * @author tarasev
 */
public class DataMapper {

    private DozerBeanMapper mapper;

    public void setDataMapper(DozerBeanMapper mapper) {
        this.mapper = mapper;
    }

    public CategoryInfoDTO getCategoryInfoDTO(Category category) {
        CategoryInfoDTO destObject = new CategoryInfoDTO();
        CategoryInfo current = mapper.map(category, CategoryInfo.class);
        destObject.setCurrent(current);

        if (category.getParent() != null) {
            CategoryInfo parrent = mapper.map(category.getParent(), CategoryInfo.class);
            destObject.setParent(parrent);
        }

        if (category.getChilds() != null) {
            for (Category c : category.getChilds()) {
                CategoryInfo child = mapper.map(c, CategoryInfo.class);
                destObject.getChilds().add(child);
            }
        }
        return destObject;
    }

    public ProductLocalizationInfo getProductLocalizationDTO(ProductLocalization localization) {
        ProductLocalizationInfo dto = mapper.map(localization, ProductLocalizationInfo.class);
        return dto;
    }

    public ProductLocalization getProductLocalization(ProductLocalizationInfo dto) {
        ProductLocalization localization = mapper.map(dto, ProductLocalization.class);
        return localization;
    }

    public PartnerInfo getPartner(Partner partner) {
        PartnerInfo dto = mapper.map(partner, PartnerInfo.class);
        return dto;
    }

    ProductInfo getProductInfoDto(Product product) {
        ProductInfo dto = mapper.map(product, ProductInfo.class);
        for (Category c : product.getCategories()) {
            dto.getCategories().add(getCategoryInfoDTO(c));
        }
        for (ProductLocalization l : product.getLocalizations()) {
            dto.getLocalizations().add(getProductLocalizationDTO(l));
        }
        if (product.getAuthor() != null) {
            dto.setAuthor(getPartner(product.getAuthor()));
        }
        if (product.getTranslator() != null) {
            dto.setTranslator(getPartner(product.getTranslator()));
        }
        return dto;
    }
}
