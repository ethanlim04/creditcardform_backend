package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.TableEntity;
import com.example.demo.service.dbService;


@RestController
@RequestMapping("/api")
// @CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin(origins = "https://github.com/ethanlim04/creditcardform")

public class controller {

    private final dbService db;

    @Autowired
    public controller(dbService db) {
        this.db = db;
    }

    @PostMapping("/processInput")
    public ResponseEntity<String> processInput(@RequestBody FormData data) {
        System.out.println("Recieved Process Request");
        db.writeDb(data);
        return ResponseEntity.ok("Processed Request");
    }

    @PostMapping("/updateInput")
    public ResponseEntity<String> updateInput(@RequestBody FormData data) {
        System.out.println("Recieved Update Request");
        System.out.println(data.phone);

        db.updateDb(getID(data.phone), data);
        return ResponseEntity.ok("Updated Request");
    }

    @PostMapping("/user")
    public ResponseEntity<TableEntity> user(@RequestBody String phone) {
        System.out.println("Recieved Request");
        System.out.println(phone);

        TableEntity res = db.getDb(getID(phone));
        return ResponseEntity.ok(res);
    }

    
    private long getID(String s) {
        long res = 0;
        int length = 9;
        for(char c : s.toCharArray()) {
            // res += (int) c;
            if(c != '(' || c != ')' || c != ' ') {
                int num = (c - '0'); //convert phone to decimal
                res += num * Math.pow(10, length);
                length--;
            }
        }
        return res;
    }
}