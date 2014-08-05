/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.domain.service;

import com.sg.domain.dto.ThreadDto;
import com.sg.domain.dto.ThreadRefDto;
import com.sg.domain.dto.ThreadUpdateDto;
import java.util.List;

/**
 *
 * @author tarasev
 */
public interface SgService {
    
    public void createThread(ThreadDto thread);
    public void deleteThread(ThreadRefDto thread);
    public void updateThread(ThreadUpdateDto thread);
    public List<ThreadDto> listThreads();   
}
