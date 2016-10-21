package com.chinawiserv.deepone.manager.model.common;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.DefaultValueProcessor;
import net.sf.json.util.CycleDetectionStrategy;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zengpzh on 2016/6/22.
 */
public class Response implements Serializable{

    private static final long serialVersionUID = -8244754396679961861L;

    private static final Class[] classDefaultValueToNull = new Class[]{Integer.class, Boolean.class, Long.class, Short.class, Byte.class, Float.class, Double.class, Character.class, String.class, List.class};

    public Response(){
        this(new ResponseHead());
    }

    public Response(ResponseHead.Status status){
        this(status, 0, null);
    }

    public Response(ResponseHead.Status status, String message) {
        this(status, null, message);
    }

    public Response(ResponseHead.Status status, String token, String message){
        this(status, token, message, 0, null);
    }

    public Response(ResponseHead.Status status, int total, List body){
        this(status, null, total, body);
    }

    public Response(ResponseHead.Status status, String message, int total, List body){
        this(status, null, message, total, body);
    }

    public Response(ResponseHead.Status status, String token, String message, int total, List body){
        this(new ResponseHead(status.getCode(), token, message, total), body);
    }

    public Response(ResponseHead responseHead){
        this(responseHead, null);
    }

    public Response(ResponseHead responseHead, List body){
        this.head = responseHead;
        this.body = body;
    }

    private ResponseHead head;

    private List body;

    public ResponseHead getHead() {
        return head;
    }

    public void setHead(ResponseHead responseHead) {
        this.head = responseHead;
    }

    public List getBody() {
        return body;
    }

    public void setBody(List body) {
        this.body = body;
    }

    public String toJsonString(String... excludeFieldToJson){
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
        if (excludeFieldToJson != null && excludeFieldToJson.length > 0) jsonConfig.setExcludes(excludeFieldToJson);
        for (Class cls : classDefaultValueToNull) {
            jsonConfig.registerDefaultValueProcessor(cls, new DefaultValueProcessor() {
                @Override
                public Object getDefaultValue(Class aClass) {
                    return null;
                }
            });
        }
        return JSONObject.fromObject(this, jsonConfig).toString();
    }

}

