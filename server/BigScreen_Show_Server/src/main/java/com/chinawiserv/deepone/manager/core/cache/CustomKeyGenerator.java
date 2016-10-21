package com.chinawiserv.deepone.manager.core.cache;


import com.chinawiserv.deepone.manager.core.util.MD5;
import org.springframework.cache.interceptor.SimpleKeyGenerator;

import java.lang.reflect.Method;

/**
 * 自定义 Key 生成器
 * @author zengpzh
 * @version 0.1
 */

public class CustomKeyGenerator extends SimpleKeyGenerator {
	
	private MD5 md5;
	
	public CustomKeyGenerator() {
		md5 = new MD5();
	}

	@Override
	public Object generate(Object target, Method method, Object... params) {
		StringBuffer originalParameter = new StringBuffer();
		if (target != null) {
			originalParameter.append(target.getClass().getName());
		}
		if (method != null) {
			originalParameter.append(method.getName());
		}
		try {
			if (params != null && params.length > 0) {
				for (int i = 0 ; i < params.length; i ++) {
					Object param = params[i];
					if (param != null) {
						originalParameter.append(param);
					}
				}
			}
		} catch (Exception e) {
		}
		return md5.getMD5ofStr(originalParameter.toString());
	}
}
