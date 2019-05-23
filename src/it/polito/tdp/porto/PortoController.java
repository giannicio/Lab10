package it.polito.tdp.porto;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import org.jgrapht.Graphs;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.Model;
import it.polito.tdp.porto.model.Paper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class PortoController {
	
	Model model; 
	
    public void setModel(Model model) {
		this.model = model;
		List<Author> authorList = model.getAuthorList();
		boxPrimo.getItems().addAll(authorList);
		boxSecondo.getItems().addAll(authorList);
	}

	@FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Author> boxPrimo;

    @FXML
    private ComboBox<Author> boxSecondo;

    @FXML
    private TextArea txtResult;

    @FXML
    void handleCoautori(ActionEvent event) {
    	// UTENTE MI DA UN AUTORE IN INPUT TRAMITE IL SUO NOME
    	txtResult.clear();
    	Author autoreScelto = boxPrimo.getValue();
    	List<Author> coAutori = model.trovaVicini(autoreScelto);
    	System.out.println(coAutori);
    	txtResult.appendText("COAUTORI DI "+autoreScelto+"\n");
    	for(Author a: coAutori) {
    		txtResult.appendText(a+"\n");
    	}
    }

    @FXML
    void handleSequenza(ActionEvent event) {
    	txtResult.clear();
    	Author autoreSource = boxPrimo.getValue();
    	Author autoreTarget = boxSecondo.getValue();
    	if((model.sonoCoautori(autoreSource, autoreTarget)) == true) {
    		txtResult.setText("Sono coautori, impossibile trovare sequenza!");
    		return;
    	}
    	List<Author> cammino = model.trovaCamminoMinimo(autoreSource, autoreTarget);
    	List<Paper> papers = model.trovaSequenzaArticoli(cammino);
    	if(cammino == null) {
    		txtResult.setText("Non esiste cammino tra i due autori!");
    		return;
    	}
    	if(papers == null) {
    		txtResult.setText("Non esiste sequenza!");
    		return;
    	}
    	txtResult.appendText("SEQUENZA DI ARTICOLI TRA "+autoreSource+"E "+autoreTarget+" :\n");
    	for(Paper p: papers) {
    		txtResult.appendText(p+ "\n");
    	}
    }

    @FXML
    void initialize() {
        assert boxPrimo != null : "fx:id=\"boxPrimo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert boxSecondo != null : "fx:id=\"boxSecondo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Porto.fxml'.";

    }
}
