package com.pkg.occasion.api.response;

import lombok.Builder;

@Builder
public class Format {
    public int code;
    public String message;
    public Object result;
    public long time;
}
