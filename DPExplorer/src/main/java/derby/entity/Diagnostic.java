package derby.entity;

import derby.Persistable;

public class Diagnostic implements Persistable{

	private long id;
	private double consommation;
	private Batiment batiment;
	private ClasseEnergitique classeEnergitique;
	
	public Diagnostic() {
	}
	
	
	public Diagnostic(double conso, ClasseEnergitique etiquetteDpe) {
		this.consommation = conso;
		this.classeEnergitique = etiquetteDpe;
	}


	public Diagnostic(double consommation, Batiment batiment, ClasseEnergitique classeEnergitique) {
		super();
		this.consommation = consommation;
		this.batiment = batiment;
		this.classeEnergitique = classeEnergitique;
	}
	public double getConsommation() {
		return consommation;
	}
	public void setConsommation(double consommation) {
		this.consommation = consommation;
	}
	public Batiment getBatiment() {
		return batiment;
	}
	public void setBatiment(Batiment batiment) {
		this.batiment = batiment;
	}
	public ClasseEnergitique getClassEnergetique() {
		return classeEnergitique;
	}
	public void setClassEnergetique(ClasseEnergitique classeEnergitique) {
		this.classeEnergitique = classeEnergitique;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "Diagnostic [consommation=" + consommation + ", batiment=" + batiment + ", classEnergitique="
				+ classeEnergitique + "]";
	}
	

}
