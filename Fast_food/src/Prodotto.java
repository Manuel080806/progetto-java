public abstract class Prodotto {
    
    protected String nome;
    protected boolean ordinabile;
    protected double prezzo;
    
    public Prodotto(String nome, boolean ordinabile, double prezzo) {
        this.nome = nome;
        this.ordinabile = ordinabile;
        this.prezzo = prezzo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isOrdinabile() {
        return ordinabile;
    }

    public void setOrdinabile(boolean ordinabile) {
        this.ordinabile = ordinabile;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    @Override
    public String toString() {
        return "Prodotto [nome=" + nome + ", ordinabile=" + ordinabile + ", prezzo=" + prezzo + "]";
    }

    public int getTipo() {
        return -1;
    }

}
