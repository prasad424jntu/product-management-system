package com.product.productcsvwritter.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.product.productcsvwritter.dao.model.Product;
import com.product.productcsvwritter.dao.repo.ProductRepository;

@Service
public class CsvAggregatorService {
	
	@Autowired
	ProductRepository productRepository;
	
	@KafkaListener(topics="product",containerFactory="kafkaListenerContainerFactory",groupId="group-id")
	public void consumeMessage(String message) throws JsonParseException, JsonMappingException, IOException {
		System.out.println("product read from kafka  " + message);
		ObjectMapper mapper = new ObjectMapper()
				.enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)
		        .registerModule(new ParameterNamesModule(Mode.PROPERTIES));;
		Product product = mapper.readValue(message, Product.class);
		//productRepository.save(product);
			if(!StringUtils.isEmpty(product.getUuId())) {
				if(productRepository.findByUuId(product.getUuId()) != null)
				{
					Product existingProduct = productRepository.findByUuId(product.getUuId());
					productRepository.save(existingProduct.builder()
											.uuId(existingProduct.getUuId())
											.available(existingProduct.getAvailable())
											.description(existingProduct.getDescription())
											.measurementUnits(existingProduct.getMeasurementUnits())
											.name(existingProduct.getName())
											.provider(existingProduct.getProvider())
											.modifiedDate(LocalDate.now().toString())
											.createdDate(existingProduct.getCreatedDate().toString())
											.build());
				}else {
					product.setCreatedDate(LocalDate.now().toString());
					productRepository.save(product);
				}
			}
		
	}
	
	public Map<String, Map<String, Long>>  getProductStatistics(){
		List<Product> productList = productRepository.findAll();
		
		Map<String, Long> createdList =
				productList.stream().filter(p -> p.getCreatedDate()!=null).collect(
                        Collectors.groupingBy(Product::getCreatedDate,
                                Collectors.counting())
                        );
		Map<String, Long> updateList =
				productList.stream().filter(p -> p.getModifiedDate()!=null).collect(
                        Collectors.groupingBy(Product::getModifiedDate,
                                Collectors.counting())
                        );
		Map<String, Map<String, Long>> totalList = new HashMap<String, Map<String, Long>>();
		totalList.put("Created Count by Date", createdList);
		totalList.put("Updated List by Date", updateList);
		//createdList.forEach((key, value) -> totalList.merge(key, value.toString(), (v1, v2) -> (v1 == v2) ? "Created Count :"+v1 : "Created Count :"+v1 + ","+ "Updated Count :" + v2)	 );
		//updateList.forEach((key, value) -> totalList.merge(key, value.toString(), (v1, v2) -> (v1 == v2) ? "Created Count :" + v1 : "Created Count :"+v1 + "," + "Updated Count :"+v2)	 );
		return totalList;
	}
	
	
	

}
