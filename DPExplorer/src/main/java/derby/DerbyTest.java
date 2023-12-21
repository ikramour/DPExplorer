package derby;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DerbyTest {

	public static void main(String[] args) {

		String urlCon = "jdbc:derby://localhost:1527/dpe";
		try {
			Connection connection = DriverManager.getConnection(urlCon);
			
			System.out.println("Connection OK : " + connection.toString());
			
			Statement statement = connection.createStatement();
			
			ResultSet resultSet =  statement.executeQuery("SELECT * FROM APP.BATIMENT b\r\n"
					+ "LEFT JOIN RUE r ON r.RUE_ID = b.ID_RUE \r\n"
					+ "LEFT JOIN CODEPOSTAL c ON c.ID_CODEPOSTAL  = b.ID_CODEPOSTAL \r\n"
					+ "LEFT JOIN VILLE  v ON v.VILLE_ID  =b.ID_VILLE ");
			
			while(resultSet.next()) {
				System.out.println("BATIMENT : "  + resultSet.getString("ID_BATIMENT"));
				System.out.println("ADRESSE : "  + resultSet.getString("NOM") + " " + resultSet.getString("VALEUR") + resultSet.getString("NOM"));
			}
			
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		

	}

}
