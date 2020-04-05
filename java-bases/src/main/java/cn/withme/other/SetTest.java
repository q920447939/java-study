/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年06月19日
 */
package cn.withme.other;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ClassName: set
 *
 * @author leegoo
 * @Description:
 * @date 2019年06月19日
 */
public class SetTest {

    public static class Inclass {
        private int pc ;

        public Inclass(int pc) {
            this.pc = pc;
        }

        public int getPc() {
            return pc;
        }

        public void setPc(int pc) {
            this.pc = pc;
        }
    }



    public static void main(String[] args) throws ScriptException {
        String str = "(a >= 0 && a <= 5)";
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        engine.put("a",4);
        Object result = engine.eval(str);

        System.out.println("结果类型:" + result.getClass().getName() + ",计算结果:" + result);
    }


    public static String  getResult (int a ,int b ) {
        return (a+b)+"";
    }
}
