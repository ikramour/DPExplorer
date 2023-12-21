package derby.entity;

import java.util.ArrayList;
import java.util.List;


import derby.Persistable;

public class CodePostal implements Persistable{

	private long id;
	private int valeur;
	
	private List<Batiment> batiments= new ArrayList<>();
	private List<Ville> villes= new ArrayList<>();

	
	public CodePostal() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public CodePostal(int cPostal) {
		valeur = cPostal;
	}

	public CodePostal(int valeur, List<Batiment> batiments, List<Ville> villes) {
		super();
		this.valeur = valeur;
		this.batiments = batiments;
		this.villes = villes;
	}
	public long getValeur() {
		return valeur;
	}
	public void setValeur(int valeur) {
		this.valeur = valeur;
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
		return "CodePostal [valeur=" + valeur + ", batiments=" + batiments + ", villes=" + villes + "]";
	}
	
	
}
