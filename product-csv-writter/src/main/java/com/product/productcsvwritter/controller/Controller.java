package com.product.productcsvwritter.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.product.productcsvwritter.dao.model.Product;
import com.product.productcsvwritter.dao.repo.ProductRepository;
import com.product.productcsvwritter.service.CsvAggregatorService;

@RestController
@RequestMapping("/pms")
public class Controller {

	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	CsvAggregatorService service;
	
	  @RequestMapping(value = "/list", method = RequestMethod.GET)
	  public List<Product> getProduct() {
	   return productRepository.findAll();
	  }
	  
	  @RequestMapping(value = "/stats", method = RequestMethod.GET)
	  public Map<String, Map<String, Long>> getProductStats() {
	   return service.getProductStatistics();
	  }
}
