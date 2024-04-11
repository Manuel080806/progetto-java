import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class Main_menu extends JFrame {
    
    private Cliente currentCliente;
    private Ordine currentOrdine;
    private Negozio negozio;

    private JPanel topPanel, topPanel1, topPanel2, topPanel3, mainPanel, sidePanel, productPanel, checkoutPanel;
    private JLabel menuTitle, orderTitle, clienteLabel, searchLabel, totLabel;
    private JButton searchButton, resetButton, payOrderButton;
    private JTextField searchField;
    private DefaultListModel<String> prodottiOrdine;
    private ButtonGroup modOrdine;
    private JRadioButton asporto, mangiaQui;
    private JComboBox<String> payMethodBox;

    public Main_menu(Negozio negozio) {
        super();
        this.negozio = negozio;
        this.currentCliente = null;
        this.currentOrdine = null;

        // Configurazione iniziale finestra
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(true);
        this.setTitle("Gestione ordini");

        Border b = BorderFactory.createLineBorder(new Color(0), 1);
        AscoltaBottone ascoltaBottone = new AscoltaBottone();

        topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 30));
        topPanel.setBorder(b);
        topPanel1 = new JPanel();
        topPanel2 = new JPanel();
        topPanel3 = new JPanel();

        mainPanel = new JPanel(new BorderLayout());

        sidePanel = new JPanel(new BorderLayout());
        sidePanel.setBorder(b);

        resetButton = new JButton("Scarta ordine");
        resetButton.addActionListener(ascoltaBottone);

        productPanel = new JPanel(new GridLayout(2, 1, 30, 30));
        productPanel.setBorder(BorderFactory.createCompoundBorder(b, BorderFactory.createEmptyBorder(120, 60, 120, 60)));
        productPanel.setBackground(new Color(0xbbeeee));

        checkoutPanel = new JPanel();

        payMethodBox = new JComboBox<String>(new String[]{"Pagamento in contanti", "Pagamento con carta"});

        payOrderButton = new JButton("Paga ordine");
        payOrderButton.addActionListener(ascoltaBottone);

        totLabel = new JLabel("Totale: 0.00€", JLabel.CENTER);

        clienteLabel = new JLabel("Cliente (Telefono)");
        clienteLabel.setFont(new Font("Arial", Font.BOLD, 20));

        searchLabel = new JLabel("Cerca cliente (numero cellulare): ");
        searchLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        searchField = new JTextField(12);
        searchField.setFont(new Font("Lucida Console", Font.PLAIN, 20));
        searchField.addKeyListener(new AscoltaField());
        searchButton = new JButton("Cerca e crea ordine");
        searchButton.addActionListener(ascoltaBottone);

        menuTitle = new JLabel("MENU' MC CADEL", JLabel.CENTER);
        menuTitle.setBorder(BorderFactory.createCompoundBorder(b, BorderFactory.createEmptyBorder(20, 0, 20, 0)));
        menuTitle.setFont(new Font("Arial", Font.BOLD, 32));

        orderTitle = new JLabel("ORDINE ATTUALE", JLabel.CENTER);
        orderTitle.setBorder(BorderFactory.createEmptyBorder(10, 80, 10, 80));
        orderTitle.setFont(new Font("Arial", Font.BOLD, 20));

        prodottiOrdine = new DefaultListModel<String>();

        modOrdine = new ButtonGroup();
        asporto = new JRadioButton("Asporto");
        mangiaQui = new JRadioButton("Mangia qui");
        modOrdine.add(asporto);
        modOrdine.add(mangiaQui);
        mangiaQui.setSelected(true);

        this.add(topPanel, BorderLayout.NORTH);
        this.add(mainPanel, BorderLayout.CENTER);
        this.add(sidePanel, BorderLayout.EAST);

        topPanel.add(topPanel1);
        topPanel.add(topPanel3);
        topPanel.add(topPanel2);

        topPanel1.add(clienteLabel);
        topPanel3.add(mangiaQui);
        topPanel3.add(asporto);
        topPanel2.add(searchLabel);
        topPanel2.add(searchField);
        topPanel2.add(searchButton);

        mainPanel.add(menuTitle, BorderLayout.NORTH);
        mainPanel.add(productPanel);

        sidePanel.add(orderTitle, BorderLayout.NORTH);
        JList<String> productList = new JList<String>(prodottiOrdine);
        productList.setFont(new Font("Lucida Console", Font.BOLD, 13));
        productList.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 0));
        sidePanel.add(new JScrollPane(productList), BorderLayout.CENTER);
        sidePanel.add(checkoutPanel, BorderLayout.SOUTH);
        checkoutPanel.add(totLabel);
        checkoutPanel.add(resetButton);
        checkoutPanel.add(payMethodBox);
        checkoutPanel.add(payOrderButton);

        //Caricamento dei prodotti nel menu grafico
        Font buttonFont = new Font("Arial", Font.BOLD, 20);
        JButton tempButton;
        for (Prodotto p: negozio.getMenu()) {
            if (p.isOrdinabile()) {
                tempButton = new JButton(p.getNome());
                tempButton.setFont(buttonFont);
                productPanel.add(tempButton);
                tempButton.addActionListener(ascoltaBottone);
            }
        }

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

    public class AscoltaBottone implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource()==searchButton) {
                if (searchField.getText().length()==10) {
                    currentCliente = negozio.getClienteByCellulare(searchField.getText());
                    if (currentCliente!=null) {
                        clienteLabel.setText(currentCliente.getNome()+" ("+currentCliente.getCellulare()+")");
                        if (currentOrdine != null) for (ProdottoUsato pu : currentOrdine.getProdotti()) {
                            currentOrdine.rimuoviProdotto(pu.getProdotto(), pu.getQuantita());
                        }
                        prodottiOrdine.clear();
                        currentOrdine = new Ordine(asporto.isSelected(), negozio);
                        JOptionPane.showMessageDialog(Main_menu.this, "L'ordine e' stato avviato con successo", "Successo", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(Main_menu.this, "Cliente non trovato", "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else if (e.getSource()==resetButton) {
                if (currentOrdine!=null) {
                    for (ProdottoUsato pu : currentOrdine.getProdotti()) {
                        currentOrdine.rimuoviProdotto(pu.getProdotto(), pu.getQuantita());
                    }
                    prodottiOrdine.clear();
                    currentOrdine = null;
                    totLabel.setText("Totale: 0.00€");
                }
            } else if (e.getSource()==payOrderButton) {
                if (currentOrdine!=null) {
                    currentOrdine.setAsporto(asporto.isSelected());
                    currentCliente.addOrdine(currentOrdine);
                    currentOrdine.pagaOrdine(((String)(payMethodBox.getSelectedItem())).endsWith("contanti")?"contanti":"altro");
                    currentOrdine.serviOrdine();
                    System.out.println(currentOrdine);
                    JOptionPane.showMessageDialog(Main_menu.this, "L'ordine e' stato pagato con successo", "Successo", JOptionPane.INFORMATION_MESSAGE);
                    if (currentOrdine!=null) {
                        for (ProdottoUsato pu : currentOrdine.getProdotti()) {
                            currentOrdine.rimuoviProdotto(pu.getProdotto(), pu.getQuantita());
                        }
                        prodottiOrdine.clear();
                        currentOrdine = null;
                    }
                }
            } else {
                if (currentOrdine!=null && currentCliente!=null) {
                    Prodotto p = Negozio.getProdottoByNome(negozio.getMenu(), ((JButton)e.getSource()).getText());
                    if (currentOrdine.verificaMagazzinoProdotto(p, 1)) {
                        boolean nuovo = true;
                        for (int i=0; i<prodottiOrdine.size(); i++) {
                            if (prodottiOrdine.get(i).endsWith(p.getNome())) {
                                for (ProdottoUsato pu : currentOrdine.getProdotti()) {
                                    if (pu.getProdotto()==p) {
                                        prodottiOrdine.setElementAt((pu.getQuantita()+1)+"x "+p.getNome(), i);
                                        nuovo = false;
                                        break;
                                    }
                                }
                            }
                        }
                        if (nuovo) prodottiOrdine.addElement("1x "+p.getNome());
                    }
                    currentOrdine.aggiungiProdotto(p, 1);
                    totLabel.setText("Totale: "+String.format("%.2f", currentOrdine.getPrezzoTotale())+"€");
                }
            }
        }

    }

    public class AscoltaField implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
            String s = ""+e.getKeyChar();
            if (!"1234567890".contains(s) || ((JTextField)e.getSource()).getText().length()>=10) e.consume();
        }

        @Override
        public void keyPressed(KeyEvent e) {
            
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
        
    }

    public Negozio getNegozio() {
        return negozio;
    }

}
