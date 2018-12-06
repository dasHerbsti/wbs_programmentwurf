package evidence;

import java.util.Arrays;

import dempster.DempsterHandler;
import dempster.Measure;

public class App 
{      
    public static void main( String[] args )
    {
        DempsterHandler dempsterHandler = new DempsterHandler(3);

        DataSet baseDataSet = new DataSet(System.getProperty("user.dir")+"\\knowledgeBase.csv");

        TestData testData = new TestData(System.getProperty("user.dir")+"\\testdata.csv",
                                         System.getProperty("user.dir")+"\\resultdata.csv",
                                         baseDataSet);        
        testData.evaluate();
    }
}
