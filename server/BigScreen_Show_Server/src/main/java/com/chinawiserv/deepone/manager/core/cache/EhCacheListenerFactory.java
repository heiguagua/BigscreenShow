package com.chinawiserv.deepone.manager.core.cache;

import net.sf.ehcache.event.CacheEventListener;
import net.sf.ehcache.event.CacheEventListenerFactory;

import java.util.Properties;

/**
 *  EhCache 缓存监听器工厂
 * @author zengpzh
 * @version 0.1
 */
public class EhCacheListenerFactory extends CacheEventListenerFactory {

	public CacheEventListener createCacheEventListener(Properties properties) {
		return new EhCacheListener();
	}
}