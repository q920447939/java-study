/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月05日
 */
package arithmetic;

import java.util.Arrays;

/**
 * ClassName: SparseArrTest
 *
 * @author leegoo
 * @Description:
 * @date 2019年06月05日
 */
public class SparseArrTest {

    public static void main(String[] args) {
        int row = 11, col = 11;
        int[][] arr = new int[row][col];
        arr[1][2] = 1;
        arr[2][3] = 2;


        int sum = 0;
        for (int[] ints : arr) {
            for (int anInt : ints) {
                if (anInt != 0) {
                    sum++;
                }
            }
        }

        //to spares
        int[][] sparesArr = new int[sum + 1][3];
        int number = 0;
        sparesArr[number][0] = row;
        sparesArr[number][1] = col;
        sparesArr[number][2] = sum;
        for (int i = 0; i < arr.length; i++) {
            for (int k = 0; k < arr[i].length; k++) {
                if (arr[i][k] != 0) {
                    sparesArr[++number][0] = i;
                    sparesArr[number][1] = k;
                    sparesArr[number][2] = arr[i][k];
                }
            }
        }
        int[][] res =new int[sparesArr[0][0]][sparesArr[0][1]];
        for (int i = 1; i < sparesArr.length; i++) {
            int val = -1; row = -1;col = -1;
            for (int k = 0; k < sparesArr[i].length; k++) {
                switch (k) {
                    case 0: row =sparesArr[i][k] ;  break;
                    case 1:col =sparesArr[i][k] ; break;
                    case 2:val = sparesArr[i][k] ;break;
                    default: break;
                }
            }
            res[row][col] = val;
        }
        for (int[] re : res) {
            System.out.println(Arrays.toString(re));
        }
    }
}
