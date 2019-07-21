package com.product.productcsvwritter.dao.repo;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.product.productcsvwritter.dao.model.Product;

public interface ProductRepository extends MongoRepository < Product, String > {
 
	Product findByUuId(String uuId);
}