package com.shopalyst.coupondistribution.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.shopalyst.coupondistribution.enums.CouponStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "coupons")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CouponsEntity {
	@Id
	private String couponCode;
	@Temporal(TemporalType.DATE)
	private Date createdDate;
	@Enumerated(EnumType.STRING)
	private CouponStatus status;
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
	private UsersEntity user;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;
}
