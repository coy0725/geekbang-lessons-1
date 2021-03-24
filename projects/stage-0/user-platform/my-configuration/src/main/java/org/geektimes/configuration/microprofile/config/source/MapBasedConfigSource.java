package org.geektimes.configuration.microprofile.config.source;

import org.eclipse.microprofile.config.spi.ConfigSource;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 基于 Map 数据结构 {@link ConfigSource} 实现
 */
public abstract class MapBasedConfigSource implements ConfigSource {

    private final String name;

    private final int ordinal;

    private Map<String, String> source;
    
    private boolean lazyLoad;

    protected MapBasedConfigSource(String name, int ordinal) {
        this(name, ordinal, false);
    }
    protected MapBasedConfigSource(String name, int ordinal,boolean lazyLoad) {
        this.name = name;
        this.ordinal = ordinal;
        this.lazyLoad=lazyLoad;
        if (!lazyLoad){
            this.source = getProperties();
        }else {
            this.source=null;
        }
        
    }

    /**
     * 获取配置数据 Map
     *
     * @return 不可变 Map 类型的配置数据
     */
    @Override
    public final Map<String, String> getProperties() {
        Map<String,String> configData = new HashMap<>();
        try {
            prepareConfigData(configData);
        } catch (Throwable cause) {
            throw new IllegalStateException("准备配置数据发生错误",cause);
        }
        return Collections.unmodifiableMap(configData);
    }

    /**
     * 准备配置数据
     * @param configData
     * @throws Throwable
     */
    protected abstract void prepareConfigData(Map configData) throws Throwable;

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public final int getOrdinal() {
        return ordinal;
    }

    @Override
    public Set<String> getPropertyNames() {
        if (null == source) {
            source=getProperties();
        }
        return source.keySet();
    }

    @Override
    public String getValue(String propertyName) {
        if (null == source) {
            source=getProperties();
        }
        return source.get(propertyName);
    }

}
