// 1a)
public class Main {
    public static void main(String[] args) {
        var table = new HashTable(30);
        table.put("a", "a");
        table.viewContent();
        table.put("b", "b");
        table.viewContent();
        table.put("ac", "ac");
        table.viewContent();
        table.put("bb", "bb");
        table.viewContent();
        System.out.println(table.get("bb"));
        System.out.println(table.get("ac"));
        System.out.println(table.get("a"));
        System.out.println(table.get("aa"));
        System.out.println(table.get("cc"));
        System.out.println(table.get("cc"));
    }
}