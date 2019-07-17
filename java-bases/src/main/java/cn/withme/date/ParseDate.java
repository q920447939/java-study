/**
 * @Project:
 * @Author: leegoo
 * @Date: 2019年07月15日
 */
package cn.withme.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ClassName: ParseDate
 *
 * @author leegoo
 * @Description:
 * @date 2019年07月15日
 */
public class ParseDate implements Runnable {



    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private int i;

    public ParseDate(int i) {
        this.i = i;
    }

    @Override
    public void run() {
        try {
            Date date = sdf.parse("2015-03-29 19:29:" + i % 60);
            System.out.println(i + ":" + date);
        } catch (NumberFormatException | ParseException e) {
            throw new NumberFormatException();
        }
    }

    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 1000; i++) {
            service.execute(new ParseDate(i));
        }
        service.shutdown();
    }
}
