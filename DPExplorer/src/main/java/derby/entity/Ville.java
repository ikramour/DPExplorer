package derby.entity;

import java.util.ArrayList;
import java.util.List;

import derby.Persistable;

public class Ville implements Persistable{
	private long id;
	private String nom;
	
	private List<Batiment> batiments= new ArrayList<>();
	private List<Rue> rues= new ArrayList<>();
	private List<CodePostal> codePostal= new ArrayList<>();
	
	public Ville() {	
	}

	public Ville(String nomVille) {
		nom = nomVille;		
	}

	public Ville(String nom, List<Batiment> batiments, List<Rue> rues, List<CodePostal> codePostal) {
		super();
		this.nom = nom;
		this.batiments = batiments;
		this.rues = rues;
		this.codePostal = codePostal;
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

	public List<Rue> getRues() {
		return rues;
	}

	public void setRues(List<Rue> rues) {
		this.rues = rues;
	}

	public List<CodePostal> getCodePostal() {
		return codePostal;
	}

	public void setCodePostal(List<CodePostal> codePostal) {
		this.codePostal = codePostal;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Ville [nom=" + nom + ", batiments=" + batiments + ", rues=" + rues + ", codePostal=" + codePostal + "]";
	}
	

}
