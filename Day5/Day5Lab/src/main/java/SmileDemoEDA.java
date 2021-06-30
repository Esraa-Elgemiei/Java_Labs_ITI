import org.apache.commons.csv.CSVFormat;
import smile.classification.RandomForest;
import smile.data.DataFrame;
import smile.data.formula.Formula;
import smile.data.measure.NominalScale;
import smile.data.vector.DoubleVector;
import smile.data.vector.IntVector;
import smile.data.vector.StringVector;
import smile.io.Read;
import smile.plot.swing.Histogram;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import static smile.math.MathEx.round;

public class SmileDemoEDA {
    private static String trainPath = "src/main/resources/titanic_train.csv";
    private static String testPath = "src/main/resources/titanic_test.csv";

    public static void main(String[] args) throws IOException, URISyntaxException {
        //preparing TrainData
        DataFrame trainData = prepareTrainData();
        //Training model
        System.out.println("======================Training model========================");
        RandomForest model = RandomForest.fit(Formula.lhs("Survived"), trainData);
        System.out.println("feature importance:");
        System.out.println(Arrays.toString(model.importance()));
        System.out.println("model matrix: "+model.metrics ());

        //TODO load test data to validate model
        //preparing TestData
        System.out.println("======================preparing TestData========================");
        DataFrame testData =  prepareTestData();
        int[] result = model.predict(testData);
        Arrays.stream(result).forEach(System.out::print);
        double averageSurvivedTest = Arrays.stream(result).average().getAsDouble();
        System.out.println("\nAverage of Survived in TestData: "+round(averageSurvivedTest*100,2)+"%");

    }

    public static int[] encodeCategory(DataFrame df, String columnName) {
        String[] values = df.stringVector (columnName).distinct ().toArray (new String[]{});
        int[] pclassValues = df.stringVector (columnName).factorize (new NominalScale (values)).toIntArray ();
        return pclassValues;
    }

    private static void eda(DataFrame titanic) throws InterruptedException, InvocationTargetException {
        titanic.summary ();
        DataFrame titanicSurvived = DataFrame.of (titanic.stream ().filter (t -> t.get ("Survived").equals (1)));
        DataFrame titanicNotSurvived = DataFrame.of (titanic.stream ().filter (t -> t.get ("Survived").equals (0)));
        titanicNotSurvived = titanicNotSurvived.omitNullRows ();
        titanicSurvived = titanicSurvived.omitNullRows ();
        titanicSurvived.summary ();
        titanicNotSurvived.summary ();
        int size = titanicSurvived.size ();
        System.out.println ("size of titanicSurvived: "+size);
        Double averageAge = titanicSurvived.stream ()
                .mapToDouble (t -> t.isNullAt ("Age") ? 0.0 : t.getDouble ("Age"))
                .average ()
                .orElse (0);
        System.out.println ("average age of titanicSurvived: "+averageAge.intValue ());
        /*Map map = titanicSurvived.stream ()
                .collect (Collectors.groupingBy (t -> Double.valueOf (t.getDouble ("Age")).intValue (), Collectors.counting ()));

        double[] breaks = ((Collection<Integer>) map.keySet ())
                .stream ()
                .mapToDouble (l -> Double.valueOf (l))
                .toArray ();

        int[] valuesInt = ((Collection<Long>) map.values ())
                .stream ().mapToInt (i -> i.intValue ())
                .toArray ();*/

        Histogram.of (titanicSurvived.doubleVector ("Age").toDoubleArray (), 15, false)
                .canvas ().setAxisLabels ("Age", "Count")
                .setTitle ("Age frequencies among surviving passengers")
                .window ();
        Histogram.of (titanicSurvived.intVector ("PClassValues").toIntArray (), 4, true)
                .canvas ().setAxisLabels ("Classes", "Count")
                .setTitle ("Pclass values frequencies among surviving passengers")
                .window ();
       /* Histogram.of(valuesInt, breaks , false).canvas().setAxisLabels ("Age", "Count")
                                                     .setTitle ("Age frequencies among surviving passengers").window();*/
        System.out.println ("titanicSurvived schema: "+titanicSurvived.schema ());

    }

    public static DataFrame  prepareTrainData() throws IOException {
        PassengerProvider pProvider = new PassengerProvider ();
        DataFrame trainData = pProvider.readCSV (trainPath);
        System.out.println (trainData.structure ());
        System.out.println ("=====================================================================================================");
        System.out.println (trainData.summary ());
        System.out.println ("=====================================================================================================");

        trainData = trainData.merge (IntVector.of ("Gender", encodeCategory (trainData, "Sex")));
        trainData = trainData.merge (IntVector.of ("PClassValues", encodeCategory (trainData, "Pclass")));

        System.out.println ("=======Encoding Non Numeric Data==============");
        System.out.println (trainData.structure ());
        //System.out.println (trainData);

        System.out.println ("=======Dropping the Name, Pclass, and Sex Columns==============");

        trainData = trainData.drop ("Name");
        trainData=trainData.drop("Pclass");
        trainData=trainData.drop("Sex");

        System.out.println (trainData.structure ());
        System.out.println (trainData.summary ());

        trainData = trainData.omitNullRows ();
        System.out.println ("=======After Omitting null Rows==============");
        System.out.println (trainData.summary ());

        System.out.println ("=======Start of Explaratory Data Analysis==============");
        try {
            eda (trainData);
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace ();
        }
        return trainData;
    }
    public static DataFrame  prepareTestData() throws IOException, URISyntaxException {
        DataFrame testData = Read.csv(testPath, CSVFormat.DEFAULT.withFirstRecordAsHeader().withDelimiter(','));
        testData = testData.select("Name","Pclass","Sex","Age");
        System.out.println (testData.structure ());
        System.out.println ("=====================================================================================================");
        System.out.println (testData.summary ());
        System.out.println ("=====================================================================================================");

        testData = testData.merge (StringVector.of ("PClassValuesString", testData.intVector("Pclass").toStringArray()));
        testData = testData.merge (IntVector.of ("Gender", encodeCategory (testData, "Sex")));
        testData = testData.merge (IntVector.of ("PClassValues", encodeCategory (testData, "PClassValuesString")));

        System.out.println ("=======Encoding Non Numeric Data==============");
        System.out.println (testData.structure ());
        System.out.println ("=======Dropping the Name, Pclass, and Sex Columns==============");

        testData = testData.drop ("Name");
        testData=testData.drop("Pclass");
        testData=testData.drop("PClassValuesString");
        testData=testData.drop("Sex");

        testData = testData.omitNullRows ();
        System.out.println ("=======After Omitting null Rows==============");
        System.out.println (testData.structure ());
        System.out.println (testData.summary ());

        return testData;
    }
}
