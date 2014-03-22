package com.stitchgalaxy.dao;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.stitchgalaxy.domain.Category;
import com.stitchgalaxy.infrastructure.persistence.CategoryRepositoryHibernate;
import java.sql.SQLException;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author tarasev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring/test-spring-context-persistence.xml")
@Transactional
public class DaoTest {
    
    @Autowired
    protected CategoryRepositoryHibernate categoryRepository;
    
    public DaoTest() {
    }
    
    @Test
    public void testStoreCascade() throws SQLException, Exception
    {
        Category l1 = new Category(null, "l1");
        Category l1_l2_1 = new Category(l1, "l1_l2_1");
        Category l1_l2_2 = new Category(l1, "l1_l2_2");
        
        categoryRepository.store(l1);
        
        Category found = categoryRepository.find(l1_l2_1.getId());
        assertEquals(found, l1_l2_1);
    }
    
    @Test
    public void testFindTopLevel() throws SQLException, Exception
    {
        Category l1 = new Category(null, "l1_1");
        Category l2 = new Category(null, "l1_2");
        
        categoryRepository.store(l1);
        categoryRepository.store(l2);
        
        List<Category> topLevelCategories = categoryRepository.getTopLeveCategories();
        assertEquals(topLevelCategories.size(), 2);
    }
}
