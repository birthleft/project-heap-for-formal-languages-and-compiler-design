import java.util.AbstractMap;
import java.util.ArrayList;

// The Program Internal Form is an ordered ArrayList of Map.Entries,
// containing the Token and the Token's Position in the Symbol Table.
public class ProgramInternalForm {
    private final ArrayList<AbstractMap.SimpleImmutableEntry<String, Integer>> list;

    public ProgramInternalForm() {
        this.list = new ArrayList<>();
    }

    public void generate(String token, Integer position){
        list.add(new AbstractMap.SimpleImmutableEntry<>(token, position));
    }

    public String viewContent() {
        StringBuilder content = new StringBuilder();
        content.append("\n");
        for(AbstractMap.SimpleImmutableEntry<String, Integer> item: list){
            content.append(item.getKey())
                    .append(" => ")
                    .append(item.getValue())
                    .append("\n");;
        }
        return content.toString();
    }
}
