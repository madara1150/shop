package com.example.shopservice.service;

import com.example.shopservice.pojo.Product;
import com.example.shopservice.pojo.Shop;
import com.example.shopservice.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class ShopService {

    @Autowired
    private ShopRepository shopRepository;

    public List<Shop> getAllShops() {
        return shopRepository.findAll();
    }

    public Optional<Shop> getShopById(String id) {
        return shopRepository.findById(id);
    }

    public Shop createShop(Shop shop) {

        if(shop.getTitle() == null || shop.getTitle().isEmpty()){
            throw new IllegalArgumentException("'title' ไม่สามารถเป็นค่าว่างได้");
        }
        if(shop.getOwner_id() == null || shop.getOwner_id().isEmpty()){
            throw new IllegalArgumentException("'Owner_id' ไม่สามารถเป็นค่าว่างได้");
        }

        if(shop.getAccount() == null || shop.getAccount().isEmpty()){
            throw new IllegalArgumentException("'account' ไม่สามารถเป็นค่าว่างได้");
        }

        if (shopRepository.findByOwnerId(shop.getOwner_id()).size() != 0) {
            throw new IllegalArgumentException("ผู้ใช้นี้มีร้านค้าอื่นอยู่แล้ว");
        }

        return (Shop) shopRepository.save(shop);
    }

    public Shop updateShopWithUserId(String user_id, Shop newShopData) {
        List<Shop> shopList = shopRepository.findByOwnerId(user_id);

        if (!shopList.isEmpty()) {
            Shop existingShop = shopList.get(0);

            if (newShopData.getTitle() != null) {
                existingShop.setTitle(newShopData.getTitle());
            }
            if (newShopData.getAccount() != null) {
                existingShop.setAccount(newShopData.getAccount());
            }
            existingShop.setEdit_at(String.valueOf(LocalDateTime.now(ZoneId.of("Asia/Bangkok"))));
            return shopRepository.save(existingShop);
        } else {
            return null;
        }
    }

    public boolean deleteShop(String id) {
        if (!shopRepository.findById(id).isEmpty()) {
            shopRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Shop> getShopByOwnerId(String owner_id){
        return shopRepository.findByOwnerId(owner_id);
    }



}
