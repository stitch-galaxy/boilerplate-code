/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.spring.configuration;

import org.dozer.DozerBeanMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author tarasev
 */
@Configuration
public class MapperConfig {

    @Bean(name = "mapper")
    public static org.dozer.Mapper mapper() {
        return new DozerBeanMapper();
    }

}
