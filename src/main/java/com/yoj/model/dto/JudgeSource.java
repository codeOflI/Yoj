package com.yoj.model.dto;

import lombok.Data;

@Data
/**
 * Source is needed by judge.
 */
public class JudgeSource {
    private Integer solutionId;
    // to update score
    private Integer userId;
    private String code;
    private Integer problemId;
    private Integer language;
    //内存限制mb
    private Integer memoryLimit;
    //时间限制ms
    private Integer timeLimit;

}
