/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年07月22日
 */
package cn.withmes.springboot.singlepointjwt;

/**
 * ClassName: User
 * @Description: 实体对象
 * @author leegoo
 * @date 2019年07月22日
 */
public class User {

    private String username;

    private String pwd;

    public User(String username, String pwd) {
        this.username = username;
        this.pwd = pwd;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
