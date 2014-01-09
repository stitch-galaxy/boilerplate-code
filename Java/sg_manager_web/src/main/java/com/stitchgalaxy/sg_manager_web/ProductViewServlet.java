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
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 *
 * @author tarasev
 */
@WebServlet("/product_view")
public class ProductViewServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try
        {
            Long productId = Long.parseLong(request.getParameter("product"));
            Product product = createProductData();
            //TODO: fetch real data
            request.setAttribute("product", product);
        }
        catch(Exception e)
        {
            ErrorHandler errorHandler = new ErrorHandler();
            errorHandler.setException(e);
            errorHandler.setMessage("Can not fetch product data");
            errorHandler.setRequest(request);
            errorHandler.setResponse(response);
            errorHandler.setServlet(this);
            errorHandler.process();
            return;
        }
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/product_view.jsp");
        rd.forward(request, response);
    }
    
    private Product createProductData()
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
        product.setComplexity(new BigDecimal("0.5"));
        product.setSales(100l);
        product.setRates(2l);
        product.setRating(9l);
        product.setTags("tree\nowl");
        
        ProductLocalization locRu = new ProductLocalization();
        locRu.setLocale("ru");
        locRu.setName("Совенок");
        locRu.setDescription("Маленький Совенок Сашуля\nКоторого мы очень любим!");
        locRu.setTags("самый крутой зверь");
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
        product.setCompleteImageUri("http://www.mediacollege.com/internet/html/images/image1.jpg");
        
        return product;
    }
}
