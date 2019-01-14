package it.gauss.invivo.server.configuration;

import it.gauss.invivo.server.persistence.FeatureConfiguration;

/**
 * @author fitsum
 *
 */
public interface ConfigurationManager {

	/**
	 * adds a tested configuration to the "tested" set of configurations. Internal
	 * details depend on the method used (tree or FM).
	 * 
	 * @param testedConf
	 * @return true if operation succeeds, false otherwise
	 */
	public boolean addTestedConfiguration(Configuration testedConf);

	/**
	 * check the status of the configuration (Tested,Untested,Unknown)
	 * @param clientConfig
	 * @return
	 */
	public ConfigurationStatus determineConfigurationStatus (Configuration clientConfig);
	
	/**
	 * Same implementation as in the client.
	 * 
	 * @param clientConfig
	 * @return
	 */
	public FeatureConfiguration determineStatus(Configuration clientConfig);

	/**
	 * write the model of tested configurations to a file whose path is passed as
	 * argument
	 * 
	 * @param filePath
	 */
	public void serializeModel(String filePath);
}
