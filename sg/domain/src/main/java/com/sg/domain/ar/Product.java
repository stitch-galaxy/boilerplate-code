/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.ar;

import com.sg.domail.vo.ProductId;
import com.sg.domail.vo.ProductInfo;
import com.sg.domain.entites.*;
import java.util.List;

/**
 *
 * @author tarasev
 */
public class Product {
    private ProductId id;
    private ProductInfo info;
    
    private List<File> files;
    private TextField name;
}
