package evidence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Person {
    private final String SEMICOLON = ";";
    private final String COLUMNNAMENUMBER = "Nr"; 

    private Map<String, AttributeValue> _attributes;
    private int _number;
    private String _resultingBook="";
    private double _belief;
    private double _plausibility;
    private double _doubt;

    public Person(){
        _attributes = new HashMap<String, AttributeValue>();
    }

    public void addAttribute(String attributeName, String attributeValue){
        if(attributeName.equals(COLUMNNAMENUMBER)){
            _number = new Integer(attributeValue);
        }
        else if(!_attributes.containsKey(attributeName)){
            _attributes.put(attributeName, new AttributeValue(attributeValue, attributeName, -1, ""+_number));
        }
    }

    public List<String> getAttributeValues(){
        return _attributes.values().stream().map(x->x.getValue()).collect(Collectors.toList());
    }

    public int getNumber(){
        return _number;
    }

    public void setResult(String resultingBook, double belief, double plausibility, double doubt){
        _resultingBook=resultingBook;
        _belief=belief;
        _plausibility=plausibility;
        _doubt=doubt;
    }

    public String getResultingBook(){
        return _resultingBook;
    }

    public double getBelief(){
        return _belief;
    }

    public double getPlausibility(){
        return _plausibility;
    }

    public double getDoubt(){
        return _doubt;
    }

    public String getCSVString(){
        StringBuilder sb = new StringBuilder();
        sb.append(_number);
        sb.append(SEMICOLON);
        for (String column : TestData.getCsvTableHeader()) {
            if(_attributes.containsKey(column)){
                sb.append(_attributes.get(column).getValue());
            }
            if(!column.equals(COLUMNNAMENUMBER)){
                sb.append(SEMICOLON);
            }
        }
        sb.append(_resultingBook);
        sb.append(SEMICOLON);
        sb.append(_belief);
        sb.append(SEMICOLON);
        sb.append(_plausibility);
        sb.append(SEMICOLON);
        sb.append(_doubt);
        return sb.toString();
    }
}