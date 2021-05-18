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





## 背景

### 训练营第22节稳定性提升课后作业

* 通过 Java 实现两种（以及）更多的一致性 Hash 算法（可选）实现服务节点动态更新

* 参考`org.apache.dubbo.rpc.cluster.loadbalance.ConsistentHashLoadBalance`

### `ConsistentHashLoadBalance` 

从名称上来看，一致性hash负载均衡

* `LoadBalance`

  ````java
  @SPI(RandomLoadBalance.NAME)
  public interface LoadBalance {
  
      /**
       * select one invoker in list.
       *
       * @param invokers   invokers.
       * @param url        refer url
       * @param invocation invocation.
       * @return selected invoker.
       */
      @Adaptive("loadbalance")
      <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) throws RpcException;
  
  }
  ````

  该接口为负载均衡的核心接口，目前能够获取的信息为从`Invoker`集合中选出一个`Invoker`，所以其核心是如何选出该`Invoker`?

* `AbstractLoadBalance`

  ````java
  public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) {
          if (CollectionUtils.isEmpty(invokers)) {
              return null;
          }
          if (invokers.size() == 1) {
              return invokers.get(0);
          }
          return doSelect(invokers, url, invocation);
      }
  ````

  该接口实现了`LoadBalance`的共用逻辑，处理`Invoker`对象为空和只有一个`Invoker`对象的情况，其他情况由子类实现`doSelect`方法去实现

* `ConsistentHashSelector<T>`

  ````java
  private static final class ConsistentHashSelector<T> {
  
          private final TreeMap<Long, Invoker<T>> virtualInvokers;
  
          private final int replicaNumber;
  
          private final int identityHashCode;
  
          private final int[] argumentIndex;
  
          ConsistentHashSelector(List<Invoker<T>> invokers, String methodName, int identityHashCode) {
              this.virtualInvokers = new TreeMap<Long, Invoker<T>>();
              this.identityHashCode = identityHashCode;
              URL url = invokers.get(0).getUrl();
              this.replicaNumber = url.getMethodParameter(methodName, HASH_NODES, 160);
              String[] index = COMMA_SPLIT_PATTERN.split(url.getMethodParameter(methodName, HASH_ARGUMENTS, "0"));
              argumentIndex = new int[index.length];
              for (int i = 0; i < index.length; i++) {
                  argumentIndex[i] = Integer.parseInt(index[i]);
              }
              for (Invoker<T> invoker : invokers) {
                  String address = invoker.getUrl().getAddress();
                  for (int i = 0; i < replicaNumber / 4; i++) {
                      byte[] digest = Bytes.getMD5(address + i);
                      for (int h = 0; h < 4; h++) {
                          long m = hash(digest, h);
                          virtualInvokers.put(m, invoker);
                      }
                  }
              }
          }
  
          public Invoker<T> select(Invocation invocation) {
              String key = toKey(invocation.getArguments());
              byte[] digest = Bytes.getMD5(key);
              return selectForKey(hash(digest, 0));
          }
  
          private String toKey(Object[] args) {
              StringBuilder buf = new StringBuilder();
              for (int i : argumentIndex) {
                  if (i >= 0 && i < args.length) {
                      buf.append(args[i]);
                  }
              }
              return buf.toString();
          }
  
          private Invoker<T> selectForKey(long hash) {
              Map.Entry<Long, Invoker<T>> entry = virtualInvokers.ceilingEntry(hash);
              if (entry == null) {
                  entry = virtualInvokers.firstEntry();
              }
              return entry.getValue();
          }
  
          private long hash(byte[] digest, int number) {
              return (((long) (digest[3 + number * 4] & 0xFF) << 24)
                      | ((long) (digest[2 + number * 4] & 0xFF) << 16)
                      | ((long) (digest[1 + number * 4] & 0xFF) << 8)
                      | (digest[number * 4] & 0xFF))
                      & 0xFFFFFFFFL;
          }
      }
  ````

  该类为`ConsistentHashLoadBalance` 的内部类，其实现了一致性hash的核心逻辑

  1. 获取`Invocation`的参数生成一个字符类型的key
  2. 生成该key的MD5字节数组
  3. 对该字节数组进行`hash` 获取一个long型的hash值
  4. 根据该hash值从`virtualInvokers`里拿到与之对应的`Invoker`对象

* ConsistentHashSelector 实例对象的生成逻辑

  ```java
  this.replicaNumber = url.getMethodParameter(methodName, HASH_NODES, 160);
  ```

  副本数量赋值为 url方法参数？这是个什么意思

## 基本概念

### hashcode()

* hashcode()

  Returns a hash code value for the object. This method is supported for the benefit of hash tables such as those provided by java.util.HashMap.

  从上述信息可以获知，hashCode方法将会返回一个int型的hash code值，这会对`hash table`很有益,看看`HashMap` 在哪里用到了`HashCode`方法也就是可以通过hashCode 来区分对象是否是同一个。使得对象均匀的分布在散列表里头

### 哈希

* 扩容，普通hash的扩容可能会影响所有元素的移动

### 一致性哈希

**背景**

在分布式系统中，节点的宕机、某个节点加入或者移出集群是常事。对于分布式存储而言，假设存储集群中有10台机子，如果采用Hash方式对数据分片(即将数据根据哈希函数映射到某台机器上存储)，哈希函数应该是这样的：hash(file) % 10。根据上面的介绍，扩容是非常不利的，如果要添加一台机器，很多已经存储到机器上的文件都需要重新分配移动，如果文件很大，这种移动就造成网络的负载。

在一致性哈希中，机器节点变成了`hash表`的桶，桶的变化引发的重新hash，而普通hash的重新hash是由元素的增加导致的重新哈希`reHash`

不管是普通哈希，还是一致性哈希，最核心的诉求是**减少数据的移动**，这种移动包括内存中的移动，或者是数据在机器节点中的移动。一致性哈希，其实就是**把哈希函数可映射空间即普通哈希中桶的数目固定下来了**

势必需要引入一个中间的东东，通过这个中间人维护外部与内部的映射关系。

**解决**

引入一个环，划分区间，这样节点的减少和增加只会影响到部分数据.相比于普通哈希方式，**一致性哈希的好处就是：当添加新机器或者删除机器时，不会影响到全部数据的存储，而只是影响到这台机器上所存储的数据(落在这台机器所负责的环上的数据)。**

![img](https://raw.githubusercontent.com/coy0725/Figure-bed/typora/img/715283-20160811153507027-302498592.png)

## 功能目标



## 设计实现



## 参考资料

[分布式hash算法](https://www.cnblogs.com/hapjin/p/5760463.html)

[五分钟理解一致性哈希算法(consistent hashing)](https://blog.csdn.net/cywosp/article/details/23397179)

[system-design-interview-consistent-hashing](https://www.acodersjourney.com/system-design-interview-consistent-hashing/)

[Consistent hashing](https://www.codeproject.com/Articles/56138/Consistent-hashing)