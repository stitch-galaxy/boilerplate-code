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
        <h3 style="color: red">Error</h3>
        <pre>
            ${stack_trace}
        </pre>
    </body>
</html>
