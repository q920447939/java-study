/**
 * @Project:
 * @Author: leegoo
 * @Date: 2021年10月21日
 */
package cn.withme.springboot.web.demo.controller;

import com.alibaba.fastjson.JSONObject;
import netscape.javascript.JSObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: HealthPredictController
 * @Description:
 * @author leegoo
 * @date 2021年10月21日
 */
@RestController
@RequestMapping("biz")
public class HealthPredictController {

    @PostMapping("faultInfo/addFaultInfo")
    public JSONObject a (@RequestBody JSONObject a) {
        System.err.println("获取到的数据。。。" + JSONObject.toJSONString(a));
        JSONObject result = new JSONObject();
        result.put("httpStatus",200);
        result.put("data",null);
        result.put("success",true);
        result.put("message","操作成功");
        return result;
    }

    @PostMapping("faultInfo/updadteFaultInfo")
    public JSONObject updadteFaultInf (@RequestBody JSONObject a) {
        System.err.println("获取到的数据。。。" + JSONObject.toJSONString(a));
        JSONObject result = new JSONObject();
        result.put("httpStatus",200);
        result.put("data",null);
        result.put("success",true);
        result.put("message","操作成功");
        return result;
    }

    @PostMapping("lifePrediction/addLifePredictionInfo")
    public JSONObject addLifePredictionInfo (@RequestBody JSONObject a) {
        System.err.println("获取到的数据。。。" + JSONObject.toJSONString(a));
        JSONObject result = new JSONObject();
        result.put("httpStatus",200);
        result.put("data",null);
        result.put("success",true);
        result.put("message","操作成功");
        return result;
    }
}
