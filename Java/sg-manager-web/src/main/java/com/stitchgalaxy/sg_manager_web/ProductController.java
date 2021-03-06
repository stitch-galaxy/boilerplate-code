/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stitchgalaxy.sg_manager_web;

import com.stitchgalaxy.dto.CategoryInfoDTO;
import com.stitchgalaxy.dto.CommandAttachProductToCategory;
import com.stitchgalaxy.dto.CommandAttachProductToPartner;
import com.stitchgalaxy.dto.CommandCreatePartner;
import com.stitchgalaxy.dto.CommandCreateProduct;
import com.stitchgalaxy.dto.CommandCreateSubcategory;
import com.stitchgalaxy.dto.CommandDetachProductFromCategory;
import com.stitchgalaxy.dto.CommandDetachProductFromPartner;
import com.stitchgalaxy.dto.CommandGetCategory;
import com.stitchgalaxy.dto.CommandGetPartners;
import com.stitchgalaxy.dto.CommandGetProduct;
import com.stitchgalaxy.dto.CommandGetProductLocalization;
import com.stitchgalaxy.dto.CommandGetRootCategory;
import com.stitchgalaxy.dto.CommandRemoveProductFile;
import com.stitchgalaxy.dto.CommandRemoveProductLocalization;
import com.stitchgalaxy.dto.CommandRemoveSubcategory;
import com.stitchgalaxy.dto.CommandStoreProductLocalization;
import com.stitchgalaxy.dto.CommandUploadProductFile;
import com.stitchgalaxy.dto.FileType;
import com.stitchgalaxy.dto.PartnerInfo;
import com.stitchgalaxy.dto.ProductInfo;
import com.stitchgalaxy.dto.ProductLocalizationInfo;
import com.stitchgalaxy.service.DomainDataService;
import com.stitchgalaxy.service.DomainDataServiceException;
import static com.stitchgalaxy.sg_manager_web.UrlConstants.URL_PRODUCT_REMOVE_THUMBNAIL;
import static com.stitchgalaxy.sg_manager_web.UrlConstants.URL_PRODUCT_UPLOAD_THUMBNAIL;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author tarasev
 */
@Controller
public class ProductController {

    @Autowired
    DomainDataService domainDataService;

    @RequestMapping(value = UrlConstants.URL_PRODUCT_ADD, method = RequestMethod.POST)
    public String addProductCommand(Model model,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "date") String sDate,
            @RequestParam(value = "price") BigDecimal price) throws DomainDataServiceException {
        CommandCreateProduct command = new CommandCreateProduct();
        LocalDate date = LocalDate.parse(sDate);
        command.setDate(date);
        command.setName(name);
        command.setPrice(price);
        domainDataService.createNewProduct(command);
        return "redirect:" + UrlConstants.URL_HOME;
    }

    @RequestMapping(value = UrlConstants.URL_PRODUCT_ADD, method = RequestMethod.GET)
    public String addProduct(Model model) throws DomainDataServiceException {
        UrlConstants.AddUrlConstants(model);
        return "product-new";
    }

    @RequestMapping(value = UrlConstants.URL_PRODUCT_VIEW, method = RequestMethod.GET)
    public String getProduct(Model model,
            @RequestParam(value = "product") Long productId) throws DomainDataServiceException {
        CommandGetProduct command = new CommandGetProduct();
        command.setProductId(productId);
        ProductInfo info = domainDataService.getProductById(command);
        model.addAttribute("product", info);
        UrlConstants.AddUrlConstants(model);
        return "product-view";
    }

    @RequestMapping(value = UrlConstants.URL_PRODUCT_SELECT_AUTHOR, method = RequestMethod.GET)
    public String selectAuthor(Model model,
            @RequestParam(value = "product") Long productId) throws DomainDataServiceException {
        List<PartnerInfo> partners = domainDataService.getAllPartners(new CommandGetPartners());
        model.addAttribute("productId", productId);
        model.addAttribute("partners", partners);
        UrlConstants.AddUrlConstants(model);
        return "authors";
    }

    @RequestMapping(value = UrlConstants.URL_PRODUCT_SELECT_AUTHOR, method = RequestMethod.POST)
    public String addAuthor(Model model,
            @RequestParam(value = "product") Long productId,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "uri") String uri) throws DomainDataServiceException {
        CommandCreatePartner command = new CommandCreatePartner();
        command.setName(name);
        command.setUri(uri);
        domainDataService.addPartner(command);
        return selectAuthor(model, productId);
    }

    @RequestMapping(value = UrlConstants.URL_PRODUCT_ASSIGN_AUTHOR, method = RequestMethod.GET)
    public String assignAuthor(Model model,
            @RequestParam(value = "product") Long productId,
            @RequestParam(value = "partner") Long partnerId
    ) throws DomainDataServiceException {
        CommandAttachProductToPartner command = new CommandAttachProductToPartner();
        command.setPartnerId(partnerId);
        command.setProductId(productId);
        domainDataService.productAssignAuthor(command);
        return "redirect:" + UrlConstants.URL_PRODUCT_VIEW + "?product=" + productId.toString();
    }

    @RequestMapping(value = UrlConstants.URL_PRODUCT_REMOVE_AUTHOR, method = RequestMethod.GET)
    public String removeAuthor(Model model,
            @RequestParam(value = "product") Long productId
    ) throws DomainDataServiceException {
        CommandDetachProductFromPartner command = new CommandDetachProductFromPartner();
        command.setProductId(productId);
        domainDataService.removeProductAuthor(command);
        return "redirect:" + UrlConstants.URL_PRODUCT_VIEW + "?product=" + productId.toString();
    }

    @RequestMapping(value = UrlConstants.URL_PRODUCT_SELECT_TRANSLATOR, method = RequestMethod.GET)
    public String selectTranslator(Model model,
            @RequestParam(value = "product") Long productId) throws DomainDataServiceException {
        List<PartnerInfo> partners = domainDataService.getAllPartners(new CommandGetPartners());
        model.addAttribute("productId", productId);
        model.addAttribute("partners", partners);
        UrlConstants.AddUrlConstants(model);
        return "translators";
    }

    @RequestMapping(value = UrlConstants.URL_PRODUCT_SELECT_TRANSLATOR, method = RequestMethod.POST)
    public String addTranslator(Model model,
            @RequestParam(value = "product") Long productId,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "uri") String uri) throws DomainDataServiceException {
        CommandCreatePartner command = new CommandCreatePartner();
        command.setName(name);
        command.setUri(uri);
        domainDataService.addPartner(command);
        return selectTranslator(model, productId);
    }

    @RequestMapping(value = UrlConstants.URL_PRODUCT_ASSIGN_TRANSLATOR, method = RequestMethod.GET)
    public String assignTranslator(Model model,
            @RequestParam(value = "product") Long productId,
            @RequestParam(value = "partner") Long partnerId
    ) throws DomainDataServiceException {
        CommandAttachProductToPartner command = new CommandAttachProductToPartner();
        command.setPartnerId(partnerId);
        command.setProductId(productId);
        domainDataService.productAssignTranslator(command);
        return "redirect:" + UrlConstants.URL_PRODUCT_VIEW + "?product=" + productId.toString();
    }

    @RequestMapping(value = UrlConstants.URL_PRODUCT_REMOVE_TRANSLATOR, method = RequestMethod.GET)
    public String removeTranslator(Model model,
            @RequestParam(value = "product") Long productId
    ) throws DomainDataServiceException {
        CommandDetachProductFromPartner command = new CommandDetachProductFromPartner();
        command.setProductId(productId);
        domainDataService.removeProductTranslator(command);
        return "redirect:" + UrlConstants.URL_PRODUCT_VIEW + "?product=" + productId.toString();
    }

    @RequestMapping(value = UrlConstants.URL_PRODUCT_ATTACH_CATEGORY, method = RequestMethod.GET)
    public String attachCategory(Model model,
            @RequestParam(value = "product") Long productId,
            @RequestParam(value = "category") Long categoryId) throws DomainDataServiceException {

        CommandAttachProductToCategory command = new CommandAttachProductToCategory();
        command.setCategoryId(categoryId);
        command.setProductId(productId);
        domainDataService.addProductToCategory(command);
        return "redirect:" + UrlConstants.URL_PRODUCT_VIEW + "?product=" + productId.toString();
    }

    @RequestMapping(value = UrlConstants.URL_PRODUCT_DETACH_CATEGORY, method = RequestMethod.GET)
    public String detechCategory(Model model,
            @RequestParam(value = "product") Long productId,
            @RequestParam(value = "category") Long categoryId) throws DomainDataServiceException {

        CommandDetachProductFromCategory command = new CommandDetachProductFromCategory();
        command.setCategoryId(categoryId);
        command.setProductId(productId);
        domainDataService.removeProductFromCategory(command);
        return "redirect:" + UrlConstants.URL_PRODUCT_VIEW + "?product=" + productId.toString();
    }

    @RequestMapping(value = UrlConstants.URL_PRODUCT_SELECT_CATEGORY, method = RequestMethod.GET)
    public String selectCategory(Model model,
            @RequestParam(value = "product") Long productId,
            @RequestParam(value = "category", required = false) Long categoryId) throws DomainDataServiceException {

        CategoryInfoDTO category = null;
        if (categoryId == null) {
            category = domainDataService.getRootCategory(new CommandGetRootCategory());
        } else {
            CommandGetCategory command = new CommandGetCategory();
            command.setCategoryId(categoryId);
            category = domainDataService.getCategoryById(command);
        }
        model.addAttribute("category", category);
        model.addAttribute("product", productId);
        UrlConstants.AddUrlConstants(model);
        return "category-view";
    }

    @RequestMapping(value = UrlConstants.URL_CATEGORY_ADD, method = RequestMethod.POST)
    public String addCategory(Model model,
            @RequestParam(value = "category") Long categoryId,
            @RequestParam(value = "product") Long productId,
            @RequestParam(value = "name") String name,
            RedirectAttributes redirectAttributes) throws DomainDataServiceException {

        CommandCreateSubcategory command = new CommandCreateSubcategory();
        command.setCategoryId(categoryId);
        command.setName(name);
        domainDataService.createSubcategory(command);
        redirectAttributes.addAttribute("category", categoryId);

        return "redirect:" + UrlConstants.URL_PRODUCT_SELECT_CATEGORY + "?product=" + productId + "&category=" + categoryId;
    }

    @RequestMapping(value = UrlConstants.URL_CATEGORY_REMOVE, method = RequestMethod.GET)
    public String removeCategory(Model model,
            @RequestParam(value = "category") Long categoryId,
            @RequestParam(value = "product") Long productId,
            @RequestParam(value = "sub-category") Long subCategoryId,
            RedirectAttributes redirectAttributes) throws DomainDataServiceException {
        CommandRemoveSubcategory command = new CommandRemoveSubcategory();
        command.setCategoryId(categoryId);
        command.setSubCategoryId(subCategoryId);
        domainDataService.removeSubcategory(command);
        redirectAttributes.addAttribute("category", categoryId);

        return "redirect:" + UrlConstants.URL_PRODUCT_SELECT_CATEGORY + "?product=" + productId + "&category=" + categoryId;
    }

    @RequestMapping(value = UrlConstants.URL_PRODUCT_LOCALIZATION_REMOVE, method = RequestMethod.GET)
    public String removeLocalization(Model model,
            @RequestParam(value = "product") Long productId,
            @RequestParam(value = "locale") String locale) throws DomainDataServiceException {
        CommandRemoveProductLocalization command = new CommandRemoveProductLocalization();
        command.setLocale(locale);
        command.setProductId(productId);
        domainDataService.removeProductLocalization(command);
        return "redirect:" + UrlConstants.URL_PRODUCT_VIEW + "?product=" + productId;
    }

    @RequestMapping(value = UrlConstants.URL_PRODUCT_LOCALIZATION_NEW, method = RequestMethod.GET)
    public String addLocalizationForm(Model model,
            @RequestParam(value = "product") Long productId) throws DomainDataServiceException {
        return "product-localization-new";
    }

    private final String EMPTY_STRING = "";

    @RequestMapping(value = UrlConstants.URL_PRODUCT_LOCALIZATION_NEW, method = RequestMethod.POST)
    public String addLocalization(Model model,
            @RequestParam(value = "product") Long productId,
            @RequestParam(value = "locale") String locale) throws DomainDataServiceException {
        CommandStoreProductLocalization command = new CommandStoreProductLocalization();
        command.setProductId(productId);
        ProductLocalizationInfo li = new ProductLocalizationInfo();
        li.setLocale(locale);
        li.setDescription(EMPTY_STRING);
        li.setName(EMPTY_STRING);
        li.setTags(EMPTY_STRING);
        command.setProductLocalization(li);
        domainDataService.storeProductLocalization(command);
        return "redirect:" + UrlConstants.URL_PRODUCT_VIEW + "?product=" + productId;
    }

    @RequestMapping(value = UrlConstants.URL_PRODUCT_LOCALIZATION_EDIT, method = RequestMethod.GET)
    public String getProductLocalization(Model model,
            @RequestParam(value = "product") Long productId,
            @RequestParam(value = "locale") String locale) throws DomainDataServiceException {
        CommandGetProductLocalization command = new CommandGetProductLocalization();
        command.setProductId(productId);
        command.setLocale(locale);
        ProductLocalizationInfo li = domainDataService.getProductLocalization(command);
        model.addAttribute("locale", li);
        return "product-localization-edit";
    }

    @RequestMapping(value = UrlConstants.URL_PRODUCT_LOCALIZATION_EDIT, method = RequestMethod.POST)
    public String editProductLocalization(Model model,
            @RequestParam(value = "product") Long productId,
            @RequestParam(value = "locale") String locale,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "description") String description,
            @RequestParam(value = "tags") String tags) throws DomainDataServiceException {
        CommandStoreProductLocalization command = new CommandStoreProductLocalization();
        command.setProductId(productId);
        ProductLocalizationInfo li = new ProductLocalizationInfo();
        li.setLocale(locale);
        li.setName(name);
        li.setDescription(description);
        li.setTags(tags);
        command.setProductLocalization(li);
        domainDataService.storeProductLocalization(command);
        return "redirect:" + UrlConstants.URL_PRODUCT_VIEW + "?product=" + productId;
    }

    @RequestMapping(value = UrlConstants.URL_PRODUCT_UPLOAD_THUMBNAIL, method = RequestMethod.POST)
    public String uploadThumbnail(Model model,
            @RequestParam(value = "product") Long productId,
            @RequestParam("file") Part file) throws DomainDataServiceException, IOException {
        CommandUploadProductFile command = new CommandUploadProductFile();
        command.setProductId(productId);
        command.setFileType(FileType.THUMBNAIL);
        InputStream filecontent = file.getInputStream();
        command.setFileContent(filecontent);
        domainDataService.uploadProductFile(command);
        return "redirect:" + UrlConstants.URL_PRODUCT_VIEW + "?product=" + productId;
    }

    @RequestMapping(value = UrlConstants.URL_PRODUCT_REMOVE_THUMBNAIL, method = RequestMethod.GET)
    public String removeThumbnail(Model model,
            @RequestParam(value = "product") Long productId) throws DomainDataServiceException {
        CommandRemoveProductFile command = new CommandRemoveProductFile();
        command.setProductId(productId);
        command.setFileType(FileType.THUMBNAIL);
        domainDataService.removeProductFile(command);
        return "redirect:" + UrlConstants.URL_PRODUCT_VIEW + "?product=" + productId;
    }
    
    @RequestMapping(value = UrlConstants.URL_PRODUCT_UPLOAD_IMAGE, method = RequestMethod.POST)
    public String uploadImage(Model model,
            @RequestParam(value = "product") Long productId,
            @RequestParam("file") Part file) throws DomainDataServiceException, IOException {
        CommandUploadProductFile command = new CommandUploadProductFile();
        command.setProductId(productId);
        command.setFileType(FileType.IMAGE);
        InputStream filecontent = file.getInputStream();
        command.setFileContent(filecontent);
        domainDataService.uploadProductFile(command);
        return "redirect:" + UrlConstants.URL_PRODUCT_VIEW + "?product=" + productId;
    }

    @RequestMapping(value = UrlConstants.URL_PRODUCT_REMOVE_IMAGE, method = RequestMethod.GET)
    public String removeImage(Model model,
            @RequestParam(value = "product") Long productId) throws DomainDataServiceException {
        CommandRemoveProductFile command = new CommandRemoveProductFile();
        command.setProductId(productId);
        command.setFileType(FileType.IMAGE);
        domainDataService.removeProductFile(command);
        return "redirect:" + UrlConstants.URL_PRODUCT_VIEW + "?product=" + productId;
    }
    
    @RequestMapping(value = UrlConstants.URL_PRODUCT_UPLOAD_PROTOTYPE, method = RequestMethod.POST)
    public String uploadPrototype(Model model,
            @RequestParam(value = "product") Long productId,
            @RequestParam("file") Part file) throws DomainDataServiceException, IOException {
        CommandUploadProductFile command = new CommandUploadProductFile();
        command.setProductId(productId);
        command.setFileType(FileType.PROTOTYPE);
        InputStream filecontent = file.getInputStream();
        command.setFileContent(filecontent);
        domainDataService.uploadProductFile(command);
        return "redirect:" + UrlConstants.URL_PRODUCT_VIEW + "?product=" + productId;
    }

    @RequestMapping(value = UrlConstants.URL_PRODUCT_REMOVE_PROTOTYPE, method = RequestMethod.GET)
    public String removePrototype(Model model,
            @RequestParam(value = "product") Long productId) throws DomainDataServiceException {
        CommandRemoveProductFile command = new CommandRemoveProductFile();
        command.setProductId(productId);
        command.setFileType(FileType.PROTOTYPE);
        domainDataService.removeProductFile(command);
        return "redirect:" + UrlConstants.URL_PRODUCT_VIEW + "?product=" + productId;
    }
    
    @RequestMapping(value = UrlConstants.URL_PRODUCT_UPLOAD_COMPLETE_PRODUCT, method = RequestMethod.POST)
    public String uploadCompleteProduct(Model model,
            @RequestParam(value = "product") Long productId,
            @RequestParam("file") Part file) throws DomainDataServiceException, IOException {
        CommandUploadProductFile command = new CommandUploadProductFile();
        command.setProductId(productId);
        command.setFileType(FileType.COMPLETE_PRODUCT);
        InputStream filecontent = file.getInputStream();
        command.setFileContent(filecontent);
        domainDataService.uploadProductFile(command);
        return "redirect:" + UrlConstants.URL_PRODUCT_VIEW + "?product=" + productId;
    }

    @RequestMapping(value = UrlConstants.URL_PRODUCT_REMOVE_COMPLETE_PRODUCT, method = RequestMethod.GET)
    public String removeCompleteProduct(Model model,
            @RequestParam(value = "product") Long productId) throws DomainDataServiceException {
        CommandRemoveProductFile command = new CommandRemoveProductFile();
        command.setProductId(productId);
        command.setFileType(FileType.COMPLETE_PRODUCT);
        domainDataService.removeProductFile(command);
        return "redirect:" + UrlConstants.URL_PRODUCT_VIEW + "?product=" + productId;
    }
    
    @RequestMapping(value = UrlConstants.URL_PRODUCT_UPLOAD_DESIGN, method = RequestMethod.POST)
    public String uploadDesign(Model model,
            @RequestParam(value = "product") Long productId,
            @RequestParam("file") Part file) throws DomainDataServiceException, IOException {
        CommandUploadProductFile command = new CommandUploadProductFile();
        command.setProductId(productId);
        command.setFileType(FileType.DESIGN);
        InputStream filecontent = file.getInputStream();
        command.setFileContent(filecontent);
        domainDataService.uploadProductFile(command);
        return "redirect:" + UrlConstants.URL_PRODUCT_VIEW + "?product=" + productId;
    }

    @RequestMapping(value = UrlConstants.URL_PRODUCT_REMOVE_DESIGN, method = RequestMethod.GET)
    public String removeDesign(Model model,
            @RequestParam(value = "product") Long productId) throws DomainDataServiceException {
        CommandRemoveProductFile command = new CommandRemoveProductFile();
        command.setProductId(productId);
        command.setFileType(FileType.DESIGN);
        domainDataService.removeProductFile(command);
        return "redirect:" + UrlConstants.URL_PRODUCT_VIEW + "?product=" + productId;
    }
    
    @RequestMapping(value = UrlConstants.URL_PRODUCT_EDIT, method = RequestMethod.GET)
    public String getProductDetails(Model model,
            @RequestParam(value = "product") Long productId) throws DomainDataServiceException {
        CommandGetProduct command = new CommandGetProduct();
        command.setProductId(productId);
        ProductInfo product = domainDataService.getProductById(command);
        model.addAttribute("product", product);
        UrlConstants.AddUrlConstants(model);
        return "product-edit";
    }

    @RequestMapping(value = UrlConstants.URL_PRODUCT_EDIT, method = RequestMethod.POST)
    public String editProductInformation(Model model,
            @RequestParam(value = "product") Long productId,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "price") BigDecimal price,
            @RequestParam(value = "date") String sDate,
            @RequestParam(value = "blocked") Boolean blocked,
            @RequestParam(value = "description") String description,
            @RequestParam(value = "sales") Long sales,
            @RequestParam(value = "rates") Long rates,
            @RequestParam(value = "rating") Long rating,
            @RequestParam(value = "complexity") Integer complexity,
            @RequestParam(value = "tags") String tags,
            @RequestParam(value = "color") String sAvgColor,
            @RequestParam(value = "width") Integer width,
            @RequestParam(value = "height") Integer height,
            @RequestParam(value = "colors") Integer colors,
            @RequestParam(value = "canvas") String canvas,
            @RequestParam(value = "threads") String threads,
            @RequestParam(value = "stitchesPerInch") BigDecimal stitchesPerInch
    ) throws DomainDataServiceException {
        return "redirect:" + UrlConstants.URL_PRODUCT_VIEW + "?product=" + productId;
    }
}
