package com.smpl.base.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 */
public final class DateFormat {


    /**
     * 获取当前 时间 返回 yyyy-MM-dd HH:mm:ss形式
     * @return
     */
    public static String getTimeFromat(){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date());
    }

    /**
     * 获取当前日期  返回 yyyy-MM-dd
     * @return
     */
    public static String getDateFromat(){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(new Date());
    }

    /**
     * 根据给定的时间格式 获取当前时间
     * @param dateFromatStr 时间格式 如："yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static String dateFromat(String dateFromatStr){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(dateFromatStr);
        return simpleDateFormat.format(new Date());
    }

    /**
     * 计算 两个时间的 时间差 返回 天数
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     * @throws ParseException
     */
    public static int getTimeDifferenceOfDate(String startTime,String endTime){
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int day=0;
        try {
            startTime =simpleDateFormat.format(startTime);
            endTime =simpleDateFormat.format(endTime);
            long start=simpleDateFormat.parse(startTime).getTime();
            long end=simpleDateFormat.parse(endTime).getTime();
            day=(int) ((end-start)/(1000*60*60*24));
        }catch (ParseException e){
            e.printStackTrace();
        }
        return day;
    }

    /**
     *  获取 两个时间的 间隔  精确到秒
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 返回 字符串
     */
    public static String getTimeDifference(String startTime,String endTime){
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr=null;
        try {
            long start=simpleDateFormat.parse(startTime).getTime();
            long end=simpleDateFormat.parse(endTime).getTime();
            long diff=end-start;
            long day=diff/(1000*60*60*24);
            long hours=diff/(1000*60*60)-(diff*24);
            long min=diff/(1000*60)-(diff*24*60);
            long sec=diff/1000-(diff*24*60*60);
            dateStr=day+"天"+hours+"小时"+min+"分钟"+sec+"秒";
        }catch (ParseException e){
            e.printStackTrace();
        }
        return dateStr;
    }

}
