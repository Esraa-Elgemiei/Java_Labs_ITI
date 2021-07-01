import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Esraa Abdulfatah
 */
public class countryCSVDAO {
    public countryCSVDAO () {
    }

    public List<Country> readCountryFromCSV(String fileName){
        List<Country> countries = new ArrayList<>();

        //access the data CSV file
        File countryFile = new File(fileName);

        //read all data lines in the csv file, and save them in a list
        List<String> lines = new ArrayList<String>();
        try{
            lines = Files.readAllLines(countryFile.toPath());
        }catch(Exception e){
            System.out.println("An issue has been happend during reading country file: "+e);
        }

        //extract all pyramid info, and save them in a list
        for(int lineIndx = 1;lineIndx < lines.size();lineIndx++){
            String line = lines.get(lineIndx);

            String[] fields = line.split(",");

            for (int fieldIndex =0;fieldIndex < fields.length;fieldIndex++){
                fields[fieldIndex] = fields[fieldIndex].trim();
            }
            Country country = new Country();
            country = createCountry(fields);
            countries.add(country);
        }

        return countries;
    }

    public Country createCountry(String[] metadata){
        Country country= new Country();

        country.setCountry(metadata[0]);
        country.setCountryID(metadata[1]);

        return country;
    }
}