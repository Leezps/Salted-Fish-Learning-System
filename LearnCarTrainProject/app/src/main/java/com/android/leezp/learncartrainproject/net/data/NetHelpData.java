package com.android.leezp.learncartrainproject.net.data;

import com.android.leezp.learncartrainproject.entities.HelpEntity;

import java.util.List;

public class NetHelpData {
    private int state;
    private String message;
    private List<HelpEntity> help;

    public NetHelpData(int state, String message, List<HelpEntity> help) {
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
