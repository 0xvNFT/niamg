package com.play.model.app;

import com.play.model.AppTab;
import com.play.model.vo.AppThirdGameVo;

import java.util.List;

public class LobbyGame {
    AppTab tab;
    List<AppThirdGameVo> games;

    public AppTab getTab() {
        return tab;
    }

    public void setTab(AppTab tab) {
        this.tab = tab;
    }

    public List<AppThirdGameVo> getGames() {
        return games;
    }

    public void setGames(List<AppThirdGameVo> games) {
        this.games = games;
    }
}
