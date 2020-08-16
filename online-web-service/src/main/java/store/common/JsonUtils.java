package store.common;

import com.alibaba.fastjson.JSONObject;

public class JsonUtils {
  public static JSONObject responseBodyBuilder(boolean success, String reason) {
    JSONObject resp = new JSONObject();
    resp.put("success", success);
    resp.put("reason", reason);
    return resp;
  }

  public static JSONObject responseBodyBuilder(boolean success, String reason, Object data) {
    JSONObject resp = new JSONObject();
    resp.put("success", success);
    resp.put("reason", reason);
    resp.put("data", data);
    return resp;
  }
}
