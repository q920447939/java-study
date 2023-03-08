package cn.withmes;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Dateutil
{

    /**
     * 取得今天的日期
     *
     * @return
     */
    public static String getToday()
    {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        return sdf.format(new Date().getTime());
    }

    /**
     * 取得昨天的日期
     *
     * @return
     */
    public static String getYestoday()
    {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date date = calendar.getTime();
        return sdf.format(date.getTime());
    }

    public static String getNow()
    {
        //HH表示用24小时制，如18；hh表示用12小时制
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return sdf.format(new Date().getTime());

    }

    public static String getTime()
    {
        //HH表示用24小时制，如18；hh表示用12小时制
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        return sdf.format(new Date().getTime());

    }
}
