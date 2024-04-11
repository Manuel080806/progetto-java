import java.util.ArrayList;

public class Composto extends Prodotto {
    
    private ArrayList<ProdottoUsato> ingredienti;
    
    public Composto(String nome, boolean ordinabile, double prezzo) {
        super(nome, ordinabile, prezzo);
        ingredienti = new ArrayList<ProdottoUsato>();
    }

    public ArrayList<ProdottoUsato> getIngredienti() {
        return ingredienti;
    }

    public void setIngredienti(ArrayList<ProdottoUsato> ingredienti) {
        this.ingredienti = ingredienti;
    }

    public void addIngrediente(ProdottoUsato p) {
        if (p!=null) ingredienti.add(p);
    }

    public int getTipo() {
        return 1;
    }

    @Override
    public String toString() {
        return "\n\tComposto [nome=" + nome + ", ordinabile=" + ordinabile + ", prezzo=" + prezzo + ", ingredienti=" + ingredienti + "]";
    }

}
