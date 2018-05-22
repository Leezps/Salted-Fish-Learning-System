package com.leezp.driver.dao.interfaces;

import java.util.List;

import com.leezp.driver.entity.ExamCheatsEntity;

public interface ExamCheatsDaoInterface {
	//ªÒ»°øº ‘√ÿºÆ
	List<ExamCheatsEntity> getExamCheats(String process_order);
}
