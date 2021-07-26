package com.shopalyst.coupondistribution.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopalyst.coupondistribution.entity.UsersEntity;

public interface UsersRepository extends JpaRepository<UsersEntity, String> {

}
