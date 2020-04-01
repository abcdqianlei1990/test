package cn.xzj.agent.util;

import java.lang.reflect.ParameterizedType;

/**
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/8/30
 * @ Des
 */
public class CreateUtil {
    public static <T> T getT(Object o, int i) {
        try {
            Class<T> mClass=((Class<T>) ((ParameterizedType) (o.getClass().getGenericSuperclass())).getActualTypeArguments()[i]);
            return mClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
