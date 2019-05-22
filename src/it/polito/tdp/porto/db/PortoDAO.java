package it.polito.tdp.porto.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.CoAuthors;
import it.polito.tdp.porto.model.Paper;

public class PortoDAO {

	/*
	 * Dato l'id ottengo l'autore.
	 */
	public Author getAutore(int id) {

		final String sql = "SELECT * FROM author where id=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, id);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {

				Author autore = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				return autore;
			}
			conn.close();
			return null;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

	/*
	 * Dato l'id ottengo l'articolo.
	 */
	public Paper getArticolo(int eprintid) {

		final String sql = "SELECT * FROM paper where eprintid = ? ";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, eprintid);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				Paper paper = new Paper(rs.getInt("eprintid"), rs.getString("title"), rs.getString("issn"),
						rs.getString("publication"), rs.getString("type"), rs.getString("types"));
				return paper;
			}
			conn.close();
			return null;

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	
	// METODO CHE MI RESTITUISCE TUTTI GLI AUTORI E CREA IDMAP
	public List<Author> getAllAutori(HashMap<Integer, Author> idMap) {
		List<Author> authorsList = new ArrayList<Author>();
		final String sql = "SELECT * FROM author";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Author autore = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				authorsList.add(autore);
				if(!idMap.containsKey((rs.getInt("id")))) {
					idMap.put(rs.getInt("id"), autore);
				}
			}
			conn.close();
			return authorsList;

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	
	
	public List<CoAuthors> getCoauthors() {
		List<CoAuthors> coList = new ArrayList<>();
		final String sql = "SELECT c1.authorid, c2.authorid " + 
				"FROM creator c1, creator c2 " + 
				"WHERE c1.eprintid = c2.eprintid " + 
				"AND c1.authorid > c2.authorid " + 
				"GROUP BY c1.authorid, c2.authorid ";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				CoAuthors c = new CoAuthors(rs.getInt("c1.authorid"), rs.getInt("c2.authorid"));
				coList.add(c);
			}
			conn.close();
			return coList;

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	
	public int trovaArticoloCondivisoTra(Author a1, Author a2) {
		final String sql = "SELECT c1.eprintid " + 
				"FROM creator AS c1, creator AS c2 " + 
				"WHERE c1.eprintid = c2.eprintid " + 
				"AND c1.authorid = ? " + 
				"AND c2.authorid = ? ";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setInt(1,a1.getId());
			st.setInt(2,a2.getId());
			
			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				int paperId = rs.getInt("c1.eprintid");
				return paperId;
			}
			conn.close();
			return 0;
			

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
}