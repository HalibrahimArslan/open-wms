import com.hisarresearch.wms.domain.enumeration.AddressMovementType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class AddressMovementTypeTest {
    fun getAddressMovementType():Array<AddressMovementType>{
        val addressMovementTypes: Array<AddressMovementType> = AddressMovementType.values();
        return addressMovementTypes;
    }


    @Test
    fun getAddressMovementTypeWithOrder(){
        val response:Array<AddressMovementType> = getAddressMovementType();
        Assertions.assertEquals(response.get(0),AddressMovementType.DEFINITION);
    }
}
