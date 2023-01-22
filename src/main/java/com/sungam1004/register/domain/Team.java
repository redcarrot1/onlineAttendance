package com.sungam1004.register.domain;

import com.sungam1004.register.exception.CustomException;
import com.sungam1004.register.exception.ErrorCode;

import java.util.List;
import java.util.stream.Stream;

public enum Team {
    ATeam, BTeam, CTeam, DTeam, ETeam;

    public static List<String> getTeamNameList() {
        return Stream.of(Team.values())
                .map(Enum::name)
                .toList();
    }

    public static Team convertTeamByString(String strTeam) {
        try {
            return Team.valueOf(strTeam);
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.NOT_FOUND_TEAM);
        }
    }
}
