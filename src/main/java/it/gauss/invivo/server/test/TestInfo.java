package it.gauss.invivo.server.test;

import java.util.ArrayList;


public class TestInfo {
	private String hash;
	ArrayList<String> testClasses = new ArrayList<String>();
	
	
	public TestInfo(String hash, ArrayList<String> testClasses) {
		this.hash = hash;
		this.testClasses = testClasses;
	}
	

	
	public TestInfo(String hash) {
		//Used for the searching
		this.hash = hash;
	}
	
	
	public TestInfo() {
	}

	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	

	public ArrayList<String> getTestClasses() {
		return testClasses;
	}
	public void setTestClasses(ArrayList<String> testClasses) {
		this.testClasses = testClasses;
	}
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!TestInfo.class.isAssignableFrom(obj.getClass())) {
            return false;
        }

        final TestInfo other = (TestInfo) obj;
        if (this.hash.equals(other.getHash())) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.hash != null ? this.hash.hashCode() : 0);
        return hash;
    }
}
