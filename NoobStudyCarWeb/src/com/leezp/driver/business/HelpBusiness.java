package com.leezp.driver.business;

import java.util.List;

import com.leezp.driver.business.interfaces.HelpBusinessInterface;
import com.leezp.driver.dao.HelpDao;
import com.leezp.driver.entity.HelpEntity;

public class HelpBusiness implements HelpBusinessInterface{
	private HelpDao help = new HelpDao();

	@Override
	public List<HelpEntity> getHelp(String role) {
		if(role.equals("0")) {
			return help.getStudentHelp();
		} else if(role.equals("1")) {
			return help.getDriverHelp();
		}
		return null;
	}

}
