/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月12日
 */
package cn.withme.arithmetic;

/**
 * ClassName: CycleBoy
 *
 * @author leegoo
 * @Description:
 * @date 2019年06月12日
 */
public class CycleBoy {
    static Boy first = null;
    static Boy curBoy = null;

    public static void main(String[] args) {
        addBoy(5);
//        //showBoy(first);
        Boy pickBoys = josephus(2, 2);
        showBoy(pickBoys);

    }

    private static Boy josephus(int k, int m) {
        Boy tempRes = new Boy(-1);
        Boy result = tempRes;
        Boy tempcur = first;
        int i = 1;
        while (true) {
            Boy cur = tempcur;
            Boy next = tempcur.getNext();
            Boy boy = new Boy(tempcur.getNo());
            //找到了第一个小孩
            if (i == k) {
                //移除第k个小孩
                //让第k个小孩的前一个 链到第k个小孩的下一个(如果k的下一个 == k的前一个,也就是Node节点只有一个元素,那么就退出)
                tempcur.getPre().setNext(next);
                next.setPre(tempcur.getPre());

                //设置当前节点的第一个元素
                tempRes = tempcur;
                tempRes = next;

                //只判断只能进来一次,后续每次都是和m有关
                k = -1;
            } else {
                //判断 m%i== 0  或者 i%m==0  只能用大的数整除小的数,否则会报错
                try {
                    if (i > 1 && (m % tempcur.getNo() == 0 | tempcur.getNo() % m == 0)) {
                        //移除第k个小孩
                        //让第k个小孩的前一个 链到第k个小孩的下一个(如果k的下一个 == k的前一个,也就是Node节点只有一个元素,那么就退出)
                        if (null != tempcur.getPre()) {
                            tempcur.getPre().setNext(next);
                        }
                        next.setPre(tempcur.getPre());

                        //累加的后移
                        tempRes.setNext(next);
                        tempRes = tempcur.getNext();

                        if (tempRes.getPre() == tempRes.getNext()) break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            i++;
            //后移
            tempcur = next;
        }
        return result;
    }


    private static void showBoy(Boy head) {
        Boy temp = head;
        while (true) {
            System.out.printf("小孩的前一个编号:%d,当前小孩的编号:%d ,小孩的后一个编号:%d \n ",
                    (null == temp.getPre() ? null : temp.getPre().getNo()),
                    temp.getNo(),
                    (null == temp.getNext() ? null : temp.getNext().getNo())
            );
            temp = temp.getNext();
            if (temp == head) break;
        }

    }


    private static void addBoy(int num) {
        for (int i = 1; i <= num; i++) {
            Boy boy = new Boy(i);
            if (i == 1) {
                first = boy;
                first.setNext(first);
                curBoy = first;
                continue;
            }
            curBoy.setNext(boy);
            boy.setPre(curBoy);
            curBoy = curBoy.getNext();
            boy.setNext(first);
        }
    }
}
