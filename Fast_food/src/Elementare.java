public class Elementare extends Prodotto {

    public Elementare(String nome, boolean ordinabile, double prezzo) {
        super(nome, ordinabile, prezzo);
    }

    public int getTipo() {
        return 0;
    }

    @Override
    public String toString() {
        return "\n\tElementare [nome=" + nome + ", ordinabile=" + ordinabile + ", prezzo=" + prezzo + "]";
    }

}
