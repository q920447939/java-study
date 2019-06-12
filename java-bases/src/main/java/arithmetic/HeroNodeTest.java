/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月11日
 */
package arithmetic;

import java.util.Stack;

/**
 * ClassName: HeroNodeTest
 *
 * @author leegoo
 * @Description:
 * @date 2019年06月11日
 */
public class HeroNodeTest {

    static HeroNode n3 = new HeroNode(5, 5, null);
    static HeroNode n2 = new HeroNode(2, 2, n3);
    static HeroNode n1 = new HeroNode(1, 1, n2);



    static HeroNode n6 = new HeroNode(6, 6, null);
    static HeroNode n5 = new HeroNode(5, 5, n6);
    static HeroNode n4 = new HeroNode(4, 4, n5);


    public static void reprintNode(HeroNode n1) {
        Stack<HeroNode> heroNodes = new Stack<>();
        HeroNode temp = n1;
        while (true) {
            heroNodes.push(temp);
            HeroNode next = temp.getNext();
            if (null == next) break;
            temp = next;
        }
        //System.out.println("temp:"+temp);
        while (!heroNodes.empty()) {
            HeroNode pop = heroNodes.pop();
            pop.setNext(null);
            System.out.println(pop);
        }

    }

    public static HeroNode mergeDoubleNode(HeroNode n1, HeroNode n2) throws  Exception{
        HeroNode re = new HeroNode(0, 0, null);
        HeroNode temp1 = n1;
        HeroNode temp2 = n2;
        HeroNode result =re;
        while (true) {
            if (null == temp1 && null == temp2) {
                break;
            }
            HeroNode intemp1 = null;
            if (null != temp1) {
                intemp1= (HeroNode)temp1.clone();
            }
            HeroNode intemp2 = null;
            if (null != temp2) {
                intemp2=(HeroNode) temp2.clone();
            }

            if (null == temp1 ) {
                intemp2.setNext(null);
                re.setNext(intemp2);
                temp2 = temp2.getNext();
            } else if (null == temp2 ) {
                intemp1.setNext(null);
                re.setNext(intemp1);
                temp1 = temp1.getNext();
            } else if (temp1.getId() > temp2.getId()) {
                intemp2.setNext(null);
                re.setNext(intemp2);
                temp2 = temp2.getNext();
            } else if (temp1.getId() == temp2.getId()) {
                intemp1.setNext(null);
                intemp2.setNext(null);

                re.setNext(intemp1);
                re = re.getNext();
                re.setNext(intemp2);

                temp1 = temp1.getNext();
                temp2 = temp2.getNext();
            } else if (temp1.getId() < temp2.getId())  {
                intemp1.setNext(null);

                re.setNext(intemp1);
                temp1 = temp1.getNext();
            }else{
                break;
            }
            re = re.getNext();
        }
        return  result.getNext();
    }

    public static void main(String[] args)throws  Exception {

        //reprintNode(n1);
        // System.out.println(n1);
        HeroNode heroNode = mergeDoubleNode(n1, n4);
        System.out.println(heroNode);

    }
}
