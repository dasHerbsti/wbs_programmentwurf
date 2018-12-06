package evidence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SetOfAlternatives {
    private String _pathOfKnowledgebase;
    private Map<Integer, String> _alternatives;

    public SetOfAlternatives(String pathOfKnowledgeBase){
        _pathOfKnowledgebase=pathOfKnowledgeBase;
        _alternatives=new HashMap<>();
        loadAlternatives();
    }

    private void loadAlternatives(){

    }
}