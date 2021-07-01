import java.util.*;
import java.util.stream.Collectors;


/**
 *
 * @author Esraa Abdulfatah
 */
public class Main {

    public static void main(String[] args) {

        //create list of countries
        countryCSVDAO conDAO = new countryCSVDAO();
        List<Country> countries = conDAO.readCountryFromCSV("countries.csv");

        //create list of cities
        cityCSVDAO cityDAO = new cityCSVDAO();
        List<City> cities = cityDAO.readCityFromCSV("cities.csv");


       /*exercise_1
        • Create a map that uses the country code as keys and a list of cities as
         the value for each country.
       */

        Map<Country,List<City>> countryMap = new HashMap<>();

        for(Country country:countries){
            List<City> cityList=new ArrayList<>();
            String code=country.getCountryID();
            for(City city:cities)
            {
                if(city.getCountryID()!=null & code!=null){
                    if(city.getCountryID().equals(code))
                        cityList.add(city);
                }
            }
            countryMap.put(country, cityList);
        }
        //print countryMap
        countryMap.forEach((k,v)->System.out.println("country = "+k.getCountry()+"--> cities"+v));
        System.out.println("--------------------------------------------------------------------------------------------");


        //print it's size before filtering
         System.out.println("size before filtering: "+countryMap.size());


        //remove rows of empty values
        countryMap.values().removeIf(List::isEmpty);

        //print it's size after filtering
        System.out.println("size after filtering: "+countryMap.size());

        //print countryMap after filtering
        //countryMap.forEach((k,v)->System.out.println("country = "+k.getCountry()+"--> cities"+v));
        System.out.println("--------------------------------------------------------------------------------------------");



       /*exercise_1
        • For a given country code sort the cities according to the population
       */

        HashMap<Country,List<City>> sortedCitiesMap = new HashMap<>();
        countryMap.forEach((k, v) -> sortedCitiesMap.put(k,v.stream()
                                                        .sorted(Comparator.comparing(City::getPopulation))
                                                        .collect(Collectors.toList())));

        sortedCitiesMap.forEach((k,v)->System.out.println("country = "+k+"--> sortedcities"+v));

        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("--------------------------------------------------------------------------------------------");

        /* exercise_3
        • Highest population city of each country
        • Highest population city by continent
        • Highest population capital
        */

        //Highest population city of each country
        Map<String, Optional<City>> countryHighestPopulationCity  = cities.stream()
                .filter(c -> c.getCountryID() != null)
                .collect(Collectors.groupingBy(
                        City::getCountryID,
                        Collectors.maxBy(Comparator.comparing(City::getPopulation))
                ));
        countryHighestPopulationCity.forEach((k,v)->System.out.println("country code= "+k+"--> Highest population city: "+v.get()));
        System.out.println("--------------------------------------------------------------------------------------------");

        //Highest population city by continent
        Map<String, Optional<City>> continentHighestPopulationCity  = cities.stream()
                .filter(c -> c.getCountryID() != null)
                .collect(Collectors.groupingBy(
                        City::getContinent,
                        Collectors.maxBy(Comparator.comparing(City::getPopulation))
                ));
        continentHighestPopulationCity.forEach((k,v)->System.out.println("continent= "+k+"--> Highest population city: "+v.get()));
        System.out.println("--------------------------------------------------------------------------------------------");

        //Highest population capital
        Map<String, Optional<City>> continentHighestPopulationCapital  = cities.stream()
                .filter(c -> c.getCountryID() != null)
                .filter(City::isCapital)
                .collect(Collectors.groupingBy(
                        City::getContinent,
                        Collectors.maxBy(Comparator.comparing(City::getPopulation))
                ));
        continentHighestPopulationCapital.forEach((k,v)->System.out.println("continent= "+k+"--> Highest population capital: "+v.get()));
        System.out.println("--------------------------------------------------------------------------------------------");

        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("--------------------------------------------------------------------------------------------");

        /* exercise_4
          The average,
          The median(divides the data into a lower half and an upper half),
          The lower quartile is the middle value of the lower half.
          The upper quartile is the middle value of the upper half.
           of the dataset based on one of the available characteristics.
        */

        //average population of cities
        double averagePopulation = cities.stream()
                                            .mapToDouble(City::getPopulation)
                                            .average()
                                            .getAsDouble();

        //The median(divides the data into a lower half and an upper half)
        double medianPopulation;
        double lowerQuartilePopulation;
        double upperQuartilePopulation;

        List<City> sortedCities = cities.stream().sorted(Comparator.comparing(City::getPopulation)).collect(Collectors.toList()) ;
        int size = sortedCities.size();
        if(size%2==0){
            double m1= sortedCities.get(size/2).getPopulation();
            double m2= sortedCities.get(size/2+1).getPopulation();

            double l1=sortedCities.get(size/4).getPopulation();
            double l2=sortedCities.get(size/4+1).getPopulation();

            double up1=sortedCities.get(size/2+size/4).getPopulation();
            double up2=sortedCities.get(size/2+size/4+1).getPopulation();

            medianPopulation =(m1+m2)/2;
            //The lower quartile is the middle value of the lower half.
            lowerQuartilePopulation = (l1+l2)/2;
            //The upper quartile is the middle value of the upper half.
            upperQuartilePopulation = (up1+up2)/2;

        }else{
            medianPopulation = sortedCities.get(size/2).getPopulation();
            //The lower quartile is the middle value of the lower half.
            lowerQuartilePopulation = sortedCities.get(size/4).getPopulation();
            //The upper quartile is the middle value of the upper half.
            upperQuartilePopulation = sortedCities.get(size/2+size/4).getPopulation();


        }

        System.out.println("The average Population of cities is: \n" + averagePopulation+
                            " \nThe median Population of cities is: " + medianPopulation+
                            " \nThe lower quartile of Population of cities is: " + lowerQuartilePopulation+
                            " \nThe upper quartile of Population of cities is: "+ upperQuartilePopulation);
    }

}
