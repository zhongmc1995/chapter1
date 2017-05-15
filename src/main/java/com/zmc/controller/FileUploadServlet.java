package com.zmc.controller;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * Created by zhongmc on 2017/5/15.
 * 文件上传
 */
@WebServlet("/upload")
public class FileUploadServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletInputStream is = req.getInputStream();
        OutputStream out = new FileOutputStream("src/upload.txt");
        Object username = req.getAttribute("username");
        String username1 = req.getParameter("username");
        Map<String, String[]> parameterMap = req.getParameterMap();
        System.out.println(parameterMap.size());
        for (Map.Entry e : parameterMap.entrySet()){
            String key = (String)e.getKey();
            String[] values = (String[]) e.getValue();
            StringBuffer val = new  StringBuffer();
            for (String value:values){
                val.append(value).append(",");
            }
            System.out.println(key+" : "+val);
        }
        byte[] bs = new byte[102];
        int count;
        while ((count=is.read(bs))!=-1){
            out.write(bs,0,count);
        }
        out.close();
        is.close();
    }
}
