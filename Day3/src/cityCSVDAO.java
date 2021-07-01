import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Esraa Abdulfatah
 */
public class cityCSVDAO {
    public cityCSVDAO () {
    }

    public List<City> readCityFromCSV(String fileName){
        List<City> cities = new ArrayList<>();

        //access the data CSV file
        File cityFile = new File(fileName);

        //read all data lines in the csv file, and save them in a list
        List<String> lines = new ArrayList<>();
        try{
            lines = Files.readAllLines(cityFile.toPath());
        }catch(Exception e){
            System.out.println("An issue has been happend during reading city file: "+e);
        }

        //extract all pyramid info, and save them in a list
        for(int lineIndx = 1;lineIndx < lines.size();lineIndx++){
            String line = lines.get(lineIndx);

//            String[] fields = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
            String[] fields = line.split(",");

            for (int fieldIndex =0;fieldIndex < fields.length;fieldIndex++){
                fields[fieldIndex] = fields[fieldIndex].trim();
            }
            //City city = new City();
            City  city = createCity(fields);
            cities.add(city);
        }

        return cities ;
    }

    public City createCity(String[] metadata){
        City city= new City();
        if(metadata.length == 6){
            city.setCityID(metadata[0]);
            city.setCityName(metadata[1]);
            city.setCountryID(metadata[2]);
            city.setCapital(metadata[3]);
            double pop;
            if(!metadata[4].isEmpty()){
                pop =Double.parseDouble(metadata[4]);
            }else{
                pop =0;
            }
            city.setPopulation(pop);
            city.setContinent(metadata[5]);

        }


        return city;
    }
}
