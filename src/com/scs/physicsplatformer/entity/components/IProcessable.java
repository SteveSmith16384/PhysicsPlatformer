package com.scs.physicsplatformer.entity.components;

public interface IProcessable {

	void preprocess(long interpol);
	
	void postprocess(long interpol);
	
}
