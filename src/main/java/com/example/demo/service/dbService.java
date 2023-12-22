package com.example.demo.service;
import java.lang.reflect.Field;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.TableEntity;
import com.example.demo.controller.FormData;
import com.example.demo.respository.dbRepository;

import jakarta.transaction.Transactional;

import java.util.Optional;

@Service
public class dbService {

    private final dbRepository repo;


    @Autowired
    public dbService(dbRepository in) {
        this.repo = in;
    }


    public void writeDb(FormData in) {
        TableEntity obj = new TableEntity(in);
        System.out.println(obj);
        System.out.println(obj.id);
        System.out.println(obj.firstname);
        this.repo.save(obj);
    }

    public TableEntity getDb(long id) {
        Optional<TableEntity> res = (this.repo.findById(id));
        return res.orElseThrow(() -> new RuntimeException("Entity not found with id: " + id));
    }

    @Transactional
    public void updateDb(long id, FormData in) {
        this.repo.deleteById(id);
        this.writeDb(in);
    }

}
