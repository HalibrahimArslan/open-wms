import com.hisarresearch.wms.domain.AurPartialItem;
import com.hisarresearch.wms.service.AurPartialItemService;
import com.hisarresearch.wms.service.dto.AurPartialItemDTO;
import com.hisarresearch.wms.web.rest.AurPartialItemResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URISyntaxException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PackageItemControllerTest {

    @Mock
    AurPartialItemService mockPackageItemService;

    @InjectMocks
    AurPartialItemResource underTest;

    @Test
    void create_shouldCreateSuccessfully() throws URISyntaxException {

        //given
        AurPartialItemDTO item = new AurPartialItemDTO();
        item.setPackageBarcode("726515487974");
        item.setId(1L);

        when(mockPackageItemService.save(any())).thenReturn(item);

        //when

        AurPartialItemDTO request = new AurPartialItemDTO();
        ResponseEntity<AurPartialItemDTO> response = underTest.createAurPartialItem(request);
        AurPartialItemDTO actual = response.getBody();

        //then
        Assertions.assertAll(
            () -> Assertions.assertNotNull(actual),
            () -> Assertions.assertEquals(HttpStatus.CREATED,response.getStatusCode()),
            () -> Assertions.assertEquals(item,actual),
            () -> Assertions.assertEquals(item.getPackageBarcode(), actual.getPackageBarcode())

        );
    }

}
