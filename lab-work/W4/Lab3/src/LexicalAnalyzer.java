import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LexicalAnalyzer {
    public SymbolTable symbolTable;
    public ProgramInternalForm programInternalForm;
    private final ArrayList<AbstractMap.SimpleImmutableEntry<String, String>> tokenList = new ArrayList<>();

    public LexicalAnalyzer() {
        this.reset();
        this.readTokens(Path.of("W2/token.in"));
    }

    private void reset(){
        symbolTable = new SymbolTable();
        programInternalForm = new ProgramInternalForm();
    }

    private void readTokens(Path filePath){
        // There are 3 Token Classifications: Separator, Operator, Reserved Word.
        String[] tokenTypes = new String[]{"separator", "operator", "reserved word"};
        int tokenType = 0;
        try {
            // Try to open the "token.in" file.
            File tokenFile = new File(filePath.toUri());
            // Try to read the "token.in" file.
            Scanner tokenReader = new Scanner(tokenFile);
            while (tokenReader.hasNextLine()) {
                // While there are still lines to be read, classify the available tokens.
                String data = tokenReader.nextLine();
                if(data.equals("")){
                    // If we stumble upon an "empty line", update the classifications for the next tokens.
                    tokenType += 1;
                }
                else{
                    // Add the Token with the Token Type to the tokenList.
                    tokenList.add(new AbstractMap.SimpleImmutableEntry<>(data, tokenTypes[tokenType]));
                }
            }
            tokenReader.close();
            System.out.println(ConsoleColors.PURPLE + "Tokens provided by " + filePath + " have been read." + ConsoleColors.RESET);
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private String computeSeparatorRegex(){
        StringBuilder separatorRegex = new StringBuilder();
        for (AbstractMap.SimpleImmutableEntry<String, String> token: tokenList){
            if (Objects.equals(token.getValue(), "separator")){
                // For each token of the "separator" type, build its regex.
                Matcher escapeSequenceMatcher = Pattern.compile("\\x5C[a-zA-Z]+").matcher(token.getKey());
                if(escapeSequenceMatcher.find()){
                    // If we stumble upon escape sequences, simply add it to the regex.
                    separatorRegex.append(escapeSequenceMatcher.group(0));
                }
                else{
                    for (int tokenChar : token.getKey().toCharArray()) {
                        // In order to dodge any "special characters" chaos,
                        // we append to the regex the characters' ASCII code in a hexadecimal representation.
                        separatorRegex.append("\\x").append(Integer.toHexString(tokenChar));
                    }
                }
                separatorRegex.append("|");
            }
        }
        // Since the end of the loop will append a "|" at the end of the regex,
        // making it unusable, we remove it from the regex and return the regex of
        // tokens of the "separator" type.
        return separatorRegex.toString().replaceAll(".$", "");
    }

    private String computeOperatorRegex(){
        StringBuilder operatorRegex = new StringBuilder();
        for (AbstractMap.SimpleImmutableEntry<String, String> token: tokenList){
            if (Objects.equals(token.getValue(), "operator")){
                // For each token of the "operator" type, build its regex.
                for (int tokenChar : token.getKey().toCharArray()) {
                    // In order to dodge any "special characters" chaos,
                    // we append to the regex the characters' ASCII code in a hexadecimal representation.
                    operatorRegex.append("\\x").append(Integer.toHexString(tokenChar));
                }
                operatorRegex.append("|");
            }
        }
        // Since the end of the loop will append a "|" at the end of the regex,
        // making it unusable, we remove it from the regex and return the regex of
        // tokens of the "operator" type.
        return operatorRegex.toString().replaceAll(".$", "");
    }

    private String computeReservedWordRegex(){
        StringBuilder reservedWordRegex = new StringBuilder();
        for (AbstractMap.SimpleImmutableEntry<String, String> token: tokenList){
            if (Objects.equals(token.getValue(), "reserved word")){
                // For each token of the "reserved word" type, build its regex.
                for (int tokenChar : token.getKey().toCharArray()) {
                    // In order to dodge any "special characters" chaos,
                    // we append to the regex the characters' ASCII code in a hexadecimal representation.
                    reservedWordRegex.append("\\x").append(Integer.toHexString(tokenChar));
                }
                // Ensure that each token of the "reserved word" type is in in-fact a word.
                reservedWordRegex.append("(?=[").append(this.computeSeparatorRegex()).append("])");
                reservedWordRegex.append("|");
            }
        }
        // Since the end of the loop will append a "|" at the end of the regex,
        // making it unusable, we remove it from the regex and return the regex of
        // tokens of the "reserved word" type.
        return reservedWordRegex.toString().replaceAll(".$", "");
    }

    private AbstractMap.SimpleImmutableEntry<String, String> startsWithToken(String data){
        Matcher tokenMatcher = Pattern.compile(
                 "^(" + computeOperatorRegex() + "|" +
                        computeSeparatorRegex() + "|" +
                        computeReservedWordRegex() + ")"
        ).matcher(data);
        if (tokenMatcher.find()) {
            // If the data received starts with a token of type "separator", "operator" or "reserved word",
            // return the token with the computed regex.
            return new AbstractMap.SimpleImmutableEntry<>(tokenMatcher.pattern().toString(), tokenMatcher.group());
        }
        // Otherwise, return null.
        return new AbstractMap.SimpleImmutableEntry<>(null, null);
    }

    /*
    private String extractToken(String data){
        String tokenRegex = "^.*?(?=["+ this.computeSeparatorRegex() + "|" + this.computeOperatorRegex() + "])";
        Matcher tokenMatcher = Pattern.compile(tokenRegex).matcher(data);
        if (tokenMatcher.find()){
            return tokenMatcher.group();
        }
        return null;
    }
    */

    private AbstractMap.SimpleImmutableEntry<String, String> startsWithIdentifier(String data){
        String identifierRegex = "^[a-zA-Z][a-zA-Z0-9]{0,255}(?=["+ this.computeSeparatorRegex() + "|" + this.computeOperatorRegex() + "])";
        Matcher identifierMatcher = Pattern.compile(identifierRegex).matcher(data);
        if (identifierMatcher.find()){
            // If the data received starts with an identifier,
            // return the token with the computed regex.
            return new AbstractMap.SimpleImmutableEntry<>(identifierRegex, identifierMatcher.group());
        }
        // Otherwise, return null.
        return new AbstractMap.SimpleImmutableEntry<>(null, null);
    }

    private AbstractMap.SimpleImmutableEntry<String, String> startsWithConstant(String data){
        String integerConstantRegex = "^0|^[-+]?[1-9][0-9]*(?=["+ this.computeSeparatorRegex() + "|" + this.computeOperatorRegex() + "])";
        String characterConstantRegex = "^[\'][ -~][\']";
        String stringConstantRegex = "^[\"][ -~]*[\"]";
        Matcher constantMatcher = Pattern.compile(
                integerConstantRegex+ "|" +
                        characterConstantRegex + "|" +
                        stringConstantRegex).matcher(data);
        if (constantMatcher.find()) {
            // If the data received starts with a constant of type int, character or string,
            // return the token with the computed regex.
            return new AbstractMap.SimpleImmutableEntry<>(constantMatcher.pattern().toString(), constantMatcher.group());
        }
        // Otherwise, return null.
        return new AbstractMap.SimpleImmutableEntry<>(null, null);
    }

    public void scanSourceCode(Path filePath){
        try {
            // Try to find the Source Code.
            File sourceCodeFile = new File(filePath.toUri());
            Scanner sourceCodeReader = new Scanner(sourceCodeFile);
            // Try to read the Source Code
            int currentlyReadLine = 1;
            ArrayList<AbstractMap.SimpleImmutableEntry<Integer, String>> lexicalErrors = new ArrayList<>();

            while (sourceCodeReader.hasNextLine()) {
                // While Source Code contains instructions, process the data received.
                String data = sourceCodeReader.nextLine();
                if (Pattern.compile("^#{2}").matcher(data).find()){
                    // If line starts with "##" (the comment character), skip it altogether.
                    continue;
                }
                // Remove all whitespace characters at the end of the line.
                data = data.replaceAll("\\s*$", "");
                while (!data.equals("")){
                    // While the line still contains tokens to classify, continue processing it.
                    AbstractMap.SimpleImmutableEntry<String, String> tokenChecker = startsWithToken(data);
                    if (!Objects.equals(tokenChecker.getValue(), null)){
                        // If the line starts with a token of type "separator", "operator" or "reserved word",
                        // add it to the PIF and remove it from the rest of the line.
                        programInternalForm.generate(tokenChecker.getValue(), -1);
                        data = data.replaceFirst(tokenChecker.getKey(), "");
                    }
                    else {
                        AbstractMap.SimpleImmutableEntry<String, String> identifierChecker = startsWithIdentifier(data);
                        AbstractMap.SimpleImmutableEntry<String, String> constantChecker = startsWithConstant(data);
                        if(!Objects.equals(identifierChecker.getValue(), null)){
                            // If the line starts with an identifier,
                            // add it to the ST and PIF and remove it from the rest of the line.
                            symbolTable.put(identifierChecker.getValue());
                            programInternalForm.generate("identifier", symbolTable.getKey(identifierChecker.getValue()));
                            data = data.replaceFirst(identifierChecker.getKey(), "");
                        }
                        else if(!Objects.equals(constantChecker.getValue(), null)){
                            // If the line starts with a constant,
                            // add it to the ST and PIF and remove it from the rest of the line.
                            symbolTable.put(constantChecker.getValue());
                            programInternalForm.generate("constant", symbolTable.getKey(constantChecker.getValue()));
                            data = data.replaceFirst(constantChecker.getKey(), "");
                        }
                        else {
                            // Store the state of any unclassified token as it
                            // contributes to the Source Code's lexical correctness.
                            Matcher tokenMatcher = Pattern.compile(".*(?=" + this.computeSeparatorRegex() + "|" + this.computeOperatorRegex() + "])").matcher(data);
                            if(tokenMatcher.find()){
                                lexicalErrors.add(new AbstractMap.SimpleImmutableEntry<>(currentlyReadLine, tokenMatcher.group()));
                            }
                            else{
                                lexicalErrors.add(new AbstractMap.SimpleImmutableEntry<>(currentlyReadLine, data));
                            }
                            break;
                        }
                    }
                    // Remove the leftover spaces for further computations.
                    data = data.replaceAll("^[ ]*", "");
                    currentlyReadLine += 1;
                }
            }

            sourceCodeReader.close();

            StringBuilder consoleOutput = new StringBuilder();
            if(lexicalErrors.size() == 0){
                consoleOutput.append(ConsoleColors.GREEN)
                        .append(sourceCodeFile.getName())
                        .append(" -- Lexically Correct")
                        .append(ConsoleColors.RESET);
            }
            else{
                for(AbstractMap.SimpleImmutableEntry<Integer, String> error: lexicalErrors){
                    consoleOutput.append(ConsoleColors.RED)
                            .append(sourceCodeFile.getName())
                            .append(" -- Lexical Error found on line ")
                            .append(error.getKey())
                            .append(": ")
                            .append(ConsoleColors.RESET)
                            .append(error.getValue())
                            .append("\n");
                }
            }
            System.out.println(consoleOutput.toString().replaceAll("[\\n]$", ""));

            this.createOutputFolder();
            this.createSymbolTableOutputFile();
            this.createProgramInternalFormOutputFile();
            this.reset();
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void createOutputFolder(){
        File folder = new File(Path.of("W4/Lab3/output").toUri());
        boolean folderState = folder.mkdir();
        if (folderState) {
            System.out.println(ConsoleColors.PURPLE + "/output/ folder created." + ConsoleColors.RESET);
        }
        else {
            System.out.println(ConsoleColors.PURPLE + "/output/ folder already created." + ConsoleColors.RESET);
        }
    }

    private void createSymbolTableOutputFile(){
        File file = new File(Path.of("W4/Lab3/output", "ST" + ".out").toUri());
        try {
            boolean fileState = file.createNewFile();
            if (fileState) {
                System.out.println(ConsoleColors.PURPLE + "ST.out file created." + ConsoleColors.RESET);
            }
            else {
                System.out.println(ConsoleColors.PURPLE + "ST.out file already created." + ConsoleColors.RESET);
            }
            FileWriter output = new FileWriter(file);
            output.write(symbolTable.viewContent());
            System.out.println(ConsoleColors.PURPLE + "ST.out file updated with data." + ConsoleColors.RESET);
            output.close();
        }
        catch(Exception e) {
            e.getStackTrace();
        }
    }

    private void createProgramInternalFormOutputFile(){
        File file = new File(Path.of("W4/Lab3/output", "PIF" + ".out").toUri());
        try {
            boolean fileState = file.createNewFile();
            if (fileState) {
                System.out.println(ConsoleColors.PURPLE + "PIF.out file created." + ConsoleColors.RESET);
            }
            else {
                System.out.println(ConsoleColors.PURPLE + "PIF.out file already created." + ConsoleColors.RESET);
            }
            FileWriter output = new FileWriter(file);
            output.write(programInternalForm.viewContent());
            System.out.println(ConsoleColors.PURPLE + "PIF.out file updated with data." + ConsoleColors.RESET);
            output.close();
        }
        catch(Exception e) {
            e.getStackTrace();
        }
    }
}
