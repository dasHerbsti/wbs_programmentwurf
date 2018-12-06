package evidence;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
 
public class DataSet{
    private String _path;
    private List<AttributeValue> _attributes;
    private Map<String, List<Double>> _basicMasses = new HashMap<String, List<Double>>();
    private Map<String, Integer> _BookNameToIndex;
    private int _numberOfTotalPersons;
    private int _numberOfPersonsWithBookA;
    private int _numberOfPersonsWithBookB;
    private int _numberOfPersonsWithBookC;

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

    public List<Double> getBasicMassToAttribute(String attributeValue){
        if(_basicMasses.containsKey(attributeValue)){
            return _basicMasses.get(attributeValue);            
        }
        else{
            return new ArrayList<Double>() {{add(0d); add(0d); add(0d); }};
        }
    }

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

            
            List<AttributeValue> filteredAttributes = _attributes.stream().filter(x -> (!x.getAttributeName().equals("Nr"))).collect(Collectors.toList());
            
            CalculateTotalDistributionValues(filteredAttributes);
            for (AttributeValue attributeValue : filteredAttributes) {
                if(_basicMasses.get(attributeValue.getValue()) == null){
                    _basicMasses.put(attributeValue.getValue(), CalculateSingleBasicMass(attributeValue.getValue()));
                }
            }
            
        } catch (IOException e){
            System.out.println("CVS inport broken. Exception: "+e);
        }        
    }

    private void CalculateTotalDistributionValues(List<AttributeValue> attributeValues){
        _numberOfTotalPersons=(int)attributeValues.stream().map(x->x.getPersonNumber()).distinct().count();
        _numberOfPersonsWithBookA=(int)attributeValues.stream().filter(x->(x.getBookCode() == 0)).map(x->x.getPersonNumber()).distinct().count();
        _numberOfPersonsWithBookB=(int)attributeValues.stream().filter(x->(x.getBookCode() == 1)).map(x->x.getPersonNumber()).distinct().count();
        _numberOfPersonsWithBookC=(int)attributeValues.stream().filter(x->(x.getBookCode() == 2)).map(x->x.getPersonNumber()).distinct().count();
    }

    private List<Double> CalculateSingleBasicMass(String attributeName){
        List<Double> basicMasses = new ArrayList<Double>();       
 
        int numberOfAttributeOccurenceBookA = (int) _attributes.stream().filter(x -> (x.getValue().equals(attributeName) && _BookNameToIndex.get("Buch_A").equals(x.getBookCode()))).count();
        int numberOfAttributeOccurenceBookB = (int) _attributes.stream().filter(x -> (x.getValue().equals(attributeName) && _BookNameToIndex.get("Buch_B").equals(x.getBookCode()))).count();
        int numberOfAttributeOccurenceBookC = (int) _attributes.stream().filter(x -> (x.getValue().equals(attributeName) && _BookNameToIndex.get("Buch_C").equals(x.getBookCode()))).count();

        int numberOfTotalOccurences = numberOfAttributeOccurenceBookA+numberOfAttributeOccurenceBookB+numberOfAttributeOccurenceBookC;

        basicMasses.add((((double)numberOfAttributeOccurenceBookA)/(numberOfTotalOccurences*_numberOfPersonsWithBookA)));
        basicMasses.add((((double)numberOfAttributeOccurenceBookB)/(numberOfTotalOccurences*_numberOfPersonsWithBookB)));
        basicMasses.add((((double)numberOfAttributeOccurenceBookC)/(numberOfTotalOccurences*_numberOfPersonsWithBookC)));

        basicMasses = normalize(basicMasses);

        return basicMasses;
    }

    private List<Double> normalize (List<Double> basicMasses){
        List<Double> newBasicMasses = new ArrayList<Double>();
        double sum = 0.0;
        for (Double basisMass : basicMasses) {
            sum+=basisMass;
        }
        for (Double basisMass : basicMasses) {
            newBasicMasses.add((basisMass/sum));
        }
        return newBasicMasses;
    }

    private void AddPersonsAttributes(String personCsv, String[] header){
        String[] attributes = personCsv.split(";");
        for(int i=0; i<8; i++){
            _attributes.add( new AttributeValue(attributes[i], header[i], _BookNameToIndex.get(attributes[8]), attributes[0]));
        }
    }
}