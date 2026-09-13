package com.hisarresearch.wms.service;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import com.hisarresearch.wms.domain.AurSayimTanim;
import com.hisarresearch.wms.domain.enumeration.SayimDurumu;
import com.hisarresearch.wms.repository.AurSayimTanimRepository;
import com.hisarresearch.wms.service.dto.counting.CountingDefinitionDTO;
import com.hisarresearch.wms.service.mapper.CountingDefinitionMapper;
import com.hisarresearch.wms.exception.api.BadRequestAlertException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AurSayimTanimServiceTest {

    @Mock
    private AurSayimTanimRepository aurSayimTanimRepository;

    @Mock
    private CountingDefinitionMapper countingDefinitionMapper;

    @Mock
    private CountingAddressExceptionService countingAddressExceptionService;

    @InjectMocks
    private AurSayimTanimService aurSayimTanimService;

    private CountingDefinitionDTO countingDefinitionDTO;
    private AurSayimTanim aurSayimTanim;

    @BeforeEach
    void setUp() {
        countingDefinitionDTO = new CountingDefinitionDTO();
        countingDefinitionDTO.setDepoNo("8");
        countingDefinitionDTO.setVisibilityAuthorities(new String[]{"ROLE_USER"});
        countingDefinitionDTO.setSayimAdi("Sayim");
        countingDefinitionDTO.setCompanyCode("4");

        aurSayimTanim = new AurSayimTanim();
        aurSayimTanim.setId(1L);
        aurSayimTanim.setDepoNo("8");
        aurSayimTanim.setSayimAdi("Sayim-1");
        aurSayimTanim.setSayimTarihi(Instant.now());
    }

    @Test
    void testSave_ShouldThrowException_WhenAuthorityExists() {
        // Given
        List<AurSayimTanim> existingCountings = Arrays.asList(
            new AurSayimTanim("8", true, SayimDurumu.ACTIVE, new String[]{"ROLE_USER"})
        );
        when(aurSayimTanimRepository.findByDepoNoAndStatusAndSayimDurumu("8", true, SayimDurumu.ACTIVE))
            .thenReturn(existingCountings);

        // When & Then
        assertThatThrownBy(() -> aurSayimTanimService.save(countingDefinitionDTO))
            .isInstanceOf(BadRequestAlertException.class)
            .hasMessageContaining("There is already counting with related authority");
    }

    @Test
    void testSave_ShouldSaveEntity_WhenAuthorityNotExists() {
        // Given
        when(aurSayimTanimRepository.findByDepoNoAndStatusAndSayimDurumu("8", true, SayimDurumu.ACTIVE))
            .thenReturn(List.of());
        when(countingDefinitionMapper.toEntity(countingDefinitionDTO)).thenReturn(aurSayimTanim);
        when(aurSayimTanimRepository.save(aurSayimTanim)).thenReturn(aurSayimTanim);
        when(aurSayimTanimRepository.findByDepoNoAndCompanyCodeOrderByIdDesc("8","4")).thenReturn(List.of());

        // When
        AurSayimTanim result = aurSayimTanimService.save(countingDefinitionDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getSayimAdi()).isEqualTo("Sayim-1");
        assertThat(result.getSayimTarihi()).isNotNull();
        verify(countingAddressExceptionService).saveCountingDefinition(result, result.getDepoNo());
    }
}
