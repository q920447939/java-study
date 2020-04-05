/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年03月20日
 */
package cn.withme.pattern.strategy2;



/**
 * ClassName: Result
 * @Description:
 * @author leegoo
 * @date 2020年03月20日
 */
public class Result<T> {
    private boolean result;
    private String resultDesc;
    private T t;

    public Result() {
    }

    public Result(boolean result) {
        this.result = result;
    }

    public  final Result<T>  fail (String desc){
        this.result = false;
        this.resultDesc = desc;
        return  this;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    @Override
    public String toString() {
        return "Result{" +
                "result=" + result +
                ", resultDesc='" + resultDesc + '\'' +
                ", t=" + t +
                '}';
    }
}
