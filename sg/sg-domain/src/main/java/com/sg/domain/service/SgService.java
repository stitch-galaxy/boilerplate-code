/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.domain.service;

import com.sg.dto.CanvasDto;
import com.sg.dto.CanvasRefDto;
import com.sg.dto.CanvasUpdateDto;
import com.sg.dto.ThreadDto;
import com.sg.dto.ThreadRefDto;
import com.sg.dto.ThreadUpdateDto;
import com.sg.dto.UserDto;
import java.util.List;

/**
 *
 * @author tarasev
 */
public interface SgService {
    
    public void create(ThreadDto dto);
    public void delete(ThreadRefDto dto);
    public void update(ThreadUpdateDto dto);
    public List<ThreadDto> listThreads();   
    
    public void create(CanvasDto dto);
    public void delete(CanvasRefDto dto);
    public void update(CanvasUpdateDto dto);
    public List<CanvasDto> listCanvases();   
    
    public UserDto getUserByEmail(String email);
    
}
