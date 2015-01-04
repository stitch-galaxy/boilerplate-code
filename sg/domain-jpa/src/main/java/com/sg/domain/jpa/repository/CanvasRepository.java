/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.domain.jpa.repository;

import com.sg.domain.entities.jpa.Canvas;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author tarasev
 */
@Repository
public interface CanvasRepository extends CrudRepository<Canvas, Integer> {
    Canvas findByCode(String code);
}
