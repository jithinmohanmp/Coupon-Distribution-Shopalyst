package com.shopalyst.coupondistribution.service;

import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.shopalyst.coupondistribution.entity.CouponsEntity;
import com.shopalyst.coupondistribution.entity.UsersEntity;
import com.shopalyst.coupondistribution.exception.ApplicationException;
import com.shopalyst.coupondistribution.repository.MembershipTypeRepository;
import com.shopalyst.coupondistribution.repository.UsersRepository;

@Service
public class UserService {
	private static final Logger LOG = LogManager.getLogger(UserService.class);
	@Value("${membershiptype.default}")
	private String defaultMembershipType;
	@Autowired
	private UsersRepository usersRepo;
	@Autowired
	private MembershipTypeRepository membershipTypeRepo;

	public UsersEntity fetchOrCreateUser(final String userId) {
		final Optional<UsersEntity> userOptional = usersRepo.findById(userId);
		UsersEntity user;
		if (userOptional.isPresent()) {
			user = userOptional.get();
		} else {
			LOG.info("User with id : {} not present. Creating GUEST..", userId);
			user = new UsersEntity(userId, membershipTypeRepo.getById(defaultMembershipType), null);
			usersRepo.saveAndFlush(user);
		}
		return user;
	}

	public UsersEntity fetchUser(final String userId) {
		final Optional<UsersEntity> userOptional = usersRepo.findById(userId);
		return userOptional.orElseThrow(() -> new ApplicationException(ApplicationException.USER_UNAVAILABLE));
	}

	public Boolean validateRedumptionLimit(final UsersEntity user) {

		final Integer redumptionLimit = user.getMembershipType().getRedumptionLimit();
		final List<CouponsEntity> coupons = user.getCoupons();
		final Integer assignedCoupons = CollectionUtils.size(coupons);
		LOG.info("Userid : {} AssignedCoupons : {} Limit : {}", user.getUserId(), assignedCoupons, redumptionLimit);
		if (assignedCoupons >= redumptionLimit) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
}
