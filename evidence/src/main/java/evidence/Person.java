package evidence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Person {
    private Map<String, AttributeValue> _attributes;

    public Person(){
        _attributes = new HashMap<String, AttributeValue>();
    }

    public void addAttribute(String attributeName, String attributeValue){
        if(!_attributes.containsKey(attributeName)){
            _attributes.put(attributeName, new AttributeValue(attributeValue, attributeName, null));
        }
    }

    public List<String> getAttributeValues(){
        return _attributes.values().stream().map(x->x.getValue()).collect(Collectors.toList());
    }
}