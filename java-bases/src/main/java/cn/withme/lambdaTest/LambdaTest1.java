/**
 * @Project:
 * @Author: leegoo
 * @Date: 2021年08月25日
 */
package cn.withme.lambdaTest;

/**
 * ClassName: LambdaTest1
 * @Description:
 * @author leegoo
 * @date 2021年08月25日
 */
public class LambdaTest1 {
    public static void main(String[] args) {
        Zoo zoo = Zoo.CREATE;
        System.out.println(zoo.create("1"));
    }


    @FunctionalInterface
    static interface Zoo {
        Zoo CREATE =  (str)-> new NoVoice();

        Voice create(String str);
    }


    static interface Voice  {

    }

    static class NoVoice implements   Voice {
        public NoVoice() {
            System.out.println(123);
        }
    }

}
