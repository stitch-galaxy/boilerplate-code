<%-- 
    Document   : home
    Created on : Dec 30, 2013, 12:52:36 PM
    Author     : tarasev
--%>

<%@page import="com.stitchgalaxy.sg_manager_web.data.Product"%>
<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>Edit product</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sg.css" type="text/css"/>
    </head>

    <body>
        <%
            Product product = (Product) request.getAttribute("product");
        %>
        <form action="${pageContext.request.contextPath}/product-edit?product=${product.id}" method="POST">
            <fieldset>
                <legend>Product parameters</legend>
                <p> 
                    <label for="name">Name</label>
                    <input name="name" type="text" placeholder="Enter design name" required class="text_input" value="${product.name}"/>
                    <label class="text_input_validation"></label>
                </p>
                <p> 
                    <label for="price">Price</label>
                    <input name="price" type="text" placeholder="Enter USD price: 1.05" pattern="^[0-9]+(\.[0-9]{1,2})?$" required class="text_input" value="${product.priceUsd}"/>
                    <label class="text_input_validation"></label>
                </p>
                <p> 
                    <label for="date">Publication date</label>
                    <input name="date" type="text" placeholder="YYYY-MM-DD" pattern="\d{4}-\d{2}-\d{2}" required class="text_input" value="${product.date}"/>
                    <label class="text_input_validation"></label>
                </p>
                <p> 
                    <label for="blocked">Blocked</label>
                    <c:choose>
                        <c:when test="${product.blocked}">
                            <input id="blocked" name="blocked" type="checkbox" checked/>
                        </c:when>
                        <c:otherwise>
                            <input id="blocked" name="blocked" type="checkbox"/>
                        </c:otherwise>
                    </c:choose>
                </p>
                <p> 
                    <label for="description">Description</label>
                    <textarea name="description" cols="100">${product.description}</textarea>
                </p>
                <p> 
                    <label for="sales">Sales</label>
                    <input name="sales" type="text" placeholder="Total sales" class="text_input" value="${product.sales}" pattern="\d+"/>
                    <label class="text_input_validation"></label>
                </p>
                <p> 
                    <label for="rates">Rates</label>
                    <input name="rates" type="text" placeholder="Total rates" class="text_input" value="${product.rates}" pattern="\d+"/>
                    <label class="text_input_validation"></label>
                </p>
                <p> 
                    <label for="rating">Total rating</label>
                    <input name="rating" type="text" placeholder="Total rating" class="text_input" value="${product.rating}" pattern="\d+"/>
                    <label class="text_input_validation"></label>
                </p>
                <p> 
                    <label for="complexity">Complexity</label>
                    <input name="complexity" type="text" placeholder="[0-9]" class="text_input" value="${product.complexity}" pattern="\d{1}"/>
                    <label class="text_input_validation"></label>
                </p>
                <p>
                    <label for="tags">Tags</label>
                    <textarea name="tags" cols="100">${product.tags}</textarea>
                </p>
                <p> 
                    <label for="color">Average color</label>
                    <%String sColor = product.getAvgColor() == null ? "" : String.format("#%02X%02X%02X", product.getAvgColor().getRed(), product.getAvgColor().getGreen(), product.getAvgColor().getBlue());%>
                    <input name="color" type="text" placeholder="html color #FFFFFF" class="text_input" value="<% out.print(sColor);%>" pattern="#[0-9,A-F,a-f]{6}"/>
                    <label class="text_input_validation"></label>
                    <a href='http://www.colorpicker.com/' target="_blank">pick a color</a>
                </p>
                <p>
                    <input class="submit_edit" type="submit" value="Save"/>
                </p>
            </fieldset>

        </form>
    </body>
</html>
