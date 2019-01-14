package it.gauss.invivo.server.persistence;

import java.io.File;
import java.util.Set;

import fr.familiar.fm.FeatureModelChecker;
import fr.familiar.fm.converter.SPLOTtoFML;
import fr.familiar.interpreter.DefaultOutput;
import fr.familiar.interpreter.FMLAssertionError;
import fr.familiar.interpreter.FMLBasicInterpreter;
import fr.familiar.interpreter.FMLFatalError;
import fr.familiar.interpreter.FMLShell;
import fr.familiar.variable.FeatureModelVariable;
import fr.familiar.variable.Variable;
import it.gauss.invivo.server.configuration.Configuration;
import splar.core.fm.FeatureModelException;
import splar.core.fm.XMLFeatureModel;

/**
 * @author fitsum
 *
 */
public class StorageManager {
	
	FeatureModelChecker featureModelChecker;
	FMLBasicInterpreter fmlInterpreter;
	FMLShell fmlShell;
	
	public StorageManager() {
		fmlInterpreter = new FMLBasicInterpreter();
		
		fmlShell = new FMLShell(System.in, new DefaultOutput(), false);
		File basePath = new File("./src/test/resources/");
		fmlShell.addPath(basePath.getAbsoluteFile());
	}
	
	/**
	 * Use the FML shell to load compatible feature models from file.
	 * 
	 * TODO clarify the path search strategy, it gives errors if there are multiple files
	 * in different locations.
	 * @param fmPath
	 * @return
	 */
	public FeatureModel loadFeatureModelFromFile (String fmPath) {
		FeatureModel fm = new FeatureModel();
		String cmd = "fm=FM('"+ fmPath +"')";
		Variable variable = fmlShell.parse(cmd);
		FeatureModelVariable fmv = (FeatureModelVariable)variable;
		if (!fmv.isNull() && fmv.isValid()) {
			fm.setFeatureModelVariable(fmv);
		}
		return fm;
	}
	
	public FeatureModel loadFeatureModelFromXmlFile (File splotFile) {
		splar.core.fm.FeatureModel featureModelSPLOT = new XMLFeatureModel(
				splotFile.getAbsolutePath(),
				XMLFeatureModel.USE_VARIABLE_NAME_AS_ID);
		try {
			featureModelSPLOT.loadModel();
		} catch (FeatureModelException e) {
			System.err.println("Unable to load SPLOT feature model "
							+ e.getMessage());
		}

		gsd.synthesis.FeatureModel<String> rFM = new SPLOTtoFML().convertToFeatureModel(featureModelSPLOT);
		FeatureModelVariable fmv = new FeatureModelVariable(splotFile.getName(), rFM) ;
		
		FeatureModel fm = new FeatureModel();
		if (!fmv.isNull() && fmv.isValid()) {
			fm.setFeatureModelVariable(fmv);
		}
		return fm;
	}
	
	public FeatureModel loadFeatureModelFromString (String strFm) {
		FeatureModel fm = new FeatureModel();
		Variable eval;
		try {
			eval = fmlInterpreter.eval(strFm);
			FeatureModelVariable fmVar = (FeatureModelVariable)eval;
			if (fmVar.isValid()) {
				fm.setFeatureModelVariable(fmVar);
			}
		} catch (FMLFatalError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FMLAssertionError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fm;
	}
	
	public FeatureConfiguration loadFeatureConfiguration (FeatureModel fm, Configuration features) {
		FeatureConfiguration fc = new FeatureConfiguration(fm.getFeatureModelVariable(), features);
		return fc;
	}
	
	public FeatureModel mergeFcToFM (FeatureConfiguration fc, FeatureModel baseFm) {
		FeatureModel fm = new FeatureModel();
		
		return fm;
	}
	
}
