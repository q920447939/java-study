/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年07月22日
 */
package cn.withmes.springboot.singlepointjwt;

/**
 * ClassName: Response
 * @Description: 响应体
 * @author leegoo
 * @date 2019年07月22日
 */
public class ResponseData<E> {

    private  String token ;

    private E data;

    private  int code ;

    private String msg ;

    public ResponseData( ) {
        this.code = -1;
    }

    public ResponseData(int code) {
        this.code = code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
