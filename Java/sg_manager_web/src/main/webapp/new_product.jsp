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
        <title>New product</title>
        
        <script src="./scripts/jquery.js"></script>
        <script src="./scripts/jquery.validate.min.js"></script>
    
        <script>

$().ready(function() {
    
    $("#productForm").validate({
                messages: {
                        date: "Please enter valid publication date",
                        price: "Please enter valid price in USD",
                        name: "Please enter design name"
                    }
                });

            });
        </script>
        
        <style type="text/css">
#productForm { width: 100%; }
#productForm label.error {
	margin-left: 10px;
	width: auto;
	display: inline;
}
</style>

    </head>

    <body>
        <form id="productForm" action="/sg_manager_web/new_product" method="POST">
            <fieldset>
                <legend>Please provide product parameters</legend>
                <p>
                    <label for="name" >Name</label>
                    <input id="name" name="name" type="text" required/>
                </p>
                <p>
                    <label for="date">Publication date (yyyy-mm-dd)</label>
                    <input id="date" name="date" type="dateISO" required/>
                </p>
                <p>
                    <label for="price">Price in USD</label>
                    <input id="price" name="price" type="number" required value="1"/>
                </p>
                <p>
                    <input class="submit" type="submit" value="Create product"/>
                </p>
            </fieldset>
        </form>
    </body>
</html>
