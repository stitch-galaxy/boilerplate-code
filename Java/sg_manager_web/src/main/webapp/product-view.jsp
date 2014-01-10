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
                            Total rating
                        </td>
                        <td>${product.rating}</td>
                    </tr>
                    <tr>
                        <td>
                            Rates
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
            <a href="${pageContext.request.contextPath}/product-edit?product=${product.id}" class="edit_button">Edit »</a>
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
                    <a href="${pageContext.request.contextPath}/product-remove-author?product=${product.id}" class="delete_button">Delete »</a>
                    <a href="${pageContext.request.contextPath}/product-set-author?product=${product.id}" class="edit_button">Change »</a>
                </p>
            </c:when>
            <c:otherwise>
                <p>
                    <a href="${pageContext.request.contextPath}/product-set-author?product=${product.id}" class="add_button">Set »</a>
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
                    <a href="${pageContext.request.contextPath}/product-remove-translator?product=${product.id}" class="delete_button">Delete »</a>
                    <a href="${pageContext.request.contextPath}/product-set-translator?product=${product.id}" class="edit_button">Change »</a>
                </p>
            </c:when>
            <c:otherwise>
                <p>
                    <a href="${pageContext.request.contextPath}/product-set-translator?product=${product.id}" class="add_button">Set »</a>    
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
                                <a href="${pageContext.request.contextPath}/product-edit-localization?product=${product.id}&localization=${localization.locale}" class="edit_button">Edit »</a> 
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
        <p>
            <a href="${pageContext.request.contextPath}/product-add-localization?product=${product.id}" class="add_button">Add »</a>
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
                                <a href="${pageContext.request.contextPath}/product-remove-category?product=${product.id}&category=${category.id}" class="delete_button">Remove »</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <p>
            <a href="${pageContext.request.contextPath}/product-add-category?product=${product.id}" class="add_button">Add »</a>
        </p>


        <h3>Images</h3>
        <div class="img_control_container">
            <h5>Thumbnail</h5>
            <div class="image_container" style="background-image: url(${product.thumbnailUri});">
                <a href="${product.thumbnailUri}" class="image_hyperlink" target="_blank"></a>
            </div>
            <p>        
            </p>
            <div>
                <div class="file_upload"   style="float: left;">
                    <form action="${pageContext.request.contextPath}/upload-thumbnail?product=${product.id}" method="post" enctype="multipart/form-data">
                        <span>Upload »</span>
                        <input type="file" name="file" class="upload" onchange="this.form.submit()"/>
                    </form>
                </div>
                <c:choose>
                    <c:when test="${product.thumbnailUri != null}">
                        <div>
                            <a href="${pageContext.request.contextPath}/product-remove-thumbnail?product=${product.id}" class="delete_button">Remove »</a>
                        </div>
                    </c:when>
                </c:choose>
            </div>
        </div>
        <div class="images_separator">
        </div>
        <div class="img_control_container">
            <h5>Prototype</h5>
            <div class="image_container" style="background-image: url(${product.prototypeUri});">
                <a href="${product.prototypeUri}" class="image_hyperlink" target="_blank"></a>
            </div>
            <p>        
            </p>
            <div>
                <div class="file_upload"   style="float: left;">
                    <form action="${pageContext.request.contextPath}/upload-prototype?product=${product.id}" method="post" enctype="multipart/form-data">
                        <span>Upload »</span>
                        <input type="file" name="file" class="upload" onchange="this.form.submit()"/>
                    </form>
                </div>
                <c:choose>
                    <c:when test="${product.prototypeUri != null}">
                        <div>
                            <a href="${pageContext.request.contextPath}/product-remove-prototype?product=${product.id}" class="delete_button">Remove »</a>
                        </div>
                    </c:when>
                </c:choose>
            </div>
        </div>
        <div class="images_separator">
        </div>
        <div class="img_control_container">
            <h5>Large image</h5>
            <div class="image_container" style="background-image: url(${product.largeImageUri});">
                <a href="${product.largeImageUri}" class="image_hyperlink" target="_blank"></a>
            </div>
            <p>        
            </p>
            <div>
                <div class="file_upload"   style="float: left;">
                    <form action="${pageContext.request.contextPath}/upload-large-image?product=${product.id}" method="post" enctype="multipart/form-data">
                        <span>Upload »</span>
                        <input type="file" name="file" class="upload" onchange="this.form.submit()"/>
                    </form>
                </div>
                <c:choose>
                    <c:when test="${product.largeImageUri != null}">
                        <div>
                            <a href="${pageContext.request.contextPath}/product-remove-large-image?product=${product.id}" class="delete_button">Remove »</a>
                        </div>
                    </c:when>
                </c:choose>
            </div>
        </div>
        <div class="images_separator">
        </div>
        <div class="img_control_container">
            <h5>Complete product</h5>
            <div class="image_container" style="background-image: url(${product.completeProductUri});">
                <a href="${product.completeProductUri}" class="image_hyperlink" target="_blank"></a>
            </div>
            <p>        
            </p>
            <div>
                <div class="file_upload"   style="float: left;">
                    <form action="${pageContext.request.contextPath}/upload-complete-product?product=${product.id}" method="post" enctype="multipart/form-data">
                        <span>Upload »</span>
                        <input type="file" name="file" class="upload" onchange="this.form.submit()"/>
                    </form>
                </div>
                <c:choose>
                    <c:when test="${product.completeProductUri != null}">
                        <div>
                            <a href="${pageContext.request.contextPath}/product-remove-complete-product?product=${product.id}" class="delete_button">Remove »</a>
                        </div>
                    </c:when>
                </c:choose>
            </div>
        </div>
    </body>
</html>