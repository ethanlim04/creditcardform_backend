package com.example.demo.respository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.TableEntity;

public interface dbRepository extends JpaRepository<TableEntity, Long>{
    // TableEntity queryUser()
}
