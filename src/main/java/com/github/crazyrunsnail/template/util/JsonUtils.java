package com.github.crazyrunsnail.template.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class JsonUtils implements ApplicationContextAware {

    private  static ObjectMapper objectMapper = null;


    public static <T> T parse(String params, TypeReference<T> ref){
        try {
            return (T) objectMapper.readValue(params, ref);
        } catch (JsonProcessingException e) {
            log.error("解析Json字符串失败！原因：{} 入参： {}", e.getMessage(), params);
            throw new RuntimeException(e);
        }
    }

    public static String toString(Object data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            log.error("转换成Json字符串失败！原因：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        objectMapper = applicationContext.getBean(ObjectMapper.class);
    }
}
