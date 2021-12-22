package com.example.demo.repository;

import com.example.demo.model.FriendMessage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created By ShaoCHi
 * Date 2021/12/19 10:42 PM
 * Tongji University
 */
public interface FriendMessageRepository extends JpaRepository<FriendMessage,Integer> {
}
