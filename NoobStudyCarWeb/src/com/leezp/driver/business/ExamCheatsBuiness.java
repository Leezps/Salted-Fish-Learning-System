package com.leezp.driver.business;

import java.util.List;

import com.leezp.driver.business.interfaces.ExamCheatsBusinessInterface;
import com.leezp.driver.dao.ExamCheatsDao;
import com.leezp.driver.entity.ExamCheatsEntity;

public class ExamCheatsBuiness implements ExamCheatsBusinessInterface{
	private ExamCheatsDao examCheats = new ExamCheatsDao();

	@Override
	public List<ExamCheatsEntity> getExamCheats(String process_order) {
		return examCheats.getExamCheats(process_order);
	}

}
