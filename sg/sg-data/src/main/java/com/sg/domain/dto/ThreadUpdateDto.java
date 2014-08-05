/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.domain.dto;

/**
 *
 * @author tarasev
 */
public class ThreadUpdateDto {
    
    private ThreadRefDto threadRef;
    private ThreadDto thread;

    /**
     * @return the threadRef
     */
    public ThreadRefDto getThreadRef() {
        return threadRef;
    }

    /**
     * @param threadRef the threadRef to set
     */
    public void setThreadRef(ThreadRefDto threadRef) {
        this.threadRef = threadRef;
    }

    /**
     * @return the thread
     */
    public ThreadDto getThread() {
        return thread;
    }

    /**
     * @param thread the thread to set
     */
    public void setThread(ThreadDto thread) {
        this.thread = thread;
    }
    
}
