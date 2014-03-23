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
        <title>Edit localization</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sg.css" type="text/css"/>
    </head>

    <body>
        <form method="POST">
            <fieldset>
                <legend>Localization parameters</legend>
                <p> 
                    <label for="name">Name</label>
                    <input name="name" type="text" placeholder="Enter design name" required class="text_input" value="${locale.name}"/>
                    <label class="text_input_validation"></label>
                </p>
                <p> 
                    <label for="description">Description</label>
                    <textarea name="description" cols="100">${locale.description}</textarea>
                </p>
                <p> 
                    <label for="tags">tags</label>
                    <textarea name="tags" cols="100">${locale.tags}</textarea>
                </p>

                <p><input class="submit_edit" type="submit" value="save"/></p>
            </fieldset>

        </form>
    </body>
</html>
