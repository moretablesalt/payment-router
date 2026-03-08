package com.prudentialguarantee.paymentrouter.repository;

import com.prudentialguarantee.paymentrouter.domain.DatafeedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DatafeedEventRepository extends JpaRepository<DatafeedEvent, Long> {
}
