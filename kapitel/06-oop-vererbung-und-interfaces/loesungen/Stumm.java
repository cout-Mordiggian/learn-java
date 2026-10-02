/**
 * Kapitel 6 - Musterloesung: keinen Laut von sich geben.
 */
public class Stumm implements QuakVerhalten {

    @Override
    public String quaken() {
        return "<< Stille >>";
    }
}
