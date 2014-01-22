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
        <title>New localization</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sg.css" type="text/css"/>
    </head>

    <body>
        <form method="POST">
            <fieldset>
                <legend>New localization parameters</legend>
                <p> 
                    <label for="locale">Locale</label>
                    <input name="locale" type="text" placeholder="Enter locale id" required class="text_input"/>
                    <label class="text_input_validation"></label>
                </p>

                <p><input class="submit_add" type="submit" value="create"/></p>
            </fieldset>

        </form>
    </body>
</html>
