<%-- 
    Document   : home
    Created on : Dec 30, 2013, 12:52:36 PM
    Author     : tarasev
--%>

<%@page contentType="text/html" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>Stitch galaxy manager home page</title>
         <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sg.css" type="text/css" />
         <style type="text/css">

/*  input:required:invalid, input:focus:invalid {
    background-image: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAeVJREFUeNqkU01oE1EQ/mazSTdRmqSxLVSJVKU9RYoHD8WfHr16kh5EFA8eSy6hXrwUPBSKZ6E9V1CU4tGf0DZWDEQrGkhprRDbCvlpavan3ezu+LLSUnADLZnHwHvzmJlvvpkhZkY7IqFNaTuAfPhhP/8Uo87SGSaDsP27hgYM/lUpy6lHdqsAtM+BPfvqKp3ufYKwcgmWCug6oKmrrG3PoaqngWjdd/922hOBs5C/jJA6x7AiUt8VYVUAVQXXShfIqCYRMZO8/N1N+B8H1sOUwivpSUSVCJ2MAjtVwBAIdv+AQkHQqbOgc+fBvorjyQENDcch16/BtkQdAlC4E6jrYHGgGU18Io3gmhzJuwub6/fQJYNi/YBpCifhbDaAPXFvCBVxXbvfbNGFeN8DkjogWAd8DljV3KRutcEAeHMN/HXZ4p9bhncJHCyhNx52R0Kv/XNuQvYBnM+CP7xddXL5KaJw0TMAF8qjnMvegeK/SLHubhpKDKIrJDlvXoMX3y9xcSMZyBQ+tpyk5hzsa2Ns7LGdfWdbL6fZvHn92d7dgROH/730YBLtiZmEdGPkFnhX4kxmjVe2xgPfCtrRd6GHRtEh9zsL8xVe+pwSzj+OtwvletZZ/wLeKD71L+ZeHHWZ/gowABkp7AwwnEjFAAAAAElFTkSuQmCC);
    background-position: right top;
    background-repeat: no-repeat;
    -moz-box-shadow: none;
  }
  input:required:valid {
    background-image: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAepJREFUeNrEk79PFEEUx9/uDDd7v/AAQQnEQokmJCRGwc7/QeM/YGVxsZJQYI/EhCChICYmUJigNBSGzobQaI5SaYRw6imne0d2D/bYmZ3dGd+YQKEHYiyc5GUyb3Y+77vfeWNpreFfhvXfAWAAJtbKi7dff1rWK9vPHx3mThP2Iaipk5EzTg8Qmru38H7izmkFHAF4WH1R52654PR0Oamzj2dKxYt/Bbg1OPZuY3d9aU82VGem/5LtnJscLxWzfzRxaWNqWJP0XUadIbSzu5DuvUJpzq7sfYBKsP1GJeLB+PWpt8cCXm4+2+zLXx4guKiLXWA2Nc5ChOuacMEPv20FkT+dIawyenVi5VcAbcigWzXLeNiDRCdwId0LFm5IUMBIBgrp8wOEsFlfeCGm23/zoBZWn9a4C314A1nCoM1OAVccuGyCkPs/P+pIdVIOkG9pIh6YlyqCrwhRKD3GygK9PUBImIQQxRi4b2O+JcCLg8+e8NZiLVEygwCrWpYF0jQJziYU/ho2TUuCPTn8hHcQNuZy1/94sAMOzQHDeqaij7Cd8Dt8CatGhX3iWxgtFW/m29pnUjR7TSQcRCIAVW1FSr6KAVYdi+5Pj8yunviYHq7f72po3Y9dbi7CxzDO1+duzCXH9cEPAQYAhJELY/AqBtwAAAAASUVORK5CYII=);
    background-position: right top;
    background-repeat: no-repeat;
  }*/
  
  
/*input[type="text"]:valid {
    color: green;
}

input[type="text"]:valid ~ .input-validation::after {
    content: "+";
    color: green;
}

input[type="text"]:invalid {
    color: red;
}*/

input:invalid + .input-validation::after { content: '  '; }
input:valid + .input-validation::after { content: ' ✓'; }
  

</style>
    </head>
    <body>
        <div class="datagrid">
            <table>
                <thead><tr><th>Design name</th><th>Identifier</th></tr></thead>
                <tbody>
                    <c:forEach items="${products}" var="product" varStatus="loopStatus">
                        <tr class="${loopStatus.index % 2 == 0 ? '' : 'alt'}">
                            <td>
                                <a href="${pageContext.request.contextPath}/edit_product?product=${product.id}">${product.name}</a>
                            </td>
                            <td>${product.id}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <br/>
        <a href="${pageContext.request.contextPath}/new_product" class="add_button">Add design »</a>
        <form action="test" method="POST">
            <fieldset>
                <legend>product parameters</legend>
                <p> 
                    <label for="text">text</label>
                    <input name="text" type="text" required value="" placeholder="enter text"/>
                    <label class="input-validation"></label>
                </p>
                <p> 
                    <label for="price">price</label>
                    <input name="price" type="text" required value="" placeholder="enter USD price: 1.05" pattern="^[0-9]+(\.[0-9]{1,2})?$"/>
                    <label class="input-validation"></label>
                </p>
                <p> 
                    <label for="date">date</label>
                    <input name="date" type="date" required value="2013-11-13"/>
                    <label class="input-validation"></label>
                </p>
                <p> 
                    <label for="color">color</label>
                    <input name="color" type="color" value=""/>
                    <label class="input-validation"></label>
                </p>
                <p> 
                    <label for="url">url</label>
                    <input name="url" type="url" pattern="https?://.+"/>
                    <label class="input-validation"></label>
                </p>
                <p> 
                    <label for="file">file</label>
                    <input name="file" type="file"/>
                    <label class="input-validation"></label>
                </p>
                
<p><input class="submit" type="submit" value="submit"/></p>
            </fieldset>
            
        </form>
    </body>
</html>