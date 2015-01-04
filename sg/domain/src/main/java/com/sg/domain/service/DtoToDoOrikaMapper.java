/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.domain.service;

import org.joda.time.LocalDate;

/**
 *
 * @author Администратор
 */
public class DtoToDoOrikaMapper extends ConfigurableMapper {

    @Override
    public void configure(MapperFactory mapperFactory) {
        mapperFactory.getConverterFactory().registerConverter(new PassThroughConverter(LocalDate.class));
    }

    @Override
    public void configureFactoryBuilder(DefaultMapperFactory.Builder builder) {
    }
}
