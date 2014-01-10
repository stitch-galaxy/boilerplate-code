<%-- 
    Document   : home
    Created on : Dec 30, 2013, 12:52:36 PM
    Author     : tarasev
--%>

<%@page import="org.joda.time.LocalDate"%>
<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>New product</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sg.css" type="text/css"/>
    </head>

    <body>
        <form action="${pageContext.request.contextPath}/product-new" method="POST">
            <fieldset>
                <legend>New product parameters</legend>
                <p> 
                    <label for="name">Name</label>
                    <input name="name" type="text" placeholder="Enter design name" required class="text_input"/>
                    <label class="text_input_validation"></label>
                </p>
                <p> 
                    <label for="price">Price</label>
                    <input name="price" type="text" placeholder="Enter USD price: 1.05" pattern="^[0-9]+(\.[0-9]{1,2})?$" required class="text_input"/>
                    <label class="text_input_validation"></label>
                </p>
                <p> 
                    <label for="date">Publication date</label>
                    <input name="date" type="text" placeholder="YYYY-MM-DD" pattern="\d{4}-\d{2}-\d{2}" required value="<%out.print(LocalDate.now()); %>" class="text_input"/>
                    <label class="text_input_validation"></label>
                </p>

                <p><input class="submit_add" type="submit" value="create"/></p>
            </fieldset>

        </form>
    </body>
</html>
