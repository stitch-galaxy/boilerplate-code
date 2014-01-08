<%-- 
    Document   : home
    Created on : Dec 30, 2013, 12:52:36 PM
    Author     : tarasev
--%>

<%@page contentType="text/html" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>Error</title>
    </head>
    <body>
        <p>
        <h1 style="color: red">${error_message}</h1>
        </p>

        <xmp>
            ${stack_trace}
        </xmp>

</body>
</html>
