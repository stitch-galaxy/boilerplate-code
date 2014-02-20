package com.stitchgalaxy.dto;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.stitchgalaxy.domain.Category;
import com.stitchgalaxy.service.DataMapper;
import com.stitchgalaxy.service.TestData;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author tarasev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring/spring-context-datamapper.xml")
public class MappingTest {
    
    @Autowired
    protected DataMapper mapper;
    
    public MappingTest() {
    }
    
    @Test
    public void testCase()
    {
        Category l1 = new Category(null, "l1");
        l1.setId(1l);
        
        Category l2 = new Category(l1, "l2");
        l2.setId(2l);
        l1.getChilds().add(l2);
        
        Category l3_1 = new Category(l2, "l3_1");
        l3_1.setId(3l);
        l2.getChilds().add(l3_1);
        
        Category l3_2 = new Category(l2, "l3_2");
        l3_2.setId(4l);
        l2.getChilds().add(l3_2);
        
        
        CategoryInfoDTO dto = mapper.getCategoryInfoDTO(l2);
    }
    
}
