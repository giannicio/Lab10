package it.polito.tdp.porto.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.porto.db.PortoDAO;

public class Model {
	
	Graph<Author, DefaultEdge> grafo;
	PortoDAO dao;
	HashMap<Integer, Author> idMap;
	
	public Model() {
		grafo = new SimpleGraph<Author, DefaultEdge>(DefaultEdge.class);
		dao = new PortoDAO();
		idMap = new HashMap<Integer, Author>();
		dao.getAllAutori(idMap);
	}
	
	public void creaGrafo() {
		List<Author> aList = dao.getAllAutori(idMap);
		Graphs.addAllVertices(grafo, aList);
		List<CoAuthors> coList = dao.getCoauthors();
		for(CoAuthors c: coList) {
			grafo.addEdge(idMap.get(c.getA1()), idMap.get(c.getA2()));
		}
		System.out.println("Numero vertici: " +grafo.vertexSet().size());
		System.out.println("Numero archi: " +grafo.edgeSet().size());
		// System.out.println(Graphs.neighborListOf(grafo, idMap.get(579)));
		
		List<Author> camminoMinimo = trovaCamminoMinimo(idMap.get(579), idMap.get(4620));
		System.out.println(camminoMinimo);
		List<Paper> paperList = trovaSequenzaArticoli(camminoMinimo);
		System.out.println(paperList);
		
		
	}
	
	public List<Author> trovaCamminoMinimo(Author partenza, Author arrivo) {
		DijkstraShortestPath<Author, DefaultEdge> dijstra = new DijkstraShortestPath<>(this.grafo) ;
		GraphPath<Author, DefaultEdge> path = dijstra.getPath(partenza, arrivo);
		return path.getVertexList() ;
	}

	public List<Paper> trovaSequenzaArticoli(List<Author> camminoMinimo) {
		List<Integer> idList = new ArrayList<>();
		List<Paper> paperList = new ArrayList<>();
		
		for(int i=0; i<camminoMinimo.size() - 1; i++) {
			// MI PRENDO L'ID DI UN ARTICOLO CONDIVISO TRA DUE AUTORI CONSECUTIVI E LO AGGIUNGO A UNA LISTA DI ID 
			int paperId = dao.trovaArticoloCondivisoTra(camminoMinimo.get(i), camminoMinimo.get(i+1));
			idList.add(paperId);
		}
		
		for(Integer id: idList) {
			paperList.add(dao.getArticolo(id));
		}
		
		return paperList;
	}
}
