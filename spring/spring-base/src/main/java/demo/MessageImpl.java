package demo; /**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年05月14日
 */

import java.lang.reflect.Field;

/**
 * ClassName: demo.MessageImpl
 * @Description:
 * @author leegoo
 * @date 2019年05月14日
 */
public class MessageImpl implements  Message {

    private static String name  = "asfdasda";

    @MyService
    private User user;

    @Override
    public Object send(Object o) {
        System.out.println("send mes");
        return null;
    }

    @Override
    public String getName() {
        return user.getName();
    }

    private void init() {
        System.out.println("init....");
    }

    private void destory() {
        System.out.println("destory");
    }

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        MessageImpl message = new MessageImpl();
        Field f = message.getClass().getDeclaredField("user");
        f.setAccessible(true);
        f.set(message, new UserImpl2());
        String name = message.user.getName();
    }
}
