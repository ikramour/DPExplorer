package derby.entity;

import java.util.ArrayList;
import java.util.List;

import derby.Persistable;


public class Rue implements Persistable{
	private long id;
	private String nom;
	
	private List<Batiment> batiments= new ArrayList<>();
	private List<Ville> villes= new ArrayList<>();
	
	public Rue() {
	}
	
	public Rue(String nomRue) {
		nom = nomRue;
	}
	public Rue(String nom, List<Batiment> batiments, List<Ville> villes) {
		super();
		this.nom = nom;
		this.batiments = batiments;
		this.villes = villes;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public List<Batiment> getBatiments() {
		return batiments;
	}
	public void setBatiments(List<Batiment> batiments) {
		this.batiments = batiments;
	}
	public List<Ville> getVilles() {
		return villes;
	}
	public void setVilles(List<Ville> villes) {
		this.villes = villes;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "Rue [nom=" + nom + ", batiments=" + batiments + ", villes=" + villes + "]";
	}
	
	

}
