/**
 * @Project:
 * @Author: leegoo
 * @Date: 2023年03月29日
 */
package com.example.nettychatserver.async;


/**
 * ClassName: CallBack
 * @Description:
 * @author leegoo
 * @date 2023年03月29日
 */
public interface CallBack {
    boolean execute();
    boolean onSuccess();
    boolean onFail();
}
