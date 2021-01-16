package test.zmh.filmtube.utils;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class CliReqRes {
    private int code;
    private String message;
    private JsonElement data;

    @Autowired
    Gson gson;

    public CliReqRes setCode(int CODE_SUCCESS) {
        this.code = CODE_SUCCESS;
        return this;
    }

    public CliReqRes setMessage(String message) {
        this.message = message;
        return this;
    }

    public CliReqRes setData(Object nativeData) {
        this.data = gson.toJsonTree(nativeData);
        return this;
    }

    public JsonObject toJsonObject() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code", this.code);
        jsonObject.addProperty("message", this.message);
        jsonObject.add("data", this.data);
        return jsonObject;
    }
}
