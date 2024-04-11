import java.time.LocalDate;
import java.util.ArrayList;

public class Ordine {
    
    private ArrayList<ProdottoUsato> prodotti;
    private double prezzoTotale;
    private LocalDate data;
    private boolean asporto;
    private boolean servito;
    private boolean pagato;
    private Negozio negozio;

    public Ordine(LocalDate data, boolean asporto, Negozio negozio) {
        prezzoTotale = 0;
        this.data = data;
        this.asporto = asporto;
        prodotti = new ArrayList<ProdottoUsato>();
        servito = false;
        pagato = false;
        this.negozio = negozio;
    }

    public Ordine(boolean asporto, Negozio negozio) {
        prezzoTotale = 0;
        data = LocalDate.now();
        this.asporto = asporto;
        prodotti = new ArrayList<ProdottoUsato>();
        servito = false;
        pagato = false;
        this.negozio = negozio;
    }


    public ArrayList<ProdottoUsato> getProdotti() {
        return prodotti;
    }

    public LocalDate getData() {
        return data;
    }

    public double getPrezzoTotale() {
        return prezzoTotale;
    }

    public void setPrezzoTotale(double prezzoTotale) {
        this.prezzoTotale = prezzoTotale;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public boolean isAsporto() {
        return asporto;
    }

    public void setAsporto(boolean asporto) {
        this.asporto = asporto;
    }

    public boolean isServito() {
        return servito;
    }

    public void setServito(boolean servito) {
        this.servito = servito;
    }

    public Negozio getNegozio() {
        return negozio;
    }

    public void calcolaPrezzo() {
        this.prezzoTotale = 0;
        for (ProdottoUsato prodottoUsato : prodotti) {
            prezzoTotale += prodottoUsato.getProdotto().getPrezzo()*prodottoUsato.getQuantita();
        }
    }

    public void pagaOrdine(String tipoPagamento) {
        if (!pagato) {
            if (tipoPagamento.equals("contanti")) getNegozio().addContanti(prezzoTotale);
            getNegozio().addIncasso(prezzoTotale);
            pagato = true;
        }
    }

    public void serviOrdine() {
        servito = true;
    }

    public void rimuoviProdotto(Prodotto prodotto, int quantita) {
        if (!servito && prodotto!=null) {
            if (prodotto.getTipo()==0) { //Elementare
                for (ProdottoUsato puMag : negozio.getMagazzino()) {
                    if (prodotto==puMag.getProdotto()) puMag.setQuantita(puMag.getQuantita()+quantita);
                }
            } else { // Composto con ricorsione
                for (ProdottoUsato ingrediente : ((Composto)prodotto).getIngredienti()) {
                    rimuoviProdotto(ingrediente.getProdotto(), quantita*ingrediente.getQuantita());                   
                }                
            }
        }
    }

    public boolean verificaMagazzinoProdotto(Prodotto prodotto, int quantita) {
        boolean ok = true;
        if (prodotto.getTipo()==0) { //Elementare
            for (ProdottoUsato puMag : negozio.getMagazzino())
                if (prodotto==puMag.getProdotto())
                    if (puMag.getQuantita()<quantita)
                        return false;
        } else { // Composto con ricorsione
            for (ProdottoUsato ingrediente : ((Composto)prodotto).getIngredienti()) {
                if (!verificaMagazzinoProdotto(ingrediente.getProdotto(), quantita*ingrediente.getQuantita()))
                    ok = false;
            }
        }
        return ok;
    }

    public void applicaMagazzinoProdotto(Prodotto prodotto, int quantita) {
        int qta = 0;
        if (prodotto.getTipo()==0) { //Elementare
            for (ProdottoUsato puMag : negozio.getMagazzino()) {
                if (prodotto==puMag.getProdotto())
                    if (puMag.getQuantita()>=quantita)  
                    {
                        qta = Math.min(puMag.getQuantita(), quantita); // qta che posso ordinare
                        qta = quantita;
                        puMag.setQuantita(puMag.getQuantita()-qta);  // Aggiorno il magazzino
                    }
            }
        } else { // Composto con ricorsione
            qta = quantita;
            for (ProdottoUsato ingrediente : ((Composto)prodotto).getIngredienti()) {
                applicaMagazzinoProdotto(ingrediente.getProdotto(), quantita*ingrediente.getQuantita());                   
            }                
        }
    }
 
    public void aggiungiProdotto(Prodotto prodotto, int quantita) {
        if(!servito && prodotto != null){
            // Aggiorno il magazzino e determino la quantità corretta
            if(verificaMagazzinoProdotto(prodotto,quantita)) {
                applicaMagazzinoProdotto(prodotto,quantita);
                // Ciclo i prodotti attuali dell'ordine perché se il prodotto è già nell'ordine aggiorno solo la quantità
                for (ProdottoUsato p : prodotti) {
                    if (p.getProdotto()==prodotto) {
                        p.setQuantita(p.getQuantita()+quantita);
                        calcolaPrezzo();
                        return;
                    }
                }
                // Se il prodotto non era già presente lo aggiungo da zero
                prodotti.add(new ProdottoUsato(quantita, prodotto));
                // Ricalcolo il prezzo
                calcolaPrezzo();
            }
        }
    }

    @Override
    public String toString() {
        return "Ordine [prodotti=" + prodotti + ", prezzoTotale=" + prezzoTotale + ", data=" + data + ", asporto="
                + asporto + ", servito=" + servito + ", pagato=" + pagato + ", negozio=" + negozio + "]";
    }

}
