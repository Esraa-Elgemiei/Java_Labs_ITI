/**
 *
 * @author Esraa Abdulfatah
 */

public class Country {
    private String country;
    private String countryID;

    public Country(){}

    public Country(String country, String countryID) {
        this.country = country;
        this.countryID = countryID;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryID() {
        return countryID;
    }

    public void setCountryID(String country_ID) {
        this.countryID = country_ID;
    }

    @Override
    public String toString() {
        return "Country{" +
                "country='" + country + '\'' +
                '}';
    }
}
