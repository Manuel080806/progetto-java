public class ProdottoUsato {
    
    private int quantita;
    private Prodotto prodotto;
    
    public ProdottoUsato(int quantita, Prodotto prodotto) {
        this.quantita = quantita;
        this.prodotto = prodotto;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public Prodotto getProdotto() {
        return prodotto;
    }

    @Override
    public String toString() {
        return "\nProdottoUsato [quantita=" + quantita + ", prodotto=" + prodotto + "]";
    }

}
