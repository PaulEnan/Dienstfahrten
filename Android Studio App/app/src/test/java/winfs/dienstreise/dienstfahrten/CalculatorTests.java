package winfs.dienstreise.dienstfahrten;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Tests for the calculator class
 *
 * @author winf101441
 */
public class CalculatorTests {

    @Test
    public void testCalculationOfMultipleStations() {

        String[] stations = new String[]{"Berlin", "Hamburg", "Warschau"};
        String[] resultText = new String[]{
                "{\"destination_addresses\":[\"Polen, Warschau\"],\"rows\":[{\"elements\":[{\"duration\":{\"text\":\"3 Stunden, 17 Minuten\",\"value\":11794},\"distance\":{\"text\":\"289 km\",\"value\":288796},\"status\":\"OK\"}]}],\"origin_addresses\":[\"Hamburg, Deutschland\"],\"status\":\"OK\"}",
                "{\"destination_addresses\":[\"Hamburg, Deutschland\"],\"rows\":[{\"elements\":[{\"duration\":{\"text\":\"3 Stunden, 17 Minuten\",\"value\":11794},\"distance\":{\"text\":\"852 km\",\"value\":288796},\"status\":\"OK\"}]}],\"origin_addresses\":[\"Berlin, Deutschland\"],\"status\":\"OK\"}"};
        String[] addressText = new String[]{
                "{\"candidates\":[{\"formatted_address\":\"Berlin, Deutschland\"}],\"status\":\"OK\"}",
                "{\"candidates\":[{\"formatted_address\":\"Hamburg, Deutschland\"}],\"status\":\"OK\"}",
                "{\"candidates\":[{\"formatted_address\":\"Hamburg, Deutschland\"}],\"status\":\"OK\"}",
                "{\"candidates\":[{\"formatted_address\":\"Warschau, Polen\"}],\"status\":\"OK\"}"};

        IApiUser apiUser = new FakeApiUser(resultText, addressText, new String[0]);
        Calculator calculator = new Calculator(apiUser);
        String result = new String();
        try {
            result = calculator.caclculateVariableCostsForMultipleStations(stations);
        } catch (DienstfahrtenException e) {
            Assert.assertFalse("should have been possible to calculate", true);
        }
        double costs = Double.parseDouble(result);
        Assert.assertEquals("should have returned 342.3 euros as costs", costs, 342.3, 0.005);
    }

    @Test
    public void testCalculationFaultyOrigin() {
        String[] stations = new String[]{"Warschau", "Berlin"};
        String[] autoCompleterText = new String[]{
                "{\"predictions\":[{\"reference\":\"ChIJAVkDPzdOqEcRcDteW0YgIQQ\",\"types\":[\"locality\",\"political\",\"geocode\"],\"matched_substrings\":[{\"offset\":0,\"length\":6}],\"terms\":[{\"offset\":0,\"value\":\"Berlin\"},{\"offset\":8,\"value\":\"Deutschland\"}],\"structured_formatting\":{\"main_text_matched_substrings\":[{\"offset\":0,\"length\":6}],\"secondary_text\":\"Deutschland\",\"main_text\":\"Anderes Warschau\"},\"description\":\"Anderes Warschau, Deutschland\",\"id\":\"6b1afbd7fcf2ec16ff8e2f95514e2badb8c2451d\",\"place_id\":\"ChIJAVkDPzdOqEcRcDteW0YgIQQ\"},{\"reference\":\"ChIJe-ff-71RqEcRqvy8lRR4PHo\",\"types\":[\"transit_station\",\"point_of_interest\",\"establishment\",\"geocode\"],\"matched_substrings\":[{\"offset\":0,\"length\":6}],\"terms\":[{\"offset\":0,\"value\":\"Berlin Hauptbahnhof\"},{\"offset\":21,\"value\":\"Europaplatz\"},{\"offset\":34,\"value\":\"Berlin\"},{\"offset\":42,\"value\":\"Deutschland\"}],\"structured_formatting\":{\"main_text_matched_substrings\":[{\"offset\":0,\"length\":6}],\"secondary_text\":\"Europaplatz, Berlin, Deutschland\",\"main_text\":\"Warschau\"},\"description\":\"Warschau, Polen\",\"id\":\"3f3350651fb1e3cc1a50e6cd5ed15adb106feb96\",\"place_id\":\"ChIJe-ff-71RqEcRqvy8lRR4PHo\"}],\"status\":\"OK\"}"
        };

        String[] addressText = new String[]{
                "{\"candidates\":[{\"formatted_address\":\"Warschau, Polen\"}],\"status\":\"Error\"}",
                "{\"candidates\":[{\"formatted_address\":\"Berlin, Deutschland\"}],\"status\":\"Error\"}"};

        IApiUser apiUser = new FakeApiUser(new String[0], addressText, autoCompleterText);
        Calculator calculator = new Calculator(apiUser);

        try {
            String result = calculator.caclculateVariableCostsForMultipleStations(stations);
            Assert.assertEquals("should have gotten 2 results", result, "Anderes Warschau, Deutschland||Warschau, Polen");
        } catch (DienstfahrtenException e) {
            Assert.assertFalse("should not have thrown an exception", true);
        }
    }

    @Test
    public void testCalculationFaultyDestination() {
        String[] stations = new String[]{"Warschau", "Berlin"};
        String[] autoCompleterText = new String[]{
                "{\"predictions\":[{\"reference\":\"ChIJAVkDPzdOqEcRcDteW0YgIQQ\",\"types\":[\"locality\",\"political\",\"geocode\"],\"matched_substrings\":[{\"offset\":0,\"length\":6}],\"terms\":[{\"offset\":0,\"value\":\"Berlin\"},{\"offset\":8,\"value\":\"Deutschland\"}],\"structured_formatting\":{\"main_text_matched_substrings\":[{\"offset\":0,\"length\":6}],\"secondary_text\":\"Deutschland\",\"main_text\":\"Berlin\"},\"description\":\"Berlin, Deutschland\",\"id\":\"6b1afbd7fcf2ec16ff8e2f95514e2badb8c2451d\",\"place_id\":\"ChIJAVkDPzdOqEcRcDteW0YgIQQ\"},{\"reference\":\"ChIJe-ff-71RqEcRqvy8lRR4PHo\",\"types\":[\"transit_station\",\"point_of_interest\",\"establishment\",\"geocode\"],\"matched_substrings\":[{\"offset\":0,\"length\":6}],\"terms\":[{\"offset\":0,\"value\":\"Berlin Hauptbahnhof\"},{\"offset\":21,\"value\":\"Europaplatz\"},{\"offset\":34,\"value\":\"Berlin\"},{\"offset\":42,\"value\":\"Deutschland\"}],\"structured_formatting\":{\"main_text_matched_substrings\":[{\"offset\":0,\"length\":6}],\"secondary_text\":\"Europaplatz, Berlin, Deutschland\",\"main_text\":\"Berlin Hauptbahnhof\"},\"description\":\"Berlin Hauptbahnhof, Europaplatz, Berlin, Deutschland\",\"id\":\"3f3350651fb1e3cc1a50e6cd5ed15adb106feb96\",\"place_id\":\"ChIJe-ff-71RqEcRqvy8lRR4PHo\"}],\"status\":\"OK\"}"
        };

        String[] addressText = new String[]{
                "{\"candidates\":[{\"formatted_address\":\"Warschau, Polen\"}],\"status\":\"Ok\"}",
                "{\"candidates\":[{\"formatted_address\":\"Berlin, Deutschland\"}],\"status\":\"Error\"}"};

        IApiUser apiUser = new FakeApiUser(new String[0], addressText, autoCompleterText);
        Calculator calculator = new Calculator(apiUser);

        try {
            String result = calculator.caclculateVariableCostsForMultipleStations(stations);
            Assert.assertEquals("should have gotten 2 results", result, "Berlin, Deutschland||Berlin Hauptbahnhof, Europaplatz, Berlin, Deutschland");
        } catch (DienstfahrtenException e) {
            Assert.assertFalse("should not have thrown an exception", true);
        }
    }

    @Test
    public void testCalculationWithFaultyName() {
        String[] stations = new String[]{"Warschau", "Berlin|Hamburg"};
        IApiUser apiUser = new FakeApiUser(new String[0], new String[0], new String[0]);
        Calculator calculator = new Calculator(apiUser);

        try {
            String result = calculator.caclculateVariableCostsForMultipleStations(stations);
            Assert.assertFalse("should not have been able to calculate", true);
        } catch (DienstfahrtenException e) {
            Assert.assertEquals("Message should be about wrong character",
                    e.getMessage(), Messages.NoAdressesWith('|'));
        }
    }

    @Test
    public void testCalculationWithNullJSON() {

        String[] stations = new String[]{"Berlin", "Hamburg"};
        String[] resultText = new String[]{
                "{\"destination_addresses\":[\"Polen, Warschau\"],\"rows\":[{\"elements\":[{\"duration\":{\"text\":\"3 Stunden, 17 Minuten\",\"value\":11794},\"distance\":{\"text\":\"289 km\",\"value\":288796},\"status\":\"OK\"}]}],\"origin_addresses\":[\"Hamburg, Deutschland\"],\"status\":\"OK\"}",
                "{\"destination_addresses\":[\"Hamburg, Deutschland\"],\"rows\":[{\"elements\":[{\"duration\":{\"text\":\"3 Stunden, 17 Minuten\",\"value\":11794},\"distance\":{\"text\":\"852 km\",\"value\":288796},\"status\":\"OK\"}]}],\"origin_addresses\":[\"Berlin, Deutschland\"],\"status\":\"OK\"}"};
        String[] addressText = new String[]{
                null,
                "{\"candidates\":[{\"formatted_address\":\"Hamburg, Deutschland\"}],\"status\":\"OK\"}"};

        IApiUser apiUser = new FakeApiUser(resultText, addressText, new String[0]);
        Calculator calculator = new Calculator(apiUser);
        String result = new String();
        try {
            result = calculator.caclculateVariableCostsForMultipleStations(stations);
            Assert.assertFalse("should not have been possible to calculate", true);
        } catch (DienstfahrtenException e) {
            Assert.assertEquals("Message should be about api error",
                    e.getMessage(), Messages.JSONError());
        }
    }

    @Test
    public void testCalculationWithMultipleStationsAndOneFaultyAddress() {

        String[] stations = new String[]{"Berlin", "Hamburg", "Warschau"};
        String[] resultText = new String[]{
                "{\"destination_addresses\":[\"Polen, Warschau\"],\"rows\":[{\"elements\":[{\"duration\":{\"text\":\"3 Stunden, 17 Minuten\",\"value\":11794},\"distance\":{\"text\":\"289 km\",\"value\":288796},\"status\":\"OK\"}]}],\"origin_addresses\":[\"Hamburg, Deutschland\"],\"status\":\"OK\"}",
                "{\"destination_addresses\":[\"Hamburg, Deutschland\"],\"rows\":[{\"elements\":[{\"duration\":{\"text\":\"3 Stunden, 17 Minuten\",\"value\":11794},\"distance\":{\"text\":\"852 km\",\"value\":288796},\"status\":\"OK\"}]}],\"origin_addresses\":[\"Berlin, Deutschland\"],\"status\":\"OK\"}"};
        String[] addressText = new String[]{
                "{\"candidates\":[{\"formatted_address\":\"Berlin, Deutschland\"}],\"status\":\"OK\"}",
                "{\"candidates\":[{\"formatted_address\":\"Hamburg, Deutschland\"}],\"status\":\"OK\"}",
                "{\"candidates\":[{\"formatted_address\":\"Hamburg, Deutschland\"}],\"status\":\"Ok\"}",
                "{\"candidates\":[{\"formatted_address\":\"Warschau, Polen\"}],\"status\":\"Error\"}"};

        String[] autoCompleterText = new String[]{
                "{\"predictions\":[{\"reference\":\"ChIJAVkDPzdOqEcRcDteW0YgIQQ\",\"types\":[\"locality\",\"political\",\"geocode\"],\"matched_substrings\":[{\"offset\":0,\"length\":6}],\"terms\":[{\"offset\":0,\"value\":\"Berlin\"},{\"offset\":8,\"value\":\"Deutschland\"}],\"structured_formatting\":{\"main_text_matched_substrings\":[{\"offset\":0,\"length\":6}],\"secondary_text\":\"Deutschland\",\"main_text\":\"Anderes Warschau\"},\"description\":\"Anderes Warschau, Deutschland\",\"id\":\"6b1afbd7fcf2ec16ff8e2f95514e2badb8c2451d\",\"place_id\":\"ChIJAVkDPzdOqEcRcDteW0YgIQQ\"},{\"reference\":\"ChIJe-ff-71RqEcRqvy8lRR4PHo\",\"types\":[\"transit_station\",\"point_of_interest\",\"establishment\",\"geocode\"],\"matched_substrings\":[{\"offset\":0,\"length\":6}],\"terms\":[{\"offset\":0,\"value\":\"Berlin Hauptbahnhof\"},{\"offset\":21,\"value\":\"Europaplatz\"},{\"offset\":34,\"value\":\"Berlin\"},{\"offset\":42,\"value\":\"Deutschland\"}],\"structured_formatting\":{\"main_text_matched_substrings\":[{\"offset\":0,\"length\":6}],\"secondary_text\":\"Europaplatz, Berlin, Deutschland\",\"main_text\":\"Warschau\"},\"description\":\"Warschau, Polen\",\"id\":\"3f3350651fb1e3cc1a50e6cd5ed15adb106feb96\",\"place_id\":\"ChIJe-ff-71RqEcRqvy8lRR4PHo\"}],\"status\":\"OK\"}"
        };
        IApiUser apiUser = new FakeApiUser(resultText, addressText, autoCompleterText);
        Calculator calculator = new Calculator(apiUser);
        String result = new String();
        try {
            result = calculator.caclculateVariableCostsForMultipleStations(stations);
        } catch (DienstfahrtenException e) {
            Assert.assertFalse("should have been possible to calculate", true);
        }
        Assert.assertEquals("should have gotten 2 results", result, "Anderes Warschau, Deutschland||Warschau, Polen");
    }

    @Test
    public void testAutoCompleterEverythingWorks() {

        String[] autoCompleterText = new String[]{
                "{\"predictions\":[{\"reference\":\"ChIJAVkDPzdOqEcRcDteW0YgIQQ\",\"types\":[\"locality\",\"political\",\"geocode\"],\"matched_substrings\":[{\"offset\":0,\"length\":6}],\"terms\":[{\"offset\":0,\"value\":\"Berlin\"},{\"offset\":8,\"value\":\"Deutschland\"}],\"structured_formatting\":{\"main_text_matched_substrings\":[{\"offset\":0,\"length\":6}],\"secondary_text\":\"Deutschland\",\"main_text\":\"Anderes Warschau\"},\"description\":\"Anderes Warschau, Deutschland\",\"id\":\"6b1afbd7fcf2ec16ff8e2f95514e2badb8c2451d\",\"place_id\":\"ChIJAVkDPzdOqEcRcDteW0YgIQQ\"},{\"reference\":\"ChIJe-ff-71RqEcRqvy8lRR4PHo\",\"types\":[\"transit_station\",\"point_of_interest\",\"establishment\",\"geocode\"],\"matched_substrings\":[{\"offset\":0,\"length\":6}],\"terms\":[{\"offset\":0,\"value\":\"Berlin Hauptbahnhof\"},{\"offset\":21,\"value\":\"Europaplatz\"},{\"offset\":34,\"value\":\"Berlin\"},{\"offset\":42,\"value\":\"Deutschland\"}],\"structured_formatting\":{\"main_text_matched_substrings\":[{\"offset\":0,\"length\":6}],\"secondary_text\":\"Europaplatz, Berlin, Deutschland\",\"main_text\":\"Warschau\"},\"description\":\"Warschau, Polen\",\"id\":\"3f3350651fb1e3cc1a50e6cd5ed15adb106feb96\",\"place_id\":\"ChIJe-ff-71RqEcRqvy8lRR4PHo\"}],\"status\":\"OK\"}"
        };
        IApiUser apiUser = new FakeApiUser(new String[0], new String[0], autoCompleterText);
        Calculator calculator = new Calculator(apiUser);
        String result = new String();
        try {
            result = calculator.autoCompleteAddress("Warschau");
        } catch (DienstfahrtenException e) {
            Assert.assertFalse("should have been possible to autocomplete", true);
        }
        Assert.assertEquals("should have gotten 2 results", result, "Anderes Warschau, Deutschland||Warschau, Polen");
    }

    @Test
    public void testAutoCompleterJSONError() {

        String[] autoCompleterText = new String[]{
                null
        };
        IApiUser apiUser = new FakeApiUser(new String[0], new String[0], autoCompleterText);
        Calculator calculator = new Calculator(apiUser);
        String result = new String();
        try {
            result = calculator.autoCompleteAddress("Warschau");
            Assert.assertFalse("should not have been possible to autocomplete", true);
        } catch (DienstfahrtenException e) {
            Assert.assertEquals("Message should be about api error",
                    e.getMessage(), Messages.JSONError());
        }
    }

    @Test
    public void testAutoCompleterInvalidResult() {

        String[] autoCompleterText = new String[]{
                "{\"predictions\":[{\"reference\":\"ChIJAVkDPzdOqEcRcDteW0YgIQQ\",\"types\":[\"locality\",\"political\",\"geocode\"],\"matched_substrings\":[{\"offset\":0,\"length\":6}],\"terms\":[{\"offset\":0,\"value\":\"Berlin\"},{\"offset\":8,\"value\":\"Deutschland\"}],\"structured_formatting\":{\"main_text_matched_substrings\":[{\"offset\":0,\"length\":6}],\"secondary_text\":\"Deutschland\",\"main_text\":\"Anderes Warschau\"},\"description\":\"Anderes Warschau, Deutschland\",\"id\":\"6b1afbd7fcf2ec16ff8e2f95514e2badb8c2451d\",\"place_id\":\"ChIJAVkDPzdOqEcRcDteW0YgIQQ\"},{\"reference\":\"ChIJe-ff-71RqEcRqvy8lRR4PHo\",\"types\":[\"transit_station\",\"point_of_interest\",\"establishment\",\"geocode\"],\"matched_substrings\":[{\"offset\":0,\"length\":6}],\"terms\":[{\"offset\":0,\"value\":\"Berlin Hauptbahnhof\"},{\"offset\":21,\"value\":\"Europaplatz\"},{\"offset\":34,\"value\":\"Berlin\"},{\"offset\":42,\"value\":\"Deutschland\"}],\"structured_formatting\":{\"main_text_matched_substrings\":[{\"offset\":0,\"length\":6}],\"secondary_text\":\"Europaplatz, Berlin, Deutschland\",\"main_text\":\"Warschau\"},\"description\":\"Warschau, Polen\",\"id\":\"3f3350651fb1e3cc1a50e6cd5ed15adb106feb96\",\"place_id\":\"ChIJe-ff-71RqEcRqvy8lRR4PHo\"}],\"status\":\"Error\"}"
        };
        IApiUser apiUser = new FakeApiUser(new String[0], new String[0], autoCompleterText);
        Calculator calculator = new Calculator(apiUser);
        String result = new String();
        try {
            result = calculator.autoCompleteAddress("Warschau");
            Assert.assertFalse("should not have been possible to autocomplete", true);
        } catch (DienstfahrtenException e) {
            Assert.assertEquals("Message should be about no result found",
                    e.getMessage(), Messages.NotFound("Warschau"));
        }
    }

    @Test
    public void testAutoCompleterEmptyResult() {

        String[] autoCompleterText = new String[]{
                "{\"predictions\":[],\"status\":\"Ok\"}"
        };
        IApiUser apiUser = new FakeApiUser(new String[0], new String[0], autoCompleterText);
        Calculator calculator = new Calculator(apiUser);
        String result = new String();
        try {
            result = calculator.autoCompleteAddress("Warschau");
            Assert.assertFalse("should not have been possible to autocomplete", true);
        } catch (DienstfahrtenException e) {
            Assert.assertEquals("Message should be about no result found",
                    e.getMessage(), Messages.NotFound("Warschau"));
        }
    }
}
