package com.monamatti.controller;

import com.monamatti.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    private static final String ATTR_PRODUCT = "product";
    private static final String VIEW_INDEX = "index";

    private final ProductService productService;

    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String index(Model model) {
        log.debug("Serving public notebook homepage request");
        model.addAttribute(ATTR_PRODUCT, productService.getFeaturedProduct());
        return VIEW_INDEX;
    }
}
