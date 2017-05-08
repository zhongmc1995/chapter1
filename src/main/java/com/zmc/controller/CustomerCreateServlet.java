package com.zmc.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by zhongmc on 2017/5/8.
 * 创建客户
 */

/**
 * 该servlet只有这一个地址访问
 * 请求中的get、post、delete、put，对应deGet doPost doDelete doPut
 */
@WebServlet("/customer_create")
public class CustomerCreateServlet extends HttpServlet {
    /**
     * 跳转创建客户页面
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO
    }

    /**
     * 处理创建客户请求
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO
    }

}
