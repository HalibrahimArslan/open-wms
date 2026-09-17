package com.hisarresearch.wms.scheduler;

import com.hisarresearch.wms.domain.AurLookupTable;
import com.hisarresearch.wms.domain.AurVwZReport;
import com.hisarresearch.wms.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import static com.hisarresearch.wms.utility.AurHelper.dayFinder;

@Component
public class DailyZReport {
    public static final String CRON_DAILY_GUN_ICI = "0 00-00 21-21 * * *";

    private  final String serviceName = "DAILY_Z_REPORT";
    private final  String lookupName = "Z_REPORT_MAIL";
    private final String zReportStatusCode = "Z_REPORT_MAIL_STATUS";

    private final Logger log = LoggerFactory.getLogger(DailyZReport.class);

    private final MailService mailService;
    private final AurLookupService lookupService;
    private final ZReportService zReportService;
    private final AurLogService logService;

    public DailyZReport(MailService mailService, AurLookupService lookupService, ZReportService zReportService, AurLogService logService) {
        this.mailService = mailService;
        this.lookupService = lookupService;
        this.zReportService = zReportService;
        this.logService = logService;
    }

    @Scheduled(cron = CRON_DAILY_GUN_ICI)
    public void sendZReportMail(){
        log.debug("Z report mail sending process is started");
        long logId = logService.logRequest(serviceName,"sendZReportMail","Service is started");

        List<AurLookupTable> zReportStatus = lookupService.getByLookupName(zReportStatusCode);

        if(!zReportStatus.isEmpty() && zReportStatus.get(0).getLookupCode().equals("false")){
            return;
        }

        List<AurVwZReport> zReportList = zReportService.dailyZReport(1);
        if (dayFinder() == DayOfWeek.SATURDAY) return;
        if(dayFinder() == DayOfWeek.SUNDAY){
            zReportList = zReportService.dailyZReport(7);
        }

        List<String> reportedList = new ArrayList<>();
        List<AurLookupTable> mailAddresses =  lookupService.getByLookupName(lookupName);

        mailAddresses.forEach(mail -> reportedList.add(mail.getLookupCode()));

        mailService.sendZReport(reportedList,zReportList);
        logService.logResponse(logId,"Z report : " + zReportList + "receiver list" + mailAddresses);
    }

}
