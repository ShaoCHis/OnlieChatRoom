package com.example.demo.repository;

import com.example.demo.model.Friends;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created By ShaoCHi
 * Date 2021/12/19 8:17 PM
 * Tongji University
 */

public interface FriendsRepository extends JpaRepository<Friends,Integer> {
}
