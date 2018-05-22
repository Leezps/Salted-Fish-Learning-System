package com.android.leezp.learncarproject.db;

import com.android.leezp.learncarproject.entity.HelpEntity;

import java.util.List;

public class HelpDB {
    private int state;
    private String message;
    private List<HelpEntity> help;

    public HelpDB(int state, String message, List<HelpEntity> help) {
        this.state = state;
        this.message = message;
        this.help = help;
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

    public List<HelpEntity> getHelp() {
        return help;
    }

    public void setHelp(List<HelpEntity> help) {
        this.help = help;
    }
}
