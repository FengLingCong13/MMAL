package com.xhhp.mmall.common;

import com.google.common.collect.Lists;
import com.xhhp.mmall.pojo.User;
import com.xhhp.mmall.util.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * JsonUtil class
 *
 * @author Flc
 * @date 2019/10/8
 */
@Slf4j
public class JsonUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        //对象的所有字段全部列入
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.ALWAYS);
        //取消默认转换timestamp的形式
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
        //忽略空bean转json的错误
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        //统一所有的日期格式,即yyyy-MM-dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDAND_FORMAT));


        //忽略在json字符串中存在，在java对象中不存在对应属性的情况。防止错误
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    }


    public static <T> String obj2String(T obj) {
        if(obj == null) {
            return null;

        }
        try {
            return obj instanceof String ? (String)obj:objectMapper.writeValueAsString(obj);
        } catch (IOException e) {
            log.warn("Parse object to String error", e);
            return null;
        }
    }


    public static <T> String obj2StringPretty(T obj) {
        if(obj == null) {
            return null;

        }
        try {
            return obj instanceof String ? (String)obj:objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (IOException e) {
            log.warn("Parse object to String error", e);
            return null;
        }
    }

    public static <T> T string2Obj(String str,Class<T> clazz) {
        if(StringUtils.isEmpty(str) || clazz == null) {
            return null;
        }
        try {
            return clazz.equals(String.class) ? (T)str:objectMapper.readValue(str,clazz);
        } catch (IOException e) {
            log.warn("Parse string to object error", e);
            return null;
        }
    }

    public static <T> T string2Obj(String str, TypeReference<T> typeReference) {
        if(StringUtils.isEmpty(str) || typeReference  == null) {
            return null;
        }
        try {
            return (T)(typeReference.getType().equals(String.class) ? (T)str:objectMapper.readValue(str,typeReference));
        } catch (IOException e) {
            log.warn("Parse string to object error", e);
            return null;
        }
    }

    public static <T> T string2Obj(String str, Class<?> collectionClass,Class<?>... elementClasses) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass,elementClasses);
        try {
            return objectMapper.readValue(str,javaType);
        } catch (IOException e) {
            log.warn("Parse string to object error", e);
            return null;
        }
    }


    public static void main(String[] args) {
        User user1 = new User();
        User user2 = new User();
        user1.setPassword("1165");
        user1.setEmail("819163324@qq.com");
//        user2.setPassword("dasd");
//        user2.setEmail("819dasdasd24@qq.com");
//        List<User> list = Lists.newArrayList();
//        list.add(user1);
//        list.add(user2);
//        String answer1 = JsonUtil.obj2String(list);
//        String answer2 = JsonUtil.obj2StringPretty(list);
//
//
//        List<User> userList = JsonUtil.string2Obj(answer1, new TypeReference<List<User>>() {
//        });
//
//        List<User> userList1 = JsonUtil.string2Obj(answer1,List.class,User.class);

        String answer3 = JsonUtil.obj2StringPretty(user1);
        System.out.println(answer3);
    }
}
