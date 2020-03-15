/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年03月14日
 */
package cn.withmes.springboot.my.aop.aop.exception;

/**
 * ClassName: MyException
 * @Description:
 * @author leegoo
 * @date 2020年03月14日
 */
public class MyException  extends Exception {
    /*无参构造函数*/
    public MyException(){
        super();
    }

    //用详细信息指定一个异常
    public MyException(String message){
        super(message);
    }

    //用指定的详细信息和原因构造一个新的异常
    public MyException(String message, Throwable cause){
        super(message,cause);
    }

    //用指定原因构造一个新的异常
    public MyException(Throwable cause) {
        super(cause);
    }
}