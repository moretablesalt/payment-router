package com.prudentialguarantee.paymentrouter.repository;

import com.prudentialguarantee.paymentrouter.domain.DatafeedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DatafeedEventRepository extends JpaRepository<DatafeedEvent, Long> {
    List<DatafeedEvent> findTop50ByStatusOrderByReceivedAt(String status);
}
