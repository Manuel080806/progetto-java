import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class Negozio {

    private String nome;
    private ArrayList<Cliente> clienti;
    private ArrayList<Ordine> ordini;
    private ArrayList<Prodotto> menu;
    private ArrayList<ProdottoUsato> magazzino;
    private double incasso;
    private double contanti;
    
    public Negozio(String nome) {
        this.nome = nome;
        clienti = loadClienti();
        menu = loadMenu();
        magazzino = loadMagazzino();
        ordini = new ArrayList<Ordine>();
        incasso = 0;
        contanti = 0;
        new Main_menu(this);
    }

    public String getNome() {
        return nome;
    }

    public ArrayList<Cliente> getClienti() {
        return clienti;
    }

    public ArrayList<Prodotto> getMenu() {
        return menu;
    }

    public void addProdottoMenu(Prodotto p) {
        if (p!=null) menu.add(p);
    }

    public void addProdottoMagazzino(Prodotto p, int quantita) {
        if (p!=null) magazzino.add(new ProdottoUsato(quantita, p));
    }

    public void addCliente(Cliente c) {
        if (c!=null) clienti.add(c);
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public double getIncasso() {
        return incasso;
    }

    public double getContanti() {
        return contanti;
    }

    public double getIncassoPos() {
        return incasso-contanti;
    }

    public void addIncasso(double soldi) {
        if (soldi > 0) incasso += soldi;
    }

    public void addContanti(double soldi) {
        if (soldi > 0) contanti += soldi;
    }

    public Ordine ordina(Cliente c, boolean asporto) {
        Ordine ordine = new Ordine(asporto, this);
        ordini.add(ordine);
        c.addOrdine(ordine);
        return ordine;
    }

    public ArrayList<ProdottoUsato> getMagazzino() {
        return magazzino;
    }

    public ArrayList<Ordine> getOrdini() {
        return ordini;
    }

    public static Prodotto getProdottoByNome(ArrayList<Prodotto> prodotti, String nome) {
        for (Prodotto prodotto : prodotti) if (prodotto.getNome().equals(nome)) return prodotto;
        return null;
    }

    public Cliente getClienteByCellulare(String cellulare) {
        for (Cliente cliente : clienti) if (cliente.getCellulare().equals(cellulare)) return cliente;
        return null;
    }

    private String fileToString(String path) {
        String result = "";
        File x = new File(path);
        try {
            Scanner sc = new Scanner(x);
            while (sc.hasNextLine()) {
                result += sc.nextLine();
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        }
        return result;
    }

    private ArrayList<Prodotto> loadMenu() {
        ArrayList<Prodotto> tempMenu = new ArrayList<Prodotto>();
        JSONArray prodotti = new JSONObject(fileToString("src/cadelMenu.json")).getJSONArray("menu");
        for (int i=0; i<prodotti.length(); i++) {
            JSONObject p = prodotti.getJSONObject(i);
            if (p.has("ingredienti")) {
                Composto composto = new Composto(p.getString("nome"), p.getBoolean("ordinabile"), p.getDouble("prezzo"));
                JSONArray ingredienti = p.getJSONArray("ingredienti");
                for (int j=0; j<ingredienti.length(); j++) {
                    Prodotto ingrediente = getProdottoByNome(tempMenu, ingredienti.getJSONObject(j).getString("nome"));
                    composto.addIngrediente(new ProdottoUsato(ingredienti.getJSONObject(j).getInt("quantita"), ingrediente));
                }
                tempMenu.add(composto);
            } else {
                tempMenu.add(new Elementare(p.getString("nome"), p.getBoolean("ordinabile"), p.getDouble("prezzo")));
            }
        }
        return tempMenu;
    }

    private ArrayList<Cliente> loadClienti() {
        ArrayList<Cliente> clienti = new ArrayList<Cliente>();
        JSONArray clientiJson = new JSONObject(fileToString("src/clienti.json")).getJSONArray("clienti");
        for (int i=0; i<clientiJson.length(); i++) {
            JSONObject c = clientiJson.getJSONObject(i);
            clienti.add(new Cliente(c.getString("nome"), c.getString("cellulare")));
        }
        return clienti;
    }

    private ArrayList<ProdottoUsato> loadMagazzino() {
        ArrayList<ProdottoUsato> magazzino = new ArrayList<ProdottoUsato>();
        JSONArray menu = new JSONObject(fileToString("src/cadelMenu.json")).getJSONArray("menu");
        for (int i=0; i<menu.length(); i++) {
            JSONObject p = menu.getJSONObject(i);
            if (p.has("disponibili"))
                magazzino.add(new ProdottoUsato(p.getInt("disponibili"), getProdottoByNome(this.menu, p.getString("nome"))));
        }
        return magazzino;
    }

    @Override
    public String toString() {
        return "Negozio [nome=" + nome + ", incasso=" + incasso + ", contanti=" + contanti + "]";
    }
    
}
