package com.sungam1004.register.domain;

import java.util.List;
import java.util.stream.Stream;

public enum Team {
    ATeam, BTeam, CTeam;

    public static List<String> getTeamNameList(){
        return Stream.of(Team.values())
                .map(Enum::name)
                .toList();
    }
}
