package com.wangs;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by WangS on 2017/2/21.
 * 代表首页的Servlet，用于展示所有商品以及浏览过的商品
 */
public class ShowGoodsServlet extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter writer = response.getWriter();

        //1.输出网站所有的商品
        writer.print("本网站有如下商品：<br/>");
        Map<String, Book> map = Db.getAll();
        for (Map.Entry<String, Book> entry : map.entrySet()) {
            Book book = entry.getValue();
            writer.print("<a href='/cookie-history/GoodsDetailServlet?id=" + book.getId() +"' target='_blank'>" + book.getName() +"</a><br>");
        }

        //2.显示用户曾经看过的商品
        writer.print("<br/>您曾看过的商品如下：<br/>");
        Cookie cookies[] = request.getCookies();
        for (int i = 0; cookies!=null && i < cookies.length; i++) {
            if (cookies[i].getName().equals("bookHistory")) {
                String ids[] = cookies[i].getValue().split("\\$");
                for (String id : ids) {
                    Book book = (Book) Db.getAll().get(id);
                    writer.print(book.getName() + "<br/>");
                }
            }
        }
    }
}

//简单模拟数据库的数据
class Db {
    private static Map<String, Book> map = new HashMap<>();

    static {
        map.put("1", new Book("1", "JavaWeb开发", "小明", "编程类书籍"));
        map.put("2", new Book("2", "JDBC开发", "小明", "编程类书籍"));
        map.put("3", new Book("3", "Spring开发", "小明", "编程类书籍"));
        map.put("4", new Book("4", "Struts开发", "小明", "编程类书籍"));
        map.put("5", new Book("5", "Android开发", "小明", "编程类书籍"));
    }

    public static Map getAll() {
        return map;
    }
}

class Book {
    private String id;
    private String name;
    private String author;
    private String description;

    public Book() {
        super();
    }

    public Book(String id, String name, String author, String description) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
