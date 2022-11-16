import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Language {
    private Configuration initialConfiguration;
    private String sequence;

    public Language(String sequence) {
        this.sequence = sequence;
    }

    public boolean isAccepted(FiniteAutomaton finiteAutomaton){
        if (!finiteAutomaton.isDeterministic()){
            throw new RuntimeException("Sequence can only have its acceptance check in the case of a DFA.");
        }
        Configuration configuration;
        configuration = initialConfiguration = new Configuration(finiteAutomaton.initialState, sequence);
        boolean reachedFinalState = false;
        while(!(reachedFinalState || configuration.sequence.isEmpty())){
            List<String> nextStates = finiteAutomaton.representation.getFunctionResult(
                    configuration.state,
                    String.valueOf(configuration.sequence.charAt(0))
            );
            configuration.set(new Configuration(nextStates.get(0), configuration.sequence.substring(1)));
            configuration = configuration.move();
            for(String finalState: finiteAutomaton.finalStates){
                if (Objects.equals(configuration.state, finalState)){
                    reachedFinalState = true;
                    break;
                }
            }
        }
        return configuration.sequence.isEmpty();
    }

    public String show(){
        StringBuilder output = new StringBuilder();
        Configuration configuration = initialConfiguration;
        output.append(configuration);
        int k = 0;
        while(configuration.next != null){
            configuration = configuration.move();
            k += 1;
        }
        output.append(" ").append("|-").append(k).append("-");
        output.append(" ").append(configuration);
        return output.toString();
    }

    public String showDetailed(){
        StringBuilder output = new StringBuilder();
        Configuration configuration = initialConfiguration;
        output.append(configuration);
        while(configuration.next != null){
            configuration = configuration.move();
            output.append(" ").append("|---");
            output.append(" ").append(configuration);
        }
        return output.toString();
    }
}
