package com.stitchgalaxy.dao;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.stitchgalaxy.domain.Category;
import com.stitchgalaxy.dto.*;
import com.stitchgalaxy.infrastructure.persistence.CategoryRepositoryHibernate;
import com.stitchgalaxy.service.DataMapper;
import com.stitchgalaxy.service.TestData;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.apache.commons.dbcp.BasicDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author tarasev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring/spring-context-persistence-test.xml")
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
        Category parent = new Category();
        parent.setName("parent");
        categoryRepository.store(parent);
        
        
        Category child1 = new Category();
        child1.setName("child1");
        
        
        Category child2 = new Category();
        child2.setName("child2");
        
        
        
        child1.setParent(parent);
        child2.setParent(parent);
        parent.getChilds().add(child1);
        parent.getChilds().add(child2);
        
        
        categoryRepository.store(child1);
        categoryRepository.store(child2);
        categoryRepository.store(parent);
        
        Category found = categoryRepository.find(parent.getId());
        assertSame(found, parent);
    }
}
