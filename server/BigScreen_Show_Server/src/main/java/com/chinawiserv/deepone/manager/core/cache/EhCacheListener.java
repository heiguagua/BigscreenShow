package com.chinawiserv.deepone.manager.core.cache;

import com.chinawiserv.deepone.manager.core.util.Tools;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *  EhCache 缓存监听器
 * @author zengpzh
 * @version 0.1
 */
public class EhCacheListener implements CacheEventListener  {
	
	private static final Log log = LogFactory.getLog(EhCacheListener.class);

	public void dispose() {
	}

	public void notifyElementEvicted(Ehcache ehcache, Element element) {
		
	}

	public void notifyElementExpired(Ehcache ehcache, Element element) {
	}

	public void notifyElementPut(Ehcache ehcache, Element element) throws CacheException {
		log.info("EHCache缓存监听：一个Java实例存入【"+ehcache.getName()+"】缓存，键 = "+element.getObjectKey()+"，值 = "+element.getObjectValue());
		Tools.println();
	}

	public void notifyElementRemoved(Ehcache ehcache, Element element) throws CacheException {
		log.info("EHCache缓存监听：一个Java实例从【"+ehcache.getName()+"】缓存中删除，键 = "+element.getObjectKey()+"，值 = "+element.getObjectValue());
		Tools.println();
	}

	public void notifyElementUpdated(Ehcache ehcache, Element element) throws CacheException {
		log.info("EHCache缓存监听：一个Java实例【"+ehcache.getName()+"】缓存更新，键 = "+element.getObjectKey()+"，值 = "+element.getObjectValue());
		Tools.println();
	}

	public void notifyRemoveAll(Ehcache ehcache) {
		log.info("EHCache缓存监听：清空【"+ehcache.getName()+"】");
		Tools.println();
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}