package exception;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.filter.Filter;

/**
 * @author javaok
 * 2023/6/21 9:10
 */
public class BusinessException extends RuntimeException {
    public static final int SQL_ERR = 501;
    public static final int PARAM_ERR = 203;
    private final int code;

    public BusinessException(BusinessException cause) {
        super(cause.getMessage());
        this.code = cause.getCode();
    }

    public BusinessException(int code, String msg) {
        super(msg);
        this.code = code;

    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("msg", getMessage());
        return jsonObject.toJSONString();
    }
}
