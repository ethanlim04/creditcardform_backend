package com.example.demo;

import com.example.demo.controller.FormData;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class TableEntity{
    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public String firstname; 
    public String midname; 
    public String lastname; 
  
    public String card_firstname; 
    public String card_midname; 
    public String card_lastname; 
    public String exp;

    public String card_num;
    public String card_type;

    public String authorized_payment;

    public String phone;
    public String email;
  
    public String addr; 
    public String addr_type; 
    public String addr_num;
    public String city; 
    public String zip;
    public String state;

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

    public TableEntity() {};
    
    public TableEntity(FormData in) {
        // this.id = getID(in.firstname + in.midname + in.lastname);
        this.id = getID(in.phone);

        this.firstname = in.firstname; 
        this.midname = in.midname; 
        this.lastname = in.lastname; 

        this.card_firstname = in.card_firstname; 
        this.card_midname = in.card_midname; 
        this.card_lastname = in.card_lastname; 
        this.exp = in.exp;

        this.card_num = in.card_num;
        this.card_type = in.card_type;

        this.authorized_payment = in.authorized_payment;

        this.phone = in.phone;
        this.email = in.email;
    
        this.addr = in.addr; 
        this.addr_type = in.addr_type; 
        this.addr_num = in.addr_num;
        this.city = in.city; 
        this.zip = in.zip;
        this.state = in.state;
    }

}
