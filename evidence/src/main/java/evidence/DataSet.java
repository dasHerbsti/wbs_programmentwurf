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
    private Map<String, Integer> _bookLookUp; 

    public DataSet(String sourcePath){
        _attributes = new ArrayList<AttributeValue>();
        _basicMasses = new HashMap<String, List<Double>>();
        _path = sourcePath;
        _bookLookUp = new HashMap<String, Integer>();

        _bookLookUp.put("Buch_A", 0);
        _bookLookUp.put("Buch_B", 1);
        _bookLookUp.put("Buch_C", 2);

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

            for (AttributeValue attributeValue : filteredAttributes) {
                if(_basicMasses.get(attributeValue.getValue()) == null){
                    _basicMasses.put(attributeValue.getValue(), CalculateSingleBasicMass(attributeValue.getValue()));
                }
            }
            
        } catch (IOException e){
           // System.out.printl("CVS inport broken. Exception: "+e);
        }        
    }

    private List<Double> CalculateSingleBasicMass(String attributeName){
        List<Double> basicMasses = new ArrayList<Double>();

        

        Integer numberOfAttributeOccurenceBookA = (int) _attributes.stream().filter(x -> (x.getValue().equals(attributeName) && x.getBookCode().equals(_bookLookUp.get("Buch_A")))).count();
        Integer numberOfAttributeOccurenceBookB = (int) _attributes.stream().filter(x -> (x.getValue().equals(attributeName) && x.getBookCode().equals(_bookLookUp.get("Buch_B")))).count();
        Integer numberOfAttributeOccurenceBookC = (int) _attributes.stream().filter(x -> (x.getValue().equals(attributeName) && x.getBookCode().equals(_bookLookUp.get("Buch_C")))).count();

        Integer numberOfTotalOccurences = numberOfAttributeOccurenceBookA+numberOfAttributeOccurenceBookB+numberOfAttributeOccurenceBookC;

        basicMasses.add(((double)numberOfAttributeOccurenceBookA/(4*numberOfTotalOccurences)));
        basicMasses.add(((double)numberOfAttributeOccurenceBookB/(4*numberOfTotalOccurences)));
        basicMasses.add(((double)numberOfAttributeOccurenceBookC/(4*numberOfTotalOccurences)));

        return basicMasses;
    }

    private void AddPersonsAttributes(String personCsv, String[] header){
        String[] attributes = personCsv.split(";");
        for(int i=0; i<8; i++){
            _attributes.add( new AttributeValue(attributes[i], header[i], _bookLookUp.get(attributes[8])));
        }
    }
}