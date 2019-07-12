/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年07月11日
 */
package cn.withme.pattern;

/**
 * ClassName: StrategyPattern
 * @Description:策略模式
 *
 * 如果不使用策略模式
 * if - else
 * 那么
 *
 *
 * @author leegoo
 * @date 2019年07月11日
 */
public class StrategyPattern {

    public interface  Eat {
        String eatFruit ();
    }

    public static class Apple implements Eat{
        @Override
        public String eatFruit() {
            return "吃了一个苹果";
        }
    }


    public static  class Banan implements Eat{
        @Override
        public String eatFruit() {
            return "吃了一个香蕉";
        }
    }

    public static class Strategy {
        private  final Eat eat;

        public Strategy(Eat eat) {
            this.eat = eat;
        }


        public String eatFruit() {
           return eat.eatFruit();
        }

    }

    public static void main(String[] args) {
        Strategy strategy = new Strategy(new Apple());
        System.out.println(strategy.eatFruit());
    }

}
