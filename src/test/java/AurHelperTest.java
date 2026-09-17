import com.hisarresearch.wms.utility.AurHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AurHelperTest {
    @Test
    void testAddSpecificCharToAnyIndex(){
        String depoNo = "3105";
        String expectedValue = "31 05";
        String actualValue = AurHelper.addSpecificCharToAnyIndex(depoNo,2);
        Assertions.assertEquals(expectedValue,actualValue);
    }

}
