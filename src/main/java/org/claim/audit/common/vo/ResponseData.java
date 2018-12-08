package org.claim.audit.common.vo;

import java.util.HashMap;
import java.util.Map;

public class ResponseData {

	private final int code;
    private final String message;
    private final Map<String, Object> data = new HashMap<String, Object>();

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public ResponseData putDataValue(String key, Object value) {
        data.put(key, value);
        return this;
    }

    public ResponseData(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ResponseData ok() {
        return new ResponseData(200, "Ok");
    }

    public static ResponseData notFound() {
        return new ResponseData(404, "Not Found");
    }

    public static ResponseData badRequest() {
        return new ResponseData(400, "Bad Request");
    }

    public static ResponseData forbidden() {
        return new ResponseData(403, "Forbidden");
    }

    public static ResponseData unauthorized() {
        return new ResponseData(401, "unauthorized");
    }

    public static ResponseData serverInternalError() {
        return new ResponseData(500, "Server Internal Error");
    }
    public static ResponseData TokenError() {
        return new ResponseData(1001, "invalid Token");
    }
    public static ResponseData TokenTimeout() {
        return new ResponseData(1002, "Token need to be updated");
    }
    public static ResponseData customerError() {
        return new ResponseData(1003, "customer Error");
    }
}
