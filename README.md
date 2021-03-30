# geekbang-lessons
极客时间课程工程


week 05
继续完善 my-rest-client POST 方法
1、实现Invocation 重写invoke()方法，

``` 
connection = (HttpURLConnection) url.openConnection();
//设置请求方式为psot            connection.setRequestMethod(HttpMethod.POST);
```

2、我想使用post 传输RequestBody该怎么弄？

一开始是这样的

``` java
Client client = ClientBuilder.newClient();
//        MediaType.APPLICATION_JSON connection.setRequestProperty("Content-Type","application/json; charset=utf-8");
        Response response = client
                                    .target("http://127.0.0.1:8079/hi")// WebTarget
                                    .request(MediaType.APPLICATION_JSON)
                                   
                                    .post(Entity.json("{\n" +
                                                              "  \"id\": 0,\n" +
                                                              "  \"name\": \"123\"\n" +
                                                              "}"));                                     //  Response
        
        String content = response.readEntity(String.class);
        
        System.out.println(content);
        
```

会报空指针异常

需要重写org.geektimes.rest.DefaultRuntimeDelegate#createHeaderDelegate 这个方法，之前是返回null

好吧 最后是之间返回了一个json的MediaType



然后再试，请求还是400，

服务端拿不到需要的RequestBody,怎么回事呢？

问了下群里的老哥

![微信图片_20210330165146](D:\Desktop\微信图片_20210330165146.png)

需要在Connection中拿到输出流，将Entity写到流里去。

然后在服务端那边又报了个token解析失败，原因是进行了两次序列化

https://blog.csdn.net/qq_30162239/article/details/86647164

坑有记录的不大好，之后再弄吧 就这样