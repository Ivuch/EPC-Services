package com.hawaii.epc;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmpiezaAquiTienda implements Serializable {
	
	public  EmpiezaAquiTienda(){}
	
	public String getMyDescription() {
		Connection con = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
		String description ="No description found.";
		log.info("INFO HAWAII: "+description);
		try {
            String selectHawaii = "select * from hawaii.hawaii";
			InitialContext dtx = new InitialContext();
			DataSource ds = (DataSource) dtx.lookup("java:comp/env/hawaii"); // .lookup("java:openejb/hawaii"); //
			con = ds.getConnection();
			stmt = con.prepareStatement(selectHawaii);
			rs = stmt.executeQuery();
            if (rs.next()) {
                 description = rs.getString("location");
                 log.info("INFO HAWAII: "+description);
            }
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 description = "Error fetching description"; 
		}
		return description;
	}
}

