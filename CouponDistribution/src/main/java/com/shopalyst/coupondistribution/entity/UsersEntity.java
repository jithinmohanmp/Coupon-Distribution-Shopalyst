package com.shopalyst.coupondistribution.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UsersEntity {
	@Id
	private String userId;
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membership_type")
	private MembershipTypeEntity membershipType;
	@OneToMany(mappedBy="user")
	private List<CouponsEntity> coupons;
}
