package com.shopalyst.coupondistribution.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shopalyst.coupondistribution.dto.CouponDto;
import com.shopalyst.coupondistribution.dto.CouponsDto;
import com.shopalyst.coupondistribution.service.CouponService;

@RestController("/")
public class CouponRestController {
	private static final Logger LOG = LogManager.getLogger(CouponRestController.class);
	@Autowired
	CouponService couponService;

	@PostMapping("assigncoupon")
	public CouponDto assignCoupon(@Valid @RequestBody final CouponDto request) {
		LOG.info(request);
		return couponService.getUnclaimedCouponForUser(request.getUserId());
	}

	@GetMapping("coupons/{userId}")
	public CouponsDto getCoupons(
			@PathVariable("userId") @NotBlank(message = "userId should not be blank") final String userId) {
		LOG.info(userId);
		return couponService.getCouponsForUser(userId);
	}

}
