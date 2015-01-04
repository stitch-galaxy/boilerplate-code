/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.domain.service;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.builtin.PassThroughConverter;
import ma.glasnost.orika.impl.ConfigurableMapper;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.joda.time.LocalDate;

/**
 *
 * @author Администратор
 */
public class DtoToDoOrikaMapper extends ConfigurableMapper implements DtoDoMapper {

    @Override
    public <S extends Object, D extends Object> D map(S s, Class<D> type)
    {
        return super.map(s, type);
    }
    
    @Override
    public <S extends Object, D extends Object> void map(S s, D d)
    {
        super.map(s, d);
    }
    
    @Override
    public void configure(MapperFactory mapperFactory) {
        mapperFactory.getConverterFactory().registerConverter(new PassThroughConverter(LocalDate.class));
    }

    @Override
    public void configureFactoryBuilder(DefaultMapperFactory.Builder builder) {
    }
}
