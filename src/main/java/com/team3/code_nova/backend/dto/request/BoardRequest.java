package com.team3.code_nova.backend.dto.request;

import com.team3.code_nova.backend.enums.BoardCategory;
import lombok.Getter;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
public class BoardRequest {

    private BoardCategory boardCategory;

    private String title;

    private String openContent;

    private String hiddenContent;

    private Integer openDuration;
}