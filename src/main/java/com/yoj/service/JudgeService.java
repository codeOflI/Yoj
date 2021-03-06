package com.yoj.service;

import com.alibaba.fastjson.JSON;
import com.yoj.model.dto.JudgeSource;
import com.yoj.model.entity.Solution;
import com.yoj.utils.AppInfo;
import com.yoj.utils.HttpUtil;
import com.yoj.utils.JudgePermitUtil;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JudgeService {
    @Autowired
    private HttpUtil httpUtil;
    @Autowired
    private JudgePermitUtil judgePermitUtil;
    @Autowired
    private SolutionService solutionService;
    @Autowired
    private AppInfo appInfo;

    public void judge(JudgeSource judgeSource) {
        String jsonString = JSON.toJSONString(judgeSource);
        StringEntity entity = new StringEntity(jsonString, "UTF-8");
        List<BasicHeader> headers = new ArrayList<>();
        headers.add(new BasicHeader("Content-Type","application/json;charset=utf8"));
        headers.add(judgePermitUtil.getJudgePermitHeader());
        Solution solution = httpUtil.doPostWithRequestBody(appInfo.getJudgeUrl(),
                entity, headers);
        // only local have return info
        if(appInfo.isLocal()){
            solutionService.updateAfterJudge(solution);
        }
    }
}



