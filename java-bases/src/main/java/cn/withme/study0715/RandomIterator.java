/**
 * @Project:
 * @Author: leegoo
 * @Date: 2021年07月15日
 */
package cn.withme.study0715;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * ClassName: Random
 * @Description:
 * @author leegoo
 * @date 2021年07月15日
 */
public class RandomIterator<T> implements  Iterable<T>{

    private final List<T> list;

    public RandomIterator(List<T> list) {
        this.list = list;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public T next() {
                return (T) (list.get(new Random().nextInt(list.size())));
            }
        };
    }

    public static void main(String[] args) {
        List<String> temList = Arrays.asList("List", "Set", "Ma");
        RandomIterator<String> iterator = new RandomIterator<>(temList);
        Iterator<String> iterator1 = iterator.iterator();
        int count = 0;
        while (iterator1.hasNext()){
            String next = iterator1.next();
            System.out.println(next);
            count ++ ;
            if (count > 10 ){
                System.out.println("done...");
                break;
            }
        }
       /* ArrayList<String> arrays = new ArrayList<String>();
        arrays.toArray(String[]::new);*/
        Optional<Integer> optionalInteger = Optional.empty();
        Optional<Integer> integer = optionalInteger.map(x -> x * x);
        System.out.println(integer);
    }




}
