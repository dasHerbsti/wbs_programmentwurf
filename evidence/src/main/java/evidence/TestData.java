package evidence;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dempster.DempsterHandler;
import dempster.Measure;
 
public class TestData{
    private static String[] _header;

    private List<Person> _evaluationData;
    private DataSet _baseDataSet;

    private static Map<Integer,String> _bookLookUp;

    private String _resultFile;
    
    public TestData(String evaluationDataFile, String resultFile, DataSet baseData){
        _evaluationData = new ArrayList<Person>();
        _baseDataSet = baseData;
        _resultFile = resultFile;

        _bookLookUp = new HashMap<>();
        _bookLookUp.put(0, "Buch_A");
        _bookLookUp.put(1, "Buch_B");
        _bookLookUp.put(2, "Buch_C");

        
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(new File(evaluationDataFile)));

            // skip header line
            _header = reader.readLine().split(";");
    
            String person;
            while((person = reader.readLine())!=null){
                AddPerson(person, _header);
            }
            reader.close();
        } catch (Exception e) {            
            e.printStackTrace();
        }            
    }

    public static String[] getHeader(){
        return _header;
    }

    public void evaluate(){
        for (Person person : _evaluationData) {
            DempsterHandler dempsterHandler = new DempsterHandler(3);
            for (String attributeValue : person.getAttributeValues()){
                Object[] basicMasses = _baseDataSet.getBasicMassToAttribute(attributeValue).toArray();
                Measure measure = dempsterHandler.addMeasure();
                if((double)basicMasses[0] > 0.0000001){
                    measure.addEntry(Arrays.asList( new Integer[]{1,0,0} ), (double)basicMasses[0]);
                }
                if((double)basicMasses[1] > 0.0000001){
                    measure.addEntry(Arrays.asList( new Integer[]{0,1,0} ), (double)basicMasses[1]);
                }
                if((double)basicMasses[2] > 0.0000001){
                    measure.addEntry(Arrays.asList( new Integer[]{0,0,1} ), (double)basicMasses[2]);                    
                }                

                // System.out.println("measure: "+attributeValue+"\n" + measure.toString());
            }
            dempsterHandler.accumulateAllMeasures();   
            person.setResult(_bookLookUp.get(dempsterHandler.getFirstMeasure().getIndexOfMostLikelyEntry()));
            printResult(dempsterHandler.getFirstMeasure(), person);       
            saveResult();  
        }
    }

    private void saveResult(){
        try{
            List<String> lines = new ArrayList<String>();
            for (Person person : _evaluationData) {
                lines.add(person.getCSVString());
            }
            Path file = Paths.get(_resultFile);
            Files.write(file, lines, Charset.forName("UTF-8"));

        }catch(Exception e){
            System.out.println("Exception eccured when creating result file.");
            e.printStackTrace();
        }
    }

    public static void printResult(Measure measure, Person person){        
        int mostLikely = measure.getIndexOfMostLikelyEntry();

        System.out.println(person.getCSVString());
        System.out.println(mostLikely);
        System.out.println(measure);

        double belief = measure.calculateBelief(0);
        double plausability = measure.calculatePlausability(0);
        double doubt = measure.calculateDoubt(0);

        System.out.println("For "+_bookLookUp.get(mostLikely));
        System.out.println("Belief: \t" + belief +"\nPlausability: \t" + plausability + "\nDoubt: \t\t" + doubt);
        System.out.println();




        // System.out.println("Accumulated measures result in: \n" + measure.toString());
        // 
        // double beliefA = measure.calculateBelief(0);
        // double plausabilityA = measure.calculatePlausability(0);
        // double doubtA = measure.calculateDoubt(0);
// 
        // System.out.println("For Book A:");
        // System.out.println("Belief: \t" + beliefA +"\nPlausability: \t" + plausabilityA + "\nDoubt: \t\t" + doubtA);
// 
        // double beliefB = measure.calculateBelief(1);
        // double plausabilityB = measure.calculatePlausability(1);
        // double doubtB = measure.calculateDoubt(1);
        // 
        // System.out.println("For Book B:");
        // System.out.println("Belief: \t" + beliefB +"\nPlausability: \t" + plausabilityB + "\nDoubt: \t\t" + doubtB);
// 
        // double beliefC = measure.calculateBelief(2);
        // double plausabilityC = measure.calculatePlausability(2);
        // double doubtC = measure.calculateDoubt(2);
        // 
        // System.out.println("For Book C:");
        // System.out.println("Belief: \t" + beliefC +"\nPlausability: \t" + plausabilityC + "\nDoubt: \t\t" + doubtC);
    }

    public List<Person> getEvaluationData(){
        return _evaluationData;
    }

    private void AddPerson(String person, String[] header){
        String[] personsAttributes = person.split(";");
        Person newPerson = new Person();
        int i = 0;
        for (String attributeValue : personsAttributes) {
            if(!attributeValue.equals("")){
                newPerson.addAttribute(header[i], attributeValue);
            }
            i++;
        }
        _evaluationData.add(newPerson);
    }
}