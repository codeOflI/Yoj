package com.yoj.utils.file;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class CaseFileUtil {
    @Autowired
    private ProblemFileUtil problemFileUtil;

    public ResponseEntity<byte[]> getCaseFile(Integer problemId, Integer caseId, Integer inOrOut) {
        String fileSavePath = null;
        if (inOrOut == 0) {
            fileSavePath = this.getInputFileFullName(problemId, caseId);
        } else {
            fileSavePath = this.getOutFileFullName(problemId, caseId);
        }
        return buildResponseEntity(new File(fileSavePath));
    }

    public String getInputFileFullName(Integer problemId, int fileId) {
        return problemFileUtil.getProblemDirPath(problemId) + File.separator + "input" + fileId + ".txt";
    }

    public String getOutFileFullName(Integer problemId, int fileId) {
        return problemFileUtil.getProblemDirPath(problemId) + File.separator + "output" + fileId + ".txt";
    }


    //读取文件
    private ResponseEntity<byte[]> buildResponseEntity(File file) {
        byte[] body = null;
        try {
            body = FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        HttpHeaders headers = new HttpHeaders();
        //设置文件类型
        headers.add("Content-Disposition", "attchement;filename=" + file.getName());
        //设置Http状态码
        HttpStatus statusCode = HttpStatus.OK;
        //返回数据
        ResponseEntity<byte[]> entity = new ResponseEntity<>(body, headers, statusCode);
        return entity;
    }
}
