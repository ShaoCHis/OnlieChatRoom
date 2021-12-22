package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Created By ShaoCHi
 * Date 2021/12/19 7:38 PM
 * Tongji University
 */


public interface UserRepository extends CrudRepository<User,String> {
}
