package it.polito.tdp.extflightdelays;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.extflightdelays.model.Model;
import it.polito.tdp.extflightdelays.model.Vicini;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea txtResult;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private ComboBox<String> cmbBoxStati;

    @FXML
    private Button btnVisualizzaVelivoli;

    @FXML
    private TextField txtT;

    @FXML
    private TextField txtG;

    @FXML
    private Button btnSimula;

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtResult.clear();
    	this.model.creaGrafo();
    	this.txtResult.appendText("Grafo creato!\n");
    	this.cmbBoxStati.getItems().clear();
    	this.cmbBoxStati.getItems().addAll(this.model.getStates());
    }

    @FXML
    void doSimula(ActionEvent event) {

    	this.txtResult.clear();
    	txtResult.appendText("Simulazione in corso");
    	Integer T = 0;
    	Integer G = 0;
    	try {
    		T = Integer.parseInt(this.txtT.getText());
    		G = Integer.parseInt(this.txtG.getText());
    	} catch (NumberFormatException e) {
    		e.printStackTrace();
    		return;
    	}
    	
    	String state = this.cmbBoxStati.getValue();
    	if (state == null)
    		return;
    	if (T>0 && G>0)
    		this.model.init(state, T, G);
    	this.txtResult.clear();
    	txtResult.appendText(this.model.getResultSimulation());
    	
    }

    @FXML
    void doVisualizzaVelivoli(ActionEvent event) {

    	this.txtResult.clear();
    	String state = this.cmbBoxStati.getValue();
    	if (state == null)
    		return;
    	List <Vicini> result = this.model.visualizzaVeivoli(state);
    	String str = "Archi uscenti dallo stato selezionato:\n";
    	for (Vicini v : result) {
    		str += "State1: "+v.getState1()+"  State2: "+v.getState2()+"  Peso: "+v.getPeso()+"\n";
    	}
    	this.txtResult.appendText(str);
    	
    }

    @FXML
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert cmbBoxStati != null : "fx:id=\"cmbBoxStati\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnVisualizzaVelivoli != null : "fx:id=\"btnVisualizzaVelivoli\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert txtT != null : "fx:id=\"txtT\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert txtG != null : "fx:id=\"txtG\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
	}
}
