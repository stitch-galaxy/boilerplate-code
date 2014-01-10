<%-- 
    Document   : home
    Created on : Dec 30, 2013, 12:52:36 PM
    Author     : tarasev
--%>

<%@page import="java.util.List"%>
<%@page import="java.util.LinkedList"%>
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
        <h1>Product information</h1>
        <h3>Main parameters</h3>
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
                        <td style="background-color: <% out.print(sColor); %>"><% out.print(sColor);%></td>
                    </tr>
                    <tr>
                        <td>
                            Tags
                        </td>
                        <td><div style="width: 100%; height:100%; overflow: auto;">${fn:replace(product.tags, newLineChar, "<br/>")}</div></td>
                    </tr>
                </tbody>
            </table>
        </div>
        <p>
            <a href="${pageContext.request.contextPath}/product_edit?product=${product.id}" class="edit_button">Edit »</a>
        </p>
        <h3>Author information</h3>
        <c:choose>
            <c:when test="${product.author != null}">
                <div class="datagrid">
                    <table>
                        <thead><tr><th>Author</th><th>Site</th></tr></thead>
                        <tbody>
                            <tr>
                                <td>
                                    ${product.author.name}
                                </td>
                                <td><a href="${product.author.uri}" target="_blank">${product.author.uri}</a></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <p>
                    <a href="${pageContext.request.contextPath}/product_remove_author?product=${product.id}" class="delete_button">Delete »</a>
                    <a href="${pageContext.request.contextPath}/product_set_author?product=${product.id}" class="edit_button">Change »</a>
                </p>
            </c:when>
            <c:otherwise>
                <p>
                    <a href="${pageContext.request.contextPath}/product_set_author?product=${product.id}" class="add_button">Set »</a>
                </p>
            </c:otherwise>
        </c:choose>
                <h3>Translator information</h3>
        <c:choose>
            <c:when test="${product.translator != null}">
                <div class="datagrid">
                    <table>
                        <thead><tr><th>Translator</th><th>Site</th></tr></thead>
                        <tbody>
                            <tr>
                                <td>
                                    ${product.translator.name}
                                </td>
                                <td><a href="${product.author.uri}" target="_blank">${product.translator.uri}</a></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <p>
                    <a href="${pageContext.request.contextPath}/product_remove_translator?product=${product.id}" class="delete_button">Delete »</a>
                    <a href="${pageContext.request.contextPath}/product_set_translator?product=${product.id}" class="edit_button">Change »</a>
                </p>
            </c:when>
            <c:otherwise>
                <p>
                    <a href="${pageContext.request.contextPath}/product_set_translator?product=${product.id}" class="add_button">Set »</a>    
                </p>
            </c:otherwise>
        </c:choose>
                <h3>Localizations</h3>
        <div class="datagrid">
            <table>
                <thead><tr><th>Locale</th><th>Action</th><th>Name</th><th>Description</th><th>Tags</th></tr></thead>
                <tbody>
                    <c:forEach var="localization" items="${product.localizations}" varStatus="loopStatus">
                        <tr class="${loopStatus.index % 2 == 0 ? '' : 'alt'}">
                            <td>
                                ${localization.locale}
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/product_edit_locale?product=${product.id}&localization=${localization.locale}" class="edit_button">Edit »</a> 
                            </td>
                            <td>${localization.name}</td>
                            <td>
                                <div style="width: 100%; height:100%; overflow: auto;">${fn:replace(localization.description, newLineChar, "<br/>")}</div>
                            </td>
                            <td>
                                <div style="width: 100%; height:100%; overflow: auto;">${fn:replace(localization.tags, newLineChar, "<br/>")}</div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <P>
            <a href="${pageContext.request.contextPath}/product_add_localization?product=${product.id}" class="add_button">Add »</a>
        </p>
        <h3>Categories</h3>
        <div class="datagrid">
            <table>
                <thead><tr><th>Category</th><th>Action</th></tr></thead>
                <tbody>
                    <c:forEach var="category" items="${product.categories}" varStatus="loopStatus">
                        <tr class="${loopStatus.index % 2 == 0 ? '' : 'alt'}">
                            <td>
                                ${category.name}
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/product_remove_category?product=${product.id}&category=${category.id}" class="delete_button">Remove »</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        
        <h3>Images</h3>
        
        <div class="datagrid">
            <table>
                <thead><tr><th>Image type</th><th>Image</th><th>Action</th></tr></thead>
                <tbody>
                    <tr>
                        <td>
                            Thumbnail
                        </td>
                        <td>
                           <div class="image_container" style="background-image: url(${product.thumbnailUri});">
                               <a href="${product.thumbnailUri}" class="image_hyperlink"></a>
                           </div> 
                        </td>
                        <td>
                            <a href="${pageContext.request.contextPath}/product_remove_thumbnail?product=${product.id}" class="delete_button">Remove »</a>
                        </td>
                    </tr>
                    
                </tbody>
            </table>
        </div>
        <h3>Images</h3>
        <div>
            <p style="text-align: center;"><b><i>Thumbnail</i></b></p>
            <div class="image_container" style="background-image: url(${product.thumbnailUri});">
                               <a href="${product.thumbnailUri}" class="image_hyperlink"></a>
                           </div> 
                           <p>
                               <a href="${pageContext.request.contextPath}/product_remove_thumbnail?product=${product.id}" class="delete_button">Remove »</a>
                           </p>
                           <div class="file_upload_container">
                               <div class="file_upload">
                                         <span>Upload »</span>
                                         <form><input type="file" class="upload" /></form>
</div>
                           </div>
                                     
        </div>
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        <div class="container" style="background-image: url(http://i.stack.imgur.com/2OrtT.jpg);">
            <a href="http://google.com" class="innera"></a>
        </div>
        
        
        <div class="img">
  <a target="_blank" href="http://www.w3schools.com/css/klematis1_big.htm">
  <img src="http://www.w3schools.com/css/klematis_big.jpg" alt="Klematis">
  </a>
  <div class="desc">Add a description of the image here</div>
</div>
<div class="img">
  <a target="_blank" href="https://cdn1.iconfinder.com/data/icons/dellipack/32/phonebook.png">
  <img src="https://cdn1.iconfinder.com/data/icons/dellipack/32/phonebook.png" alt="Klematis">
  </a>
  <div class="desc">
      <form id="upload_form" action="test" method="post" enctype="multipart/form-data">
<!--          <div class="upload">
        <input type="file" name="upload" onchange="this.form.submit()"/>
    </div>-->
          <div class="fileUpload btn btn-primary">
<span>Upload</span>

    <input type="file" class="upload" />
</div>
          
          
</form>
</div>
</div>
<div class="img">
  <a target="_blank" href="http://www.w3schools.com/css/klematis3_big.htm">
  <img src="http://www.w3schools.com/css/klematis3_big.jpg" alt="Klematis">
  </a>
  <div class="desc">Add a description of the image here</div>
</div>
<div class="img">
  <a target="_blank" href="http://www.w3schools.com/css/klematis4_big.htm">
  <img src="http://www.w3schools.com/css/klematis4_big.jpg" alt="Klematis">
  </a>
  <div class="desc">Add a description of the image here</div>
</div>
        
        
        
        
        
    </body>
</html>