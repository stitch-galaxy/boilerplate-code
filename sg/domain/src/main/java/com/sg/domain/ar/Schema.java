/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.ar;

import com.sg.domail.vo.FileUrl;
import com.sg.domail.vo.Locale;
import com.sg.domail.vo.SchemaId;
import com.sg.domail.vo.SchemaParameters;
import com.sg.domail.vo.TextField;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author tarasev
 */
public class Schema {

    private SchemaId id;
    private SchemaParameters parameters;
    private TextField name;
    private TextField description;
    private FileUrl image;
    private FileUrl thumbnail;
    private final Map<Locale, FileUrl> pdfSchemas;

    public Schema() {
        pdfSchemas = new HashMap<Locale, FileUrl>();
    }

    /**
     * @return the id
     */
    public SchemaId getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(SchemaId id) {
        this.id = id;
    }

    /**
     * @return the parameters
     */
    public SchemaParameters getParameters() {
        return parameters;
    }

    /**
     * @param parameters the parameters to set
     */
    public void setParameters(SchemaParameters parameters) {
        this.parameters = parameters;
    }

    /**
     * @return the name
     */
    public TextField getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(TextField name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public TextField getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(TextField description) {
        this.description = description;
    }

    /**
     * @return the image
     */
    public FileUrl getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(FileUrl image) {
        if (image == null) {
            throw new IllegalArgumentException();
        }
        this.image = image;
    }

    /**
     * @return the thumbnail
     */
    public FileUrl getThumbnail() {
        return thumbnail;
    }

    /**
     * @param thumbnail the thumbnail to set
     */
    public void setThumbnail(FileUrl thumbnail) {
        if (thumbnail == null) {
            throw new IllegalArgumentException();
        }
        this.thumbnail = thumbnail;
    }

    /**
     * @return the pdfSchemas
     */
    public Map<Locale, FileUrl> getPdfSchemas() {
        Map<Locale, FileUrl> retValue = new HashMap<Locale, FileUrl>();
        retValue.putAll(this.pdfSchemas);
        return retValue;
    }

    public void setPdfSchema(Locale locale, FileUrl pdf)
    {
        if (locale == null || pdf == null)
            throw new IllegalArgumentException();
        pdfSchemas.put(locale, pdf);
    }
}
