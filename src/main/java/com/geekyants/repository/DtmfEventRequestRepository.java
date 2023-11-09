package com.geekyants.repository;

import com.geekyants.entity.DtmfEventRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DtmfEventRequestRepository extends MongoRepository<DtmfEventRequest, String> {
}
