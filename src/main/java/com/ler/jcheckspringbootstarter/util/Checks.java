package com.ler.jcheckspringbootstarter.util;

import com.ler.jcheckspringbootstarter.config.UserFriendlyException;

/**
 * @author lww
 * @date 2019-09-04 16:27
 */
public class Checks {

	public static void isTrue(Boolean ex, String msg) {
		if (!ex) {
			throw new UserFriendlyException(msg);
		}
	}

}
