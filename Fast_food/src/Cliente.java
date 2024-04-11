import java.util.ArrayList;

public class Cliente {
    
    private String nome;
    private String cellulare;
    private ArrayList<Ordine> ordini;

    public Cliente(String nome, String cellulare) {
        this.nome = nome;
        this.cellulare = cellulare;
        ordini = new ArrayList<Ordine>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCellulare() {
        return cellulare;
    }

    public void setCellulare(String cellulare) {
        this.cellulare = cellulare;
    }

    public void addOrdine(Ordine o) {
        if (o!=null) ordini.add(o);
    }

    public ArrayList<Ordine> getOrdini() {
        return ordini;
    }

    @Override
    public String toString() {
        return "Cliente [nome=" + nome + ", cellulare=" + cellulare + ", ordini=" + ordini + "]";
    }

}
