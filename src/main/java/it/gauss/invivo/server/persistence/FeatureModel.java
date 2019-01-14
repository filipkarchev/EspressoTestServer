package it.gauss.invivo.server.persistence;

import fr.familiar.variable.FeatureModelVariable;

/**
 * @author fitsum
 *
 */
public class FeatureModel {
	private FeatureModelVariable featureModelVariable;
	
	public FeatureModel() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * copy constructor
	 * @param other
	 */
	public FeatureModel(FeatureModel other) {
		// TODO 
	}
	
	public FeatureModel (String fmPath){
		
	}
	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return featureModelVariable.toString();
	}

	public FeatureModelVariable getFeatureModelVariable() {
		return featureModelVariable;
	}

	public void setFeatureModelVariable(FeatureModelVariable featureModelVariable) {
		this.featureModelVariable = featureModelVariable;
	}
}
