package com.example.demo.repository;

import com.example.demo.model.Admin;
import com.example.demo.model.User;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface AdminRepository extends MongoRepository<Admin, String> {

}
