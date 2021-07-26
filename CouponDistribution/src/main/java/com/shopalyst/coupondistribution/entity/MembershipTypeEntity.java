package com.shopalyst.coupondistribution.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "membership_types")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MembershipTypeEntity {
	@Id
	private String membershipType;
	private Integer redumptionLimit;
}
