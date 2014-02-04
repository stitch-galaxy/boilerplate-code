/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stitchgalaxy.service;

import com.stitchgalaxy.domain.Category;
import com.stitchgalaxy.dto.CategoryInfo;
import com.stitchgalaxy.dto.CategoryInfoDTO;
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
    
    public CategoryInfoDTO getCategoryInfoDTO(Category category)
    {
        CategoryInfoDTO destObject = new CategoryInfoDTO();
        CategoryInfo current = mapper.map(category, CategoryInfo.class);
        destObject.setCurrent(current);
        
        if (category.getParent() != null)
        {
            CategoryInfo parrent = mapper.map(category.getParent(), CategoryInfo.class);
            destObject.setParent(parrent);
        }
        
        if (category.getChilds() != null)
        {
            for (Category c : category.getChilds()) {
                CategoryInfo child = mapper.map(category.getParent(), CategoryInfo.class);
                destObject.getChilds().add(child);
            }
        }
        return destObject;
    }
}
