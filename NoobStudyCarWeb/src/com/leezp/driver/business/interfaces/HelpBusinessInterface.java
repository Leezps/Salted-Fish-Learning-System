package com.leezp.driver.business.interfaces;

import java.util.List;

import com.leezp.driver.entity.HelpEntity;

public interface HelpBusinessInterface {
	List<HelpEntity> getHelp(String role);
}
