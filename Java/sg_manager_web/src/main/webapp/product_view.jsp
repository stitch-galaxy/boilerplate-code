<%-- 
    Document   : home
    Created on : Dec 30, 2013, 12:52:36 PM
    Author     : tarasev
--%>

<%@page import="com.stitchgalaxy.sg_manager_web.data.Product"%>
<%@page contentType="text/html" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>View product</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sg.css" type="text/css" />
    </head>
    <body>
        <%
            Product product = (Product) request.getAttribute("product");
        %>
        <div class="datagrid">
            <table>
                <thead><tr><th>Parameter</th><th>Value</th></tr></thead>
                <tbody>
                    <tr>
                        <td>
                            Identifier
                        </td>
                        <td>${product.id}</td>
                    </tr>
                    <tr class="alt">
                        <td>
                            Name
                        </td>
                        <td>${product.name}</td>
                    </tr>
                    <tr>
                        <td>
                            Price
                        </td>
                        <td>${product.priceUsd}</td>
                    </tr>
                    <tr class="alt">
                        <td>
                            Publication date
                        </td>
                        <td>${product.date}</td>
                    </tr>
                    <tr>
                        <td>
                            Blocked
                        </td>
                        <td>${product.isBlocked()}</td>
                    </tr>
                    <tr class="alt">
                        <td>
                            Description
                        </td>
                        <td><div style="width: 100%; height:100%; overflow: auto;">${fn:replace(product.description, newLineChar, "<br/>")}</div></td>
                    </tr>
                    <tr>
                        <td>
                            Sales
                        </td>
                        <td>${product.sales}</td>
                    </tr>
                    <tr class="alt">
                        <td>
                            Rating (sum of all user rates)
                        </td>
                        <td>${product.rating}</td>
                    </tr>
                    <tr>
                        <td>
                            Rates amount
                        </td>
                        <td>${product.rates}</td>
                    </tr>
                    <tr class="alt">
                        <td>
                            Average rating
                        </td>
                        <td>${product.rates != null && product.rating != null ? product.rating / product.rates : 0}</td>
                    </tr>
                    <tr>
                        <td>
                            Complexity
                        </td>
                        <td>${product.complexity}</td>
                    </tr>
                    <tr class="alt">
                        <td>
                            Average color
                        </td>
                        <%String sColor = product.getAvgColor() == null ? "" : String.format("#%02X%02X%02X", product.getAvgColor().getRed(), product.getAvgColor().getGreen(), product.getAvgColor().getBlue());%>
                        <td style="background-color: <% out.print(sColor); %>"><% out.print(sColor); %></td>
                    </tr>
                </tbody>
            </table>
        </div>
        <br/>
        <a href="${pageContext.request.contextPath}/product_new" class="add_button">Add design »</a>
    </body>
</html>