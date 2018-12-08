package org.claim.audit.common.util;

import org.claim.audit.common.vo.UserBean;

public class ThreadLocalUtil {
	private static ThreadLocal<UserBean> user=new ThreadLocal<UserBean>();

	public static UserBean getUser() {
		return user.get();
	}

	public static void setUser(UserBean user) {
		ThreadLocalUtil.user.set(user);;
	}
	public static void removeUser() {
		ThreadLocalUtil.user.remove();
	}
	
}
