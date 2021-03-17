package org.geektimes.configuration.microprofile.config.source;

import org.eclipse.microprofile.config.spi.ConfigSource;

import java.util.Set;

/**
 * @author coy
 * @since 2021/3/17
 **/
public class SystemEnvConfigSource implements ConfigSource {
    @Override
    public Set<String> getPropertyNames() {
        return System.getenv().keySet();
    }
    
    @Override
    public String getValue(String s) {
        return System.getenv(s);
    }
    
    @Override
    public String getName() {
        return "OS system Env Properties";
    }
}
