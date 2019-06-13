/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月05日
 */
package cn.withme.arithmetic;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;


/**
 * ClassName: QueueTest
 *
 * @author leegoo
 * @Description:
 * @date 2019年06月05日
 */
public class QueueTest {

    static class ArrQueue {
        private int head = 0;
        private int end = -1;
        private int max;
        private int[] arr;


        public ArrQueue(int max) {
            this.max = max;
            this.arr = new int[max];
            for (int i : this.arr) arr[i] = -1;
        }

        public String shows() {
            return Arrays.toString(arr);
        }

        public void store(int num) {
            if ((end+1) >= max) {
                throw new RuntimeException("队列已满");
            }
            arr[++end] = num;
        }

        public int fetch() {
            if (end < 0) {
                throw new RuntimeException("没有元素了....");
            }
            int res = arr[head];
            System.arraycopy(arr, head + 1, arr, 0, arr.length-1);
            arr[end] = -1;
            end--;
            return res;
        }

        public int headE() {
            if (this.head < 0) {
                throw new RuntimeException("还没有插入元素....");
            }
            return arr[head];
        }

        public int endE() {
            if (this.end < 0) {
                throw new RuntimeException("还没有插入元素....");
            }
            return arr[end];
        }


    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean flg = true;
        ArrQueue arrQueue = new ArrQueue(3);
        while (flg) {
            try {
            char c = scanner.next().charAt(0);
                switch (c) {
                    case 'a':
                        arrQueue.store(new Random().nextInt(10));
                        break;
                    case 'r':
                        System.out.println("取出来的元素是:" + arrQueue.fetch());
                        break;
                    case 's':
                        System.out.println("所有的元素是:" + arrQueue.shows());
                        break;
                    case 'h':
                        System.out.println("头部元素是:" + arrQueue.headE());
                        break;
                    case 'e':
                        System.out.println("尾部元素是:" + arrQueue.endE());
                        break;
                    default:
                        flg = false;
                        scanner.close();
                        break;
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

}
