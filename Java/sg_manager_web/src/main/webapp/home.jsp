<%-- 
    Document   : home
    Created on : Dec 30, 2013, 12:52:36 PM
    Author     : tarasev
--%>

<%@page import="java.util.List"%>
<%@page import="com.stitchgalaxy.sg_manager_web.data.Product"%>
<%@page contentType="text/html" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>Stitch galaxy manager home page</title>
    </head>
    <body>
        <a href="/sg_manager_web/new_product">Add new design</a>
        <br/>
        <br/>
        <table border="1">
            <tr>
                <td><b>Design Name</b></td>
                <td><b>Id</b></td>
            </tr>
<% 
for (Product product : (List<Product>)request.getAttribute("products"))
{
%>
            <tr>
                <td>
                    <a href="/sg_manager_web/edit_product?product=<% out.print(product.getId()); %>"><% out.print(product.getName()); %></a>
                </td>
                <td><% out.print(product.getId()); %></td>
            </tr>
<%
}
%>
        </table>
    </body>
</html>