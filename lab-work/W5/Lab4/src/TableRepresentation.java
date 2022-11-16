import java.util.*;

public class TableRepresentation {
    private final Map<String, Integer> sourceStates = new HashMap<>();
    private final Map<String, Integer> alphabetElements = new HashMap<>();
    private final Map<Integer, List<String>> destinationStates = new HashMap<>();
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


        Integer tableData = table[this.sourceStates.get(sourceState)][this.alphabetElements.get(alphabetElement)];
        if(tableData == null){

            this.destinationStates.put(destinationStatesCount++, new LinkedList<>(Collections.singleton(destinationState)));
            table[this.sourceStates.get(sourceState)][this.alphabetElements.get(alphabetElement)]
                    = destinationStatesCount - 1;
            return;
        }
        else {
            for (Map.Entry<Integer, List<String>> entry: this.destinationStates.entrySet()){
                if(Objects.equals(entry.getKey(), tableData)){
                    entry.getValue().add(destinationState);
                    return;
                }
            }
        }

        throw new RuntimeException("Unexpected error when adding function.");
    }

    public List<String> getFunctionResult(String sourceState, String alphabetElement){
        if (!this.sourceStates.containsKey(sourceState) || !this.alphabetElements.containsKey(alphabetElement)) {
            return null;
        }
        Integer encodedAlphabetElement = table[this.sourceStates.get(sourceState)][this.alphabetElements.get(alphabetElement)];
        for (Map.Entry<Integer, List<String>> entry : this.destinationStates.entrySet()) {
            if (Objects.equals(encodedAlphabetElement, entry.getKey())) {
                return entry.getValue();
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
