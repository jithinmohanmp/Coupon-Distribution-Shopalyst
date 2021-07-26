package com.shopalyst.coupondistribution.dataimport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.shopalyst.coupondistribution.entity.CouponsEntity;
import com.shopalyst.coupondistribution.entity.MembershipTypeEntity;
import com.shopalyst.coupondistribution.entity.UsersEntity;
import com.shopalyst.coupondistribution.enums.CouponStatus;
import com.shopalyst.coupondistribution.repository.CouponsRepository;
import com.shopalyst.coupondistribution.repository.MembershipTypeRepository;
import com.shopalyst.coupondistribution.repository.UsersRepository;

@Component
public class ImportTestData {
	private static final Logger LOG = LogManager.getLogger(ImportTestData.class);
	@Autowired
	private MembershipTypeRepository membershipTypeRepository;
	@Autowired
	private UsersRepository usersRepository;
	@Autowired
	private CouponsRepository couponsRepository;

	@EventListener(ApplicationReadyEvent.class)
	public void importTestData() {
		try {
			final Map<String, MembershipTypeEntity> membershipTypes = importMembershipTypes();
			importUsers(membershipTypes);
			importCoupons();
			LOG.info("Sample data import completed..");
		} catch (final IOException e) {
			LOG.error(e);
		}
	}

	private void importCoupons() throws IOException {
		final File csvFile = getFile("sample_data/coupons.csv");
		LOG.info("Importing file {}", csvFile);
		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (StringUtils.isNotBlank(line)) {
					final String[] record = StringUtils.split(line, ',');
					CompletableFuture.runAsync(() -> {
						if (record.length == 3) {
							final DateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
							try {
								final CouponsEntity coupon = CouponsEntity.builder().couponCode(record[0])
										.createdDate(dayFormat.parse(record[1])).status(CouponStatus.valueOf(record[2]))
										.build();
								couponsRepository.save(coupon);
							} catch (final Exception e) {
								LOG.error("Skipping line {}", record[0]);
								LOG.error(e);
							}
						}
					});
				}
			}
		}

	}

	private void importUsers(final Map<String, MembershipTypeEntity> membershipTypes) throws IOException {
		final File csvFile = getFile("sample_data/users.csv");
		LOG.info("Importing file {}", csvFile);
		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
			String line;
			while ((line = br.readLine()) != null) {

				if (StringUtils.isNotBlank(line)) {
					final String[] record = StringUtils.split(line, ',');
					CompletableFuture.runAsync(() -> {
						if (record.length == 2) {
							final String userId = record[0];
							final MembershipTypeEntity membershipType = membershipTypes.get(record[1]);
							if (membershipType == null) {
								LOG.error("Membershiptype {} not found : line {}", record[1], record);

							} else {
								final UsersEntity user = new UsersEntity(userId, membershipType,null);
								usersRepository.save(user);
							}
						}
					});
				}
			}
		}

	}

	private Map<String, MembershipTypeEntity> importMembershipTypes() throws IOException {
		final File csvFile = getFile("sample_data/membershipType.csv");
		LOG.info("Importing file {}", csvFile);
		final Map<String, MembershipTypeEntity> membershipTypes = new HashMap<>();
		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (StringUtils.isNotBlank(line)) {
					final String[] record = StringUtils.split(line, ',');
					if (record.length == 2) {
						final MembershipTypeEntity entity = new MembershipTypeEntity(record[0], Integer.parseInt(record[1]));
						membershipTypeRepository.save(entity);
						membershipTypes.put(record[0], entity);
					}
				}
			}
		}
		return membershipTypes;
	}

	private File getFile(final String fileName) throws IOException {
		final InputStream inputStream = new ClassPathResource(fileName).getInputStream();
		final File csvFile = File.createTempFile(fileName, ".csv");
		try {
			FileUtils.copyInputStreamToFile(inputStream, csvFile);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
		return csvFile;
	}
}
