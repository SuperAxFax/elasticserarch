package com.kuang.utils;

import com.kuang.pojo.Content;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class ParseMianShiYa {
    public static void main(String[] args) {
        //使用jsoup获取网页中的html源文件，转化成Document对象，
        try {
            Document parse = Jsoup.parse(new URL("https://pic.netbian.com/"), 5000);
            System.out.println(parse); //输出的源文件数据信息
            //通过document对象来获取需要element对象
            Elements img = parse.getElementsByAttributeValue("alt", "天空小姐姐 黑色唯美裙子 厚涂画风 4k动漫壁纸");
            Elements title = parse.getElementsByAttributeValue("title", "4k壁纸");
            Elements select = parse.select(".w");
            System.out.println("++++++++++++++++++++");
            System.out.println(img);
            System.out.println(title);
            System.out.println(select);
            //获取Element对象中的数据
            String href = img.get(0).attr("src");
            String href1 = title.get(1).attr("href");
            String text = select.text();
            System.out.println("+++++++++++++++++++++++++++");
            System.out.println("href"+href);
            System.out.println("href1"+href1);
            System.out.println("text"+text);
            System.out.println(href1+href);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
