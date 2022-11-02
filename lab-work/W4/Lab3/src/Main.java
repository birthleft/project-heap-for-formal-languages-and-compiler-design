import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        var scanner = new LexicalAnalyzer();
        scanner.scanSourceCode(Path.of("W1/p1.txt"));
        scanner.scanSourceCode(Path.of("W1/p2.txt"));
        scanner.scanSourceCode(Path.of("W1/p3.txt"));
        scanner.scanSourceCode(Path.of("W1/p1err.txt"));
    }
}