package com.kuang.controller;


import com.kuang.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

//请求编写
@RestController
public class ContentController {
    @Autowired
    private ContentService contentservice;

    /**
     * 使用restful风格来写地址，调用service层中的方法，由于有@RestController<,所以返回的值是一个字符
     * @param keyword
     * @return
     */
    @GetMapping("/parse/{keyword}")
    public Boolean parse(@PathVariable("keyword") String keyword) throws IOException {
        return contentservice.parseContent(keyword);
    }

    //人们的恐惧往往来源于未知
    @GetMapping("/search/{keyword}/{pageNo}/{pageSize}")
    public List<Map<String,Object>> search(@PathVariable("keyword") String keyword,
                                           @PathVariable("pageNo") int pageNo,
                                           @PathVariable("pageSize") int pageSize) throws IOException {
        return contentservice.searchPage(keyword,pageNo,pageSize);
    }

    /**
     * 使用高亮对其查询
     * @param keyword
     * @param pageNo
     * @param pageSize
     * @return
     * @throws IOException
     */
    @GetMapping("/highlightsearch/{keyword}/{pageNo}/{pageSize}")
    public List<Map<String,Object>> highlightsearch(@PathVariable("keyword") String keyword,
                                           @PathVariable("pageNo") int pageNo,
                                           @PathVariable("pageSize") int pageSize) throws IOException {
        return contentservice.searchHighlight(keyword,pageNo,pageSize);
    }
}
