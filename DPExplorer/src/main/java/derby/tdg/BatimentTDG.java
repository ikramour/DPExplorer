package derby.tdg;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import derby.AbstractTDG;
import derby.TDGRegistry;
import derby.entity.CodePostal;
import derby.entity.Diagnostic;
import derby.entity.Rue;
import derby.entity.Ville;

import derby.entity.Batiment;

public class BatimentTDG  extends AbstractTDG<Batiment> {

	private static final String CREATE ="CREATE TABLE APP.BATIMENT (ID_BATIMENT BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
			+ "ID_VILLE BIGINT,ID_RUE BIGINT,ID_DIAGNOSTIC BIGINT,ID_CODEPOSTAL BIGINT,	ANNEECONSTRUCTION INTEGER, NUM_BATIMENT VARCHAR(50),"
			+ "CONSTRAINT BATIMENT_PK PRIMARY KEY (ID_BATIMENT),"
			+ "CONSTRAINT BATIMENT_FK FOREIGN KEY (ID_RUE) REFERENCES APP.RUE(RUE_ID),"
			+ "CONSTRAINT BATIMENT_FK_1 FOREIGN KEY (ID_VILLE) REFERENCES APP.VILLE(VILLE_ID),"
			+ "CONSTRAINT BATIMENT_FK_2 FOREIGN KEY (ID_CODEPOSTAL) REFERENCES APP.CODEPOSTAL(ID_CODEPOSTAL),"
			+ "CONSTRAINT BATIMENT_FK_3 FOREIGN KEY (ID_DIAGNOSTIC) REFERENCES APP.DIAGNOSTIC(ID_DIAGNOSTIC))";
	private static final String DROP = "DROP TABLE APP.BATIMENT";

	private static final String INSERT = "INSERT INTO APP.BATIMENT (NUM_BATIMENT, ANNEECONSTRUCTION, ID_RUE, ID_CODEPOSTAL, ID_VILLE, ID_DIAGNOSTIC) VALUES(?, ?, ?, ?, ?, ?)";
	private static final String UPDATE = "UPDATE Batiment b SET  b.NUMERO=?, b.ANNEE_CONSTRUCTION = ?, b.DIAGNOSTIC_ID=?, b.CODEPOSTAL_ID=? , b.VILLE_ID=? , b.RUE_ID=?,  WHERE b.Batiment_ID = ?";
	private static final String DELETE = "DELETE FROM Batiment b WHERE b.Batiment_ID = ?";
	private static final String FIND_BY_ID = "SELECT Batiment_ID, CONSOMMATION, DIAGNOSTIC_ID, CODEPOSTAL_ID, VILLE_ID, RUE_ID FROM Batiment b WHERE b.Batiment_ID=?";
	private static final String WHERE = "SELECT Batiment_ID FROM Batiment b WHERE ";


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
	protected Batiment retrieveFromDB(long id) throws SQLException {
		Batiment b = null;
		try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(FIND_BY_ID)) {
			pst.setLong(1, id);
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					b = new Batiment();
					b.setId(rs.getLong(1));
					b.setNumero(rs.getString(2));
					b.setAnneeConstruction(rs.getString(3));
					long diagnosticId = rs.getLong(4);
					if (diagnosticId != 0) {
						b.setDiagnostic(TDGRegistry.findTDG(Diagnostic.class).findById(diagnosticId));
					}
					b.setCodePostal(TDGRegistry.findTDG(CodePostal.class).findById(rs.getLong(5)));
					b.setVille(TDGRegistry.findTDG(Ville.class).findById(rs.getLong(6)));
					b.setRue(TDGRegistry.findTDG(Rue.class).findById(rs.getLong(7)));

				}
			}
		}
		return b;
	}

	@Override
	public Batiment insertIntoDB(Batiment b) throws SQLException {
		try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(INSERT,Statement.RETURN_GENERATED_KEYS)) {
			pst.setString(1, b.getNumero());
			pst.setString(2, b.getAnneeConstruction());
			if(b.getRue() != null) {
				pst.setLong(3,b.getRue().getId());
			}else {
				pst.setString(3, null);
			}
			if(b.getCodePostal() != null) {
				pst.setLong(4, b.getCodePostal().getId());
			}else {
				pst.setString(4, null);
			}
			if(b.getVille() != null) {
				pst.setLong(5, b.getVille().getId());
			}else {
				pst.setString(5, null);
			}
			if(b.getDiagnostic() != null) {
				pst.setLong(6, b.getDiagnostic().getId());
			}else {
				pst.setString(6, null);
			}

			int result = pst.executeUpdate();
			assert result == 1;
			try (ResultSet keys = pst.getGeneratedKeys()) {
				if (keys.next()) {
					b.setId(keys.getLong(1));
				}
			}
			return b;
		}
	}

	@Override
	public Batiment updateIntoDB(Batiment b) throws SQLException {
		try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(UPDATE)) {
			assert findById(b.getId()) == b;
			pst.setString(1, b.getNumero());
			pst.setString(2, b.getAnneeConstruction());
			
			if (b.getDiagnostic() == null) {
                pst.setNull(3, Types.BIGINT);
            } else {
                Diagnostic d = b.getDiagnostic();
                if (d.getId() == 0) {
                    TDGRegistry.findTDG(Diagnostic.class).insert(d);
                }else {
                	TDGRegistry.findTDG(Diagnostic.class).update(d);
                }
                pst.setLong(3, d.getId());
            }
			pst.setLong(4, b.getCodePostal().getId());
			pst.setLong(5, b.getVille().getId());
			pst.setLong(6, b.getRue().getId());
			pst.setLong(7,b.getId());

			
			int result = pst.executeUpdate();
			assert result == 1;
			return b;
		}
	}

	@Override
	public Batiment deleteFromDB(Batiment b) throws SQLException {
		try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(DELETE)) {
			assert findById(b.getId()) == b;
			pst.setLong(1, b.getId());
			int result = pst.executeUpdate();
			assert result == 1;
			return b;
		}
	}

	@Override
	protected Batiment refreshIntoDB(Batiment b) throws SQLException {
		try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(FIND_BY_ID)) {
			pst.setLong(1, b.getId());
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					b.setId(rs.getLong(1));
					b.setNumero(rs.getString(2));
					b.setAnneeConstruction(rs.getString(3));
					long diagnosticId = rs.getLong(4);
					if( diagnosticId != 0) {
						b.setDiagnostic(TDGRegistry.findTDG(Diagnostic.class).findById(diagnosticId));
					}
					b.setCodePostal(TDGRegistry.findTDG(CodePostal.class).findById( rs.getLong(5)));
					b.setVille(TDGRegistry.findTDG(Ville.class).findById( rs.getLong(6)));
					b.setRue(TDGRegistry.findTDG(Rue.class).findById( rs.getLong(7)));

				}
			}
		}
		return b;
	}

	@Override
	protected String getWherePrefix() {
		return WHERE;
	}


}