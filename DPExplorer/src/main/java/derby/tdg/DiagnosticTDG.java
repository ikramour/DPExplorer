package derby.tdg;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import derby.AbstractTDG;
import derby.TDGRegistry;
import derby.entity.ClasseEnergitique;
import derby.entity.Diagnostic;

public class DiagnosticTDG extends AbstractTDG<Diagnostic> {

	private static final String CREATE = "CREATE TABLE APP.DIAGNOSTIC (ID_DIAGNOSTIC BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
			+ "CLASSE_ENERGETIQUE VARCHAR(1),CONSOMMATION VARCHAR(100),CONSTRAINT DIAGNOSTIC_PK PRIMARY KEY (ID_DIAGNOSTIC))";
	private static final String  DROP="DROP TABLE Diagnostic";
	private static final String INSERT = "INSERT INTO Diagnostic (CLASSE_ENERGETIQUE,CONSOMMATION) VALUES(?,?)";
	private static final String UPDATE = "UPDATE Diagnostic d SET s.CLASSE_ENERGETIQUE=?, d.CONSOMMATION = ?,  WHERE d.Diagnostic_ID = ?";
	private static final String DELETE = "DELETE FROM Diagnostic d WHERE d.Diagnostic_ID = ?";
	private static final String FIND_BY_ID = "SELECT Diagnostic_ID, CLASSE_ENERGETIQUE, CONSOMMATION FROM Diagnostic d WHERE d.Diagnostic_ID=?";
	private static final String WHERE = "SELECT Diagnostic_ID FROM Diagnostic d WHERE ";

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
	protected Diagnostic retrieveFromDB(long id) throws SQLException {
		Diagnostic d = null;
		try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(FIND_BY_ID)) {
			pst.setLong(1, id);
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					d = new Diagnostic();
					d.setId(rs.getLong(1));
					d.setConsommation(rs.getDouble(3));					
					d.setClassEnergetique(ClasseEnergitique.valueOf(rs.getString(2)));
										
				}
			}
		}
		return d;
	}

	@Override
	public Diagnostic insertIntoDB(Diagnostic d) throws SQLException {
		try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(INSERT,
				Statement.RETURN_GENERATED_KEYS)) {
			pst.setString(1, d.getClassEnergetique().toString());
			pst.setDouble(2, d.getConsommation());
			
			 int result = pst.executeUpdate();
	            assert result == 1;
	            try (ResultSet keys = pst.getGeneratedKeys()) {
	                if (keys.next()) {
	                    d.setId(keys.getLong(1)); // long
	                }
	            }

			return d;
		}
	}

	@Override
	public Diagnostic updateIntoDB(Diagnostic d) throws SQLException {
		try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(UPDATE)) {
			assert findById(d.getId()) == d;
			
			pst.setString(1, d.getClassEnergetique().toString());
			pst.setDouble(2, d.getConsommation());
			pst.setLong(3, d.getId());
			
			int result = pst.executeUpdate();
			assert result == 1;
			return d;
		}
	}

	@Override
	public Diagnostic deleteFromDB(Diagnostic d) throws SQLException {
		try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(DELETE)) {
			assert findById(d.getId()) == d;
			pst.setLong(1, d.getId());
			int result = pst.executeUpdate();
			assert result == 1;
			return d;
		}
	}

	@Override
	protected Diagnostic refreshIntoDB(Diagnostic d) throws SQLException {
		try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(FIND_BY_ID)) {
			pst.setLong(1, d.getId());
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					d.setId(rs.getLong(1));
					d.setClassEnergetique(ClasseEnergitique.valueOf(rs.getString(2)));
					d.setConsommation(rs.getDouble(3));
					
				}
			}
		}
		return d;
	}

	@Override
	protected String getWherePrefix() {
		return WHERE;
	}


}