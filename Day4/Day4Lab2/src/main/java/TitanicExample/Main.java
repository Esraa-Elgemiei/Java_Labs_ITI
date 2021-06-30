package TitanicExample;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {

        List<TitanicPassenger> passengerList =getPassengersFromJsonFile();
        graphPassengerAges(passengerList);
        graphPassengerClass(passengerList);
    }

    // Read json file and return a list containing TitanicPassenger Objects
    public static List<TitanicPassenger> getPassengersFromJsonFile() {
        List<TitanicPassenger> allPassengers = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure (DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try (InputStream input = new FileInputStream("src/main/resources/data/titanic_csv.json")) {
        //Read JSON file
            allPassengers = objectMapper.readValue (input, new TypeReference<List<TitanicPassenger>>() { });
        } catch (FileNotFoundException e) {
            e.printStackTrace ();
        } catch (IOException e) {
            e.printStackTrace ();
        }
        return allPassengers;
    }

    // Print graphPassengerAges
    public static void graphPassengerAges(List<TitanicPassenger> passengerList) {
        //filter to get an array of passenger ages
        List<Float> pAges = passengerList.stream ()
                                         .map (TitanicPassenger::getAge)
                                         .limit (8)
                                         .collect (Collectors.toList ());
        //filter to get an array of passenger Names
        List<String> pNames = passengerList.stream ()
                                           .map (TitanicPassenger::getName)
                                           .limit (8)
                                           .collect (Collectors.toList ());

        //Using XChart to graph the Ages
        // Create Chart
        CategoryChart chart = new CategoryChartBuilder().width (1024)
                                                        .height (768)
                                                        .title ("Age Histogram")
                                                        .xAxisTitle ("Names")
                                                        .yAxisTitle("Age")
                                                        .build ();

        // Customize Chart
        chart.getStyler ().setLegendPosition (Styler.LegendPosition.InsideNW);
        chart.getStyler ().setHasAnnotations (true);
        chart.getStyler ().setStacked (true);

        // Series
        chart.addSeries ("Passenger's Ages", pNames, pAges);

        // Show it
        new SwingWrapper(chart).displayChart ();
    }

    // Print graphPassengerClass
    public static void graphPassengerClass(List<TitanicPassenger> passengerList) {
        //filter to get a map of passenger class and total number of passengers in each class
        Map<String, Long> result = passengerList.stream ()
                                                .collect (
                                                        Collectors.groupingBy (TitanicPassenger::getPclass,
                                                                                Collectors.counting ()
                                                        )
                                                );
        // Create Chart
        PieChart chart = new PieChartBuilder().width (800)
                                              .height (600)
                                              .title ("Passenger Classes")
                                              .build ();
        // Customize Chart
        Color[] sliceColors = new Color[]{new Color (150, 80, 20),
                                          new Color (10, 190, 120),
                                          new Color (255, 170, 200)};

        chart.getStyler ().setSeriesColors (sliceColors);

        // Series
        chart.addSeries ("First Class", result.get ("1"));
        chart.addSeries ("Second Class", result.get ("2"));
        chart.addSeries ("Third Class", result.get ("3"));
        // Show it
        new SwingWrapper (chart).displayChart ();
    }
}
