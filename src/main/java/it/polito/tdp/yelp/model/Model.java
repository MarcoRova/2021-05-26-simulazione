package it.polito.tdp.yelp.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	
	private YelpDao dao;
	private Graph<Business, DefaultWeightedEdge> grafo;
	private Map<String, Business> businessIdMap;
	
	public Model() {
		super();
		this.dao = new YelpDao();
	}
	
	public void creaGrafo(String city, Integer anno) {
		
		this.grafo = new SimpleWeightedGraph<Business, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		this.businessIdMap = new HashMap<>();
		
		loadIdMap();
		
		Graphs.addAllVertices(this.grafo, this.dao.getVertici(city, anno));
		
		aggiungiArchi(city, anno);
	}
	
	public String getInfoGrafo() {
		return "Numero vertici: "+this.grafo.vertexSet().size() + " Numero archi: "+this.grafo.edgeSet().size();
	}
	
	public List<String>getAllCities() {
		return this.dao.getAllCities();
	}
	
	public Graph<Business, DefaultWeightedEdge> getGrafo(){
		return this.grafo;
	}
	
	public void loadIdMap(){
		
		List<Business> business = this.dao.getAllBusiness();
		for(Business b : business) {
			this.businessIdMap.put(b.getBusinessId(), b);
		}
	}
	
	public void aggiungiArchi(String city, Integer anno) {
		
		List<Arco> archi = this.dao.getArco(city, anno);
		
		for(Arco a1 : archi) {
			for(Arco a2 : archi) {
				if(this.grafo.containsVertex(this.businessIdMap.get(a1.getbID())) 
						&& this.grafo.containsVertex(this.businessIdMap.get(a2.getbID()))
						&& !this.grafo.containsEdge(this.businessIdMap.get(a1.getbID()), this.businessIdMap.get(a2.getbID()))
						&& !this.grafo.containsEdge(this.businessIdMap.get(a2.getbID()), this.businessIdMap.get(a1.getbID()))){
				
					if(a1.getAvg() < a2.getAvg()) {
						Graphs.addEdgeWithVertices(this.grafo, this.businessIdMap.get(a1.getbID()), this.businessIdMap.get(a2.getbID()), a2.getAvg()-a1.getAvg());
					}
					if(a1.getAvg() > a2.getAvg()) {
						Graphs.addEdgeWithVertices(this.grafo, this.businessIdMap.get(a2.getbID()), this.businessIdMap.get(a1.getbID()), a1.getAvg()-a2.getAvg());
					}
				}	
			}
		}
		
	}
	
	public Business trovaMigliore() {
		
		double best = 0.0;
		Business bBest = null;
		
		for(Business b : this.grafo.vertexSet()) {
			
			double val = 0.0;
			
			for(DefaultWeightedEdge d : this.grafo.incomingEdgesOf(b)) {
				val += this.grafo.getEdgeWeight(d);
			}
			
			for(DefaultWeightedEdge d : this.grafo.outgoingEdgesOf(b)) {
				val -= this.grafo.getEdgeWeight(d);
			}
			
			if(val > best) {
				best = val;
				bBest = b;
			}
		}
		
		return bBest;	
	}
	
	
}
 