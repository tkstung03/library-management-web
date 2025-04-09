package com.example.librarymanagement.base;

import com.example.librarymanagement.constant.RestStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RestData<T> {
    public RestStatus status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public RestData(T data) {
        this.status = RestStatus.SUCCESS;
        this.data = data;
    }

    public static RestData<?> error(Object message) {return new RestData<>(RestStatus.ERROR, message, null); }
}
