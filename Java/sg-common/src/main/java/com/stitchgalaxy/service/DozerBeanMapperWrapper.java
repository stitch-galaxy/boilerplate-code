/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stitchgalaxy.service;

import com.stitchgalaxy.dto.CategoryInfoDTO;
import java.util.ArrayList;
import java.util.List;
import org.dozer.DozerBeanMapper;

/**
 *
 * @author tarasev
 */
public class DozerBeanMapperWrapper {
    
    private DozerBeanMapper mapper;
    
    private DozerBeanMapperWrapper()
    {
        List myMappingFiles = new ArrayList();
        myMappingFiles.add("dozer/dozer-global-configuration.xml");
        myMappingFiles.add("dozer/dozer-bean-mapper-category.xml");
        mapper = new DozerBeanMapper();
        mapper.setMappingFiles(myMappingFiles);
    }
    
    public DozerBeanMapper getMapper()
    {
        return mapper;
    }
    
    private static volatile DozerBeanMapperWrapper instance;
    
    public static DozerBeanMapperWrapper getInstance()
    {
        DozerBeanMapperWrapper localInstance = instance;
        if (localInstance == null)
        {
            synchronized (DozerBeanMapperWrapper.class)
            {
                localInstance = instance;
                if (localInstance == null)
                {
                    instance = localInstance = new DozerBeanMapperWrapper();
                }
            }
        }
        return localInstance;
    }
    
}
