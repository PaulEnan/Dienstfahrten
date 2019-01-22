package winfs.dienstreise.dienstfahrten;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;

public class CentralLogicTests {
    final double delta = 0.01;

    @Test
    public void testCalculationOfMultipleAdresses() {

        DODestination[] stations = new DODestination[]{new DODestination(0, 0, 0, new DOLocation("", "", "Berlin", 0), null, null),
                new DODestination(0, 0, 0, new DOLocation("", "", "Hamburg", 0), null, null),
                new DODestination(0, 0, 0, new DOLocation("", "", "Warschau", 0), null, null)};
        String[] resultText = new String[]{
                "{\"destination_addresses\":[\"Polen, Warschau\"],\"rows\":[{\"elements\":[{\"duration\":{\"text\":\"3 Stunden, 17 Minuten\",\"value\":11794},\"distance\":{\"text\":\"289 km\",\"value\":288796},\"status\":\"OK\"}]}],\"origin_addresses\":[\"Hamburg, Deutschland\"],\"status\":\"OK\"}",
                "{\"destination_addresses\":[\"Hamburg, Deutschland\"],\"rows\":[{\"elements\":[{\"duration\":{\"text\":\"3 Stunden, 17 Minuten\",\"value\":11794},\"distance\":{\"text\":\"852 km\",\"value\":288796},\"status\":\"OK\"}]}],\"origin_addresses\":[\"Berlin, Deutschland\"],\"status\":\"OK\"}"};
        String[] addressText = new String[]{
                "{\"candidates\":[{\"formatted_address\":\"Berlin, Deutschland\"}],\"status\":\"OK\"}",
                "{\"candidates\":[{\"formatted_address\":\"Hamburg, Deutschland\"}],\"status\":\"OK\"}",
                "{\"candidates\":[{\"formatted_address\":\"Hamburg, Deutschland\"}],\"status\":\"OK\"}",
                "{\"candidates\":[{\"formatted_address\":\"Warschau, Polen\"}],\"status\":\"OK\"}"};

        IApiUser apiUser = new FakeApiUser(resultText, addressText, new String[0]);
        DOSession session = new DOSession(0, Arrays.asList(stations), "test1", null,
                 null, null);
        CentralLogic logic = new CentralLogic(
                apiUser, new FakeSaveLoadHandler(new LinkedList<DOSession>(Arrays.asList(session))));
        String[] result = new String[0];
        try {
            result = logic.calculateCosts();
        } catch (DienstfahrtenException e) {
            Assert.assertFalse("should have been possible to calculate", true);
        }
        double costs = Double.parseDouble(result[0]);
        Assert.assertEquals("should have gotten only one result", 1, result.length);
        Assert.assertEquals("should have returned 342.3 euros as costs", costs, 342.3, delta);
        Assert.assertEquals("should have different final costs now", 342.3, session.getFinalCosts(), 0.0005);
    }

    @Test
    public void testCalculationWithInvalidName() {
        DODestination[] stations = new DODestination[]{new DODestination(0, 0, 0, new DOLocation("", "", "Berlin", 0), null, null),
                new DODestination(0, 0, 0, new DOLocation("", "", "Hamburg|Berlin", 0), null, null),
                new DODestination(0, 0, 0, new DOLocation("", "", "Warschau", 0), null, null)};
        IApiUser apiUser = new FakeApiUser(new String[0], new String[0], new String[0]);
        DOSession session = new DOSession(0, Arrays.asList(stations), "test1", null,
                null, null);
        CentralLogic logic = new CentralLogic(
                apiUser, new FakeSaveLoadHandler(new LinkedList<DOSession>(Arrays.asList(session))));
        String[] result = new String[0];
        try {
            result = logic.calculateCosts();
            Assert.assertFalse("should not have been possible to calculate", true);
        } catch (DienstfahrtenException e) {
            Assert.assertEquals("Message should be about wrong character",
                    e.getMessage(), Messages.NoAdressesWith('|'));
        }
    }

    @Test
    public void testCalculateWithAutoCompleter() {
        DODestination[] stations = new DODestination[]{new DODestination(0, 0, 0, new DOLocation("", "", "Berlin", 0), null, null),
                new DODestination(0, 0, 0, new DOLocation("", "", "Hamburg", 0), null, null),
                new DODestination(0, 0, 0, new DOLocation("", "", "Warschau", 0), null, null)};
        String[] autoCompleterText = new String[] {
          "{\"predictions\":[{\"reference\":\"ChIJAVkDPzdOqEcRcDteW0YgIQQ\",\"types\":[\"locality\",\"political\",\"geocode\"],\"matched_substrings\":[{\"offset\":0,\"length\":6}],\"terms\":[{\"offset\":0,\"value\":\"Berlin\"},{\"offset\":8,\"value\":\"Deutschland\"}],\"structured_formatting\":{\"main_text_matched_substrings\":[{\"offset\":0,\"length\":6}],\"secondary_text\":\"Deutschland\",\"main_text\":\"Berlin\"},\"description\":\"Berlin, Deutschland\",\"id\":\"6b1afbd7fcf2ec16ff8e2f95514e2badb8c2451d\",\"place_id\":\"ChIJAVkDPzdOqEcRcDteW0YgIQQ\"},{\"reference\":\"ChIJe-ff-71RqEcRqvy8lRR4PHo\",\"types\":[\"transit_station\",\"point_of_interest\",\"establishment\",\"geocode\"],\"matched_substrings\":[{\"offset\":0,\"length\":6}],\"terms\":[{\"offset\":0,\"value\":\"Berlin Hauptbahnhof\"},{\"offset\":21,\"value\":\"Europaplatz\"},{\"offset\":34,\"value\":\"Berlin\"},{\"offset\":42,\"value\":\"Deutschland\"}],\"structured_formatting\":{\"main_text_matched_substrings\":[{\"offset\":0,\"length\":6}],\"secondary_text\":\"Europaplatz, Berlin, Deutschland\",\"main_text\":\"Berlin Hauptbahnhof\"},\"description\":\"Berlin Hauptbahnhof, Europaplatz, Berlin, Deutschland\",\"id\":\"3f3350651fb1e3cc1a50e6cd5ed15adb106feb96\",\"place_id\":\"ChIJe-ff-71RqEcRqvy8lRR4PHo\"}],\"status\":\"OK\"}"
        };

        String[] addressText = new String[]{
                "{\"candidates\":[{\"formatted_address\":\"Berlin, Deutschland\"}],\"status\":\"Error\"}",
                "{\"candidates\":[{\"formatted_address\":\"Warschau, Polen\"}],\"status\":\"Ok\"}"};

        IApiUser apiUser = new FakeApiUser(new String[0], addressText, autoCompleterText);
        DOSession session = new DOSession(0, Arrays.asList(stations), "test1", null,
                null, null);
        CentralLogic logic = new CentralLogic(
                apiUser, new FakeSaveLoadHandler(new LinkedList<DOSession>(Arrays.asList(session))));

        try {
            String[] result = logic.calculateCosts();
            Assert.assertEquals("should have returned two adresses.", 2, result.length);
            Assert.assertTrue("first adress should be Berlin", result[0].equals("Berlin, Deutschland"));
            Assert.assertTrue("second address should be Hauptbahnhof", result[1].equals("Berlin Hauptbahnhof, Europaplatz, Berlin, Deutschland"));
        } catch (DienstfahrtenException e) {
            Assert.assertFalse("should not have thrown an exception", true);
        }
    }

    @Test
    public void testAutoCompleter() {
        String[] autoCompleterText = new String[] {
                "{\"predictions\":[{\"reference\":\"ChIJAVkDPzdOqEcRcDteW0YgIQQ\",\"types\":[\"locality\",\"political\",\"geocode\"],\"matched_substrings\":[{\"offset\":0,\"length\":6}],\"terms\":[{\"offset\":0,\"value\":\"Berlin\"},{\"offset\":8,\"value\":\"Deutschland\"}],\"structured_formatting\":{\"main_text_matched_substrings\":[{\"offset\":0,\"length\":6}],\"secondary_text\":\"Deutschland\",\"main_text\":\"Berlin\"},\"description\":\"Berlin, Deutschland\",\"id\":\"6b1afbd7fcf2ec16ff8e2f95514e2badb8c2451d\",\"place_id\":\"ChIJAVkDPzdOqEcRcDteW0YgIQQ\"},{\"reference\":\"ChIJe-ff-71RqEcRqvy8lRR4PHo\",\"types\":[\"transit_station\",\"point_of_interest\",\"establishment\",\"geocode\"],\"matched_substrings\":[{\"offset\":0,\"length\":6}],\"terms\":[{\"offset\":0,\"value\":\"Berlin Hauptbahnhof\"},{\"offset\":21,\"value\":\"Europaplatz\"},{\"offset\":34,\"value\":\"Berlin\"},{\"offset\":42,\"value\":\"Deutschland\"}],\"structured_formatting\":{\"main_text_matched_substrings\":[{\"offset\":0,\"length\":6}],\"secondary_text\":\"Europaplatz, Berlin, Deutschland\",\"main_text\":\"Berlin Hauptbahnhof\"},\"description\":\"Berlin Hauptbahnhof, Europaplatz, Berlin, Deutschland\",\"id\":\"3f3350651fb1e3cc1a50e6cd5ed15adb106feb96\",\"place_id\":\"ChIJe-ff-71RqEcRqvy8lRR4PHo\"}],\"status\":\"OK\"}"
        };

        IApiUser apiUser = new FakeApiUser(new String[0], new String[0], autoCompleterText);
        CentralLogic logic = new CentralLogic(
                apiUser, new FakeSaveLoadHandler(null));

        try {
            String[] result = logic.useAutoCompleter("Berlin");
            Assert.assertEquals("should have returned two adresses.", 2, result.length);
            Assert.assertTrue("first adress should be Berlin", result[0].equals("Berlin, Deutschland"));
            Assert.assertTrue("second address should be Hauptbahnhof", result[1].equals("Berlin Hauptbahnhof, Europaplatz, Berlin, Deutschland"));
        } catch (DienstfahrtenException e) {
            Assert.assertFalse("should not have thrown an exception", true);
        }
    }
}
