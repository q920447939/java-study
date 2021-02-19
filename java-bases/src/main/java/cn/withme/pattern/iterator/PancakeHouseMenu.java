/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年12月02日
 */
package cn.withme.pattern.iterator;

import lombok.ToString;

import java.util.ArrayList;

/**
 * ClassName: PancakeHouseMenu
 * @Description:
 * @author leegoo
 * @date 2020年12月02日
 */
public class PancakeHouseMenu extends MenuItem{
     ArrayList<PancakeHouseMenu> meanItems;

    PancakeHouseMenu(){
        meanItems = new ArrayList<>();
        this.buildMenus();
    }

    PancakeHouseMenu(String name, String description, boolean vegetation, double price) {
        this.name = name;
        this.description = description;
        this.vegetation = vegetation;
        this.price = price;
    }


    public void buildMenus() {
        meanItems.add( new PancakeHouseMenu("广东肠粉","广东肠粉",false,6));
        meanItems.add( new PancakeHouseMenu("豆浆","豆浆",false,4));
        meanItems.add( new PancakeHouseMenu("鸡蛋灌饼","鸡蛋灌饼",false,4));
    }


    public ArrayList<PancakeHouseMenu> getMeanItems() {
        return meanItems;
    }

    public void setMeanItems(ArrayList<PancakeHouseMenu> meanItems) {
        this.meanItems = meanItems;
    }

    @Override
    public String toString() {
        return "PancakeHouseMenu{" +
                "meanItems=" + meanItems +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", vegetation=" + vegetation +
                ", price=" + price +
                '}';
    }
}
