/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stitchgalaxy.dto;

import java.io.InputStream;

/**
 *
 * @author tarasev
 */
public class CommandUploadProductFile {
    
    private Long productId;
    private InputStream fileContent;
    private FileType fileType;

    /**
     * @return the fileType
     */
    public FileType getFileType() {
        return fileType;
    }

    /**
     * @param fileType the fileType to set
     */
    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    /**
     * @return the productId
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * @param productId the productId to set
     */
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    /**
     * @return the fileContent
     */
    public InputStream getFileContent() {
        return fileContent;
    }

    /**
     * @param fileContent the fileContent to set
     */
    public void setFileContent(InputStream fileContent) {
        this.fileContent = fileContent;
    }
}
