/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.domain.entities.jpa;

import com.sg.domain.enumerations.FileType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author tarasev
 */
@Entity(name = "file")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;
    
    @Column(name="url", nullable=false, unique=true)
    private String url;
    
    @Column(name="file_type", nullable=false)
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
