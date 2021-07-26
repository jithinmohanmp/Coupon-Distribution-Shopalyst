package com.shopalyst.coupondistribution.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopalyst.coupondistribution.entity.CouponsEntity;
import com.shopalyst.coupondistribution.enums.CouponStatus;

@Repository
public interface CouponsRepository extends JpaRepository<CouponsEntity, String> {
	Optional<CouponsEntity> findFirstByStatus(CouponStatus status);
}
