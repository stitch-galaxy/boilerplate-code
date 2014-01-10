<%-- 
    Document   : home
    Created on : Dec 30, 2013, 12:52:36 PM
    Author     : tarasev
--%>

<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>Edit product</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sg.css" type="text/css"/>
    </head>

    <body>
        <form action="${pageContext.request.contextPath}/product-new" method="POST">
            <fieldset>
                <legend>Product parameters</legend>
                <p> 
                    <label for="name">Name</label>
                    <input name="name" type="text" placeholder="Enter design name" required class="text_input" value="${product.name}"/>
                    <label class="text_input_validation"></label>
                </p>
                <p> 
                    <label for="price">Price</label>
                    <input name="price" type="text" placeholder="Enter USD price: 1.05" pattern="^[0-9]+(\.[0-9]{1,2})?$" required class="text_input" value="${product.price}"/>
                    <label class="text_input_validation"></label>
                </p>
                <p> 
                    <label for="date">Publication date</label>
                    <input name="date" type="text" placeholder="YYYY-MM-DD" pattern="\d{4}-\d{1,2}-\d{1,2}" required class="text_input" value="${product.date}"/>
                    <label class="text_input_validation"></label>
                </p>
                <p> 
                    <label for="blocked">Blocked</label>
                    <input name="blocked" type="checkbox" value=""/>
                    <label class="text_input_validation"></label>
                </p>
                <p> 
                    <label for="description">Description</label>
                    <textarea name="description">${product.description}</textarea>
                </p>

                <p><input class="submit_edit" type="submit" value="Edit"/></p>
            </fieldset>

        </form>
    </body>
</html>
