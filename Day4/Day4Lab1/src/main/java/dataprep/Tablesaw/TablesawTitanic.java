package dataprep.Tablesaw;

import java.io.IOException;
import tech.tablesaw.api.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TablesawTitanic {
    Table titanicData;
    String dataPath = "src/main/resources/data/titanic.csv";

    public TablesawTitanic() {
    }

    public static void main(String[] args) {
        TablesawTitanic tda = new TablesawTitanic ();
        try {
            //load Data from CSV
            tda.titanicData = tda.loadDataFromCVS (tda.dataPath);

            //getting the Structure of the data
            String structure = tda.getDataInfoStructure (tda.titanicData);
            System.out.println ("the Structure of the data");
            System.out.println (structure);
            System.out.println ("=====================================================================================");

            //getting Data summery
            String summary = tda.getDataSummary (tda.titanicData);
            System.out.println ("the Summary of the data");
            System.out.println (summary);
            System.out.println ("=====================================================================================");

            //Create DataFrame Object
            Table dataFrame1 = tda.titanicData.select("name","pclass","survived","sex");
            System.out.println ("the Structure of the dataFrame 1");
            String structure1 = tda.getDataInfoStructure(dataFrame1);
            System.out.println(structure1);
            System.out.println ("=====================================================================================");

            //Create DataFrame Object
            Table dataFrame2 = tda.titanicData.select("name","age", "fare", "ticket");
            System.out.println ("the Structure of the dataFrame 2");
            String structure2 = tda.getDataInfoStructure(dataFrame2);
            System.out.println(structure2);
            System.out.println ("=====================================================================================");

            // Join 2 DataFrame Objects
            Table jointDataFrames = dataFrame1.joinOn("name").inner(dataFrame2);

            //getting jointStructure
            System.out.println ("the Structure of the jointDataFrames");
            String jointStructure = tda.getDataInfoStructure(jointDataFrames);
            System.out.println(jointStructure);
            System.out.println ("=====================================================================================");

            //getting jointSummary
            String jointSummary = tda.getDataSummary (jointDataFrames);
            System.out.println ("the Summary of the jointDataFrames");
            System.out.println (jointSummary);
            System.out.println ("=====================================================================================");


            // Adding date Column
            Table dataWithDate = tda.addDateColumnToData (tda.titanicData);
            System.out.println ("the Structure of the data after Adding date Column");
            System.out.println (dataWithDate.structure ());
            System.out.println ("=====================================================================================");

            //Sorting on the added Date Field
            Table sortedData = dataWithDate.sortAscendingOn ("Fake Date");

            //getting the first 10 rows
            System.out.println ("Printing the first rows of the table");
            Table firstTenRows = sortedData.first (50);
            System.out.println (firstTenRows);
            System.out.println ("=====================================================================================");

            //Adding Gender Column
            Table mappedData = tda.mapTextColumnToNumber (tda.titanicData);
            System.out.println ("the Structure of the data after Adding Gender Column");
            System.out.println (mappedData.structure ());
            System.out.println ("=====================================================================================");

            //getting the first 5 rows
            Table firstFiveRows = mappedData.first (5);
            System.out.println ("Printing the first 5 rows of the table");
            System.out.println (firstFiveRows);
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }


    ///  Load Data From CSV File
    public Table loadDataFromCVS(String path) throws IOException {
        Table titanicData = Table.read ().csv (path);
        return titanicData;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// get the structure of the data
    public String getDataInfoStructure(Table data) {
        Table dataStructure = data.structure ();
        return dataStructure.toString ();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //get Data Summary
    public String getDataSummary(Table data) {
        Table summary = data.summary ();
        return summary.toString ();
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Adding Columns
    public Table addDateColumnToData(Table data) {
        int rowCount = data.rowCount ();
        List<LocalDate> dateList = new ArrayList<> ();
        for (int i = 0; i < rowCount; i++) {
            dateList.add (LocalDate.of (2021, 3, i % 31 == 0 ? 1 : i % 31));
        }
        DateColumn dateColumn = DateColumn.create ("Fake Date", dateList);
        data.insertColumn (data.columnCount (), dateColumn);
        return data;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // mapping text data to numeric values on the sex field female=1,male=0 and adding a column named gender
    public Table mapTextColumnToNumber(Table data) {
        NumberColumn mappedGenderColumn = null;
        StringColumn gender = (StringColumn) data.column ("Sex");
        List<Number> mappedGenderValues = new ArrayList<> ();
        for (String v : gender) {
            if ((v != null) && (v.equals ("female"))) {
                mappedGenderValues.add (new Double (1));
            } else {
                mappedGenderValues.add (new Double (0));
            }
        }
        mappedGenderColumn = DoubleColumn.create ("gender", mappedGenderValues);
        data.addColumns (mappedGenderColumn);
        return data;
    }
}

