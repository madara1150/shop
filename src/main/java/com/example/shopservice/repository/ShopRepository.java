package com.example.shopservice.repository;

import com.example.shopservice.pojo.Shop;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ShopRepository extends MongoRepository<Shop, String> {
    @Query(value = "{owner_id:  '?0'}")
    List<Shop> findByOwnerId(String owner_id);

}
