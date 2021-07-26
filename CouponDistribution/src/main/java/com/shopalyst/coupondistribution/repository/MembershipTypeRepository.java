package com.shopalyst.coupondistribution.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopalyst.coupondistribution.entity.MembershipTypeEntity;

@Repository
public interface MembershipTypeRepository extends JpaRepository<MembershipTypeEntity, String> {

}
