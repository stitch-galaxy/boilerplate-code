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
                        name: "required",
                        sales: 
                        {
                            digits: true
                        },
                        rating: 
                        {
                            digits: true
                        },
                        rates:
                        {
                            digits: true
                        },
                        author_uri:
                        {
                            url: true
                        },
                        translator_uri:
                        {
                            url: true
                        }
                },
                messages: {
                        id: "Please enter valid product uuid",
                        date: "Please enter valid publication date",
                        price: "Please enter valid price in USD",
                        name: "Please enter design name",
                        sales:
                        {
                            digits: "Please enter valid number"
                        },
                        rating: {
                            digits: "Please enter valid number"
                        },
                        rates: {
                            digits: "Please enter valid number"
                        },
                        author_uri:
                        {
                            url: "Please enter valid uri"
                        },
                        translator_uri:
                        {
                            url: "Please enter valid uri"
                        },
                        complexity:
                        {
                            number: "Please enter valid number"
                        }
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
        <form class="cmxform" id="productForm" action="/sg_manager_web/new_design" method="POST">
            <fieldset>
                <legend>Please provide product parameters</legend>
                <p>
                    <label for="id">ID</label>
                    <input id="id" name="id" type="text" required/>
                </p>
                <p>
                    <label for="blocked">Blocked</label>
                    <input id="blocked" name="blocked" type="checkbox"/>
                </p>
                <p>
                    <label for="id">Name</label>
                    <input id="name" name="name" type="text" required/>
                </p>
                <p>
                    <label for="description">Description</label>
                </p>
                <p>
                    <textarea id="description" name="description" cols="50" rows="3"></textarea>
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
                    <label for="author">Author</label>
                    <input id="author" name="author" type="text"/>
                </p>
                <p>
                    <label for="author_uri">Author site</label>
                    <input id="author_uri" name="author_uri" type="url"/>
                </p>
                <p>
                    <label for="translator">Translator</label>
                    <input id="translator" name="translator" type="text"/>
                </p>
                <p>
                    <label for="translator_uri">Translator site</label>
                    <input id="translator_uri" name="translator_uri" type="url"/>
                </p>
                <p>
                    <label for="sales">Sales</label>
                    <input id="sales" name="sales" type="text"/>
                </p>
                <p>
                    <label for="rating">Total rating</label>
                    <input id="rating" name="rating" type="text"/>
                </p>
                <p>
                    <label for="rates">Rates</label>
                    <input id="rates" name="rates" type="text"/>
                </p>
                <p>
                    <label for="tags">Tags <br/><b>Note: each tag should be place on the new line</b></label>
                </p>
                <p>
                    <textarea id="tags" name="tags" cols="50" rows="3"></textarea>
                </p>
                <p>
                    <label for="colorpickerField1">Average color</label>
                    <input type="text" maxlength="6" size="6" id="colorpickerField1" value="" />
                </p>
                <p>
                    <label for="complexity">Complexity</label>
                    <input id="complexity" name="complexity" type="number"/>
                </p>
                <p>
                    <input class="submit" type="submit" value="Create"/>
                </p>
            </fieldset>
        </form>
    </body>
</html>
