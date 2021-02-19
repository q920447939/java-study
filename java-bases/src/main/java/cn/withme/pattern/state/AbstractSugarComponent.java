/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年12月08日
 */
package cn.withme.pattern.state;

/**
 * ClassName: AbstractSugarComponent
 * @Description:
 * @author leegoo
 * @date 2020年12月08日
 */
public abstract class AbstractSugarComponent {
    protected boolean matchMoney;
    protected boolean existsStock;

    protected void sellSugar(){
        if (isMatchMoney()){
            System.out.println("条件符合...有25分");
            if (isExistsStock()){
                System.out.println("售出糖果");
            }else{
                System.out.println("不足糖果");
            }
        }else{
            System.out.println("条件不符合...没有25分");

        }
    }

    abstract boolean isMatchMoney();
    abstract boolean isExistsStock();


    public static void main(String[] args) {
        new AbstractSugarComponent() {
            @Override
            boolean isMatchMoney() {
                return true;
            }

            @Override
            boolean isExistsStock() {
                return true;
            }
        }.sellSugar();
    }
}
