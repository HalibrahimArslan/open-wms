package com.hisarresearch.wms.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.hisarresearch.wms.constants.AurConstants;
import com.hisarresearch.wms.domain.ApiParameters;
import com.hisarresearch.wms.domain.AurCompany;
import com.hisarresearch.wms.domain.AurVwDepoStokAdres;
import com.hisarresearch.wms.domain.AurVwFirmOrderDetail;
import com.hisarresearch.wms.domain.AurVwFirmOrderExists;
import com.hisarresearch.wms.domain.AurVwUserMenuRel;
import com.hisarresearch.wms.domain.enumeration.ErpConnectionType;
import com.hisarresearch.wms.service.dto.AurCariDto;
import com.hisarresearch.wms.service.dto.AurCariOrderDetailListDto;
import com.hisarresearch.wms.service.dto.AurCompanyDTO;
import com.hisarresearch.wms.service.dto.AurUserMenuDto;
import com.hisarresearch.wms.service.dto.address.AurDepoStokAdresDto;
import com.hisarresearch.wms.utility.AurHelper;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

/**
 * Uygulamada ModelMapper ile yapilan donusumlerin (view/entity -> DTO) sonucunu sabitler.
 * ModelMapper surumu yukseltildiginde ortuk alan eslestirmesinin degismedigini gosterir.
 */
class ModelMapperMappingTest {

    private final ModelMapper mm = new ModelMapper();

    @Test
    void userMenuView() {
        AurVwUserMenuRel source = new AurVwUserMenuRel();
        source.setMenuId(7L);
        source.setParentMenuId(3);
        source.setMenuName("Sayim");
        source.setMenuType("PAGE");
        source.setPath("/sayim");
        source.setIndex(true);
        source.setIcon("list");

        AurUserMenuDto dto = mm.map(source, AurUserMenuDto.class);

        assertThat(dto.getMenuId()).isEqualTo(7);
        assertThat(dto.getParentMenuId()).isEqualTo(3);
        assertThat(dto.getMenuName()).isEqualTo("Sayim");
        assertThat(dto.getMenuType()).isEqualTo("PAGE");
        assertThat(dto.getPath()).isEqualTo("/sayim");
        assertThat(dto.getIndex()).isTrue();
        assertThat(dto.getIcon()).isEqualTo("list");
        assertThat(dto.getCompanyCode()).isNull();
    }

    @Test
    void company() {
        ApiParameters apiParameters = new ApiParameters();
        apiParameters.setErpApiActive(true);
        apiParameters.setDepoNo(List.of(1, 2));
        apiParameters.setUsername("erp");
        apiParameters.setPassword("secret");
        AurCompany source = new AurCompany();
        source.setId(5L);
        source.setCompanyCode(1);
        source.setCompanyName("Ornek");
        source.setErpType(ErpConnectionType.UYUMSOFT);
        source.setApiEndPoint("http://erp");
        source.setApiParameters(apiParameters);

        AurCompanyDTO dto = mm.map(source, AurCompanyDTO.class);

        assertThat(dto.getId()).isEqualTo(5L);
        assertThat(dto.getCompanyCode()).isEqualTo(1);
        assertThat(dto.getCompanyName()).isEqualTo("Ornek");
        assertThat(dto.getErpType()).isEqualTo(ErpConnectionType.UYUMSOFT);
        assertThat(dto.getApiEndPoint()).isEqualTo("http://erp");
        assertThat(dto.getApiParameters().getErpApiActive()).isTrue();
        assertThat(dto.getApiParameters().getDepoNo()).containsExactly(1, 2);
        assertThat(dto.getApiParameters().getUsername()).isEqualTo("erp");
        assertThat(dto.getApiParameters().getPassword()).isEqualTo("secret");
    }

    @Test
    void firmOrderDetailView() {
        AurVwFirmOrderDetail source = new AurVwFirmOrderDetail();
        source.setId(11L);
        source.setStokKodu("STK1");
        source.setStokAdi("Vida");
        source.setSiparisMiktar(10.0);
        source.setTeslimMiktar(4.0);
        source.setBarkod("8690000000001");
        source.setStokBirimi("ADET");

        AurCariOrderDetailListDto dto = mm.map(source, AurCariOrderDetailListDto.class);

        assertThat(dto.getId()).isEqualTo(11);
        assertThat(dto.getStokKodu()).isEqualTo("STK1");
        assertThat(dto.getStokAdi()).isEqualTo("Vida");
        assertThat(dto.getSiparisMiktar()).isEqualTo(10.0);
        assertThat(dto.getTeslimMiktar()).isEqualTo(4.0);
        assertThat(dto.getBarkod()).isEqualTo("8690000000001");
        assertThat(dto.getStokBirimi()).isEqualTo("ADET");
        assertThat(dto.getOrderNo()).isNull();
        assertThat(dto.getStokMiktar()).isNull();
    }

    @Test
    void firmOrderExistsView() {
        AurVwFirmOrderExists source = new AurVwFirmOrderExists();
        source.setCariKod("C001");
        source.setCariUnvan("Musteri");
        source.setCariBaglantiTipi("0");
        source.setCariHareketTipi("1");
        source.setBolgeKodu("B1");
        source.setBolgeAdi("Bolge");

        AurCariDto dto = mm.map(source, AurCariDto.class);

        assertThat(dto.getCariKod()).isEqualTo("C001");
        assertThat(dto.getCariUnvan()).isEqualTo("Musteri");
        assertThat(dto.getCariBaglantiTipi()).isEqualTo("0");
        assertThat(dto.getCariHareketTipi()).isEqualTo("1");
        assertThat(dto.getBolgeKodu()).isEqualTo("B1");
        assertThat(dto.getBolgeAdi()).isEqualTo("Bolge");
        assertThat(dto.getOrderLineItemCount()).isNull();
        assertThat(dto.getOrderList()).isNull();
    }

    @Test
    void depoStokAdresView() {
        Date updated = new Date(1_700_000_000_000L);
        AurVwDepoStokAdres source = new AurVwDepoStokAdres();
        source.setId(21L);
        source.setUrunAdresId(22L);
        source.setStokKodu("STK1");
        source.setLastUpdateDate(updated);
        source.setLastUpdateUser("admin");
        source.setDepoCode("D1");
        source.setMiktar(3.5);
        source.setBarkod("8690000000001");
        source.setAdres("A-01-01");
        source.setStatus(true);
        source.setStockName("Vida");

        AurDepoStokAdresDto dto = mm.map(source, AurDepoStokAdresDto.class);

        assertThat(dto.getId()).isEqualTo(21L);
        assertThat(dto.getUrunAdresId()).isEqualTo(22L);
        assertThat(dto.getStokKodu()).isEqualTo("STK1");
        assertThat(dto.getLastUpdateDate()).isEqualTo(updated);
        assertThat(dto.getLastUpdateUser()).isEqualTo("admin");
        assertThat(dto.getDepoCode()).isEqualTo("D1");
        assertThat(dto.getMiktar()).isEqualTo(3.5);
        assertThat(dto.getBarkod()).isEqualTo("8690000000001");
        assertThat(dto.getAdres()).isEqualTo("A-01-01");
        assertThat(dto.getStatus()).isTrue();
        assertThat(dto.getStockName()).isEqualTo("Vida");
    }

    @Test
    void helperConvertsDatesToStrings() {
        Date created = new Date(1_700_000_000_000L);
        DatedSource source = new DatedSource();
        source.setName("kayit");
        source.setCreated(created);

        List<DatedDto> result = AurHelper.convertDomainListToDto(List.of(source), DatedDto.class);

        assertThat(result).singleElement().satisfies(dto -> {
            assertThat(dto.getName()).isEqualTo("kayit");
            assertThat(dto.getCreated()).isEqualTo(AurConstants.DATETIME_FORMATTER.format(created));
        });
    }

    public static class DatedSource {

        private String name;
        private Date created;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Date getCreated() {
            return created;
        }

        public void setCreated(Date created) {
            this.created = created;
        }
    }

    public static class DatedDto {

        private String name;
        private String created;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }
    }
}
