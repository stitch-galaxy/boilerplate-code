<%-- 
    Document   : home
    Created on : Dec 30, 2013, 12:52:36 PM
    Author     : tarasev
--%>

<%@page import="com.stitchgalaxy.domain.Design"%>
<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>Edit design</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sg.css" type="text/css"/>
    </head>

    <body>
        <%
            Design design = (Design) request.getAttribute("design");
        %>
        <form method="POST">
            <fieldset>
                <legend>Design parameters</legend>
                <p> 
                    <label for="width">Width</label>
                    <input name="width" type="text" placeholder="Enter design width" pattern="^\d*" class="text_input" value="${design.width}"/>
                    <label class="text_input_validation"></label>
                </p>
                <p> 
                    <label for="height">Height</label>
                    <input name="height" type="text" placeholder="Enter design height" pattern="^\d*" class="text_input" value="${design.height}"/>
                    <label class="text_input_validation"></label>
                </p>
                <p> 
                    <label for="colors">Colors</label>
                    <input name="colors" type="text" placeholder="Enter design colors" pattern="^\d*" class="text_input" value="${design.colors}"/>
                    <label class="text_input_validation"></label>
                </p>                
                <p> 
                    <label for="stitchesPerInch">Stitches per inch</label>
                    <input name="stitchesPerInch" type="text" placeholder="Enter stitches per inch: 14" pattern="\d+(\.\d{1,10})?" class="text_input" value="${design.stitchesPerInch}"/>
                    <label class="text_input_validation"></label>
                </p>
                <p> 
                    <label for="canvas">Canvas</label>
                    <input name="canvas" type="text" placeholder="Enter canvas name" class="text_input" value="${design.canvas}"/>
                    <label class="text_input_validation"></label>
                </p>
                <p> 
                    <label for="threads">Threads manufacturer</label>
                    <input name="threads" type="text" placeholder="Enter threads manufacturer" class="text_input" value="${design.threads}"/>
                    <label class="text_input_validation"></label>
                </p>
                <p>
                    <input class="submit_edit" type="submit" value="Save"/>
                </p>
            </fieldset>
        </form>

        <h3>Resources</h3>
        <div class="img_control_container">
            <h5>Thumbnail</h5>
            <div class="image_container" style="background-image: url(${design.thumbnailUri});">
                <a href="${design.thumbnailUri}" class="image_hyperlink" target="_blank"></a>
            </div>
            <p>        
            </p>
            <div>
                <div class="file_upload"   style="float: left;">
                    <form action="${pageContext.request.contextPath}/design-upload-thumbnail?product=${productId}&design=${design.id}" method="post" enctype="multipart/form-data">
                        <span>Upload »</span>
                        <input type="file" name="file" class="upload" onchange="this.form.submit()"/>
                    </form>
                </div>
                <c:choose>
                    <c:when test="${design.thumbnailUri != null}">
                        <div>
                            <a href="${pageContext.request.contextPath}/design-remove-thumbnail?product=${productId}&design=${design.id}" class="delete_button">Remove »</a>
                        </div>
                    </c:when>
                </c:choose>
            </div>
        </div>
        <div class="images_separator">
        </div>
        <div class="img_control_container">
            <h5>Design file</h5>
            <c:choose>
                <c:when test="${design.fileUri != null}">
                    <div class="image_container" style="background-image: url(${pageContext.request.contextPath}/images/data.png);">
                        <a href="${design.fileUri}" class="image_hyperlink" target="_blank"></a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="image_container" style="background-image: url(${pageContext.request.contextPath}/images/no_data.png);">
                    </div>
                </c:otherwise>
            </c:choose>
            <p>        
            </p>
            <div>
                <div class="file_upload"   style="float: left;">
                    <form action="${pageContext.request.contextPath}/design-upload-file?product=${productId}&design=${design.id}" method="post" enctype="multipart/form-data">
                        <span>Upload »</span>
                        <input type="file" name="file" class="upload" onchange="this.form.submit()"/>
                    </form>
                </div>
                <c:choose>
                    <c:when test="${design.fileUri != null}">
                        <div>
                            <a href="${pageContext.request.contextPath}/design-remove-file?product=${productId}&design=${design.id}" class="delete_button">Remove »</a>
                        </div>
                    </c:when>
                </c:choose>
            </div>
        </div>
    </body>
</html>
