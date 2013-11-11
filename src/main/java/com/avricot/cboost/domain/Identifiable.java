package com.avricot.cboost.domain;

import java.io.Serializable;

public interface Identifiable<K extends Serializable> extends Serializable {

	K getId();
	
	void setId(K id);
	
	
}
