/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.sg_rest_api.test;

import com.sg.domain.service.SgService;
import com.sg.sg_rest_api.configuration.WebConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

/**
 *
 * @author tarasev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestServiceContext.class, WebConfig.class})
@WebAppConfiguration
public class ThreadsControllerTest {
    private MockMvc mockMvc;
    
    @Autowired 
    private SgService service;
    
    @Test
    public void test(){
    }
}
