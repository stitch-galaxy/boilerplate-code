package com.sg.domain.test.jpa;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.sg.constants.Roles;
import com.sg.dto.CanvasDto;
import com.sg.dto.CanvasRefDto;
import com.sg.dto.CanvasUpdateDto;
import com.sg.dto.ThreadDto;
import com.sg.dto.ThreadRefDto;
import com.sg.dto.ThreadUpdateDto;
import com.sg.domain.service.SgService;
import com.sg.domain.spring.configuration.JpaContext;
import com.sg.domain.spring.configuration.JpaServiceContext;
import com.sg.domain.spring.configuration.MapperContext;
import com.sg.dto.AccountDto;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import org.joda.time.LocalDate;
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
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {JpaContext.class, MapperContext.class, JpaServiceContext.class})
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
        accountsTest();
    }
    
    private static final String AIDA_14 = "Aida 14";
    private static final String AIDA_18 = "Aida 18";
    private static final String AIDA_15 = "Aida 15";
    
    private static final String DMC = "DMC";
    private static final String ANCHOR = "Anchor";
    private static final String DMC_ERROR = "DMC error";
    
    private void canvasesTest()
    {
        List<CanvasDto> list = new ArrayList<CanvasDto>();
        
        CanvasDto dto = new CanvasDto();
        dto.setCode(AIDA_15);
        dto.setStitchesPerInch(new BigDecimal(15));
        service.create(dto);
        list.add(dto);
        
        dto = new CanvasDto();
        dto.setCode(AIDA_18);
        dto.setStitchesPerInch(new BigDecimal(18));
        service.create(dto);
        list.add(dto);
        
        Assert.assertEquals(list, service.listCanvases());
        
        CanvasUpdateDto updateDto = new CanvasUpdateDto();
        CanvasRefDto refDto = new CanvasRefDto();
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
        
        list = new ArrayList<CanvasDto>();
        list.add(dto);
        Assert.assertEquals(list, service.listCanvases());
    }
    

    private void threadsTest() {
        List<ThreadDto> list = new ArrayList<ThreadDto>();
        
        ThreadDto dto = new ThreadDto();
        dto.setCode(DMC_ERROR);
        service.create(dto);
        list.add(dto);
        dto = new ThreadDto();
        dto.setCode(ANCHOR);
        service.create(dto);
        list.add(dto);
        
        Assert.assertEquals(list, service.listThreads());
        
        ThreadUpdateDto updateDto = new ThreadUpdateDto();
        ThreadRefDto refDto = new ThreadRefDto();
        dto = new ThreadDto();
        
        refDto.setCode(DMC_ERROR);
        
        dto.setCode(DMC);
        
        updateDto.setDto(dto);
        updateDto.setRef(refDto);
        
        service.update(updateDto);
        
        refDto = new ThreadRefDto();
        refDto.setCode(ANCHOR);
        
        service.delete(refDto);
        
        list = new ArrayList<ThreadDto>();
        list.add(dto);
        
        Assert.assertEquals(list, service.listThreads());
    }
    
    private void accountsTest()
    {
        AccountDto dto = new AccountDto();
        dto.setEmail(USER_EMAIL);
        dto.setEmailVerified(Boolean.FALSE);
        List<String> roles = new ArrayList<String>();
        roles.add(Roles.ROLE_ADMIN);
        roles.add(Roles.ROLE_USER);
        dto.setRoles(roles);
        dto.setUserBirthDate(USER_BIRTH_DATE);
        dto.setUserFirstName(USER_FIRST_NAME);
        dto.setUserLastName(USER_LAST_NAME);
        
        service.create(dto);
        
        AccountDto found = service.getUserByEmail(USER_EMAIL);
        
        Assert.assertEquals(dto, found);
    }
    private static final String USER_LAST_NAME = "Tarasova";
    private static final String USER_FIRST_NAME = "Nadezhda";
    private static final LocalDate USER_BIRTH_DATE = LocalDate.parse("1985-01-28");
    private static final String USER_EMAIL = "test@example.com";
    
}
