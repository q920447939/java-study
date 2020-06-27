/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年04月10日
 */
package cn.withme.other;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ClassName: Weight
 * @Description:权重
 * @author leegoo
 * @date 2020年04月10日
 */
public class Weight {

    static Random random = new Random();

    public static void main(String[] args) {

        for (int i = 0; i < 10; i++) {
            doWeight();
        }
    }

    private static void doWeight() {
        Set<WeightInner> weightInners = new LinkedHashSet<>();
        weightInners.add(new WeightInner(0, 3.5));
        weightInners.add(new WeightInner(3.5, 10));
        for (int i = 0; i < 10; i++) {
            int i1 = random.nextInt(10);
            weightInners.stream().filter(k->{
                return k.min <= i1 && k.max > i1;
            }).findFirst().ifPresent(k->k.count++);
            System.out.print(i1+"\t");
        }
        System.out.println("\n"+weightInners);
    }

    public  static  class WeightInner {
        private double min ;
        private  double max;
        private int count;

        public WeightInner(double min, double max) {
            this.min = min;
            this.max = max;
        }

        public void setMax(int max) {
            this.max = max;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof WeightInner)) return false;

            WeightInner that = (WeightInner) o;

            if (min != that.min) return false;
            if (max != that.max) return false;
            return count == that.count;
        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            temp = Double.doubleToLongBits(min);
            result = (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(max);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            result = 31 * result + count;
            return result;
        }

        @Override
        public String toString() {
            return "WeightInner{" +
                    " count=" + count +
                    '}';
        }
    }
}
