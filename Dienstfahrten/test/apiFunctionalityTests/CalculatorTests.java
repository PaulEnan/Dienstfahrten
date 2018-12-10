package apiFunctionalityTests;

import apifunctionality.ApiUser;
import apifunctionality.Calculator;
import org.junit.Test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Paul Enan
 */
public class CalculatorTests {

    @Test
    public void manualTest() {
        Calculator calculator = new Calculator(new ApiUser(), 1, 15);
        String money = calculator.calculateCosts("Berlin", "Reinbeker Redder");
        String asd = money;
    }

    @Test
    public void manualTestOfMultipleInstances() {
        Calculator calculator = new Calculator(new ApiUser(), 1, 15);
        String money = calculator.caclculateCostsForMultipleStations(new String[]{"Berlin", "Reinbeker Redder", "München", "Rome"});
        String asd = money;
    }
}
