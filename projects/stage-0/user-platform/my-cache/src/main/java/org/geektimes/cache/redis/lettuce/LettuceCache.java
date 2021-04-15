package org.geektimes.cache.redis.lettuce;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.geektimes.cache.AbstractCache;
import org.geektimes.cache.ExpirableEntry;

import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.configuration.Configuration;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * @author coy
 * @since 2021/4/13
 **/
public class LettuceCache <K extends Serializable, V extends Serializable> extends AbstractCache<K, V> {
    

    
    private  RedisCommands<K, V> commands;
    
    private final StatefulRedisConnection<K, V> connect;
    
    protected LettuceCache(CacheManager cacheManager, String cacheName, Configuration<K, V> configuration,StatefulRedisConnection<K, V> connect) {
        super(cacheManager, cacheName, configuration);
        this.connect=connect;
        commands=connect.sync();
    }
    
    
    @Override
    protected boolean containsEntry(K key) throws CacheException, ClassCastException {
        V v = commands.get(key);
        return Objects.nonNull(v);
    }
    
    @Override
    protected ExpirableEntry<K, V> getEntry(K key) throws CacheException, ClassCastException {
        V v = commands.get(key);
        return ExpirableEntry.of(key,v);
    }
    
    @Override
    protected void putEntry(ExpirableEntry<K, V> entry) throws CacheException, ClassCastException {
    
        commands.set(entry.getKey(),entry.getValue());
    }
    
    @Override
    protected ExpirableEntry<K, V> removeEntry(K key) throws CacheException, ClassCastException {
        ExpirableEntry<K, V> entry = getEntry(key);
        commands.del(key);
        return entry;
    }
    
    @Override
    protected void clearEntries() throws CacheException {
    
    }
    
    @Override
    protected Set<K> keySet() {
        return null;
    }
    
    
}
