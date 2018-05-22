package com.android.leezp.learncarproject.db;

import com.android.leezp.learncarproject.entity.ExamCheatsEntity;

import java.util.List;

public class ExamCheatsDB {
    private int state;
    private String message;
    private List<ExamCheatsEntity> examCheats;

    public ExamCheatsDB(int state, String message, List<ExamCheatsEntity> examCheats) {
        super();
        this.state = state;
        this.message = message;
        this.examCheats = examCheats;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ExamCheatsEntity> getExamCheats() {
        return examCheats;
    }

    public void setExamCheats(List<ExamCheatsEntity> examCheats) {
        this.examCheats = examCheats;
    }
}
