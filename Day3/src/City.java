/**
 *
 * @author Esraa Abdulfatah
 */

public class City {
    private String cityID;
    private String cityName;
    private String countryID;
    private String capital;
    private double population;
    private String continent;

    public City(){}

    public City(String cityID, String cityName, String countryID, String capital, double population, String continent) {
        this.cityID = cityID;
        this.cityName = cityName;
        this.countryID = countryID;
        this.capital = capital;
        this.population = population;
        this.continent = continent;
    }

    public String getCityID() {
        return cityID;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountryID() {
        return countryID;
    }

    public void setCountryID(String countryID) {
        this.countryID = countryID;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public  double getPopulation() {
        return population;
    }

    public void setPopulation(double population) {
        this.population = population;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public boolean isCapital() {
        return getCapital().equals("primary");
    }


    @Override
    public String toString() {
        return this.getCityName();
        //return "city name is : "+this.getCity(); //To change body of generated methods, choose Tools | Templates.
    }

}

