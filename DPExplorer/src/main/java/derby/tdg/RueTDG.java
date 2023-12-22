package derby.tdg;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import derby.AbstractTDG;
import derby.TDGRegistry;
import derby.entity.Rue;
import derby.entity.Ville;

public class RueTDG extends AbstractTDG<Rue> {

	private static String CREATE;
	private static String DROP;

	private static final String INSERT = "INSERT INTO Rue (NOM) VALUES(?)";
	private static final String UPDATE = "UPDATE Rue a SET a.NOM = ? WHERE a.ID = ?";
	private static final String DELETE = "DELETE FROM Rue a WHERE a.ID = ?";
	private static final String FIND_BY_ID = "SELECT ID,NOM FROM Rue a WHERE a.ID=?";
	private static final String WHERE = "SELECT ID FROM Rue a WHERE ";
	private static final Properties QUERIES = new Properties();
	{
		try {
			QUERIES.load(Ville.class.getResourceAsStream("Rue.properties"));
			CREATE = QUERIES.getProperty("CREATE");
			DROP = QUERIES.getProperty("DROP");
		} catch (IOException ioe) {
			System.err.println(ioe);
		}
	}

	@Override
	public void createTable() throws SQLException {
		try (Statement stm = TDGRegistry.getConnection().createStatement()) {
			System.out.println(CREATE);
			stm.executeUpdate(CREATE);
		}
	}

	@Override
	public void deleteTable() throws SQLException {
		try (Statement stm = TDGRegistry.getConnection().createStatement()) {
			stm.executeUpdate(DROP);
		}
	}

	@Override
	protected Rue retrieveFromDB(long id) throws SQLException {
		Rue a = null;
		try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(FIND_BY_ID)) {
			pst.setLong(1, id);
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					a = new Rue();
					a.setNom(rs.getString(2));
				}
			}
		}
		return a;
	}

	@Override
	public Rue insertIntoDB(Rue rue) throws SQLException {
		try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(INSERT,
				Statement.RETURN_GENERATED_KEYS)) {
			pst.setString(1, rue.getNom());
			int result = pst.executeUpdate();
			assert result == 1;
            try (ResultSet keys = pst.getGeneratedKeys()) {
                if (keys.next()) {
                	rue.setId(keys.getLong(1)); // long
                }
            }
			return rue;
		}
	}

	@Override
	public Rue updateIntoDB(Rue a) throws SQLException {
		try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(UPDATE)) {
			assert findById(a.getId()) == a;
			pst.setString(2, a.getNom());
			
			int result = pst.executeUpdate();
			assert result == 1;
			return a;
		}
	}

	@Override
	public Rue deleteFromDB(Rue a) throws SQLException {
		try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(DELETE)) {
			assert findById(a.getId()) == a;
			pst.setLong(1, a.getId());
			int result = pst.executeUpdate();
			assert result == 1;
			return a;
		}
	}

	@Override
	protected Rue refreshIntoDB(Rue a) throws SQLException {
		try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(FIND_BY_ID)) {
			pst.setLong(1, a.getId());
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					a.setId(rs.getLong(1));
					a.setNom(rs.getString(2));

				}
			}
		}
		return a;
	}

	@Override
	protected String getWherePrefix() {
		return WHERE;
	}


}
