package com.team3.code_nova.backend.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BoardCategory {
    BRAND("브랜드"),
    ART("아트"),
    CULTURE("컬쳐"),
    PLACE("플레이스"),
    PEOPLE("피플");

    private final String value;
}