package derby.entity;

import derby.Persistable;

public class Batiment implements Persistable {

	private long id;
	private String numero;
	private String anneeConstruction;
	
	private Rue rue;
	private CodePostal codepostal;
	private Ville ville;
	private Diagnostic diagnostic;

	public Batiment() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public Batiment(String numVoie, String anneeConstruction, Rue nomRue, CodePostal cPostal, Ville nomVille,
			Diagnostic diagnostic) {
		this.numero = numVoie;
		this.anneeConstruction = anneeConstruction;
		this.rue = nomRue;
		this.codepostal = cPostal;
		this.ville = nomVille;
		this.diagnostic = diagnostic;
	}


	public Batiment(String numero, String anneeConstruction, Diagnostic diagnostic, CodePostal codepostal, Ville ville,
			Rue rue) {
		super();
		this.numero = numero;
		this.anneeConstruction = anneeConstruction;
		this.diagnostic = diagnostic;
		this.codepostal = codepostal;
		this.ville = ville;
		this.rue = rue;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getAnneeConstruction() {
		return anneeConstruction;
	}
	public void setAnneeConstruction(String anneeConstruction) {
		this.anneeConstruction = anneeConstruction;
	}
	public Diagnostic getDiagnostic() {
		return diagnostic;
	}
	public void setDiagnostic(Diagnostic diagnostic) {
		this.diagnostic = diagnostic;
	}
	public CodePostal getCodePostal() {
		return codepostal;
	}
	public void setCodePostal(CodePostal codepostal) {
		this.codepostal = codepostal;
	}
	public Ville getVille() {
		return ville;
	}
	public void setVille(Ville ville) {
		this.ville = ville;
	}
	public Rue getRue() {
		return rue;
	}
	public void setRue(Rue rue) {
		this.rue = rue;
	}
	@Override
	public String toString() {
		return "batiment [numero=" + numero + ", annee_construction=" + anneeConstruction + ", diagnostique="
				+ diagnostic + ", codepostal=" + codepostal + ", ville=" + ville + ", rue=" + rue + "]";
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

}
