package com.kuang.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//spring操作就两步：
//1: 找对象  2：将对象放到spring中
@Configuration
public class ElasticSearchClientConfig {
    //spring中有一个beans来绑定我们的配置文件，但在springboot中就使用@Bean来代替了。<beans id="方法名" class="返回值类型">
    //因为我们需要这个对象，所以我们就把它配置到springboot中
    @Bean
    public RestHighLevelClient restHighLevelClient(){
        RestHighLevelClient client = new RestHighLevelClient(//我们new了一个高级客户端
                RestClient.builder(new HttpHost("127.0.0.1",9200,"http")));//我们通过构建器去绑定本地信息
        return client;
    }
//此时Springboot与Elasticsearch的集成就搞定了
}
