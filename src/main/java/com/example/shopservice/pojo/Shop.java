package com.example.shopservice.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static java.time.LocalTime.now;

@Data
@Document("shop")
public class Shop implements Serializable {
    @Id
    private String _id;
    private String title;
    private String owner_id;
    private String account;
    private String edit_at;
    private String create_at;

    public Shop(String _id, String title, String owner_id, String account, String edit_at) {
        this._id = _id;
        this.title = title;
        this.owner_id = owner_id;
        this.account = account;
        this.edit_at = edit_at;
        this.create_at = String.valueOf(LocalDateTime.now(ZoneId.of("Asia/Bangkok")));
    }

}


