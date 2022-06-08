package com.kuang.service;


import com.alibaba.fastjson.JSON;
import com.kuang.pojo.Content;
import com.kuang.utils.HtmlParseUtil;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


//业务编写
@Service
public class ContentService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

/**将使用jsoup爬取出来的网站数据放到elasticsearch中间件中
     *
     * 使用封装好的工具类HtmlParseutil对关键词进行解析
     * 创建一个批处理请求
     * 对解析出来的每个关键字先转换成JSON形式，然后创建索引，最后将索引加到请求中
     * 客户端发送请求并获得请求后的结果
     * 返回响应后是否成功的结果
     * @return*/

    public Boolean parseContent(String keywords) throws IOException {
        //使用封装好的工具类HtmlPareseUtil对关键词进行解析
        List<Content> contents = new HtmlParseUtil().parseJD(keywords);
        //创建一个批处理请求
        BulkRequest bulkRequest = new BulkRequest();
        //对解析出来的每个关键字先转换成JSON格式，然乎创建索引把这些JSON格式放到索引值中，最后将索引丢到批处理请求中
        for (int i = 0; i < contents.size(); i++) {//不写id就是默认随机id
            bulkRequest.add(new IndexRequest("jd_goods")
            .source(com.alibaba.fastjson.JSON.toJSONString(contents.get(i)),XContentType.JSON));
        }
        //客户端发送请求并获得请求后的结果
        BulkResponse response = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        //返回是否批处理请求成功的结果
        return !response.hasFailures();


    }


    /**对elasticsearch中的数据进行搜索
     *
     *
     * @return
     * */


     public List<Map<String,Object>> searchPage(String keyword, int pageNo, int pageSize) throws IOException {
         //创建搜索请求，创建搜索资源source构造器
         SearchRequest request = new SearchRequest("jd_goods");
         SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
         //使用构造器进行分页
         sourceBuilder.from(pageNo);
         sourceBuilder.size(pageSize);
         //使用query构造器进行查询，执行source构造器中的query方法，参数为查询builder
         TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("title", keyword);
         sourceBuilder.query(termQueryBuilder);
         //执行request请求的source方法，参数为source构造器
         request.source(sourceBuilder);
         //客户端执行请求并得到响应结果
         SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
         //将查询到的结果循环加入到list列表中
         ArrayList<Map<String,Object>> list = new ArrayList<>();
         for (SearchHit hit : response.getHits().getHits()) {
             Map<String, Object> sourceAsMap = hit.getSourceAsMap();
             list.add(sourceAsMap);
         }
         //返回list列表
         return list;
     }

    public List<Map<String,Object>> searchHighlight(String keyword, int pageNo, int pageSize) throws IOException {
        //创建搜索请求，创建搜索资源source构造器
        SearchRequest request = new SearchRequest("jd_goods");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //使用构造器进行分页
        sourceBuilder.from(pageNo);
        sourceBuilder.size(pageSize);

        //高亮(实现不同的功能就使用不同的builder即可)
        //1: 创建一个高亮构造器
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        //2：设置高亮的字段，前缀，后缀
        highlightBuilder.field("title");
        highlightBuilder.requireFieldMatch(false);
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");
        //3：执行source构造器中的highlighter方法
        sourceBuilder.highlighter(highlightBuilder);

        //使用query构造器进行查询，执行source构造器中的query方法，参数为查询builder
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("title", keyword);
        sourceBuilder.query(termQueryBuilder);
        //执行request请求的source方法，参数为source构造器
        request.source(sourceBuilder);
        //客户端执行请求并得到响应结果
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        //将查询到的结果循环加入到list列表中
        ArrayList<Map<String,Object>> list = new ArrayList<>();
        for(SearchHit hit : response.getHits().getHits()){
            //获取高亮字段
            Map<String, HighlightField> highlightfields =  hit.getHighlightFields();
            //得到高亮字段的标题
            HighlightField title = highlightfields.get("title");
            //获取原来的source结果
            Map<String,Object> sourceAsMap =  hit.getSourceAsMap();
            //如果标题不为空，将原来字段替换为高亮字段
            if(title!=null){
                Text[] fragments = title.fragments();///如果高亮字段存在把这个高亮字段取出来
                String n_title = "";
                for (Text text : fragments) {
                    n_title += text;
                }
                sourceAsMap.put("title",n_title);//高亮字段替换掉原来的字段。用新n_title替换原来的title。
            }
            //将替换后的字段放到list列表中
            list.add(sourceAsMap);
        }
        //返回list列表
        return list;
    }
}
