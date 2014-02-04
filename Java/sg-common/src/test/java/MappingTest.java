/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
@ContextConfiguration("spring/datamapper-config.xml")
public class MappingTest {
    
    @Autowired
    protected DataMapper mapper;
    
    public MappingTest() {
    }
    
    @Test
    public void testCase()
    {
        mapper.getCategoryInfoDTO(TestData.createProductCategory());
    }
    
}
