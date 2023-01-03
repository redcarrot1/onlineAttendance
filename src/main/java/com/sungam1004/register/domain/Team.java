package com.sungam1004.register.domain;

import com.sungam1004.register.exception.CustomException;
import com.sungam1004.register.exception.ErrorCode;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public enum Team {
    ATeam, BTeam, CTeam;

    public static List<String> getTeamNameList() {
        return Stream.of(Team.values())
                .map(Enum::name)
                .toList();
    }

    public static Team convertTeamByString(String strTeam) {
        return Stream.of(Team.values())
                .filter(team -> Objects.equals(team.toString(), strTeam))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TEAM));
    }
}
