package com.kuang;

import com.alibaba.fastjson.JSON;
import com.kuang.pojo.User;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;


/**
 * es高级客户端测试API
 */
@SpringBootTest
class KuangshenEsApiApplicationTests {

    @Autowired
    @Qualifier("restHighLevelClient")//这一句是变量名restHighLevelClient的名字与引用处不一样才会用到
    private RestHighLevelClient restHighLevelClient;

    //测试创建索引
    //下面的测试就等同于命令行中的：PUT kuangshen_index

    /**
     * 创建索引请求
     * 客户端执行请求并得到响应
     * 查看相应结果
     * @throws IOException
     */
    @Test
    void testCreateIndex() throws IOException {
      //1:创建索引请求
        CreateIndexRequest request = new CreateIndexRequest("fax_index");

        // 2: 客户端执行请求并得到相应
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);

        //3: 查看相应结果
        System.out.println(createIndexResponse);
    }

    //测试获取索引

    /**
     * 创建获取索引的请求
     * 客户端执行请求并获得响应
     * 输出得到的索引结果
     * @throws IOException
     */
    @Test
    void testGetIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest("kuangshen_index");
        GetIndexResponse getIndexResponse = restHighLevelClient.indices().get(request, RequestOptions.DEFAULT);
        System.out.println(getIndexResponse);
    }

    //测试删除索引

    /**
     * 获取删除索引的请求
     * 客户端进行请求并得到响应
     * 输出是否删除成功的结果
     * @throws IOException
     */
    @Test
    void testDeleteInndex() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest("kuangshen_index");
        AcknowledgedResponse delete = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
        System.out.println(delete.isAcknowledged());
    }

    //测试添加文档
    /**
     *    //创建对象
     *    //创建请求
     *    //规则是 put /kuang_index/_doc/1
     *    //将我们的数据放入请求
     *    //客户端发送请求，获取响应的结果
     */
    @Test
    void testAddDocument() throws IOException {
        //创建对象
        User fax = new User("fax", 11);
        //创建请求
        IndexRequest request = new IndexRequest("fax_index");
        //规则是 put /kuang_index/_doc/1    这就是一条请求request
        request.id("1");
        request.timeout("1s");
        //将我们的数据放入请求
        request.source(JSON.toJSONString(fax), XContentType.JSON);
        //客户端发送请求，获取响应的结果
        IndexResponse index = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        System.out.println(index.toString());
        System.out.println(index.status());
    }

    //测试文档是否存在
    /**
     * 创建获取文档信息的请求
     * 客户端发出请求并获得响应结果
     * 输出是否存在的判断结果
     */
    @Test
    void testIsExist() throws IOException {
        //创建获取文档信息的请求
        GetRequest request = new GetRequest("fax_index", "1");
        //客户端发出请求并获得响应结果
        Boolean indexResponse = restHighLevelClient.exists(request, RequestOptions.DEFAULT);
        //输出是否存在的判断结果
        System.out.println(indexResponse);
    }


    //测试获得文档信息
    /**
     * 创建获取文档信息的请求
     * 客户端发出请求并获得响应的结果
     * 输出文档内容
     */
    @Test
    void testGetDocument() throws IOException {
        //创建获取文档信息的请求
        GetRequest request = new GetRequest("fax_index", "1");
        //客户端发出请求并获得响应的结果
        GetResponse documentFields = restHighLevelClient.get(request, RequestOptions.DEFAULT);
        //输出文档内容
        System.out.println(documentFields.getSourceAsString());
        System.out.println(documentFields);
    }

    //测试更新文档信息
    /**
     * 创建更新的请求
     * 创建更新后的对象
     * 将我们的数据放入请求
     * 客户端执行更新的请求并获得更新的响应
     * 输出更新后的状态码
     */
    @Test
    void testUpdateRequest() throws IOException {
        //创建更新的请求
        UpdateRequest request = new UpdateRequest("fax_index", "1");
        //创建更新后的对象
        User superfax = new User("superfax", 21);
        //将我们的数据放入请求
        request.doc(JSON.toJSONString(superfax),XContentType.JSON);
        //客户端执行更新的请求并获得更新的响应
        UpdateResponse update = restHighLevelClient.update(request, RequestOptions.DEFAULT);
        //输出更新后的状态码
        System.out.println(update.status());
    }

    //测试删除文档记录
    /**
     * 创建删除请求
     * 客户端执行删除请求并拿到响应结果
     * 输出删除后的状态码
     * @throws IOException
     */
    @Test
    void testDeleteDocument() throws IOException {
        //创建删除请求
        DeleteRequest request = new DeleteRequest("fax_index", "1");
        //客户端执行执行删除请求并拿到响应结果
        DeleteResponse response = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
        //输出删除后的状态码
        System.out.println(response.status());
    }


    //测试批量插入数据

    /**
     * bulkrequest是批量请求的意思
     * 1：
     */
    @Test
    void testBulkRequest(){
        //创建批量操作的请求
        BulkRequest request = new BulkRequest();
        //创建一个ArrayList数组，并往数组中添加内容
        ArrayList<User> userList = new ArrayList<>();
        //根据数组大小进行循环创建id并将数据放入请求中
        //客户端发送请求并接收返回的结果
        //输出查看是否批量创建成功
    }
}
