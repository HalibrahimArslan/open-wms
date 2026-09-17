package com.hisarresearch.wms.repository;

import com.hisarresearch.wms.domain.AurSayimUrun;
import com.hisarresearch.wms.domain.enumeration.SayimDurumu;
import com.hisarresearch.wms.service.dto.ComparativeCountingResultDTO;
import com.hisarresearch.wms.service.dto.CountingReportMicroDto;
import com.hisarresearch.wms.service.dto.CountingReportDto;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the AurSayimUrun entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AurSayimUrunRepository extends JpaRepository<AurSayimUrun, Long>, JpaSpecificationExecutor<AurSayimUrun> {
    List<AurSayimUrun> findByAurSayimTanim_Id(Long sayimTanimId);
    List<AurSayimUrun> findByAurSayimTanim_IdAndStatusAndProduct_Id_Barkod(Long sayimTanimId, SayimDurumu sayimDurumu,String barcode);
    Optional<AurSayimUrun> findByAddress_UrunAdresIdAndStokKodAndAurSayimTanim_Id(Long addressId,String stockCode,Long sayimTanimId);
    List<AurSayimUrun> findByAurSayimTanim_IdAndStatus(long sayimTanimId,SayimDurumu sayimDurumu);
    @Query(
        value = "select asu.stok_kod as stokKod," +
            "esi.stok_adi as stokAdi," +
            "asu.miktar as adet," +
            "adua.adres as adres," +
            "esi.ana_grup as anaGrup," +
            "esi.stok_birimi as stokBirimi,"+
            "esi.kategori_adi as kategoriAdi from aur_sayim_urun asu " +
            "left join product esi " +
            "on asu.barkod = esi.barkod " +
            "inner join aur_depo_urun_adres adua " +
            "on asu.address_id = adua.id " +
            "where asu.aur_sayim_tanim_id = :countingDefinitionId ",
        nativeQuery = true
    )
    List<CountingReportDto> getCountingReport(@Param("countingDefinitionId") Long countingDefinitionId);

    @Query(
        value = "SELECT esi.stok_kodu AS stokKod," +
        "esi.stok_adi AS stokAdi," +
        "t.miktar AS adet,"+
        "esi.miktar AS mikro,"+
        "esi.stok_birimi AS stokBirimi,"+
        "(t.miktar-esi.miktar) AS fark,"+
        "esi.ana_grup as anaGrup,"+
        "esi.kategori_adi as kategoriAdi,"+
        "esi.description as detaylar "+
        "FROM (SELECT asu.barkod,sum(asu.miktar) AS miktar from aur_sayim_urun asu WHERE asu.aur_sayim_tanim_id = :countingDefinitionId group by barkod) AS t " +
        "LEFT JOIN product esi " +
        "ON t.barkod = esi.barkod " +
        "ORDER BY esi.stok_kodu",
        nativeQuery = true
    )
    List<CountingReportMicroDto> getCountingReportMicro(@Param("countingDefinitionId") Long countingDefinitionId);

    @Query(value = "select * from (" +
        "with counting AS (" +
        "select " +
        "asu.aur_sayim_tanim_id," +
        "asu.barkod," +
        "aduas.adres," +
        "aduas.bolum," +
        "aduas.unite," +
        "aduas.kat," +
        "asu.miktar as sayim_miktar," +
        "asu.last_modified_by as sayim_son_guncelleyen," +
        "esi.kategori_adi," +
        "esi.ana_grup," +
        "esi.stok_adi," +
        "esi.stok_kodu," +
        "esi.miktar as mikroMiktar," +
        "esi.description AS description " +
        "from aur_sayim_urun asu " +
        "left join product esi " +
        "on asu.barkod = esi.barkod " +
        "left join aur_depo_urun_adres aduas " +
        "on asu.address_id = aduas.id " +
        "where asu.aur_sayim_tanim_id = :countingId)" +
        "select " +
        "COALESCE(counting.kategori_adi,controlling.kategori_adi) AS kategoriAdi," +
        "COALESCE(counting.ana_grup,controlling.ana_grup) AS anaGrup," +
        "COALESCE(counting.barkod,controlling.barkod) as barkod," +
        "COALESCE(counting.stok_kodu,controlling.stok_kodu) AS stokKodu," +
        "COALESCE(counting.description, controlling.description) AS description, " +
        "COALESCE((select 1 from aur_partial_item api where api.package_barcode = COALESCE(counting.barkod,controlling.barkod)),0) AS anaParca," +
        "COALESCE(counting.stok_adi,controlling.stok_adi) AS stokAdi," +
        "COALESCE(counting.bolum,controlling.bolum) AS bolum," +
        "COALESCE(counting.unite,controlling.unite) AS unite," +
        "COALESCE(counting.kat,controlling.kat) AS kat," +
        "COALESCE(counting.adres,'OKUTULMADI') AS sayimAdresi," +
        "COALESCE(controlling.adres,'OKUTULMADI') AS kontrolAdresi," +
        "ROUND(COALESCE(CAST(counting.mikroMiktar AS numeric),CAST(controlling.mikroMiktar as numeric),0),2) AS mikroMiktar," +
        "counting.sayim_son_guncelleyen AS sayimSonGuncelleyen," +
        "controlling.kontrol_son_guncelleyen AS kontrolSonGuncelleyen," +
        "ROUND(COALESCE(CAST(controlling.sayim_miktar AS numeric), 0), 2) AS kontrolMiktar," +
        "ROUND(COALESCE(CAST(counting.sayim_miktar AS numeric), 0), 2) AS sayimMiktar," +
        "ROUND(COALESCE(CAST(counting.sayim_miktar AS numeric), 0) - COALESCE(CAST(controlling.sayim_miktar AS numeric), 0), 2) AS fark " +
        "FROM counting " +
        "full join (" +
        "select " +
        "asu.aur_sayim_tanim_id," +
        "asu.barkod," +
        "asu.last_modified_date as kontrol_son_guncelleyen," +
        "aduas.adres," +
        "aduas.bolum," +
        "aduas.unite," +
        "aduas.kat," +
        "asu.miktar as sayim_miktar," +
        "esi.kategori_adi," +
        "esi.ana_grup," +
        "esi.stok_adi," +
        "esi.stok_kodu," +
        "esi.miktar as mikroMiktar," +
        "esi.description AS description from aur_sayim_urun asu " +
        "left join product esi " +
        "on asu.barkod = esi.barkod " +
        "left join aur_depo_urun_adres aduas " +
        "on asu.address_id = aduas.id " +
        "where asu.aur_sayim_tanim_id = :controllingId) as controlling " +
        "on counting.barkod = controlling.barkod and counting.adres = controlling.adres) as t",
        nativeQuery = true)
    List<ComparativeCountingResultDTO> getComparativeCountingReport(@Param("countingId") long countingId, @Param("controllingId") long controllingId);


}
