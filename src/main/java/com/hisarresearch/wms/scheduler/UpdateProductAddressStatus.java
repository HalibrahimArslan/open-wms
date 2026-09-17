package com.hisarresearch.wms.scheduler;

import com.hisarresearch.wms.domain.address.AurDepoUrunAdresStok;
import com.hisarresearch.wms.repository.address.AurDepoUrunAdresStokRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
/*
* Scheduler ve cron yapısının kullanılması için yazıldı.
* Adres işlemlerinde miktarı 0 olup statusu false yapılmamış serviceler güncellendikten sonra kapatılabilir.
* */
@Component
public class UpdateProductAddressStatus {
    public static final String CRON_DAILY_GUN_ICI = "0 */30 8-20 * * *"; //Her gün saat 08.00-20.00 arasında (20.00 haric) 30 dakikada bir çalışır.
    private final Logger log = LoggerFactory.getLogger(UpdateProductAddressStatus.class);

    @Autowired
    AurDepoUrunAdresStokRepository aurDepoUrunAdresStokRepository;


    @Scheduled(cron = CRON_DAILY_GUN_ICI)
    public void updateStatus(){
        try {
            log.info("Miktarı Sıfıra düşen ürünlerin statusunun false olarak güncellenmesi");
            List<AurDepoUrunAdresStok> urunAdresStokList = aurDepoUrunAdresStokRepository.findByStatusAndMiktar(true,0.0);
            if(!urunAdresStokList.isEmpty()){
                urunAdresStokList.forEach(item -> item.setStatus(false));
            }
        }
        catch(Exception e){
            log.info("Ürün statü güncelleme servisi hata aldı. Hata detayı :" + e.getMessage());

        }
    }
}
