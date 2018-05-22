package com.leezp.driver.dao.interfaces;

import java.util.List;

import com.leezp.driver.entity.HelpEntity;

public interface HelpDaoInterface {
	//²éÑ¯°ïÖú
	List<HelpEntity> getStudentHelp();
	List<HelpEntity> getDriverHelp();
}
