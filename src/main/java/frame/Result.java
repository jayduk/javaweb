package frame;

import com.alibaba.fastjson2.JSON;
import exception.BusinessException;

/**
 * @author javaok
 * 2023/6/26 16:17
 */
public class Result {
    int code;
    String msg;
    Object data;

    public Result(BusinessException businessException) {
        code = businessException.getCode();
        msg = businessException.getMessage();
        data = null;
    }

    public Result(Object data) {
        code = 200;
        msg = "success";
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
