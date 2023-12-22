package derby;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import derby.entity.Batiment;
import derby.entity.ClasseEnergitique;
import derby.entity.CodePostal;
import derby.entity.Diagnostic;
import derby.entity.Rue;
import derby.entity.Ville;
import derby.tdg.BatimentTDG;
import derby.tdg.CodePostalTDG;
import derby.tdg.DiagnosticTDG;
import derby.tdg.RueTDG;
import derby.tdg.VilleTDG;

public class MainTDG {

	public static RueTDG rueTDG;
	public static CodePostalTDG codePostalTDG;
	public static VilleTDG villeTDG;
	public static DiagnosticTDG diagnosticTDG;
	public static BatimentTDG batimentTDG;

	public static void main(String[] args) throws SQLException, ParseException {
		InitDB initDB = new InitDB();

		String csvFile = "src/main/resources/dpe.csv";

		String[] HEADERS = { "N°DPE", "Etiquette_DPE", "Année_construction", "N°_voie_(BAN)", "Code_postal_(BAN)",
				"Nom__rue_(BAN)", "Nom__commune_(Brut)", "Conso_5_usages_é_finale" };

		CSVFormat csvFormat = CSVFormat.DEFAULT
				.builder()
				.setSkipHeaderRecord(true)
				.setDelimiter(";")
				.setHeader(HEADERS)
				.build();

		try (Reader reader = new FileReader(csvFile);CSVParser csvParser = new CSVParser(reader, csvFormat)) {
			long start = System.currentTimeMillis();
			
			System.out.println("--- START OF DB INSERTION  ---");
			for (CSVRecord csvRecord : csvParser) {
				
				Rue rue = null;
				CodePostal codePostal = null;
				Ville ville = null;
				Diagnostic diagnostic = null;
				Batiment batiment = null;

				// Accéder aux valeurs de chaque colonne
				String nDpe = csvRecord.get("N°DPE");
				String etiquetteDpe = csvRecord.get("Etiquette_DPE");
				String anneeConstruction = csvRecord.get("Année_construction");
				String numVoie = csvRecord.get("N°_voie_(BAN)");
				String cPostal = csvRecord.get("Code_postal_(BAN)");
				String nomRue = csvRecord.get("Nom__rue_(BAN)");
				String nomVille = csvRecord.get("Nom__commune_(Brut)");
				String conso = csvRecord.get("Conso_5_usages_é_finale");

				// creation d'une rue
				if (nomRue != null && !"".equals(nomRue)) {
					rueTDG = new RueTDG();
					rue = rueTDG.insertIntoDB(new Rue(nomRue));
				}
				// creation d'une ville
				if (nomVille != null && !"".equals(nomVille)) {
					villeTDG = new VilleTDG();
					ville = villeTDG.insertIntoDB(new Ville(nomVille));
				}
				// creation du code postal
				if (cPostal != null && !"".equals(cPostal)) {
					codePostalTDG = new CodePostalTDG();
					codePostal = codePostalTDG.insertIntoDB(new CodePostal(Integer.parseInt(cPostal)));
				}
				// creation diagnostic
				if (conso != null && !"".equals(conso) && etiquetteDpe != null && !"".equals(etiquetteDpe)) {
					diagnosticTDG = new DiagnosticTDG();
					NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
					Number number = format.parse(conso);
					double dConso = number.doubleValue();
					diagnostic = diagnosticTDG
							.insertIntoDB(new Diagnostic(dConso, (ClasseEnergitique.valueOf(etiquetteDpe))));
				}
				// creation batiment
				batimentTDG = new BatimentTDG();
				batiment = new Batiment();
				if (numVoie != null && !"".equals(numVoie)) {
					batiment.setNumero(numVoie);
				}
				if (anneeConstruction != null && !"".equals(anneeConstruction)) {
					batiment.setAnneeConstruction(anneeConstruction);
				}
				batiment.setCodePostal(codePostal);
				batiment.setVille(ville);
				batiment.setRue(rue);
				batiment.setDiagnostic(diagnostic);
				batimentTDG.insertIntoDB(batiment);

			}
			long finish = System.currentTimeMillis();
			long timeElapsed = finish - start;
			System.out.println("--- END OF DB INSERTION ---");
			System.out.println("--- TIME :   " + timeElapsed/60   +"s ---");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
