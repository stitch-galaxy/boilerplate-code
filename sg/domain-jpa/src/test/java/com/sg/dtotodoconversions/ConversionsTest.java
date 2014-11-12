/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.dtotodoconversions;

import com.sg.domain.service.mapping.components.MapperComponent;
import ma.glasnost.orika.MapperFacade;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author tarasev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ConversionsTest {

    @Configuration
    @ComponentScan(basePackageClasses = {MapperComponent.class})
    public static class ConversionTestContext {
    }

    public enum TestEnum {

        ONE, TWO;

    }

    public static class DomainObjectWrapper {

        private TestEnum testEnum;

        /**
         * @return the testEnum
         */
        public TestEnum getTestEnum() {
            return testEnum;
        }

        /**
         * @param testEnum the testEnum to set
         */
        public void setTestEnum(TestEnum testEnum) {
            this.testEnum = testEnum;
        }
    }

    public static class DtoObjectWrapper {

        private String testEnum;

        /**
         * @return the testEnum
         */
        public String getTestEnum() {
            return testEnum;
        }

        /**
         * @param testEnum the testEnum to set
         */
        public void setTestEnum(String testEnum) {
            this.testEnum = testEnum;
        }

    }

    @Autowired
    private MapperFacade mapper;

    @Test
    public void testEnumConversions() {
        Assert.assertNotNull(mapper);

        DomainObjectWrapper domainObject = new DomainObjectWrapper();
        domainObject.setTestEnum(ConversionsTest.TestEnum.ONE);

        DtoObjectWrapper dto = mapper.map(domainObject, DtoObjectWrapper.class);

        Assert.assertEquals("ONE", dto.getTestEnum());

        domainObject = mapper.map(dto, DomainObjectWrapper.class);

        Assert.assertEquals(domainObject.getTestEnum(), ConversionsTest.TestEnum.ONE);
    }
}
