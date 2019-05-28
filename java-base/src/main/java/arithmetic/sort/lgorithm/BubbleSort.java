/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年05月24日
 */
package arithmetic.sort.lgorithm;

import java.util.Arrays;
import java.util.HashMap;
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
        int idx = 0;
        int left = 0;
        int right = arr.length - 1;

        while (idx != arr.length) {
            idx++;
            int middle = (left + right) / 2;
            int temp = arr[middle];
            if (temp == x) return middle;
            else if (x < temp ) right-=1;
            else if (x > temp ) left+=1;
        }
        return -1;
    }

    public static void main(String[] args) {
        int[] ints = new int[]{3, 11, 22, 34, 55,56,64};
        //  Arrays.sort(ints);
        System.out.println("正确排序后的数组:" + Arrays.toString(ints));
        int i = twoPointFind(ints, 55);
        System.out.println(i);
    }
}
