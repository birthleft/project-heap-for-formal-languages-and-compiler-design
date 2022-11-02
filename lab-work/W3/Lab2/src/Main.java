// 1a)
public class Main {
    public static void main(String[] args) {
        var table = new SymbolTable();

        table.put("a");
        table.put("ac");
        table.put("bb");
        table.put(3);

        table.printInternalContent();
        System.out.println(table.viewContent());
        System.out.println();

        System.out.println("\"a\" is at position " + table.getKey("a"));
        System.out.println("\"ac\" is at position " + table.getKey("ac"));
        System.out.println("\"bb\" is at position " + table.getKey("bb"));
        System.out.println("3 is at position " + table.getKey(3));

        System.out.println("\"aa\" is at position " + table.getKey("aa"));
        System.out.println("\"cc\" is at position " + table.getKey("cc"));
        System.out.println("\"c\" is at position " + table.getKey("c"));
        System.out.println("4 is at position " + table.getKey(4));
    }
}