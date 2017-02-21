package com.wangs;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by WangS on 2017/2/21.
 * 显示商品详细信息
 */
public class GoodsDetailServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter writer = response.getWriter();

        //1.根据用户带过来的id，显示相应商品的详细信息
        String id = request.getParameter("id");
        Book book = (Book)Db.getAll().get(id);
        writer.write(book.getId() + "<br/>");
        writer.write(book.getName() + "<br/>");
        writer.write(book.getAuthor() + "<br/>");
        writer.write(book.getDescription() + "<br/>");

        //2.构建cookie，回写给浏览器
        String cookieValue = buildCookie(id, request);
        Cookie cookie = new Cookie("bookHistory", cookieValue);
        cookie.setMaxAge(1*30*24*3600);
        cookie.setPath("/cookie-history");
        response.addCookie(cookie);
    }

    private String buildCookie(String id, HttpServletRequest request) {
        String bookHistory = null;
        Cookie cookies[] = request.getCookies();
        for (int i = 0; cookies!=null && i < cookies.length; i++) {
            if (cookies[i].getName().equals("bookHistory")) {
                bookHistory = cookies[i].getValue();
            }
        }

        if (bookHistory == null)
            return id;

        LinkedList<String> list = new LinkedList(Arrays.asList(bookHistory.split("\\$")));

        if (list.contains(id)) {
            list.remove(id);
        }  else {
            if (list.size() >= 3) {
                list.removeLast();
            }
        }
        list.addFirst(id);

        StringBuffer sb = new StringBuffer();
        for (String bid : list) {
            sb.append(bid + "$");
        }
        return sb.deleteCharAt(sb.length()-1).toString();
    }
}
