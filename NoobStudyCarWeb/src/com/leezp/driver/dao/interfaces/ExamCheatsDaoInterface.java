package com.leezp.driver.dao.interfaces;

import java.util.List;

import com.leezp.driver.entity.ExamCheatsEntity;

public interface ExamCheatsDaoInterface {
	//��ȡ�����ؼ�
	List<ExamCheatsEntity> getExamCheats(String process_order);
}
