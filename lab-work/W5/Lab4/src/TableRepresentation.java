import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TableRepresentation {
    private final Map<String, Integer> sourceStates = new HashMap<>();
    private final Map<String, Integer> alphabetElements = new HashMap<>();
    private final Map<String, Integer> destinationStates = new HashMap<>();
    private final Integer[][] table;
    private Integer sourceStatesCount, alphabetElementsCount, destinationStatesCount;

    public TableRepresentation(Integer statesCount, Integer alphabetCount){
        this.sourceStatesCount = this.alphabetElementsCount = this.destinationStatesCount = 0;
        table = new Integer[statesCount][alphabetCount];
    }

    public void addFunction(String sourceState, String alphabetElement, String destinationState){
        if (!this.sourceStates.containsKey(sourceState)){
            this.sourceStates.put(sourceState, sourceStatesCount++);
        }
        if (!this.alphabetElements.containsKey(alphabetElement)){
            this.alphabetElements.put(alphabetElement, alphabetElementsCount++);
        }
        if (!this.destinationStates.containsKey(destinationState)){
            this.destinationStates.put(destinationState, destinationStatesCount++);
        }

        table[this.sourceStates.get(sourceState)][this.alphabetElements.get(alphabetElement)]
                = this.destinationStates.get(destinationState);
    }

    public String getFunctionResult(String sourceState, String alphabetElement){
        if (!this.sourceStates.containsKey(sourceState) || !this.alphabetElements.containsKey(alphabetElement)) {
            return null;
        }
        Integer encodedAlphabetElement = table[this.sourceStates.get(sourceState)][this.alphabetElements.get(alphabetElement)];
        for (Map.Entry<String, Integer> entry : this.destinationStates.entrySet()) {
            if (Objects.equals(encodedAlphabetElement, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public String getFunctionList(){
        StringBuilder output = new StringBuilder();
        for (Map.Entry<String, Integer> sourceStateEntry: this.sourceStates.entrySet()){
            for (Map.Entry<String, Integer> alphabetElementEntry: this.alphabetElements.entrySet()){
                output.append("(").append(sourceStateEntry.getKey())
                        .append(", ").append(alphabetElementEntry.getKey())
                        .append(") -> ")
                        .append(this.getFunctionResult(sourceStateEntry.getKey(), alphabetElementEntry.getKey()));
                output.append("\n");
            }
        }
        return output.toString();
    }

}
