import com.hisarresearch.wms.domain.enumeration.AddressMovementType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AddressMovementTypeTest {

    @Test
    void getAddressMovementTypeWithOrder() {
        Assertions.assertEquals(AddressMovementType.DEFINITION, AddressMovementType.values()[0]);
    }
}
