package cn.withme.domain;



public class HeroNode {

    private int val;

    private String name;

    private HeroNode next;

    private int iud;

    public HeroNode() {
    }

    public HeroNode(int val, HeroNode next) {
        this.val = val;
        this.next = next;
    }

    public HeroNode(int i) {
        this.val = i;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public HeroNode getNext() {
        return next;
    }

    public void setNext(HeroNode next) {
        this.next = next;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getIud() {
        return iud;
    }

    public void setIud(int iud) {
        this.iud = iud;
    }

    @Override
    public String toString() {
        return "HeroNode{" +
                "val=" + val +
                ", name='" + name + '\'' +
                ", iud=" + iud +
                ", next=" + next +
                '}';
    }
}

