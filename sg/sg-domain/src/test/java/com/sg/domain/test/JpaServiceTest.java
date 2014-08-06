package com.sg.domain.test;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.sg.domain.dto.CanvasDto;
import com.sg.domain.dto.CanvasRefDto;
import com.sg.domain.dto.CanvasUpdateDto;
import com.sg.domain.dto.ThreadDto;
import com.sg.domain.dto.ThreadRefDto;
import com.sg.domain.dto.ThreadUpdateDto;
import com.sg.domain.service.SgService;
import com.sg.domain.spring.configuration.JpaConfig;
import com.sg.domain.spring.configuration.JpaServiceConfig;
import com.sg.domain.spring.configuration.MapperConfig;
import java.math.BigDecimal;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 *
 * @author tarasev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {JpaConfig.class, MapperConfig.class, JpaServiceConfig.class})
public class JpaServiceTest {
    
    @Autowired
    SgService service;
            
    public JpaServiceTest() {
    }
    
    @Test
    public void testServiceLayer()
    {
        canvasesTest();
        threadsTest();
    }
    
    private static final String AIDA_14 = "Aida 14";
    private static final String AIDA_18 = "Aida 18";
    private static final String AIDA_15 = "Aida 15";
    
    private static final String DMC = "DMC";
    private static final String ANCHOR = "Anchor";
    private static final String DMC_ERROR = "DMC error";
    
    private void canvasesTest()
    {
        CanvasDto dto;
        CanvasUpdateDto updateDto;
        CanvasRefDto refDto;
        List<CanvasDto> list;
        
        dto = new CanvasDto();
        dto.setCode(AIDA_15);
        dto.setStitchesPerInch(new BigDecimal(15));
        service.create(dto);
        
        dto = new CanvasDto();
        dto.setCode(AIDA_18);
        dto.setStitchesPerInch(new BigDecimal(18));
        service.create(dto);
        
        list = service.listCanvases();
        Assert.assertEquals(2, list.size());
        
        updateDto = new CanvasUpdateDto();
        refDto = new CanvasRefDto();
        dto = new CanvasDto();
        
        refDto.setCode(AIDA_15);
        
        dto.setCode(AIDA_14);
        dto.setStitchesPerInch(new BigDecimal(14));
        
        updateDto.setDto(dto);
        updateDto.setRef(refDto);
        
        service.update(updateDto);
        
        refDto = new CanvasRefDto();
        refDto.setCode(AIDA_18);
        
        service.delete(refDto);
        
        list = service.listCanvases();
        Assert.assertEquals(1, list.size());

        dto = list.get(0);
        Assert.assertEquals(AIDA_14, dto.getCode());
        Assert.assertEquals(dto.getStitchesPerInch(), new BigDecimal(14));
    }
    

    private void threadsTest() {
        ThreadDto dto;
        ThreadUpdateDto updateDto;
        ThreadRefDto refDto;
        List<ThreadDto> list;
        
        dto = new ThreadDto();
        dto.setCode(DMC_ERROR);
        service.create(dto);
        
        dto = new ThreadDto();
        dto.setCode(ANCHOR);
        service.create(dto);
        
        list = service.listThreads();
        Assert.assertEquals(2, list.size());
        
        updateDto = new ThreadUpdateDto();
        refDto = new ThreadRefDto();
        dto = new ThreadDto();
        
        refDto.setCode(DMC_ERROR);
        
        dto.setCode(DMC);
        
        updateDto.setDto(dto);
        updateDto.setRef(refDto);
        
        service.update(updateDto);
        
        refDto = new ThreadRefDto();
        refDto.setCode(ANCHOR);
        
        service.delete(refDto);
        
        list = service.listThreads();
        Assert.assertEquals(1, list.size());

        dto = list.get(0);
        Assert.assertEquals(DMC, dto.getCode());
    }
    
}
