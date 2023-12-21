package derby.utils;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CSVParserMain {
	
	
	public static void main(String[] args) {
        String csvFile = "src/main/resources/dpe.csv";
        
        String[] HEADERS = { "N°DPE", "Etiquette_DPE"};
        
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setSkipHeaderRecord(true)
                .setDelimiter(";")
                .setHeader(HEADERS)
                .build();

        try (Reader reader = new FileReader(csvFile);
        		
             CSVParser csvParser = new CSVParser(reader, csvFormat)) {

            for (CSVRecord csvRecord : csvParser) {
                // Accéder aux valeurs de chaque colonne
                String col1 = csvRecord.get("N°DPE");
                String col2 = csvRecord.get(1);

                // Faire quelque chose avec les valeurs (afficher dans cet exemple)
                System.out.println("Colonne 1 : " + col1);
                System.out.println("Colonne 2 : " + col2);
                System.out.println("-------------");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
