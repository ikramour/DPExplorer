package derby.tdg;

import java.io.IOException;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import derby.AbstractTDG;
import derby.TDGRegistry;
import derby.entity.CodePostal;
import derby.entity.Ville;

public class VilleTDG extends AbstractTDG<Ville> {

	private static String CREATE;
	private static String DROP;

	private static final String INSERT = "INSERT INTO Ville (NOM) VALUES(?)";
	private static final String UPDATE = "UPDATE Ville a SET a.NOM = ?, a.CODEPOSTAL=? WHERE a.ID = ?";
	private static final String DELETE = "DELETE FROM Ville a WHERE a.ID = ?";
	private static final String FIND_BY_ID = "SELECT ID,NOM,CODEPOSTAL FROM Ville a WHERE a.ID=?";
	private static final String WHERE = "SELECT ID FROM Ville a WHERE ";
	private static final Properties QUERIES = new Properties();
	{
		try {
			QUERIES.load(Ville.class.getResourceAsStream("Ville.properties"));
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
	protected Ville retrieveFromDB(long id) throws SQLException {
		Ville a = null;
		try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(FIND_BY_ID)) {
			pst.setLong(1, id);
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					a = new Ville();
					a.setNom(rs.getString(2));
				}
			}
		}
		return a;
	}

	@Override
	public Ville insertIntoDB(Ville ville) throws SQLException {
		try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(INSERT,
				Statement.RETURN_GENERATED_KEYS)) {
			pst.setString(1, ville.getNom());
			int result = pst.executeUpdate();
			assert result == 1;
            try (ResultSet keys = pst.getGeneratedKeys()) {
                if (keys.next()) {
                	ville.setId(keys.getLong(1));
                }
            }
			return ville;
		}
	}

	@Override
	public Ville updateIntoDB(Ville a) throws SQLException {
		try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(UPDATE)) {
			assert findById(a.getId()) == a;
			pst.setString(2, a.getNom());
			pst.setArray(3, (Array) a.getCodePostal());
			int result = pst.executeUpdate();
			assert result == 1;
			return a;
		}
	}

	@Override
	public Ville deleteFromDB(Ville a) throws SQLException {
		try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(DELETE)) {
			assert findById(a.getId()) == a;
			pst.setLong(1, a.getId());
			int result = pst.executeUpdate();
			assert result == 1;
			return a;
		}
	}

	@Override
	protected Ville refreshIntoDB(Ville a) throws SQLException {
		try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(FIND_BY_ID)) {
			pst.setLong(1, a.getId());
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					a.setId(rs.getLong(1));
					a.setNom(rs.getString(2));
					a.setCodePostal((List<CodePostal>) rs.getArray(3));
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
