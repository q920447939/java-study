package cn.withme.pattern.observe;

import javax.swing.*;
import java.awt.*;

/**
 * @author liming
 * @Description:
 * @date 2020年01月03日
 */
public class MyFrame {

    private static JFrame jFrame;

    public static void main(String[] args) {
        jFrame  = new JFrame();
        JButton bt = new JButton();
        bt.addActionListener(t->{
            System.out.println("i am jack");
        });
        bt.addActionListener(k->{
            System.out.println("i am rose");
        });
        jFrame.getContentPane().add(BorderLayout.CENTER,bt);
        jFrame.show();
    }
}
