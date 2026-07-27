package com.monamatti.controller;

import com.monamatti.entity.Product;
import com.monamatti.service.ProductService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    private static final String REDIRECT_ADMIN = "redirect:/admin";
    private static final String ATTR_PRODUCT = "product";
    private static final String VIEW_DASHBOARD = "admin/dashboard";

    private final ProductService productService;

    public AdminController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("")
    public String dashboard(Model model) {
        log.debug("Serving Admin Dashboard CMS console request");
        model.addAttribute(ATTR_PRODUCT, productService.getFeaturedProduct());
        return VIEW_DASHBOARD;
    }

    @PostMapping("/product/save")
    public String saveProduct(@Valid @ModelAttribute(ATTR_PRODUCT) Product product,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.warn("Validation error while saving product: {}", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating product details.");
            return REDIRECT_ADMIN;
        }
        productService.saveProduct(product);
        log.info("Product details saved successfully: {}", product.getName());
        redirectAttributes.addFlashAttribute("successMessage", "Product updated successfully!");
        return REDIRECT_ADMIN;
    }
}
