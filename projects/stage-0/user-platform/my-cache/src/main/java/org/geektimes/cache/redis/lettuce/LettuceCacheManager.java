package org.geektimes.cache.redis.lettuce;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import org.geektimes.cache.AbstractCacheManager;

import javax.cache.Cache;
import javax.cache.configuration.Configuration;
import javax.cache.spi.CachingProvider;
import java.net.URI;
import java.util.Properties;

/**
 * @author coy
 * @since 2021/4/15
 **/
public class LettuceCacheManager extends AbstractCacheManager {
    
    private final RedisClient client;
    
    

    
    public LettuceCacheManager(CachingProvider cachingProvider, URI uri, ClassLoader classLoader, Properties properties) {
        super(cachingProvider, uri, classLoader, properties);
        this.client = RedisClient.create(RedisURI.create(uri));
    }
    
    @Override
    protected <K, V, C extends Configuration<K, V>> Cache doCreateCache(String cacheName, C configuration) {
        StatefulRedisConnection<String, String> connect = client.connect();
        return new LettuceCache(this,cacheName,configuration,connect);
    }
    
    
    @Override
    protected void doClose() {
        client.shutdown();
    }
}
