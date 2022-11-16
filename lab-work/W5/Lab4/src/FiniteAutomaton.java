import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;

public class FiniteAutomaton {
    public TableRepresentation representation;
    private final Set<String> allStates = new HashSet<>();
    private final Set<String> alphabet = new HashSet<>();
    public String initialState;
    public final Set<String> finalStates = new HashSet<>();

    public FiniteAutomaton() {
        this.readFiniteAutomaton(Path.of("W5/Lab4/input/FA.in"));
    }

    public FiniteAutomaton(String finiteAutomatonPath) {
        this.readFiniteAutomaton(Path.of(finiteAutomatonPath));
    }

    private void readFiniteAutomaton(Path finiteAutomatonPath){
        try {
            File finiteAutomatonFile = new File(finiteAutomatonPath.toUri());
            Scanner finiteAutomatonReader = new Scanner(finiteAutomatonFile);
            int inputType = 0;
            while (finiteAutomatonReader.hasNextLine()) {
                String fileLine = finiteAutomatonReader.nextLine();
                if(inputType == 0){
                    String[] states = fileLine.replaceAll("\\s+","").split(",");

                    if(states.length < 2) {
                        throw new RuntimeException("There must be at least 2 states.");
                    }

                    for(String state: states){
                        if(!Pattern.compile("\\S+").matcher(state).matches()){
                            throw new RuntimeException("All states must be valid.");
                        }
                    }

                    allStates.addAll(Arrays.asList(states));

                    inputType++;
                } else if (inputType == 1) {
                    String[] letters = fileLine.replaceAll("\\s+","").split(",");

                    for(String letter: letters){
                        if(!Pattern.compile("\\S").matcher(letter).matches()){
                            throw new RuntimeException("Letters can only be of one character long.");
                        }
                    }

                    alphabet.addAll(Arrays.asList(letters));

                    this.representation = new TableRepresentation(allStates.size(), alphabet.size());

                    inputType++;

                } else if (inputType == 2) {
                    String[] importantStates = fileLine.replaceAll("\\s+","").split(",");

                    if(importantStates.length < 2) {
                        throw new RuntimeException("There must be at least 2 states.");
                    }

                    if(!Pattern.compile("\\S+").matcher(importantStates[0]).matches()){
                        throw new RuntimeException("Initial state must be valid.");
                    }

                    if(!allStates.contains(importantStates[0])){
                        throw new RuntimeException("Initial state not found in the list of states.");
                    }

                    initialState = importantStates[0];

                    Arrays.stream(importantStates)
                            .skip(1)
                            .forEach(state -> {
                                if(!Pattern.compile("\\S+").matcher(state).matches()){
                                    throw new RuntimeException("All final states must be valid.");
                                }

                                if(!allStates.contains(state)){
                                    throw new RuntimeException("Final state not found in the list of states.");
                                }

                                this.finalStates.add(state);
                            });

                    inputType++;
                } else if (inputType == 3) {

                    String[] functionElements = fileLine.replaceAll("\\s+","").split("=");

                    if(functionElements.length != 2) {
                        throw new RuntimeException("No transition identified.");
                    }

                    if(!Pattern.compile("\\S+").matcher(functionElements[1]).matches()){
                        throw new RuntimeException("Destination state must be valid.");
                    }

                    if(!allStates.contains(functionElements[1])){
                        throw new RuntimeException("Destination state not found in the list of states.");
                    }

                    String[] functionInput = functionElements[0].split(",");

                    if(functionInput.length != 2) {
                        throw new RuntimeException("Invalid transition parameters.");
                    }

                    if(!Pattern.compile("\\S+").matcher(functionInput[0]).matches()){
                        throw new RuntimeException("Source state must be valid.");
                    }

                    if(!allStates.contains(functionInput[0])){
                        throw new RuntimeException("Source state not found in the list of states.");
                    }

                    if(!Pattern.compile("\\S").matcher(functionInput[1]).matches()){
                        throw new RuntimeException("Alphabet letter must be valid.");
                    }

                    if(!alphabet.contains(functionInput[1])){
                        throw new RuntimeException("Letter not found in the alphabet.");
                    }

                    representation.addFunction(
                            functionInput[0],
                            functionInput[1],
                            functionElements[1]);
                }
            }
            finiteAutomatonReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public boolean isDeterministic(){
        for(String state: allStates){
            for(String letter: alphabet) {
                List<String> destinationStates = representation.getFunctionResult(state, letter);
                if (destinationStates != null && destinationStates.size() > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    public void showUI(){
        Scanner userInputScanner = new Scanner(System.in);
        while(true){
            System.out.println();
            System.out.println("Application Menu");
            System.out.println();
            System.out.println("1 -> Show the set of states.");
            System.out.println("2 -> Show the alphabet.");
            System.out.println("3 -> Show the transitions.");
            System.out.println("4 -> Show the initial state.");
            System.out.println("5 -> Show the set of final states.");
            System.out.println();
            System.out.println("Choose your option or type \"exit\" to exit the application:");
            String userInput = userInputScanner.nextLine();
            if(Objects.equals(userInput, "exit")){
                break;
            } else if (Objects.equals(userInput, "1")) {
                StringBuilder output = new StringBuilder();
                output.append("Set of States: ");
                for (String state: allStates){
                    output.append(state).append(", ");
                }
                output.replace(output.length() - 2, output.length(), "");

                System.out.println();
                System.out.println(output);
                System.out.println();
            } else if (Objects.equals(userInput, "2")) {
                StringBuilder output = new StringBuilder();
                output.append("Alphabet: ");
                for (String letter: alphabet){
                    output.append(letter).append(", ");
                }
                output.replace(output.length() - 2, output.length(), "");

                System.out.println();
                System.out.println(output);
                System.out.println();
            } else if (Objects.equals(userInput, "3")) {
                System.out.println();
                System.out.println("Transitions:");
                System.out.print(representation.getFunctionList());
                System.out.println();
            } else if (Objects.equals(userInput, "4")) {
                System.out.println();
                System.out.println("Initial State: " + initialState);
                System.out.println();
            } else if (Objects.equals(userInput, "5")) {
                StringBuilder output = new StringBuilder();
                output.append("Set of Final States: ");
                for (String state: finalStates){
                    output.append(state).append(", ");
                }
                output.replace(output.length() - 2, output.length(), "");

                System.out.println();
                System.out.println(output);
                System.out.println();
            } else {
                throw new RuntimeException("Invalid Option!");
            }
        }
    }
}
