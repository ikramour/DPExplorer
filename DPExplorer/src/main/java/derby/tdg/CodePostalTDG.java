package derby.tdg;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Properties;

import derby.AbstractTDG;
import derby.TDGRegistry;

import derby.entity.CodePostal;

public class CodePostalTDG extends AbstractTDG<CodePostal> {

	private static String CREATE;
	private static String DROP;

	private static final String INSERT = "INSERT INTO CodePostal (VALEUR) VALUES(?)";
	private static final String UPDATE = "UPDATE CodePostal a SET a.VALEUR = ?, WHERE a.ID = ?";
	private static final String DELETE = "DELETE FROM CodePostal a WHERE a.ID = ?";
	private static final String FIND_BY_ID = "SELECT ID,VALEUR FROM CodePostal a WHERE a.ID=?";
	private static final String WHERE = "SELECT ID FROM CodePostal a WHERE ";
	private static final Properties QUERIES = new Properties();
	{
		try {
			QUERIES.load(CodePostal.class.getResourceAsStream("CodePostal.properties"));
			System.out.println(QUERIES);
			CREATE = QUERIES.getProperty("CREATE");
			DROP = QUERIES.getProperty("DROP");
		} catch (IOException ioe) {
			System.err.println(ioe);
		}

	}

	@Override
	public void createTable() throws SQLException {
		try (Statement stm = TDGRegistry.getConnection().createStatement()) {
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
	protected CodePostal retrieveFromDB(long id) throws SQLException {
		CodePostal a = null;
		try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(FIND_BY_ID)) {
			pst.setLong(1, id);
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					a = new CodePostal();
					a.setValeur(rs.getInt(2));
				}
			}
		}
		return a;
	}

	@Override
	public CodePostal insertIntoDB(CodePostal cp) throws SQLException {
		try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(INSERT,
				Statement.RETURN_GENERATED_KEYS)) {
			pst.setLong(1, cp.getValeur());
			int result = pst.executeUpdate();
			assert result == 1;
            try (ResultSet keys = pst.getGeneratedKeys()) {
                if (keys.next()) {
                	cp.setId(keys.getLong(1)); // long
                }
            }
			return cp;
		}
	}

	@Override
	public CodePostal updateIntoDB(CodePostal a) throws SQLException {
		try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(UPDATE)) {
			assert findById(a.getId()) == a;
			pst.setLong(2, a.getValeur());
			int result = pst.executeUpdate();
			assert result == 1;
			return a;
		}
	}

	@Override
	public CodePostal deleteFromDB(CodePostal a) throws SQLException {
		try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(DELETE)) {
			assert findById(a.getId()) == a;
			pst.setLong(1, a.getId());
			int result = pst.executeUpdate();
			assert result == 1;
			return a;
		}
	}

	@Override
	protected CodePostal refreshIntoDB(CodePostal a) throws SQLException {
		try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(FIND_BY_ID)) {
			pst.setLong(1, a.getId());
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					a.setId(rs.getLong(1));
					a.setValeur(rs.getInt(2));
								
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