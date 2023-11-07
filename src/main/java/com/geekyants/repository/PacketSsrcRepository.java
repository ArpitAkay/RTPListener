package com.geekyants.repository;

import com.geekyants.entity.PacketSsrc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacketSsrcRepository extends MongoRepository<PacketSsrc, String> {
}
