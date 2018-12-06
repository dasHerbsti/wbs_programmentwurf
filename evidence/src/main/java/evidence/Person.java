package evidence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Person {
    private final String SEMICOLON = ";";

    private Map<String, AttributeValue> _attributes;
    private int _number;
    private String _result="";

    public Person(){
        _attributes = new HashMap<String, AttributeValue>();
    }

    public void addAttribute(String attributeName, String attributeValue){
        if(attributeName.equals("Nr")){
            _number = new Integer(attributeValue);
        }
        if(!_attributes.containsKey(attributeName)){
            _attributes.put(attributeName, new AttributeValue(attributeValue, attributeName, null));
        }
    }

    public List<String> getAttributeValues(){
        return _attributes.values().stream().map(x->x.getValue()).collect(Collectors.toList());
    }

    public int getNumber(){
        return _number;
    }

    public void setResult(String result){
        _result=result;
    }

    public String getResult(){
        return _result;
    }

    public String getCSVString(){
        StringBuilder sb = new StringBuilder();
        sb.append(_number);
        sb.append(SEMICOLON);
        for (String column : TestData.getHeader()) {
            if(_attributes.containsKey(column)){
                sb.append(_attributes.get(column).getValue());
            }
            sb.append(SEMICOLON);
        }
        sb.append(_result);
        return sb.toString();
    }
}