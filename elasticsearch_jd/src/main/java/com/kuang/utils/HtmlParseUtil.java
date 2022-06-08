package com.kuang.utils;

import com.kuang.pojo.Content;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
public class HtmlParseUtil {
    /*public static void main(String[] args) throws IOException {
        new HtmlParseUtil().parseJD("fax").forEach(System.out::println);
    }*/

    /**
     * 定义所要爬取网页地址及关键词
     * 解析网页并返回一个document对象
     * 通过id获取document对象中的信息
     * 获取所有的li元素
     * 根据需要获取每一个li元素中的内容，并将所的内容存入列表
     * 将获取的数据以列表的形式返回
     * @param keywords
     * @return
     * @throws IOException
     */
    public List<Content> parseJD(String keywords) throws IOException {
        //定义所要爬取的网页地址以及关键词
        String url = "https://search.jd.com/Search?keyword="+keywords;
        //解析网页并返回一个document对象
        Document document = Jsoup.parse(new URL(url), 30000);
        //通过id获取document对象中的信息
        Element element = document.getElementById("J_goodsList");
        //获取所有的li元素
        Elements elements = element.getElementsByTag("li");
        //根据需要获取每一个li元素中的内容
        ArrayList<Content> goodList = new ArrayList<>();
        for (Element el : elements) {
            String img = el.getElementsByTag("img").eq(0).attr("data-lazy-img");
            String price = el.getElementsByClass("p-price").eq(0).text();
            String title = el.getElementsByClass("p-name").eq(0).text();

            Content content = new Content();
            content.setImg(img);
            content.setPrice(price);
            content.setTitle(title);
            goodList.add(content);
        }
        //将获取的内容放到定义的ArrayList列表中并返回
        return goodList;
    }

}
