package evidence;

public class App 
{      
    public static void main( String[] args )
    {
        try{
            DataSet baseDataSet = new DataSet("knowledgeBase.csv");
            
            TestData testData = new TestData("testdata.csv","resultdata.csv",
            baseDataSet);        
            testData.evaluate();
        }
        catch(Exception e){
            System.out.println("Application broken. An error occured while executing the main process. Exception: ");
            e.printStackTrace();
        }
    }
}
