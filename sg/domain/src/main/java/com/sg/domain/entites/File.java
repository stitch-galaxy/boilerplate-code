/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.domain.entites;

import com.sg.domain.enumerations.FileType;

/**
 *
 * @author tarasev
 */
public class File {

    private Long id;
    private String url;
    private Integer fileType;
    
    public FileType getFileType()
    {
        return FileType.parse(this.fileType);
    }
    
    public void setFileType(FileType fileType)
    {
        this.fileType = fileType.getValue();
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }
}
