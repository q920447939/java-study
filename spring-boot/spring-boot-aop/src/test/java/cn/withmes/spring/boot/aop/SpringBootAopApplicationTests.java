package cn.withmes.spring.boot.aop;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootAopApplicationTests {

    @Autowired
    UserSevice userSevice;

    @Test
    public void contextLoads() {
        User.userList.add(User.builder().id(2).name("admin").build());
        userSevice.findUser(null);
    }


    /**
     * @Description:判断字符串长度
     * @param: [s]
     * @return: int
     * @auther: liming
     * @date: 2019/4/15 11:52
     */
    public static int getLength(String s) {
        int length = 0;
        for (int i = 0; i < s.length(); i++) {
            int ascii = Character.codePointAt(s, i);
            if (ascii >= 0 && ascii <= 255) {
                length++;
            } else {
                length += 2;
            }
        }
        return length;
    }

    public static void main(String[] args) {
        int length = getLength("无法买配件无法买配件无法买配件无法买配件无法买配件无法买配件");
        System.out.println(length);
    }


}
