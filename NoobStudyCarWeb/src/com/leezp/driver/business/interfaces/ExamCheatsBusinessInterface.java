package com.leezp.driver.business.interfaces;

import java.util.List;

import com.leezp.driver.entity.ExamCheatsEntity;

public interface ExamCheatsBusinessInterface {
	List<ExamCheatsEntity> getExamCheats(String process_order);
}
