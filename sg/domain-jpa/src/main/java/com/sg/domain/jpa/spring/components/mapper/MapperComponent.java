/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.jpa.spring.components.mapper;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.builtin.PassThroughConverter;
import ma.glasnost.orika.impl.ConfigurableMapper;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;

/**
 *
 * @author tarasev
 */
@Component
public class MapperComponent extends ConfigurableMapper {

    @Override
    public void configure(MapperFactory mapperFactory) {
        mapperFactory.getConverterFactory().registerConverter(new PassThroughConverter(LocalDate.class));
    }

    @Override
    public void configureFactoryBuilder(DefaultMapperFactory.Builder builder) {
    }
}
