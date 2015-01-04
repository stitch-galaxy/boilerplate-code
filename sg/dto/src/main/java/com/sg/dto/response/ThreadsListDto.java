/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.dto.response;

import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 *
 * @author tarasev
 */
public class ThreadsListDto {

    public static class ThreadInfo {

        private String code;

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || this.getClass() != obj.getClass()) {
                return false;
            }

            ThreadInfo other = (ThreadInfo) obj;
            return new EqualsBuilder().
                    append(this.getCode(), other.getCode()).
                    isEquals();

        }

        /**
         * @return the code
         */
        public String getCode() {
            return code;
        }

        /**
         * @param code the code to set
         */
        public void setCode(String code) {
            this.code = code;
        }
    }

    private List<ThreadInfo> threads;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        ThreadsListDto other = (ThreadsListDto) obj;
        return new EqualsBuilder().
                append(this.threads, other.threads).
                isEquals();

    }

    /**
     * @return the codes
     */
    public List<ThreadInfo> getThreads() {
        return threads;
    }

    /**
     * @param codes the codes to set
     */
    public void setThreads(List<ThreadInfo> threads) {
        this.threads = threads;
    }

}
