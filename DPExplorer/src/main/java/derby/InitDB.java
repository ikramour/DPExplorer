package derby;

import java.sql.SQLException;

import derby.tdg.BatimentTDG;
import derby.tdg.CodePostalTDG;
import derby.tdg.DiagnosticTDG;
import derby.tdg.RueTDG;
import derby.tdg.VilleTDG;

public class InitDB {
	
	public RueTDG rueTDG;
	public CodePostalTDG codePostalTDG;
	public VilleTDG villeTDG;
	public DiagnosticTDG diagnosticTDG;
	public BatimentTDG batimentTDG;
	
	
	
	public InitDB() throws SQLException {
		initRue();
		initCodePostal();
		initVille();
		initDiagnostic();
		initBatiment();
	}
	
	private void initBatiment() throws SQLException {
		new BatimentTDG().createTable();
	}

	private void initDiagnostic() throws SQLException {
		new DiagnosticTDG().createTable();
	}

	private void initCodePostal() throws SQLException {
		new CodePostalTDG().createTable();
		
	}

	public void initRue() throws SQLException {
		new RueTDG().createTable();
	}
	
	public void initVille() throws SQLException {
		new VilleTDG().createTable();
	}

}
