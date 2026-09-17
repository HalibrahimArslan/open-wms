package com.hisarresearch.wms.repository.address;

import com.hisarresearch.wms.domain.address.AddressParams;
import com.hisarresearch.wms.domain.address.AurDepoUrunAdres;
import java.util.List;
import java.util.Optional;

import com.hisarresearch.wms.service.dto.address.AddressCountingResponseDto;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AurDepoAdresRepository extends JpaRepository<AurDepoUrunAdres, Long> ,JpaSpecificationExecutor<AurDepoUrunAdres> {
    @Query(
        value = "select adua.urun_adres_id  from aur_depo_urun_adres adua  order by adua .urun_adres_id  desc limit 1 ",
        nativeQuery = true
    )
    Long findTop1ByOrderByPaletBarkodIdDesc();

    AurDepoUrunAdres findByAdres(String adres);

    Optional<AurDepoUrunAdres> findByAdresAndDepoNo(String address, String depoNo );

    Optional<AurDepoUrunAdres> findByAdresAndDepoNoAndCompanyCode(String adres, String depoNo, String companyCode);

    Optional<AurDepoUrunAdres> findByAdresAndDepoNoAndCompanyCodeAndStatus(String adres, String depoNo, String companyCode,boolean status);

    AurDepoUrunAdres findByUrunAdresId(Long urunAdresId);

    List<AurDepoUrunAdres> findByDepoNoAndCompanyCodeAndGeciciAdresAndStatusTrue(String depoNo, String companyCode, Boolean geciciAdres);

    List<AurDepoUrunAdres> findByDepoNoAndCompanyCodeAndStatusAndKontrolAdres(String depoNo, String companyCode,Boolean status, Boolean kontrolAdres);

    @Query(
        value = "select aduas.id,aduas.adres from aur_depo_urun_adres aduas " +
            "left join (select * from counting_address_exception where counting_definition_id = :sayimTanimId and status = true) as tmp " +
            "on aduas.id = tmp.address_id  where tmp.id is null and aduas.depo_no = :depoCode order by aduas.adres",
        nativeQuery = true
    )
    List<AddressCountingResponseDto> getCountOfCountableAddress(@Param("sayimTanimId") Long sayimTanimId,@Param("depoCode") String depoCode);

    @Query(
        value = "SELECT a.urunAdresId as id, a.adres as adres " +
            "FROM AurDepoUrunAdres a " +
            "WHERE a.status = true " +
            " AND NOT EXISTS ( " +
            "    SELECT 1 " +
            "    FROM AurSayimUrun su " +
            "    WHERE su.address.urunAdresId = a.urunAdresId " +
            "      AND su.aurSayimTanim.id = :countingDefinitionId " +
            ") " +
            "  AND NOT EXISTS ( " +
            "    SELECT 1 " +
            "    FROM CountingAddressException cae " +
            "    WHERE cae.address.urunAdresId = a.urunAdresId " +
            "      AND cae.countingDefinition.id = :countingDefinitionId " +
            ")"
    )
    List<AddressCountingResponseDto> getNotCountedAddresses(@Param("countingDefinitionId") Long countingDefinitionId);

    @Query(
        value = "SELECT a.urunAdresId as id, a.adres as adres " +
            "FROM AurDepoUrunAdres a " +
            "WHERE a.urunAdresId IN :addressIds " +
            " AND EXISTS ( " +
            "    SELECT 1 " +
            "    FROM AurSayimUrun su " +
            "    WHERE su.address.urunAdresId = a.urunAdresId " +
            "      AND su.aurSayimTanim.id = :countingDefinitionId " +
            ") " +
            "  AND NOT EXISTS ( " +
            "    SELECT 1 " +
            "    FROM CountingAddressException cae " +
            "    WHERE cae.address.urunAdresId = a.urunAdresId " +
            "      AND cae.countingDefinition.id = :countingDefinitionId " +
            ")"
    )
    List<AddressCountingResponseDto> getCountedAddresses(@Param("countingDefinitionId") Long countingDefinitionId,@Param("addressIds") List<Long> addressIds);

    List<AurDepoUrunAdres> findByCountableAndDepoNoAndStatus(Boolean countable,String depoNo,Boolean status);

    @EntityGraph(attributePaths = {"productAddressList"})
    List<AurDepoUrunAdres> findByDepoNoAndStatusAndCompanyCode(String depoNo,Boolean status, String companyCode);

    List<AurDepoUrunAdres> findByDepoNoAndStatusAndCompanyCodeAndReyon(String depoNo,Boolean status, String companyCode,String rayon);

    @Query(value="select depo_no AS depoNo,company_code AS companyCode,bolum AS param from aur_depo_urun_adres group by depo_no,company_code,bolum order by bolum", nativeQuery = true)
    List<AddressParams> findGroupByDepoNoAndCompanyCodeAndBolum();

    @Query(value="select depo_no AS depoNo,company_code AS companyCode,reyon AS param from aur_depo_urun_adres group by depo_no,company_code,reyon order by reyon", nativeQuery = true)
    List<AddressParams> findGroupByDepoNoAndCompanyCodeAndReyon();

    @Query(value="select depo_no AS depoNo,company_code AS companyCode,unite AS param from aur_depo_urun_adres group by depo_no,company_code,unite order by unite", nativeQuery = true)
    List<AddressParams> findGroupByDepoNoAndCompanyCodeAndUnite();

    @Query(value="select depo_no AS depoNo,company_code AS companyCode,kat AS param from aur_depo_urun_adres group by depo_no,company_code,kat order by kat", nativeQuery = true)
    List<AddressParams> findGroupByDepoNoAndCompanyCodeAndKat();

    @Query(value="select depo_no AS depoNo,company_code AS companyCode,oda AS param from aur_depo_urun_adres group by depo_no,company_code,oda order by oda", nativeQuery = true)
    List<AddressParams> findGroupByDepoNoAndCompanyCodeAndOda();

    @Query(value="select depo_no AS depoNo,company_code AS companyCode,adres_tipi AS param from aur_depo_urun_adres group by depo_no,company_code,adres_tipi order by adres_tipi", nativeQuery = true)
    List<AddressParams> findGroupByDepoNoAndCompanyCodeAndAdresTipi();


}
