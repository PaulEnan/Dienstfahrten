package winfs.dienstreise.dienstfahrten;

import org.junit.Test;

/**
 * @author Paul Enan
 */
public class CalculatorTests {

    @Test
    public void manualTest() throws DienstfahrtenException {
        Calculator calculator = new Calculator(new ApiUser());
        String money = calculator.calculateVariableCosts("Berlin", "Reinbeker Redder", 15);
        String asd = money;
    }

    @Test
    public void manualTestOfMultipleInstances() throws DienstfahrtenException {
        Calculator calculator = new Calculator(new ApiUser());
        String money = calculator.caclculateVariableCostsForMultipleStations(new String[]{"Berlin", "Reinbeker Redder", "München", "Rome"}, 15);
        String asd = money;
    }
}
