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
        <title>Home page</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sg.css" type="text/css" />
    </head>
    <body>
        <div class="datagrid">
            <table>
                <thead><tr><th>Design name</th><th>Identifier</th></tr></thead>
                <tbody>
                    <c:forEach items="${products}" var="product" varStatus="loopStatus">
                        <tr class="${loopStatus.index % 2 == 0 ? '' : 'alt'}">
                            <td>
                                <a href="${pageContext.request.contextPath}/product-view?product=${product.id}">${product.name}</a>
                            </td>
                            <td>${product.id}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <p>
            <a href="${pageContext.request.contextPath}/product-new" class="add_button">Add design »</a>
        </p>

    </body>
</html>