/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.domain.service;

import com.sg.domain.dto.ThreadDto;
import java.util.List;

/**
 *
 * @author tarasev
 */
public interface SgService {
    
    public void addThread(ThreadDto thread);
    public List<ThreadDto> getAllThreads();
    
}
