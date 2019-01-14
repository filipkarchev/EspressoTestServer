package it.gauss.invivo.server.configuration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author fitsum, luca
 *
 */
public class Configuration {
	private Map<String, String> features;

	public Configuration() {
		features = new HashMap<String, String>();
	}

	
	/**
	 * TODO this should be fixed. For now, feature name and feature value are the same, i.e., feature value.
	 * The preferred way to use this class is by passing a Map rather than a Set of feature values.
	 * @param featureSet
	 */
	public Configuration(Set<String> featureSet) {
		this();
		for (String f : featureSet) {
			features.put(f, f);
		}
	}
	
	public Configuration(Map<String, String> fs) {
		features = new HashMap<String, String>();
		features.putAll(fs);
	}
	
	public Set<String> getFeatures() {
		Set<String> fs = new HashSet<String>();
		fs.addAll(features.values());
		return fs;
	}

	public void setFeatures(Map<String, String> features) {
		this.features = features;
	}
	
}
