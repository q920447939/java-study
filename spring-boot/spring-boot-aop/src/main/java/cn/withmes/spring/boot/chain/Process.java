/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年05月14日
 */
package cn.withmes.spring.boot.chain;

import java.util.List;

/**
 * ClassName: Process
 *
 * @author leegoo
 * @Description:
 * @date 2019年05月14日
 */
public abstract class Process {

    private Process chain;

    public static List<Process> node = null;

    public  void setNode(List<Process> node) {
        Process.node = node;
    }

    private int index = 0;

    public Process getChain() {
        return chain;
    }

    public void setChain(Process chain) {
        this.chain = chain;
    }


    protected abstract void execute();

    public  void processHandler() {
        if (0 != node.size() && index < node.size()) {
            Process process = node.get(index++);
            process.execute();
            processHandler();
        }
    }
}
