/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stitchgalaxy.sg_manager_web;

import com.stitchgalaxy.sg_manager_web.data.Category;
import com.stitchgalaxy.sg_manager_web.data.Design;
import com.stitchgalaxy.sg_manager_web.data.Partner;
import com.stitchgalaxy.sg_manager_web.data.Product;
import com.stitchgalaxy.sg_manager_web.data.ProductLocalization;
import java.awt.Color;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 *
 * @author tarasev
 */
public class TestData {
    
    public static List<Product> createProductsList()
    {
        List<Product> products = new LinkedList<Product>();
        Product p1 = createProductData();
        products.add(p1);
        Product p2 = createProductData();
        products.add(p2);
        return products;
    }
    
    public static Product createProductData()
    {
        Product product = new Product();
        product.setId(1l);
        product.setBlocked(true);
        product.setDescription("Description\nHello moto. I don't like to tell lot of words. Come on\nAstalavista baby");
        product.setName("Name");
        DateTimeFormatter dtf = ISODateTimeFormat.date();
        LocalDate dt = dtf.parseLocalDate("2013-11-13");
        product.setDate(dt);
        product.setPriceUsd(new BigDecimal("1.5"));
        
        Partner author = new Partner();
        author.setName("Author");
        author.setUri("http://google.com");
        product.setAuthor(author);
       
        Partner translator = new Partner();
        translator.setName("Translator");
        translator.setUri("http://yandex.ru");
        product.setTranslator(translator);
        
        product.setAvgColor(Color.PINK);
        product.setComplexity(5);
        product.setSales(100l);
        product.setRates(2l);
        product.setRating(9l);
        product.setTags("tree\nowl");
        
        ProductLocalization locRu = new ProductLocalization();
        locRu.setLocale("ru");
        locRu.setName("�������");
        locRu.setDescription("��������� ������� ������\n�������� �� ����� �����!");
        locRu.setTags("����� ������ �����");
        product.getLocalizations().add(locRu);
        
        Design design = new Design();
        design.setId(1l);
        design.setCanvas("Aida14");
        design.setColors(20);
        design.setHeight(100);
        design.setPartId(1);
        design.setStitchesPerInch(new BigDecimal("14.0"));
        design.setThreads("DMC");
        design.setWidth(200);
        design.setThumbnailUri("http://www.mediacollege.com/internet/html/images/image1.jpg");
        
        product.getDesigns().add(design);
        
        Category category = new Category();
        category.setId(1l);
        category.setName("animals");
        product.getCategories().add(category);
        
        product.setPrototypeUri("http://www.mediacollege.com/internet/html/images/image1.jpg");
        product.setThumbnailUri("http://www.mediacollege.com/internet/html/images/image1.jpg");
        product.setLargeImageUri("http://www.mediacollege.com/internet/html/images/image1.jpg");
        product.setCompleteProductUri("http://www.mediacollege.com/internet/html/images/image1.jpg");
        
        return product;
    }
}