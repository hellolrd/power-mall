package com.powernode.model;

import com.powernode.constant.AuthConstant;
import com.powernode.constant.BusinessEnum;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "统一返回结果")
public class Result<T> implements Serializable {
    private Integer code; // 状态码
    private String msg; // 提示信息

    private T data; // 数据


    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(BusinessEnum.SUCCESS.getCode());
        result.setMsg(BusinessEnum.SUCCESS.getMsg());// 成功状态码
        result.setData(data); // 成功状态码
        return result;
    }

    public static <T> Result<T> fail(Integer code, String msg) {
        Result<T> result = new Result<>();
        result.setCode(code); // 失败状态码
        result.setMsg(msg);
        result.setData(null);// 失败提示信息
        return result;
    }
    public static <T> Result<T> fail(BusinessEnum businessEnum) {
        Result<T> result = new Result<>();
        result.setCode(businessEnum.getCode()); // 失败状态码
        result.setMsg(businessEnum.getMsg());
        result.setData(null);// 失败提示信息
        return result;
    }


    public static Result<String> handle(boolean flag) {
        Result<Object> result = new Result<>();
        if(flag){
            return Result.success("操作成功" );
        }
        return Result.fail(BusinessEnum.OPERATION_FAIL);
    }
}
