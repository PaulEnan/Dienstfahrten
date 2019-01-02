package winfs.dienstreise.dienstfahrten;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;

public class CentralLogicTests {
    final double delta = 0.01;

    @Test
    public void testCalculation() throws DienstfahrtenException {

        String[] stations = new String[]{"Berlin", "Hamburg", "Warschau"};
        String resultText = "{\n\"destination_addresses\":[\"Deutschland\"],\n\"origin_addresses\":[\"Deutschland\"],\n\"rows\":[\n{\n\"elements\":[\n{\n\"distance\":{\n\"text\":\"100 km\",\n\"value\":283311\n},\n\"duration\":{\n\"text\":\"3 Stunden, 18 Minuten\",\n\"value\":11909\n},\n\"status\":\"OK\"\n}\n]\n}\n],\n\"status\":\"OK\"\n}";
        resultText = resultText.replaceAll("\n", "\\n");
        FakeApiUser apiUser = new FakeApiUser(resultText, "", "");
        CentralLogic logic = new CentralLogic(apiUser, new FakeSaveLoadHandler(), new LinkedList(Arrays.asList(stations)), 29, 1.5, "Test1", "", "", "");
        String[] result = logic.calculateCosts();
        double costs = Double.parseDouble(result[0]);
        Assert.assertEquals(costs, 329.0, delta);
    }
}
