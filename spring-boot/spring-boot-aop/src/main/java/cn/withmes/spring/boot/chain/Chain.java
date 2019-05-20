/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年05月14日
 */
package cn.withmes.spring.boot.chain;

import lombok.Data;
import lombok.ToString;

import java.util.Arrays;

/**
 * ClassName: Chain
 *
 * @author leegoo
 * @Description:
 * @date 2019年05月14日
 */
public class Chain {


    @Data
    @ToString
    static class ChainSubA extends Process {
        @Override
        protected void execute() {
            System.out.println("in ChainSubA...");
        }

    }

    @Data
    @ToString
    static class ChainSubB extends Process {
        @Override
        protected void execute() {
            System.out.println("in ChainSubB...");
        }
    }

    @Data
    @ToString
    static class ChainSubC extends Process {
        @Override
        protected void execute() {
            System.out.println("in ChainSubC...");
        }
    }

    public static void main(String[] args) {
        /*Process chainSubA = new ChainSubA();
        Process chainSubB = new ChainSubB();
        Process chainSubC = new ChainSubC();
        chainSubA.setChain(chainSubB);
        chainSubB.setChain(chainSubC);
        chainSubA.processHandler();*/
        Process chainSubA = new ChainSubA();
        chainSubA.setNode( Arrays.asList(new ChainSubA(),new ChainSubB(),new ChainSubC()));
        chainSubA.processHandler();
    }
}
