/**
 * @Project:
 * @Author: leegoo
 * @Date: 2022年12月02日
 */
package cn.withme.createDocument;

/**
 * ClassName: CreateA
 * @Description:
 * @author leegoo
 * @date 2022年12月02日
 */
public class CreateA {
    public static void main(String[] args) {
        printCreateA(29);
    }

    public static void printCreateA(int number) {
        for (int i = 1; i <= number; i++) {
            System.out.println("a"+i);
        }
    }
}
