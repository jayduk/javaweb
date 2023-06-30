package frame;

import com.alibaba.fastjson2.JSONObject;
import exception.BusinessException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author javaok
 * 2023/6/26 18:15
 */

public class ControllerHolder {
    private final Object controller;
    private final Method method;

    public ControllerHolder(Object controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }

    public void invoke(HttpServletRequest req, HttpServletResponse resp, JSONObject requestData) {
        try {
            Parameter[] parameters = method.getParameters();
            Object[] args = new Object[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];

                if (parameter.getType() == HttpServletRequest.class) {
                    args[i] = req;
                } else if (parameter.getType() == HttpServletResponse.class) {
                    args[i] = resp;
                } else {
                    args[i] = requestData.getObject(parameter.getName(), parameter.getType());
                }
            }

            method.setAccessible(true);
            Object response = method.invoke(controller, args);
            if (response != null) {
                resp.getWriter().write(new Result(response).toString());
            }
        } catch (IllegalAccessException | IOException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof BusinessException) {
                throw new BusinessException((BusinessException) e.getCause());
            }
            throw new RuntimeException(e);
        }

    }
}