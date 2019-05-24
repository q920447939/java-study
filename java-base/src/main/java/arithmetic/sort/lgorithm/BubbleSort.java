/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年05月24日
 */
package arithmetic.sort.lgorithm;

import java.util.Arrays;
import java.util.Random;

/**
 * ClassName: BubbleSort
 *
 * @author leegoo
 * @Description:
 * @date 2019年05月24日
 */
public class BubbleSort {

    public static int[] create(int length) {
        int[] arr = new int[length];
        Random random = new Random();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = random.nextInt(100) - random.nextInt(100);
        }
        return arr;
    }

    // 24,33, 36, -38, -27, 23, -11]
    public static void sort(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int k = 0; k < arr.length - 1; k++) {
                if (arr[k] > arr[k + 1]) {
                    int temp = arr[k];
                    arr[k] = arr[k + 1];
                    arr[k + 1] = temp;
                }
            }
        }
    }

    public static int twoPointFind(int[] arr, int x) {
        int res = -1;
        int idx = 0;
        int hight = arr.length-1;
        return null;
    }

    public static void main(String[] args) {
        int[] ints = new int[]{1,2,3,4,5};
      //  Arrays.sort(ints);
        System.out.println("正确排序后的数组:" + Arrays.toString(ints));
        int i = twoPointFind(ints, 2);
        System.out.println(i);
    }
}
