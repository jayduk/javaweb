package frame;

import annotation.*;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import exception.BusinessException;
import util.ClassUtil;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author javaok
 * 2023/6/21 22:00
 */
@WebServlet("/jav/*")
@MultipartConfig
public class DispatcherServlet extends HttpServlet {
    Map<Level, Map<String, Object>> ioc = new HashMap<Level, Map<String, Object>>();

    Map<String, ControllerHolder> getMapping = new HashMap<String, ControllerHolder>();
    Map<String, ControllerHolder> postMapping = new HashMap<String, ControllerHolder>();

    @Override
    public void init() {
        autoWired();
        initRequestMapping();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        ControllerHolder controllerHolder = getMapping.get(req.getRequestURI());
        if (controllerHolder == null) {
            throw new BusinessException(404, "404 Not Found");
        }
        JSONObject getParameters = null;
        if (controllerHolder.getMethod().getAnnotation(NoneAutoParameter.class) == null) {
            getParameters = buildParameterMap(req);
        }
        controllerHolder.invoke(req, resp, getParameters);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        ControllerHolder controllerHolder = postMapping.get(req.getRequestURI());

        if (controllerHolder == null) {
            throw new BusinessException(404, "404 Not Found");
        }
        JSONObject postParameters = null;
        if (controllerHolder.getMethod().getAnnotation(NoneAutoParameter.class) == null) {
            postParameters = buildParameterMap(req);
        }
        controllerHolder.invoke(req, resp, postParameters);
    }

    private JSONObject buildGetParametersMap(HttpServletRequest req) {
        JSONObject parameterMap = new JSONObject();
        Enumeration<String> parameterNames = req.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            String parameterValue = req.getParameter(parameterName);
            parameterMap.put(parameterName, parameterValue);
        }
        return parameterMap;
    }

    private JSONObject buildParameterMap(HttpServletRequest req) {

        try {
            JSONObject parameterMap = buildGetParametersMap(req);

            StringBuilder requestBody = new StringBuilder();
            BufferedReader reader = req.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
            reader.close();

            String json = requestBody.toString();
            if (JSON.isValid(json)) {
                parameterMap.putAll(JSONObject.parseObject(json));
            } else {
                log("请求体不是json格式" + json);
            }

            return parameterMap;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void autoWired() {
        autoWired("dao.impl", Level.DAO);
        autoWired("service.impl", Level.SERVICE);
        autoWired("controller", Level.CONTROLLER);
    }

    private void autoWired(String packageName, Level level) {
        Map<String, Object> iocContainer;
        if (ioc.containsKey(level)) {
            iocContainer = ioc.get(level);
        } else {
            iocContainer = new HashMap<String, Object>();
            ioc.put(level, iocContainer);
        }

        List<Class<?>> components = ClassUtil.getPackageClass(packageName);
        for (Class<?> component : components) {
            Component annotation = component.getAnnotation(Component.class);
            if (annotation == null) {
                continue;
            }

            Object instance = ClassUtil.newInstance(component);
            if (!annotation.level().equals(level)) {
                System.out.println(annotation.value() + " 不是 " + level + " 级别的组件 " + component.getName());
            }
            String value = "".equals(annotation.value()) ? component.getSimpleName() : annotation.value();
            iocContainer.put(value, instance);

            Field[] fields = component.getDeclaredFields();
            for (Field field : fields) {
                AutoWired autoWired = field.getAnnotation(AutoWired.class);
                if (autoWired == null) {
                    continue;
                }

                String wired = "".equals(autoWired.value()) ? field.getName() : autoWired.value();
                Level wiredLevel = Level.values()[annotation.level().ordinal() - 1];
                Object wiredInstance = ioc.get(wiredLevel).get(wired);

                if (wiredInstance == null) {
                    throw new RuntimeException("没有找到 " + wired + " 对应的实例");
                }
                field.setAccessible(true);
                try {
                    field.set(instance, wiredInstance);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void initRequestMapping() {
        Collection<Object> controllers = ioc.get(Level.CONTROLLER).values();
        for (Object controller : controllers) {
            Class<?> aClass = controller.getClass();
            RequestMapping requestMapping = aClass.getAnnotation(RequestMapping.class);

            String prefixUrl = requestMapping == null ? "" : requestMapping.value();

            Method[] methods = aClass.getDeclaredMethods();
            for (Method method : methods) {
                RequestMapping methodRequestMapping = method.getAnnotation(RequestMapping.class);
                if (methodRequestMapping == null) {
                    continue;
                }
                String postUrl = methodRequestMapping.value();
                String path = prefixUrl + postUrl;
                if (methodRequestMapping.method().equals("GET")) {
                    getMapping.put(path, new ControllerHolder(controller, method));
                } else if (methodRequestMapping.method().equals("POST")) {
                    postMapping.put(path, new ControllerHolder(controller, method));
                } else {
                    throw new RuntimeException("不支持的请求方法 " + methodRequestMapping.method());
                }
            }
        }
    }
}
