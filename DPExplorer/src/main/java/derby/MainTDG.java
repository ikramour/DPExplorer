package derby;

import java.sql.SQLException;
import java.text.ParseException;

import derby.utils.InserInDbFromCsv;

public class MainTDG {

	public static void main(String[] args) throws SQLException, ParseException {
		new InitDB().initDataBase();

		String CSVPATH = "src/main/resources/dpe.csv";

		String[] HEADERS = { "N°DPE", "Etiquette_DPE", "Année_construction", "N°_voie_(BAN)", "Code_postal_(BAN)",
				"Nom__rue_(BAN)", "Nom__commune_(Brut)", "Conso_5_usages_é_finale" };

		InserInDbFromCsv inserInDbFromCsv = new InserInDbFromCsv();
		inserInDbFromCsv.insertCsvIntoDerbyDB(CSVPATH, HEADERS);

	}
}
