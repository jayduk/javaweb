package filter;


import com.alibaba.fastjson2.JSON;
import exception.BusinessException;
import frame.Result;
import util.JdbcUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * @author javaok
 * 2023/6/21 9:31
 */

@WebFilter("/jav/*")
public class GlobalFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        response.setHeader("Content-Type", "text/html;charset=UTF-8");

        request.setCharacterEncoding("UTF-8");
        try {
            JdbcUtil.start();
            chain.doFilter(request, response);
            JdbcUtil.commit();
        } catch (BusinessException businessException) {
            System.out.println(businessException.getMessage());
            response.setStatus(businessException.getCode());

            response.getWriter().write(new Result(businessException).toString());
            try {
                JdbcUtil.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
