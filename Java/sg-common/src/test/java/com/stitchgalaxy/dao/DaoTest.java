package com.stitchgalaxy.dao;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.stitchgalaxy.domain.Category;
import com.stitchgalaxy.infrastructure.persistence.CategoryRepositoryHibernate;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.sql.SQLException;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author tarasev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring/test-spring-context-persistence.xml")
@Transactional
//@TransactionConfiguration(defaultRollback = false)
public class DaoTest {
    
    @Autowired
    protected CategoryRepositoryHibernate categoryRepository;
    
    public DaoTest() {
    }
    
    @Test
    public void test() throws SQLException, Exception
    {
        Category l1 = new Category(null, "l1");
        Category l1_l2_1 = new Category(l1, "l1_l2_1");
        l1.getChilds().add(l1_l2_1);
        Category l1_l2_2 = new Category(l1, "l1_l2_2");
        l1.getChilds().add(l1_l2_2);
        
        categoryRepository.store(l1);
        
        Category found = categoryRepository.find(l1_l2_1.getId());
        assertEquals(found, l1_l2_1);
    }
}
