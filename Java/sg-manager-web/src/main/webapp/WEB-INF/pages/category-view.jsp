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
        <title>Manage category</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sg.css" type="text/css" />
    </head>
    <body>
        <p>
            <a href="${pageContext.request.contextPath}${URL_HOME}">Home</a> 
        </p>
        <h4>Category name: ${category.current.name}</h4>
            <c:choose>
                
                  <c:when test="${category.parent != null}">
                      
                <p>
                    <label>Parent:</label>
                    <a href="${pageContext.request.contextPath}${URL_CATEGORY_VIEW}?category=${category.parent.id}">${category.parent.name}</a> 
                    </p>
                </c:when>
                
            </c:choose>
        <div class="datagrid">
            <table>
                <thead><tr><th>Category</th><th>Action</th></tr></thead>
                <tbody>
                    <c:forEach items="${category.childs}" var="subcategory" varStatus="loopStatus">
                        <tr class="${loopStatus.index % 2 == 0 ? '' : 'alt'}">
                            <td>
                                <a href="${pageContext.request.contextPath}${URL_CATEGORY_VIEW}?category=${subcategory.id}">${subcategory.name}</a>
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}${URL_CATEGORY_REMOVE}?category=${category.current.id}&sub-category=${subcategory.id}" class="delete_button">Remove »</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <form method="POST" action="${pageContext.request.contextPath}${URL_CATEGORY_ADD}?category=${category.current.id}">
            <fieldset>
                <legend>Sub category parameters</legend>
                <p> 
                    <label for="name">Name</label>
                    <input name="name" type="text" placeholder="Enter category name" required class="text_input"/>
                    <label class="text_input_validation"></label>
                </p>

                <p><input class="submit_add" type="submit" value="Add subcategory"/></p>
            </fieldset>
        </form>
    </body>
</html>