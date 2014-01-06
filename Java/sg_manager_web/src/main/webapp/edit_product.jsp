<%-- 
    Document   : home
    Created on : Dec 30, 2013, 12:52:36 PM
    Author     : tarasev
--%>

<%@page import="com.stitchgalaxy.sg_manager_web.data.ProductLocalization"%>
<%@page import="org.joda.time.format.DateTimeFormatter"%>
<%@page import="org.joda.time.format.ISODateTimeFormat"%>
<%@page import="com.stitchgalaxy.sg_manager_web.data.Product"%>
<%@page contentType="text/html" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>Edit product</title>

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
                submitHandler: function() {
                    alert("submitted!");
                }
            });

            $().ready(function() {

                $("#productForm").validate({
                    rules: {
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

                $("#edit_locale").click(function(e) {
                    e.preventDefault();
                    var locale = $("#locale").val();
                    if (locale)
                    {

                        var href = $(this).attr("href");
                        href += locale;
                        window.location = href;
                    }
                    else
                    {
                        alert("Locale should not be empty");
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
                <legend>Please edit product parameters</legend>
                <p>
                    <%
                        Product product = (Product) request.getAttribute("product");
                        DateTimeFormatter dtf = ISODateTimeFormat.date();
                    %>
                    <label for="id">ID</label>
                    <input id="id" name="id" type="text" required readonly value="<% out.print(product.getId()); %>"/>
                </p>
                <p>
                    <label for="blocked">Blocked</label>
                    <%
                        if (product.isBlocked()) {
                    %>
                    <input id="blocked" name="blocked" type="checkbox" checked/>
                    <%
                    } else {
                    %>
                    <input id="blocked" name="blocked" type="checkbox"/>
                    <%
                        }
                    %>
                </p>
                <p>
                    <label for="id">Name</label>
                    <input id="name" name="name" type="text" required value="<% out.print(product.getName()); %>"/>
                </p>
                <p>
                    <label for="description">Description</label>
                </p>
                <p>
                    <textarea id="description" name="description" cols="50" rows="3"><% out.print(product.getDescription() == null ? "" : product.getDescription()); %></textarea>
                </p>
                <p>
                    <label for="date">Publication date</label>
                    <input id="date" name="date" type="dateISO" required value="<% out.print(product.getDate().toString(dtf)); %>"/>
                </p>
                <p>
                    <label for="price">Price in USD</label>
                    <input id="price" name="price" type="number" required value="<% out.print(product.getPriceUsd()); %>"/>
                </p>

                <p>
                    <%
                        if (product.getAuthor() != null) {
                    %>
                    Author:
                    <b><i><% out.print(product.getAuthor().getName()); %></i></b>
                    <a href="<% out.print(product.getAuthor().getUri()); %>" target="_blank">site</a>
                    <br/>
                    <a href="/sg_manager_web/remove_author?product=<% out.print(product.getId());%>">Remove</a>
                    <%
                        }
                    %>
                    <a href="/sg_manager_web/select_author?product=<% out.print(product.getId()); %>">Set author</a>
                </p>

                <p>
                    <%
                        if (product.getTranslator() != null) {
                    %>
                    Translator:
                    <b><i><% out.print(product.getTranslator().getName()); %></i></b>
                    <a href="<% out.print(product.getTranslator().getUri()); %>" target="_blank">site</a>
                    <br/>
                    <a href="/sg_manager_web/remove_translator?product=<% out.print(product.getId());%>">Remove</a>
                    <%
                        }
                    %>
                    <a href="/sg_manager_web/select_translator?product=<% out.print(product.getId());%>">Set translator</a>
                </p>

                <p>
                    <label for="sales">Sales</label>
                    <input id="sales" name="sales" type="text" value="<% out.print(product.getSales() == null ? "" : product.getSales()); %>"/>
                </p>
                <p>
                    <label for="rating">Total rating (sum of all user rates)</label>
                    <input id="rating" name="rating" type="text" value="<% out.print(product.getRating() == null ? "" : product.getRating()); %>"/>
                </p>
                <p>
                    <label for="rates">Rates</label>
                    <input id="rates" name="rates" type="text" value="<% out.print(product.getRates() == null ? "" : product.getRates()); %>"/>
                </p>
                <p>
                    <label for="tags">Tags <br/><b>Note: place each tag on the new line</b></label>
                </p>
                <p>
                    <textarea id="tags" name="tags" cols="50" rows="3"><% out.print(product.getTags() == null ? "" : product.getTags()); %></textarea>
                </p>
                <p>
                    <label for="colorpickerField1">Average color</label>
                    <input type="text" maxlength="6" size="6" id="colorpickerField1" value="<% out.print(product.getAvgColor() == null ? "" : String.format("%02X%02X%02X", product.getAvgColor().getRed(), product.getAvgColor().getGreen(), product.getAvgColor().getBlue())); %>" />
                </p>
                <p>
                    <label for="complexity">Complexity</label>
                    <input id="complexity" name="complexity" type="number" value="<% out.print(product.getComplexity() == null ? "" : product.getComplexity()); %>"/>
                </p>
                <p>
                    <input class="submit" type="submit" value="Update"/>
                </p>
            </fieldset>
        </form>
        <table border="1">
            <tr><td><b>Localization</b></td></tr>
            <%
                for (ProductLocalization localization : product.getLocalizations()) {
            %>
            <tr>
                <td>
                    <a href="/sg_manager_web/edit_localization?product=<% out.print(product.getId());%>&locale=<% out.print(localization.getLocale());%>"><% out.print(localization.getLocale());%></a>
                </td>
            </tr>
            <%
                }
            %>
        </table>
        <p>
            <label for="locale">Locale</label>
            <input id="locale" type="text"><a style="margin-left:10px;" href="/sg_manager_web/edit_localization?product=<% out.print(product.getId());%>&locale=" id="edit_locale">add or edit</a>
        </p>

    </body>
</html>
