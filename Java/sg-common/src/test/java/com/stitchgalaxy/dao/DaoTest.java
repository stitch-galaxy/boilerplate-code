package com.stitchgalaxy.dao;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.stitchgalaxy.dto.*;
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

/**
 *
 * @author tarasev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring/spring-context-persistence-test.xml")
public class DaoTest {
    
    @Autowired
    protected BasicDataSource datasource;
    
    public DaoTest() {
    }
    
    @Test
    public void testCase() throws SQLException
    {
        Connection test = datasource.getConnection();
        test.close();
    }
    
}