public class Main {
    public static void main(String[] args) {
        FiniteAutomaton fa = new FiniteAutomaton("W5/Lab4/input/FA.in");
        Language sequence = new Language("aabb");
        System.out.println(sequence.isAccepted(fa));
        System.out.println(sequence.show());
        System.out.println(sequence.showDetailed());
        fa.showUI();
    }
}
