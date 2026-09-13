import com.hisarresearch.wms.domain.AurLookupTable;
import com.hisarresearch.wms.domain.AurVwZReport;
import com.hisarresearch.wms.service.AurLookupService;
import com.hisarresearch.wms.service.MailService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.hisarresearch.wms.utility.AurHelper.dayFinder;

public class SampleUnitTest {
    Calculator calculatorTest = new Calculator();
    class Calculator{
        int add(int a, int b){
            return a + b;
        }
    }
    @Test
    public void test_add(){
        //given
        int firstNumber = 10;
        int secondNumber = 20;
        int expected = 30;

        //when
        int actual = calculatorTest.add(firstNumber, secondNumber);

        //then
        Assertions.assertEquals(expected, actual);
    }

    public DayOfWeek dayFinder() {
        LocalDate currendate = LocalDate.now();
        return  currendate.getDayOfWeek();

    }

//    @Test
//    public void toDayIs() {
//        DayOfWeek desiorDay = DayOfWeek.SATURDAY;
//
//        System.out.println(dayFinder());
//        Assertions.assertEquals(desiorDay, dayFinder());
//    }

}
