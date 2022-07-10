package nuc.bysjxtglxt.controller;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeController {
    @Scheduled(cron = "0/2 * * * * *")
    public String timer(){
        //获取当前时间
        LocalDateTime localDateTime =LocalDateTime.now();
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}
