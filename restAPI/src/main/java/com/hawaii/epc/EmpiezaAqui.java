package com.hawaii.epc;


import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Named
@Slf4j
@SessionScoped
public class EmpiezaAqui implements Serializable {
	
	@Inject
	EmpiezaAquiTienda tienda;
	
	private String companyName = "Enterprise";

	public EmpiezaAqui() {}

	@PostConstruct
	public void getDescriptionService(){

		String myDescription = "Mate";
		log.info("INFO HAWAII: "+myDescription);
		myDescription = tienda.getMyDescription();
		this.setCompanyName(myDescription);
		log.info("INFO HAWAII: "+myDescription);

	}
	
	public String getCompanyName() {

		return companyName;
	}

	public void setCompanyName(String companyName) {

		this.companyName = companyName;
	}
	
	
}
