/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.mapper;

import com.sg.domain.service.DtoDoMapper;
import com.sg.domain.service.DtoToDoOrikaMapper;
import ma.glasnost.orika.MapperFacade;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author tarasev
 */
public class ConversionsTest {

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

    @Test
    public void testEnumConversions() {
        DtoDoMapper mapper = new DtoToDoOrikaMapper();

        DomainObjectWrapper domainObject = new DomainObjectWrapper();
        domainObject.setTestEnum(ConversionsTest.TestEnum.ONE);

        DtoObjectWrapper dto = mapper.map(domainObject, DtoObjectWrapper.class);

        Assert.assertEquals("ONE", dto.getTestEnum());

        domainObject = mapper.map(dto, DomainObjectWrapper.class);

        Assert.assertEquals(domainObject.getTestEnum(), ConversionsTest.TestEnum.ONE);
    }
}
