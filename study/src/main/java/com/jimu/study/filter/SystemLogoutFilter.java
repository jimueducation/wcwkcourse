package com.jimu.study.filter;

import com.alibaba.fastjson.JSON;
import com.jimu.study.common.HttpResult;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author hxt
 * @date 2020/1/22 14:52
 */
public class SystemLogoutFilter extends LogoutFilter {

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        try {
            subject.logout();
        } catch (Exception ex) {
            //TODO
        }
        this.writeResult(response);
        return false;
    }

    private void writeResult(ServletResponse response){
        //响应Json结果
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.append(JSON.toJSONString(HttpResult.ok()));
        } catch (IOException e) {
            //TODO
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
