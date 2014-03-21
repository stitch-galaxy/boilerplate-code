<%-- 
    Document   : home
    Created on : Dec 30, 2013, 12:52:36 PM
    Author     : tarasev
--%>

<%@page contentType="text/html" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>Select category</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sg.css" type="text/css" />
    </head>
    <body>
        <p>
            <c:choose>
                <c:when test="${category.parent != null}">
                    <label for="parent">Parent</label>
                    <a href="${pageContext.request.contextPath}${URL_PRODUCT_SELECT_CATEGORY}?product=${productId}&category=${category.parent.id}">${category.parent.name}</a> 
                </c:when>
                <c:otherwise>
                    <label for="parent">Root</label>
                    <a href="${pageContext.request.contextPath}${URL_PRODUCT_SELECT_CATEGORY}?product=${productId}&category=${category.parent.id}">${category.parent.name}</a> 
                </c:otherwise>
            </c:choose>
        </p>
        <div class="datagrid">
            <table>
                <thead><tr><th>Category</th><th>Action</th></tr></thead>
                <tbody>
                    <c:forEach items="${category.childs}" var="subcategory" varStatus="loopStatus">
                        <tr class="${loopStatus.index % 2 == 0 ? '' : 'alt'}">
                            <td>
                                <a href="${pageContext.request.contextPath}${URL_PRODUCT_SELECT_CATEGORY}?product=${productId}&category=${subcategory.id}">${subcategory.name}</a>
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}${URL_PRODUCT_ATTACH_CATEGORY}?product=${productId}&category=${subcategory.id}" class="edit_button">Select »</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </body>
</html>