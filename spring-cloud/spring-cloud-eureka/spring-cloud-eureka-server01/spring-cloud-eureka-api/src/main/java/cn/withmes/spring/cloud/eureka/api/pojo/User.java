/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年07月06日
 */
package cn.withmes.spring.cloud.eureka.api.pojo;

/**
 * ClassName: User
 * @Description:
 * @author leegoo
 * @date 2019年07月06日
 */
public class User {

    private  Integer id ;
    private  String name ;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
