package com.yoj.utils.file;

import com.yoj.model.dto.JudgeCase;
import com.yoj.model.entity.Problem;
import com.yoj.model.properties.JudgeProperties;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ProblemFileUtil {

    @Autowired
    private JudgeProperties judgeProperties;

    public boolean createProblemFile(Problem problem) {
        String dirPath = this.getProblemDirPath(problem.getProblemId());
        List<JudgeCase> judgeData = problem.getJudgeData();
        if (judgeData == null) {
            return false;
        }
        File file = new File(dirPath);
        //先形成一个空目录
        if (file.exists()) {
            try {
                FileUtils.cleanDirectory(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            file.mkdirs();
        }
        //创建子文件
        for (int judgeNumber = 0; judgeNumber < judgeData.size(); judgeNumber++) {
            JudgeCase judgeCase = judgeData.get(judgeNumber);
            try {
                // input(caseId + 1).txt / ... caseId begin with 1
                FileUtils.write(new File(dirPath + "/input" + (judgeNumber + 1) + ".txt"), judgeCase.getIn(), "utf-8");
                FileUtils.write(new File(dirPath + "/output" + (judgeNumber + 1) + ".txt"), judgeCase.getOut(), "utf-8");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return true;
    }

    public boolean updateProblemFile(int problemId, List<JudgeCase> judgeData) {
        String dirPath = this.getProblemDirPath(problemId);
        if (judgeData == null) {
            return false;
        }
        File dir = new File(dirPath);
        // dir doesn't exist or isn't directory
        if (!dir.exists() || !dir.isDirectory()) {
            return false;
        }
        File[] files = dir.listFiles();
        int dataSize = judgeData.size();
        //numbers of file is bigger or equal,don't need remove file.
        if (dataSize * 2 >= files.length) {
            for (int i = 0; i < judgeData.size(); i++) {
                try {
                    FileUtils.write(new File(this.getInputFileFullName(dirPath, i + 1)), judgeData.get(i).getIn(), "utf-8");
                    FileUtils.write(new File(this.getOutFileFullName(dirPath, i + 1)), judgeData.get(i).getOut(), "utf-8");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            for (int i = dataSize; i < files.length / 2; i++) {
                File file = new File(getInputFileFullName(dirPath, i + 1));
                if (file.exists()) {
                    file.delete();
                }

                file = new File(getOutFileFullName(dirPath, i + 1));
                if (file.exists()) {
                    file.delete();
                }
            }
        }

        return true;
    }

    /**
     * 根据pid返回相应的文件夹
     *
     * @param problemId
     * @return
     */
    public String getProblemDirPath(Integer problemId) {
        if(judgeProperties.isLocal()){
            return judgeProperties.getLinuxFilePath() + File.separator + problemId;
        }else {
            return judgeProperties.getWindowsFilePath() + File.separator + problemId;
        }
    }

    public List<JudgeCase> getJudgeData(Integer problemId) {
        File dir = new File(this.getProblemDirPath(problemId));
        // dir doesn't exist or isn't directory
        if (!dir.exists() || !dir.isDirectory()) {
            return null;
        }
        File[] files = dir.listFiles();
        JudgeCase[] judgeData = new JudgeCase[files.length / 2];
        for (File file : files) {
            String fileName = file.getName();
            Pattern p = Pattern.compile("\\d+");
            Matcher m = p.matcher(fileName);
            int caseId = 0;
            if (m.find()) {
                String s = m.group();
                // caseId begin with 1
                caseId = Integer.valueOf(s) - 1;
            } else {
                System.out.println("not find caseId");
            }
            if (judgeData[caseId] == null) {
                judgeData[caseId] = new JudgeCase();
            }
            try {
                String fileContent = FileUtils.readFileToString(file, "utf-8");
                if (fileName.startsWith("input")) {
                    judgeData[caseId].setIn(fileContent);
                } else {
                    judgeData[caseId].setOut(fileContent);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Arrays.asList(judgeData);
    }

    public String getInputFileFullName(String dirPath, int fileId) {
        return dirPath + File.separator + "input" + fileId + ".txt";
    }

    public String getOutFileFullName(String dirPath, int fileId) {
        return dirPath + File.separator + "output" + fileId + ".txt";
    }
}
