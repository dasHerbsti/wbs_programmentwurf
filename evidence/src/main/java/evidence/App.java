package evidence;

import java.util.Arrays;

import dempster.DempsterHandler;
import dempster.Measure;

public class App 
{      
    public static void main( String[] args )
    {
        DempsterHandler dempsterHandler = new DempsterHandler(3);

        DataSet baseDataSet = new DataSet(System.getProperty("user.dir")+"\\E004.csv");

        TestData testData = new TestData(System.getProperty("user.dir")+"\\testdata4.csv",
                                         System.getProperty("user.dir")+"\\resultdata4.csv",
                                         baseDataSet);
        
        testData.evaluate();

        // System.out.println("___________________________________________");


        // // Merkmal: Kinderanzahl 1
        // Measure m1 = dempsterHandler.addMeasure();
        // m1.addEntry(Arrays.asList(new Integer[] {1 ,0 , 0}) , (9.0/92));
        // m1.addEntry(Arrays.asList(new Integer[] {0 ,1 , 0}) , (1.0/92));
        // m1.addEntry(Arrays.asList(new Integer[] {0 ,0 , 1}) , (13.0/92));
        // System.out.println("First measure: \n" + m1.toString());
        // 
        // // Merkmal: Alter <18 
        //  Measure m2 = dempsterHandler.addMeasure();
        //  m2.addEntry(Arrays.asList(new Integer[] {0, 1, 0}) , 0.125);
        //  m2.addEntry(Arrays.asList(new Integer[] {0 ,0, 1}) , 0.125);
        //  System.out.println("Second measure: \n" + m2.toString());
 // 
        //  //Merkmal: Geschlecht m
        //  Measure m3 = dempsterHandler.addMeasure();
        //  m3.addEntry(Arrays.asList(new Integer[] {1 ,0 , 0}) , (15.0/224));
        //  m3.addEntry(Arrays.asList(new Integer[] {0 ,1 , 0}) , (20.0/224));
        //  m3.addEntry(Arrays.asList(new Integer[] {0 ,0 , 1}) , (21.0/224));
        //  System.out.println("Third measure: \n" + m3.toString());
// 
        //  dempsterHandler.accumulateAllMeasures();
        // 
        // System.out.println("Accumulated measures result in: \n" + dempsterHandler.getFirstMeasure().toString());
        // 
        // double beliefA = dempsterHandler.getFirstMeasure().calculateBelief(0);
        // double plausabilityA = dempsterHandler.getFirstMeasure().calculatePlausability(0);
        // double doubtA = dempsterHandler.getFirstMeasure().calculateDoubt(0);
        // 
        // System.out.println("For Book A:");
        // System.out.println("Belief: \t" + beliefA +"\nPlausability: \t" + plausabilityA + "\nDoubt: \t\t" + doubtA);
// 
        // double beliefB = dempsterHandler.getFirstMeasure().calculateBelief(1);
        // double plausabilityB = dempsterHandler.getFirstMeasure().calculatePlausability(1);
        // double doubtB = dempsterHandler.getFirstMeasure().calculateDoubt(1);
        // 
        // System.out.println("For Book B:");
        // System.out.println("Belief: \t" + beliefB +"\nPlausability: \t" + plausabilityB + "\nDoubt: \t\t" + doubtB);
// 
        // double beliefC = dempsterHandler.getFirstMeasure().calculateBelief(2);
        // double plausabilityC = dempsterHandler.getFirstMeasure().calculatePlausability(2);
        // double doubtC = dempsterHandler.getFirstMeasure().calculateDoubt(2);
        // 
        // System.out.println("For Book C:");
        // System.out.println("Belief: \t" + beliefC +"\nPlausability: \t" + plausabilityC + "\nDoubt: \t\t" + doubtC);
    }
}
