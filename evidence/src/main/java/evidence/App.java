package evidence;

import java.util.Arrays;

import dempster.DempsterHandler;
import dempster.Measure;

public class App 
{      
    public static void main( String[] args )
    {
        DempsterHandler dempsterHandler = new DempsterHandler(3);
        

        // Merkmal: Alter <18 
        Measure m1 = dempsterHandler.addMeasure();
        m1.addEntry(Arrays.asList(new Integer[] {1 ,0 , 0}) , 0.102272727272f);
        m1.addEntry(Arrays.asList(new Integer[] {0 ,1 , 0}) , 0.011363636363f);
        m1.addEntry(Arrays.asList(new Integer[] {0 ,0 , 1}) , 0.136363636363f);
        System.out.println("First measure: \n" + m1.toString());
        
        // Merkmal: Kinderanzahl 1
        Measure m2 = dempsterHandler.addMeasure();
        m2.addEntry(Arrays.asList(new Integer[] {0, 1, 0}) , 0.125f);
        m2.addEntry(Arrays.asList(new Integer[] {0 ,0, 1}) , 0.125f);
        System.out.println("Second measure: \n" + m2.toString());

        //Merkmal: Geschlecht m
        Measure m3 = dempsterHandler.addMeasure();
        m3.addEntry(Arrays.asList(new Integer[] {1 ,0 , 0}) , 0.0669642857142857f);
        m3.addEntry(Arrays.asList(new Integer[] {0 ,1 , 0}) , 0.0892857142857143f);
        m3.addEntry(Arrays.asList(new Integer[] {0 ,0 , 1}) , 0.09375f);
        System.out.println("First measure: \n" + m3.toString());
            
        dempsterHandler.accumulateAllMeasures();
        System.out.println("Accumulated measures result in: \n" + dempsterHandler.getFirstMeasure().toString());
        
        double belief = dempsterHandler.getFirstMeasure().calculateBelief(1);
        double plausability = dempsterHandler.getFirstMeasure().calculatePlausability(1);
        double doubt = dempsterHandler.getFirstMeasure().calculateDoubt(1);
        
        System.out.println("Belief: \t" + belief +"\nPlausability: \t" + plausability + "\nDoubt: \t\t" + doubt);
    }
}
