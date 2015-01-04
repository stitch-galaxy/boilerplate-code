/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.domain.repository;

import com.sg.domain.entites.Canvas;

/**
 *
 * @author Администратор
 */
public interface CanvasRepository {
    public Canvas findByCode(String code);
    public Iterable<Canvas> findAll();
    public void save(Canvas canvas);
    public void delete(Canvas canvas);
}
