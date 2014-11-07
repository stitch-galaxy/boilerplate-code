/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.service.jpa.spring;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.builtin.PassThroughConverter;
import ma.glasnost.orika.impl.ConfigurableMapper;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.joda.time.LocalDate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author tarasev
 */
@Configuration
public class DtoDomainObjectsMapperContextConfig {

    @Bean
    public MyCustomMapper mapper() {
        return new MyCustomMapper();
    }

    public static class MyCustomMapper extends ConfigurableMapper {

        @Override
        public void configure(MapperFactory mapperFactory) {
            mapperFactory.getConverterFactory().registerConverter(new PassThroughConverter(LocalDate.class));
        }

        @Override
        public void configureFactoryBuilder(DefaultMapperFactory.Builder builder) {
        }
    }

}
