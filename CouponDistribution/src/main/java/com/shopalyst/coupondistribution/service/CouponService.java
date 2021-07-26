package com.shopalyst.coupondistribution.service;

import static com.shopalyst.coupondistribution.exception.ApplicationException.NO_COUPONS_AVAILABLE;
import static com.shopalyst.coupondistribution.exception.ApplicationException.REDUMTION_LIMIT_EXCEEDED;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopalyst.coupondistribution.dto.CouponDto;
import com.shopalyst.coupondistribution.dto.CouponsDto;
import com.shopalyst.coupondistribution.entity.CouponsEntity;
import com.shopalyst.coupondistribution.entity.UsersEntity;
import com.shopalyst.coupondistribution.enums.CouponStatus;
import com.shopalyst.coupondistribution.exception.ApplicationException;
import com.shopalyst.coupondistribution.repository.CouponsRepository;

@Service
public class CouponService {
	private static final Logger LOG = LogManager.getLogger(CouponService.class);
	@Autowired
	private UserService userService;
	@Autowired
	private CouponsRepository couponsRepo;

	public CouponDto getUnclaimedCouponForUser(final String userId) {

		final UsersEntity user = userService.fetchOrCreateUser(userId);
		if (!userService.validateRedumptionLimit(user)) {
			LOG.error("User exceeded redumption limit!!");
			throw new ApplicationException(REDUMTION_LIMIT_EXCEEDED);
		}
		final CouponsEntity coupon = fetchAndAssignCoupon(user);

		return new CouponDto(coupon.getCouponCode(), userId, coupon.getUpdatedDate());
	}

	public CouponsDto getCouponsForUser(final String userId) {
		final UsersEntity user = userService.fetchUser(userId);
		if (CollectionUtils.isNotEmpty(user.getCoupons())) {
			final List<CouponDto> coupons = user.getCoupons().stream().map(
					couponEntity -> new CouponDto(couponEntity.getCouponCode(), null, couponEntity.getUpdatedDate()))
					.collect(Collectors.toList());
			return new CouponsDto(coupons, userId);
		}
		throw new ApplicationException(ApplicationException.NO_COUPONS_ASSIGNMENTS);
	}

	private synchronized CouponsEntity fetchAndAssignCoupon(final UsersEntity user) {
		final Optional<CouponsEntity> couponOptional = couponsRepo.findFirstByStatus(CouponStatus.UNCLAIMED);
		if (!couponOptional.isPresent()) {
			throw new ApplicationException(NO_COUPONS_AVAILABLE);
		}
		final CouponsEntity coupon = couponOptional.get();
		coupon.setUser(user);
		coupon.setStatus(CouponStatus.CLAIMED);
		coupon.setUpdatedDate(new Date());
		couponsRepo.saveAndFlush(coupon);
		return coupon;
	}

}
