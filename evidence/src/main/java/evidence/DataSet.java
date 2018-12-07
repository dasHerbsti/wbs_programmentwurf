package evidence;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
 
public class DataSet{
    // Factor to weigh uncertainty 
    // (1 - factor) is the basic mass of omega
    private final double NORMALIZE_FACTOR = 1.0; 

    // path of the knowledge base file
    private String _path;

    // list of all attribute values
    private List<AttributeValue> _attributes;

    // collection of the calculated basic masses for each attribute characteristic
    private Map<String, List<Double>> _basicMasses = new HashMap<String, List<Double>>();

    // look up for the book names 
    private Map<String, Integer> _BookNameToIndex;

    // count of persons
    private int _TotalPersons;
    // count of persons with book A
    private int _PersonsWithBookA;
    // count of persons with book B
    private int _PersonsWithBookB;
    // count of persons with book C
    private int _PersonsWithBookC;

    
    
    public DataSet(String sourcePath){
        _attributes = new ArrayList<AttributeValue>();
        _basicMasses = new HashMap<String, List<Double>>();
        _path = sourcePath;
        _BookNameToIndex = new HashMap<String, Integer>();

        _BookNameToIndex.put("Buch_A", 0);
        _BookNameToIndex.put("Buch_B", 1);
        _BookNameToIndex.put("Buch_C", 2);

        CalculateBasicMasses();
    }

    /**
	* Gets the basic masses for each book for the given attribute value 
	* @param attributeValue the value of the attribute for which the basic masses are requested
	* @return a list with the basic mass values for each book 
	*/
    public List<Double> getBasicMassToAttribute(String attributeValue){
        if(_basicMasses.containsKey(attributeValue)){
            return _basicMasses.get(attributeValue);            
        }
        else{
            return new ArrayList<Double>() {{add(0d); add(0d); add(0d); }};
        }
    }

    /**
     * Calculates the basic masses from the data source file
     */
    private void CalculateBasicMasses(){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(_path)));
            
            // skip header line
            String[] header = reader.readLine().split(";");

            String person;
            while((person = reader.readLine())!=null){
                AddPersonsAttributes(person, header);
            }
            reader.close();

            // filter Nr fields to get only the characteristics
            List<AttributeValue> filteredAttributes = _attributes.stream().filter(x -> (!x.getAttributeName().equals("Nr"))).collect(Collectors.toList());
            
            // count persons and occurences of each book
            CalculateTotalDistributionValues(filteredAttributes);
            
            // calculate basic mass for each attribute characteristic
            for (AttributeValue attributeValue : filteredAttributes) {
                if(_basicMasses.get(attributeValue.getValue()) == null){
                    _basicMasses.put(attributeValue.getValue(), CalculateSingleBasicMass(attributeValue.getValue()));
                }
            }
            
        } catch (IOException e){
            System.out.println("CVS inport broken. Exception: "+e);
        }        
    }

    /**
     * Calculates the values of the distribution of all book on the given person set from the data source
     * @param attributeValues the {@link AttributeValue}s from the data source
     */
    private void CalculateTotalDistributionValues(List<AttributeValue> attributeValues){
        _TotalPersons=(int)attributeValues.stream().map(x->x.getPersonNumber()).distinct().count();
        _PersonsWithBookA=(int)attributeValues.stream().filter(x->(x.getBookCode() == 0)).map(x->x.getPersonNumber()).distinct().count();
        _PersonsWithBookB=(int)attributeValues.stream().filter(x->(x.getBookCode() == 1)).map(x->x.getPersonNumber()).distinct().count();
        _PersonsWithBookC=(int)attributeValues.stream().filter(x->(x.getBookCode() == 2)).map(x->x.getPersonNumber()).distinct().count();
    }

    /**
     * Calculates the basic masses for a single attribute characteristic
     * @param attributeName the name of the attribute
     * @return A list with the basic mass values for book A, book B and book C
     */
    private List<Double> CalculateSingleBasicMass(String attributeName){
        List<Double> basicMasses = new ArrayList<Double>();       
 
        int AttributeOccurenceBookA = (int) _attributes.stream().filter(x -> (x.getValue().equals(attributeName) && _BookNameToIndex.get("Buch_A").equals(x.getBookCode()))).count();
        int AttributeOccurenceBookB = (int) _attributes.stream().filter(x -> (x.getValue().equals(attributeName) && _BookNameToIndex.get("Buch_B").equals(x.getBookCode()))).count();
        int AttributeOccurenceBookC = (int) _attributes.stream().filter(x -> (x.getValue().equals(attributeName) && _BookNameToIndex.get("Buch_C").equals(x.getBookCode()))).count();

        int TotalOccurences = AttributeOccurenceBookA+AttributeOccurenceBookB+AttributeOccurenceBookC;

        basicMasses.add((((double)AttributeOccurenceBookA*_TotalPersons)/(TotalOccurences*_PersonsWithBookA)));
        basicMasses.add((((double)AttributeOccurenceBookB*_TotalPersons)/(TotalOccurences*_PersonsWithBookB)));
        basicMasses.add((((double)AttributeOccurenceBookC*_TotalPersons)/(TotalOccurences*_PersonsWithBookC)));

        basicMasses = normalize(basicMasses);

        return basicMasses;
    }

    /**
     * Normalizes the basic mass values in a way that they sum up to 1.
     * @param basicMasses A List with the basic masses for the books A, B and C
     * @return A list with the normalized basic masses for the books A, B and C
     */
    private List<Double> normalize (List<Double> basicMasses){
        List<Double> newBasicMasses = new ArrayList<Double>();
        // calculate the sum of the basic masses
        double sum = 0.0;
        for (Double basisMass : basicMasses) {
            sum+=basisMass;
        }
        // calculate the new basic masses by dividing with the sum
        for (Double basisMass : basicMasses) {
            newBasicMasses.add(((basisMass/sum)*NORMALIZE_FACTOR));
        }
        return newBasicMasses;
    }

    /**
     * Parses a persons attributes from a csv line to the attributes collection
     * @param personCsv the csv string that represents a person
     * @param header the header of the csv table that gives the attributes names
     */
    private void AddPersonsAttributes(String personCsv, String[] header){
        String[] attributes = personCsv.split(";");
        for(int i=0; i<8; i++){
            _attributes.add( new AttributeValue(attributes[i], header[i], _BookNameToIndex.get(attributes[8]), attributes[0]));
        }
    }
}