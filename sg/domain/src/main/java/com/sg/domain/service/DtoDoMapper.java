/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.domain.service;

/**
 *
 * @author Администратор
 */
public interface DtoDoMapper {
    public <S extends Object, D extends Object> D map(S s, Class<D> type);
    public <S extends Object, D extends Object> void map(S s, D d);
}
