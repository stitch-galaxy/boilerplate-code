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
@TransactionConfiguration(defaultRollback = false)
public class DaoTest {
    
    @Autowired
    protected CategoryRepositoryHibernate categoryRepository;
    
    public DaoTest() {
    }
    
    @Test
    public void testCase2() throws SQLException, Exception
    {
        Category category = new Category();
        category.setName("test");
        category.setId(1l);
        categoryRepository.store(category);
    }
    
    @Test
    public void testCase0() throws SQLException, Exception
    {
        Category test = categoryRepository.find(1l);
        int i = 0;
    }
    
}
