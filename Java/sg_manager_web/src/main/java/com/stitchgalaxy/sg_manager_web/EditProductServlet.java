/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stitchgalaxy.sg_manager_web;

import com.stitchgalaxy.sg_manager_web.data.Partner;
import com.stitchgalaxy.sg_manager_web.data.Product;
import com.stitchgalaxy.sg_manager_web.data.ProductLocalization;
import java.awt.Color;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 *
 * @author tarasev
 */
public class EditProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Product product = null;
        //TODO: fetch product
        product = new Product();
        product.setId(1l);
        product.setBlocked(true);
        product.setDescription("Description");
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
        product.setComplexity(0.5);
        product.setSales(100l);
        product.setRates(2l);
        product.setRating(9l);
        product.setTags("tree\nowl");
        
        ProductLocalization locRu = new ProductLocalization();
        locRu.setLocale("ru");
        locRu.setName("Совенок");
        locRu.setDescription("Маленький Совенок Сашуля");
        locRu.setTags("самый крутой зверь");
        product.getLocalizations().add(locRu);
                
        request.setAttribute("product", product);
        
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/edit_product.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sId = request.getParameter("id");
        String sName = request.getParameter("name");
        String sDate = request.getParameter("date");
        String sPriceUsd = request.getParameter("price");
        try
        {
            //TODO: store product.
            response.sendRedirect("/");
        }
        catch(Exception e)
        {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            request.setAttribute("error_message", sw.toString());

            RequestDispatcher rd = getServletContext().getRequestDispatcher("/error_save_product.jsp");
            rd.forward(request, response);
        }
    }


    @Override
    public String getServletInfo() {
        return "SG manager edit product servlet";
    }

}
