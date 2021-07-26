package com.shopalyst.coupondistribution.exception;

import lombok.Getter;
import lombok.Setter;

public class ApplicationException extends RuntimeException {
	public static final String REDUMTION_LIMIT_EXCEEDED = "ERR_001";
	public static final String NO_COUPONS_AVAILABLE = "ERR_002";
	public static final String USER_UNAVAILABLE = "ERR_003";
	public static final String NO_COUPONS_ASSIGNMENTS = "ERR_004";
	private static final long serialVersionUID = 1L;
	
	@Getter
	@Setter
	private String code;

	public ApplicationException(final String code) {
		super(code);
		this.code = code;
	}
}
