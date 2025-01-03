package com.team3.code_nova.backend.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BoardCategory {
    ESSAY("수필"),
    POEM("시"),
    NOVEL("소설"),
    BOOK("독후감"),
    SOCIAL("사회/문화"),
    ROMANCE("로맨스"),
    COMEDY("코미디"),
    SF("SF"),
    FANTASY("판타지");

    private final String value;
}