package org.geektimes.rest.demo;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

/**
 * @author coy
 * @since 2021/3/30
 **/
public class RestClientPostDemo {
    public static void main(String[] args) {
        Client client = ClientBuilder.newClient();
        Response response = client
                                    .target("http://127.0.0.1:8079/world")      // WebTarget
                                    .request() // Invocation.Builder
                                    .post(Entity.json("{\"name\":\"zzz\"}"));                                     //  Response
        
        String content = response.readEntity(String.class);
        
        System.out.println(content);
        
    }
}
