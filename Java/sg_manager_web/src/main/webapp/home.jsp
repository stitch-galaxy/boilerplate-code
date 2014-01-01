<%-- 
    Document   : home
    Created on : Dec 30, 2013, 12:52:36 PM
    Author     : tarasev
--%>

<%@page import="java.util.List"%>
<%@page import="com.stitchgalaxy.sg_manager_web.data.ProductRef"%>
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
for (ProductRef product : (List<ProductRef>)request.getAttribute("products"))
{
%>
            <tr>
                <td>
<%
    StringBuilder sb = new StringBuilder();
    sb.append("<a href=\"sg_manager_web/edit_product?id=");
    sb.append(product.getUuid().toString());
    sb.append("\">");
    sb.append(product.getName());
    sb.append("</a>");
    out.println(sb.toString());
%>
                </td>
                <td>
<%
    out.println(product.getUuid().toString());
%>                    
                </td>
            </tr>
<%
}
%>
        </table>
    </body>
</html>