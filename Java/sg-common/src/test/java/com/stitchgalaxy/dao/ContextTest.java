package com.stitchgalaxy.dao;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.stitchgalaxy.domain.Category;
import com.stitchgalaxy.infrastructure.persistence.CategoryRepositoryHibernate;
import com.stitchgalaxy.service.DomainDataService;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.sql.SQLException;

/**
 *
 * @author tarasev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring/spring-context-*.xml")
public class ContextTest {
    
    @Autowired
    protected DomainDataService dataservice;
    
    public ContextTest() {
    }
    
    @Test
    public void test() throws SQLException, Exception
    {
    }
}
