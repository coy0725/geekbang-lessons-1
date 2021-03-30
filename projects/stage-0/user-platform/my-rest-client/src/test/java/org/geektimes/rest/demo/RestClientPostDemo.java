package org.geektimes.rest.demo;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author coy
 * @since 2021/3/30
 **/
public class RestClientPostDemo {
    public static void main(String[] args) {
        Client client = ClientBuilder.newClient();
//        MediaType.APPLICATION_JSON connection.setRequestProperty("Content-Type","application/json; charset=utf-8");
        Response response = client
                                    .target("http://127.0.0.1:8079/hi")// WebTarget
                                    .request()
                                    .header("Content-Type","application/json; charset=utf-8")// Invocation.Builder
                                    .post(Entity.json("{\n" +
                                                              "  \"id\": 0,\n" +
                                                              "  \"name\": \"123\"\n" +
                                                              "}"));                                     //  Response
        
        String content = response.readEntity(String.class);
        
        System.out.println(content);
        
    }
}
