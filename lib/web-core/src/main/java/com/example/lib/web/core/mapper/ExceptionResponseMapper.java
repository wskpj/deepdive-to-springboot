package com.example.lib.web.core.mapper;

import com.example.lib.web.core.dto.ApiError;

public interface ExceptionResponseMapper {

    boolean supports(Exception e);

    ApiError map(Exception e, String uri);
}
