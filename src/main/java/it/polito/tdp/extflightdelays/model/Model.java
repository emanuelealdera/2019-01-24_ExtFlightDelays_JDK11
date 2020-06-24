package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	
	private Graph <String, DefaultWeightedEdge> graph;
	private List <Vicini> vicini;
	private List<String> stati;
	
	//parametri di simulazione
	private Integer T = 1000;
	private Integer G = 10;
	//ad ogni turista, identificato con un codice univoco, corrisponde lo stato in cui si trova
	private Map<Integer, String> people;
	private Map<String, Integer> states;

	public void creaGrafo() {
		ExtFlightDelaysDAO dao = new ExtFlightDelaysDAO();
		this.graph = new DefaultDirectedWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		this.stati = new ArrayList<> (dao.loadAllStates());
		Graphs.addAllVertices(this.graph, this.stati);
		this.vicini = dao.getVicini();
		for (Vicini v : vicini) {
			Graphs.addEdge(this.graph, v.getState1(), v.getState2(), v.getPeso());
		}
		System.out.println(this.graph.vertexSet().size()+"  "+this.graph.edgeSet().size());
	}
	
	public List<String> getStates () {
		List <String> result = new ArrayList<>(this.stati);
		Collections.sort(result);
		return result;
	}
	
	public List<Vicini> visualizzaVeivoli (String state) {
		List <Vicini> result = new ArrayList<>();
		for (String s : Graphs.successorListOf(this.graph, state)) {
			result.add(new Vicini (state, s, (int) this.graph.getEdgeWeight(this.graph.getEdge(state, s))));
		}
		Collections.sort(result);
		return result;
	}
	
	public void init(String start, Integer T, Integer G) {
		this.T = T;
		this.G = G;
		this.people = new HashMap<>();
		this.states = new HashMap<>();
		for (int i=1; i<=T; i++) {
			people.put(i, start);
		}
		for (String s : this.stati) {
			this.states.put(s, 0);
			if (start.equals(s))
				this.states.put(s, T);
		}
		simula();
	}
	
	private void simula() {
		
		int giorno = 1;
		
		while (giorno<this.G) {
			
			for (Integer turist : this.people.keySet()) {
				
				String attuale = this.people.get(turist);
				List <Vicini> vicini = visualizzaVeivoli(attuale);
				Collections.sort(vicini);
				int cont = 0;
				for (Vicini v : vicini) {
					if (Math.random() < (v.getPeso()/this.getSommaArchiUscenti(attuale).doubleValue())) {
						people.put(turist, v.getState2());
						states.put(v.getState1(), states.get(v.getState1()) -1);
						states.put(v.getState2(), states.get(v.getState2()) +1);
						break;
					} else {
						cont+=v.getPeso();
					}
					
				}
				
			}
			giorno++;
		}
	}
	
	public String getResultSimulation () {
		String str= "";
		for (String state : this.states.keySet()) {
			str += state +":  "+states.get(state)+" people\n";
		}
		return str;
	}
 	
	private Integer getSommaArchiUscenti(String state) {
		int cont = 0;
		for (String s : Graphs.successorListOf(this.graph, state)) {
			cont += this.graph.getEdgeWeight(this.graph.getEdge(state, s));
		}
		return cont;
	}
}
