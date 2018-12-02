package evidence;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
 
public class DataSet{

    String _path;
    private List<Triple<String, String, Integer>> _attributes;
    private Map<String, List<Float>> _basicMasses = new HashMap<String, List<Float>>();

    private Map<String, Integer> _bookLookUp; 

    public DataSet(String sourcePath){
        _attributes = new ArrayList<Triple<String, String, Integer>>();
        _basicMasses = new HashMap<String, List<Float>>();
        _path = sourcePath;
        _bookLookUp = new HashMap<String, Integer>();

        _bookLookUp.put("Buch_A", 0);
        _bookLookUp.put("Buch_B", 1);
        _bookLookUp.put("Buch_C", 2);

        CalculateBasicMasses();
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

            List<Triple<String, String, Integer>> filteredAttributes = _attributes.stream().filter(x -> (!x.y.equals("Nr"))).collect(Collectors.toList());

            for (Triple<String, String, Integer> attribute : filteredAttributes) {
                if(_basicMasses.get(attribute.x) == null){
                    _basicMasses.put(attribute.x, CalculateSingleBasicMass(attribute.x));
                }
            }
            
        } catch (IOException e){
           // System.out.printl("CVS inport broken. Exception: "+e);
        }        
    }

    private List<Float> CalculateSingleBasicMass(String attributeName){
        List<Float> basicMasses = new ArrayList<Float>();

        Integer numberOfAttributeOccurenceBookA = (int) _attributes.stream().filter(x -> (x.x == attributeName && x.z == _bookLookUp.get("Buch_A"))).count();
        Integer numberOfAttributeOccurenceBookB = (int) _attributes.stream().filter(x -> (x.x == attributeName && x.z == _bookLookUp.get("Buch_B"))).count();
        Integer numberOfAttributeOccurenceBookC = (int) _attributes.stream().filter(x -> (x.x == attributeName && x.z == _bookLookUp.get("Buch_C"))).count();

        Integer numberOfTotalOccurences = numberOfAttributeOccurenceBookA+numberOfAttributeOccurenceBookB+numberOfAttributeOccurenceBookC;

        basicMasses.add((float)(numberOfAttributeOccurenceBookA/(4*numberOfTotalOccurences)));
        basicMasses.add((float)(numberOfAttributeOccurenceBookB/(4*numberOfTotalOccurences)));
        basicMasses.add((float)(numberOfAttributeOccurenceBookC/(4*numberOfTotalOccurences)));

        return basicMasses;
    }

    private void AddPersonsAttributes(String personCsv, String[] header){
        String[] attributes = personCsv.split(";");
        for(int i=0; i<8; i++){
            _attributes.add( new Triple<String, String, Integer>(attributes[i], header[i], _bookLookUp.get(attributes[8])));
        }
    }
}