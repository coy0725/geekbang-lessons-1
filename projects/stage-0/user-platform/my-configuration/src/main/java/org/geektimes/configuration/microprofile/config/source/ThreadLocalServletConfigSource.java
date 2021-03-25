package org.geektimes.configuration.microprofile.config.source;

/**
 * @author coy
 * @since 2021/3/25
 **/
public class ThreadLocalServletConfigSource {
    ThreadLocal<ThreadLocalServletConfigSource> source;
    
    public ThreadLocal<ThreadLocalServletConfigSource> getSource() {
        return source;
    }
    
    public void setSource(ThreadLocal<ThreadLocalServletConfigSource> source) {
        this.source = source;
    }
}
