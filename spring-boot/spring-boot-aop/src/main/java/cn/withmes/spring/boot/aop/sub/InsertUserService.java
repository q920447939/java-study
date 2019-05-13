package cn.withmes.spring.boot.aop.sub;

import cn.withmes.spring.boot.aop.User;
import org.springframework.stereotype.Service;

@Service
public class InsertUserService {

    public Boolean insert() {
        User user = User.userList.get(0);
        System.out.println("insert user start...user message:"+user);
        return true;
    }


    public Boolean insertHaveExceoption() throws  Exception{
        User user = User.userList.get(0);
        System.out.println("insert user start...user message:"+user);
        return true;
    }
}
