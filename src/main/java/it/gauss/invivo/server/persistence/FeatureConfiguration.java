package it.gauss.invivo.server.persistence;

/**
 * @author fitsum
 *
 */
import java.util.Set;

import org.xtext.example.mydsl.fml.OpSelection;

import fr.familiar.parser.ConfigurationVariableBDDImpl;
import fr.familiar.variable.ConfigurationVariable;
import fr.familiar.variable.FeatureModelVariable;
import it.gauss.invivo.server.configuration.Configuration;
import it.gauss.invivo.server.configuration.ConfigurationStatus;

public class FeatureConfiguration {
	
	private FeatureModelVariable featureModelVariable;
	private ConfigurationVariable configurationVariable;
	
	private boolean isValid;
	
	private ConfigurationStatus status;
	
	public FeatureConfiguration(FeatureModelVariable fmv) {
		featureModelVariable = fmv;
		isValid = true;
	}
	
	public FeatureConfiguration (FeatureModelVariable fmv, Configuration clientConfiguration) {
		featureModelVariable = fmv;
		isValid = true;
		String ns = "";
		configurationVariable = new ConfigurationVariableBDDImpl(featureModelVariable, ns);
		boolean success = true;
		for (String feature : clientConfiguration.getFeatures()) {
			success = configurationVariable.applySelection(feature, OpSelection.SELECT);
			isValid = isValid && success;
		}
		
	}
	
	public boolean isValid () {
		if (isValid) {
			return configurationVariable.isValid();
		} else {
			return isValid;
		}
	}

	public ConfigurationVariable getConfigurationVariable() {
		return configurationVariable;
	}

	public void setConfigurationVariable(ConfigurationVariable configurationVariable) {
		this.configurationVariable = configurationVariable;
	}

	public FeatureModelVariable getFeatureModelVariable() {
		return featureModelVariable;
	}

	public void setFeatureModelVariable(FeatureModelVariable featureModelVariable) {
		this.featureModelVariable = featureModelVariable;
	}
	
	@Override
	public String toString() {
		if (configurationVariable != null)
			return configurationVariable.toString();
		else
			return "";
	}

	public ConfigurationStatus getStatus() {
		return status;
	}

	public void setStatus(ConfigurationStatus status) {
		this.status = status;
	}
}


