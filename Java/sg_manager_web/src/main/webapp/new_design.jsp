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
        <title>New design</title>
    </head>
    
    <body>
        <script type="text/javascript" src="./scripts/jquery.validate.min.js" />
        <form action="/sg_manager_web/new_design" method="POST">
            <table>
                <tr>
                    <td>
                        Id
                    </td>
                    <td>
                        <input readonly type="text" name="id" id="id"/>
                    </td>
                </tr>
            </table>
            <br/>
            <input type="submit" value="Submit" />
        </form>
    </body>
</html>
