/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stitchgalaxy.service;

import com.stitchgalaxy.domain.Category;
import com.stitchgalaxy.domain.Design;
import com.stitchgalaxy.domain.Partner;
import com.stitchgalaxy.domain.Product;
import com.stitchgalaxy.domain.ProductLocalization;
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

    public static List<Product> createProductsList() {
        List<Product> products = new LinkedList<Product>();
        for (int i = 0; i < 10; ++i) {
            products.add(createProductData());
        }
        return products;
    }

    public static Product createProductData() {
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
        product.setCanvas("Aida14");
        product.setColors(20);
        product.setHeight(100);
        product.setStitchesPerInch(new BigDecimal("14.0"));
        product.setThreads("DMC");
        product.setWidth(200);
        product.setThumbnailUri("http://www.mediacollege.com/internet/html/images/image1.jpg");
        product.setFileUri("http://www.mediacollege.com/internet/html/images/image1.jpg");
        
        product.getLocalizations().add(createProductLocalization());        

        product.getCategories().add(new Category(null, "l1_category"));

        product.setPrototypeUri("http://www.mediacollege.com/internet/html/images/image1.jpg");
        product.setThumbnailUri("http://www.mediacollege.com/internet/html/images/image1.jpg");
        product.setLargeImageUri("http://www.mediacollege.com/internet/html/images/image1.jpg");
        product.setCompleteProductUri("http://www.mediacollege.com/internet/html/images/image1.jpg");

        return product;
    }
    
//    public static Category createProductCategory()
//    {
//        return new Category(null, "root");
//    }
    
    public static Design createProductDesign()
    {
        Design design = new Design();
        design.setId(1l);
        design.setCanvas("Aida14");
        design.setColors(20);
        design.setHeight(100);
        design.setStitchesPerInch(new BigDecimal("14.0"));
        design.setThreads("DMC");
        design.setWidth(200);
        design.setThumbnailUri("http://www.mediacollege.com/internet/html/images/image1.jpg");
        design.setFileUri("http://www.mediacollege.com/internet/html/images/image1.jpg");
        
        return design;
    }
    
    public static ProductLocalization createProductLocalization()
    {
        ProductLocalization locRu = new ProductLocalization();
        locRu.setLocale("ru");
        locRu.setName("�������");
        locRu.setDescription("��������� ������� ������\n�������� �� ����� �����!");
        locRu.setTags("����� ������ �����");
        return locRu;
    }

    public static List<Partner> createPartnersList() {
        List<Partner> partners = new LinkedList<Partner>();
        for (int i = 0; i < 10; ++i) {
            partners.add(createPartnerData());
        }
        return partners;
    }

    public static Partner createPartnerData() {
        Partner partner = new Partner();
        partner.setName("Ksenia");
        partner.setUri("http://zlataya.info/");
        partner.setId(1l);
        return partner;
    }
}
