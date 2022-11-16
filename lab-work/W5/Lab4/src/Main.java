public class Main {
    public static void main(String[] args) {
        FiniteAutomaton fa = new FiniteAutomaton("W5/Lab4/input/FA.in");
        Language sequence1 = new Language("aab");
        Language sequence2 = new Language("aabb");
        System.out.println(sequence1.isAccepted(fa));
        System.out.println(sequence1.showDetailed());
        System.out.println(sequence2.isAccepted(fa));
        System.out.println(sequence2.showDetailed());
        fa.showUI();
    }
}
