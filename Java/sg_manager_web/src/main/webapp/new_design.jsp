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

        <link rel="stylesheet" href="./css/cmxform.css" />
        
        <link rel="stylesheet" href="./css/colorpicker.css" type="text/css" />
        
        <script src="./scripts/jquery.js"></script>
        <script src="./scripts/jquery.validate.min.js"></script>
        

	<script type="text/javascript" src="./js/colorpicker.js"></script>
        <script type="text/javascript" src="./js/eye.js"></script>
        <script type="text/javascript" src="./js/utils.js"></script>
        <script type="text/javascript" src="./js/layout.js?ver=1.0.2"></script>

        
        <script>

$.validator.setDefaults({
        submitHandler: function() { alert("submitted!"); }
});

$().ready(function() {
    
    $("#productForm").validate({
                rules: {
                        id: "required",
                        date: "required",
                        price: "required",
                        description: {
                                required: false
                        }
                },
                messages: {
                        id: "Please enter valid product uuid",
                        date: "Please enter valid publication date",
                        price: "Please enter valid price in USD"
                    }
                });

            });
        </script>
        
        <style type="text/css">
#productForm { width: 670px; }
#productForm label.error {
	margin-left: 10px;
	width: auto;
	display: inline;
}
</style>

    </head>

    <body>
        <form class="cmxform" id="productForm" action="/sg_manager_web/new_design" method="POST">
            <fieldset>
                <legend>Please provide product parameters</legend>
                <p>
                    <label for="id">ID</label>
                    <input id="id" name="id" type="text" required/>
                </p>
                <p>
                    <label for="date">Publication date</label>
                    <input id="date" name="date" type="dateISO" required/>
                </p>
                <p>
                    <label for="price">Price in USD</label>
                    <input id="price" name="price" type="number" required/>
                </p>
                <p>
                    <label for="description">Description</label>
                    <textarea id="description" name="description"></textarea>
                </p>
                
                <p>
                    <input type="text" maxlength="6" size="6" id="colorpickerField1" value="" />
                </p>

                <p>
                    <input class="submit" type="submit" value="Submit"/>
                </p>
            </fieldset>
        </form>
    </body>
</html>
