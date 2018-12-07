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
    // the header of the csv table with the column names (=attribute names)
    private static String[] _csvTableHeader;

    // the list of the person that should be evaluated by the program
    private List<Person> _evaluationData;

    // the knowledge base
    private DataSet _baseDataSet;

    // maps a books index to its name
    private static Map<Integer,String> _bookIndexToName;

    // the path of the results file
    private String _resultFile;
    
    public TestData(String evaluationDataFile, String resultFile, DataSet baseData){
        _evaluationData = new ArrayList<Person>();
        _baseDataSet = baseData;
        _resultFile = resultFile;

        // map book names to arry indices
        _bookIndexToName = new HashMap<>();
        _bookIndexToName.put(0, "Buch_A");
        _bookIndexToName.put(1, "Buch_B");
        _bookIndexToName.put(2, "Buch_C");
        

        // read test data from file "testdata.csv" and create person instances
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(new File(evaluationDataFile)));

            // skip header line
            _csvTableHeader = reader.readLine().split(";");
    
            String person;
            while((person = reader.readLine())!=null){
                AddPerson(person, _csvTableHeader);
            }
            reader.close();
        } catch (Exception e) {   
            System.out.println("A problem occured at trying to import the test data from testdata.csv. Exception: ");         
            e.printStackTrace();
        }            
    }
    /**
	 * Gets the header of the csv test data table
	 * @return the header of the csv test data table
	 */
    public static String[] getCsvTableHeader(){
        return _csvTableHeader;
    }

    /**
	* Evaluate the test data 
	*/
    public void evaluate(){
        for (Person person : _evaluationData) {
            DempsterHandler dempsterHandler = new DempsterHandler(3);
            // add measures for each known attribute of the test person to the dempster handler
            for (String attributeValue : person.getAttributeValues()){
                List<Double> basicMasses = _baseDataSet.getBasicMassToAttribute(attributeValue);
                Measure measure = dempsterHandler.addMeasure();
                if((double)basicMasses.get(0) > 0.0000001){
                    measure.addEntry(Arrays.asList( new Integer[]{1,0,0} ), (double)basicMasses.get(0));
                }
                if((double)basicMasses.get(1) > 0.0000001){
                    measure.addEntry(Arrays.asList( new Integer[]{0,1,0} ), (double)basicMasses.get(1));
                }
                if((double)basicMasses.get(2) > 0.0000001){
                    measure.addEntry(Arrays.asList( new Integer[]{0,0,1} ), (double)basicMasses.get(2));                    
                }                

                // System.out.println("measure: "+attributeValue+"\n" + measure.toString());
            }
            
            dempsterHandler.accumulateAllMeasures();   

            int indexOfMostLikelyEntry = dempsterHandler.getFirstMeasure().getIndexOfMostLikelyEntry();
            person.setResult(_bookIndexToName.get(indexOfMostLikelyEntry),
                             dempsterHandler.getFirstMeasure().calculateBelief(indexOfMostLikelyEntry),
                             dempsterHandler.getFirstMeasure().calculatePlausability(indexOfMostLikelyEntry),
                             dempsterHandler.getFirstMeasure().calculateDoubt(indexOfMostLikelyEntry));
            printResult(dempsterHandler.getFirstMeasure(), person);       
            saveResult();  
        }
    }

    /**
     * Saves the results (persons with attributes and assigned books) to a csv file with the same format than the input file
     * The path is taken from the field _resultFile
     */
    private void saveResult(){
        try{
            List<String> lines = new ArrayList<String>();
            lines.add(String.join(";", _csvTableHeader)+";Buch");
            // lines.add(String.join(";", _csvTableHeader)+";Buch;Belief;Plausibility;Zweifel");
            for (Person person : _evaluationData) {
                lines.add(person.getCSVString());
            }
            Path file = Paths.get(_resultFile);
            Files.write(file, lines, Charset.forName("UTF-8"));

        }catch(Exception e){
            System.out.println("A problem occured at trying to the result in the resultdata.csv. Exception: "+e);
            e.printStackTrace();
        }
    }

    /**
     * print the result of a single measure to the command line
     * @param measure 
     * @param person
     */
    public static void printResult(Measure measure, Person person){        
        System.out.println(person.toOutputString());        
        
        // System.out.println(measure);
        // double belief = measure.calculateBelief(0);
        // double plausability = measure.calculatePlausability(0);
        // double doubt = measure.calculateDoubt(0);
        // 
        // System.out.println("Belief: \t" + belief +"\nPlausability: \t" + plausability + "\nDoubt: \t\t" + doubt);

        System.out.println();
    }

    public List<Person> getEvaluationData(){
        return _evaluationData;
    }

    /**
     * Parses a person from a csv string and adds it to the test data list
     * @param person the csv string
     * @param header the header of the csv table with the attribute names
     */
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