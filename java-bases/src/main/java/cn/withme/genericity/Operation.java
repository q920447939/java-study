/**
 * @Project:
 * @Author: leegoo
 * @Date: 2020年06月21日
 */
package cn.withme.genericity;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * ClassName: Operation
 * @Description:
 * @author leegoo
 * @date 2020年06月21日
 */
public class  Operation {



    public static void main(String[] args) {
        Optional<OperationDemo> operationDemo = OperationDemo.fromString("+");
        //operationDemo.ifPresent();


    }
}

 enum  OperationDemo {
    PLUS {
        @Override
        public double apply(double x, double y){return x + y;}
    },
    MINUS {
        @Override
        public double apply(double x, double y){return x - y;}};

    public abstract double apply(double x, double y);

     private static final Map<String, OperationDemo> stringToEnum = Stream.of(OperationDemo.values()).collect(Collectors.toMap(Objects::toString, e -> e));

     public static Optional<OperationDemo> fromString(String symbol) {
            return Optional.ofNullable(stringToEnum.get(symbol));
     }
}

/*

 PLUS {public double apply(double x, double y){return x + y;}},
         MINUS {public double apply(double x, double y){return x - y;}},
         TIMES {public double apply(double x, double y){return x * y;}},
         DIVIDE{public double apply(double x, double y){return x / y;}};
         public abstract double apply(double x, double y);*/
