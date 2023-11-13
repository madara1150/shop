package com.example.shopservice.controller;
import com.example.shopservice.pojo.Product;
import com.example.shopservice.pojo.Shop;
import com.example.shopservice.pojo.User;
import com.example.shopservice.repository.JwtService;
import com.example.shopservice.service.ErrorResponse;
import com.example.shopservice.service.ShopService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
@CrossOrigin
@RestController
@RequestMapping("/api")
public class ShopController {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private ShopService shopService;


    @GetMapping("/shops")
    public ResponseEntity<?> getAllShops() {
        return ResponseEntity.ok(shopService.getAllShops());
    }

    @GetMapping("/shops/{id}")
    public ResponseEntity<?> getShopById(@PathVariable String id) {
        if(shopService.getShopById(id).isEmpty() || shopService.getShopById(id) == null){
            ErrorResponse errorResponse = new ErrorResponse("ไม่พบ Shop", "ไม่พบ Shop ที่ต้องการ");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        return ResponseEntity.ok(shopService.getShopById(id));
    }

    private ResponseEntity<User> ChangeUser(String bearerToken) {
        String apiUrl = "https://user2-908649839259189283.rcf2.deploys.app/api/shop";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + bearerToken);

        RestTemplate restTemplate = new RestTemplate();
        org.springframework.http.HttpEntity<String> requestEntity = new org.springframework.http.HttpEntity<>(headers);

        return restTemplate.exchange(apiUrl, HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<User>() {});
    }

    @PostMapping("/shops")
    public ResponseEntity<?> createShop(@RequestBody Shop shop, @RequestHeader("Authorization") String token) {
        String[] tokens = token.split(" ");
        if (tokens.length > 1) {
            Claims claims = jwtService.parseToken(tokens[1]);
            shop.setOwner_id(claims.getSubject());
        }
        else {
            return ResponseEntity.status(404).body("ไม่พบ token");
        }
        try {
            ResponseEntity<User> user = ChangeUser(tokens[1]);
            return ResponseEntity.ok(shopService.createShop(shop));
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("ไม่สามารถเพิ่ม Shop ใหม่ได้", e.getMessage());
            return ResponseEntity.status(404).body(errorResponse);
        }
    }

    @PutMapping("/shops")
    public ResponseEntity<?> updateShop(@RequestBody Shop shop, @RequestHeader("Authorization") String token) {
        String[] tokens = token.split(" ");
        if (tokens.length > 1) {
            Claims claims = jwtService.parseToken(tokens[1]);
            shop.setOwner_id(claims.getSubject());
        }
        else {
            return ResponseEntity.status(404).body("ไม่พบ token");
        }
        try {
            Claims claims = jwtService.parseToken(tokens[1]);
            return ResponseEntity.ok(shopService.updateShopWithUserId(claims.getSubject(), shop));
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("ไม่สามารถอัปเดต Shop ใหม่ได้", e.getMessage());
            return ResponseEntity.status(404).body(errorResponse);
        }
    }

    @DeleteMapping("/shops/{id}")
    public ResponseEntity<?> deleteShop(@PathVariable String id) {
        if(shopService.deleteShop(id)){
            return ResponseEntity.ok(true);
        }
        ErrorResponse errorResponse = new ErrorResponse("ไม่สามารถลบ shop ได้","ไอดีของ shop ไม่ถูกต้อง");
        return ResponseEntity.status(404).body(errorResponse);
    }



    @GetMapping("/shops/me")
    public ResponseEntity<?> getShopByOwnerId(@RequestHeader("Authorization") String token) {
        String[] tokens = token.split(" ");
        try {
                Claims claims = jwtService.parseToken(tokens[1]);

            return ResponseEntity.ok(shopService.getShopByOwnerId(claims.getSubject()));
        }
        catch (Exception e){
            ErrorResponse errorResponse = new ErrorResponse("เกิดข้อผิดพลาดไม่พบ shop", e.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

}
