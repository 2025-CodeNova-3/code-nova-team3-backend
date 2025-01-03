package com.team3.code_nova.backend.dto.request;

import com.team3.code_nova.backend.enums.BoardCategory;
import lombok.Getter;

@Getter
public class BoardCreateRequest {

    private BoardCategory boardCategory;

    private String title;

    private String openContent;

    private String hiddenContent;

    private Integer openDuration;
}