package dataprep.Joinary;

import joinery.DataFrame;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JoinaryTitanic {
    DataFrame<Object> titanicData;
    String dataPath = "src/main/resources/data/titanic.csv";

    public  JoinaryTitanic(){
    }

    public static void main(String args[]){
        JoinaryTitanic tda = new JoinaryTitanic ();
        try {
            //load Data from CSV
            tda.titanicData= tda.loadDataFromCVS(tda.dataPath);

            //getting Data summery
            String summary = tda.getDataSummary (tda.titanicData);
            System.out.println ("the Summary of the data");
            System.out.println (summary);
            System.out.println ("=====================================================================================");


            //Create DataFrame Object
            DataFrame<Object> dataFrame1 = tda.titanicData.retain("name","pclass","survived","sex");

            //getting Data summery
            String summary1 = tda.getDataSummary (dataFrame1);
            System.out.println ("the Summary of the dataFrame1");
            System.out.println (summary1);
            System.out.println ("=====================================================================================");

            //Create DataFrame Object
            DataFrame<Object> dataFrame2 = tda.titanicData.retain("name","age", "fare", "ticket");

            //getting Data summery
            String summary2 = tda.getDataSummary (dataFrame2);
            System.out.println ("the Summary of the dataFrame2");
            System.out.println (summary2);
            System.out.println ("=====================================================================================");

            // Join 2 DataFrame Objects
            DataFrame<Object> jointDataFrames = dataFrame1.join(dataFrame2);

            //getting the first 10 rows of jointDataFrames
            System.out.println ("Printing the first 10 rows of the jointDataFrames");
            System.out.println (jointDataFrames.head());
            System.out.println ("=====================================================================================");

            //getting jointSummary
            String jointSummary = tda.getDataSummary (jointDataFrames);
            System.out.println ("the Summary of the jointDataFrames");
            System.out.println (jointSummary);
            System.out.println ("=====================================================================================");

            // Adding date Column
            DataFrame<Object> dataWithDate = tda.addDateColumnToData (tda.titanicData);

            //getting the first 10 rows
            DataFrame<Object> firstTenRows = dataWithDate.head();
            System.out.println ("Printing the first 10 rows of the DataFrame");
            System.out.println (firstTenRows);
            System.out.println ("=====================================================================================");
            //getting Summary of the DataFrame after Adding date Column
            String newSummary1 = tda.getDataSummary (dataWithDate);
            System.out.println ("the Summary of the DataFrame after Adding date Column");
            System.out.println (newSummary1);
            System.out.println ("=====================================================================================");

            //Adding Gender Column
            DataFrame<Object> mappedData = tda.mapTextColumnToNumber (tda.titanicData);

            //getting the last 10 rows
            DataFrame<Object> lastTenRows = dataWithDate.tail();
            System.out.println ("Printing the last 10 rows of the DataFrame");
            System.out.println (lastTenRows);
            System.out.println ("=====================================================================================");
            //getting Summary of the DataFrame after Adding Gender Column
            String newSummary2 = tda.getDataSummary (mappedData);
            System.out.println ("the Summary of the DataFrame after Adding Gender Column");
            System.out.println (newSummary2);


        } catch (IOException e) {
            e.printStackTrace ();
        }
    }
    ///  Load Data From CSV File
    public DataFrame<Object> loadDataFromCVS(String path) throws IOException {
        DataFrame<Object> titanicData =  DataFrame.readCsv(path);
        return titanicData;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //get Data Summary
    public String getDataSummary(DataFrame<Object> data) {
        DataFrame<Object> summary = data.describe ();
        return summary.toString ();
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Adding Columns
    public DataFrame<Object> addDateColumnToData(DataFrame<Object> data) {
        int rowCount = data.length();
        List<LocalDate> dateList = new ArrayList<>();
        for (int i = 0; i < rowCount; i++) {
            dateList.add (LocalDate.of (2021, 3, i % 31 == 0 ? 1 : i % 31));
        }
        data.add("Fake Date",new ArrayList<Object>(dateList));
        return data;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // mapping text data to numeric values on the sex field female=1,male=0 and adding a column named gender
    public DataFrame<Object> mapTextColumnToNumber(DataFrame<Object> data) {
        List<Number> mappedGenderValues = new ArrayList<> ();
        List<String> gender = data.cast(String.class).col(3);
        for (String v : gender) {
            if ((v != null) && (v.equals ("female"))) {
                mappedGenderValues.add (new Double (1));
            } else {
                mappedGenderValues.add (new Double (0));
            }
        }
        data.add("Gender", new ArrayList<Object>(mappedGenderValues));
        return data;
    }

}
