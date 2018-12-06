package evidence;

public class AttributeValue { 
    private final String _value; 
    private final String _attributeName; 
    private final Integer _bookCode; 

    public AttributeValue(String value, String attributeName, Integer bookCode) { 
      this._value=value;
      this._attributeName=attributeName;
      this._bookCode=bookCode;  
    } 

    public String getValue(){
      return _value;
    }

    public String getAttributeName(){
      return _attributeName;
    }

    public Integer getBookCode(){
      return _bookCode;
    }
  } 