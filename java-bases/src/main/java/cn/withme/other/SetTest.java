/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月19日
 */
package cn.withme.other;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ClassName: set
 *
 * @author leegoo
 * @Description:
 * @date 2019年06月19日
 */
public class SetTest {

    public static class Inclass {
        private int pc ;

        public Inclass(int pc) {
            this.pc = pc;
        }

        public int getPc() {
            return pc;
        }

        public void setPc(int pc) {
            this.pc = pc;
        }
    }



    public static void main(String[] args) {
        List<Inclass>  list  = new ArrayList<>();
        list.add(new Inclass(1));
        list.add(new Inclass(2));
        Integer pc = 1;
        System.out.println( list.stream().max(Comparator.comparingInt(Inclass::getPc)).get().getPc());
        Set<Integer> collect = list.stream().map(e -> e.getPc()).collect(Collectors.toSet());
        Integer max = Collections.max(collect);
        if (max > 0) {
            pc = max + 1;
        }
        System.out.println(pc);
    }
}
