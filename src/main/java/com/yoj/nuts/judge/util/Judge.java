//package judge;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.OutputStream;
//import java.io.PrintWriter;
//
//import org.apache.logging.log4j.util.PropertiesUtil;
//
//import com.yoj.nuts.judge.bean.ExecMessage;
//import com.yoj.nuts.judge.bean.Results;
//
//import bean.JudgeTask;
//
////@Log4j
//public class Judge {
//
//	public static Results judge(JudgeTask task) {
//		Results result = new Results();
//		result.setSubmitId(task.getSubmitId());
//		//String path = PropertiesUtil.StringValue("workspace") + "/" + task.getSubmitId();
//		String path = "/opt" + "/" + task.getSubmitId();
//
//		File file = new File(path);
//		file.mkdirs();
//		try {
//			createFile(task.getCompilerId(), path, task.getSource());
//		} catch (Exception e) {
//			e.printStackTrace();
//			result.setStatus(8);
//			ExecutorUtil.exec("rm -rf " + path);
//			return result;
//		}
//		// compile the source
//		String message = complie(task.getCompilerId(), path);
//		if (message != null && task.getCompilerId() != 4) {
//			result.setStatus(7);
//			result.setErrorMessage(message);
//			ExecutorUtil.exec("rm -rf " + path);
//			return result;
//		}
//		// chmod -R 755 path
//		ExecutorUtil.exec("chmod -R 755 " + path);
//		// judge
//		String process = process(task.getCompilerId(), path);
////		String judge_data = PropertiesUtil.StringValue("judge_data") + "/" + task.getProblemId();
//		String cmd = "python " + PropertiesUtil.StringValue("judge_script") + " " + process + " " + judge_data + " "
//				+ path + " " + task.getTimeLimit() + " " + task.getMemoryLimit();
//		parseToResult(cmd, result);
//		ExecutorUtil.exec("rm -rf " + path);
//		return result;
//	}
//
//	private static void createFile(int compilerId, String path, String source) throws Exception {
//		String filename = "";
//		switch (compilerId) {
//		case 1:
//			filename = "main.c";
//			break;
//		case 2:
//			filename = "main.cpp";
//			break;
//		case 3:
//			filename = "Main.java";
//			break;
//		case 4:
//			filename = "main.pas";
//			break;
//		case 5:
//			filename = "main.py";
//			break;
//		}
//		File file = new File(path + "/" + filename);
//		file.createNewFile();
//		OutputStream output = new FileOutputStream(file);
//		PrintWriter writer = new PrintWriter(output);
//		writer.print(source);
//		writer.close();
//		output.close();
//	}
//
//	private static String complie(int compilerId, String path) {
//		/**
//		 * '1': 'gcc','g++', '3': 'java', '4': 'pascal', '5': 'python',
//		 */
//		String cmd = "";
//		switch (compilerId) {
//		case 1:
//			cmd = "gcc " + path + "/main.c -o " + path + "/main";
//			break;
//		case 2:
//			cmd = "g++ " + path + "/main.cpp -o " + path + "/main";
//			break;
//		case 3:
//			cmd = "javac " + path + "/Main.java";
//			break;
//		case 4:
//			cmd = "fpc " + path + "/main.pas -O2 -Co -Ct -Ci";
//			break;
//		case 5:
//			cmd = "python3 -m py_compile " + path + "/main.py";
//			break;
//		}
//		return ExecutorUtil.exec(cmd).getError();
//	}
//
//	private static String process(int compileId, String path) {
//		switch (compileId) {
//		case 1:
//			return path + "/main";
//		case 2:
//			return path + "/main";
//		case 3:
//			return "javawzy-classpathwzy" + path + "wzyMain";
//		case 4:
//			return path + "/main";
////		case 5:
////			return "python3wzy" + path + "/__pycache__/" + PropertiesUtil.StringValue("python_cacheName");
//		}
//		return null;
//	}
//
//	private static void parseToResult(String cmd, Results result) {
//		ExecMessage exec = ExecutorUtil.exec(cmd);
//		if (exec.getError() != null) {
//			result.setStatus(5);
//			result.setErrorMessage(exec.getError());
//			System.out.println("=====error====" + result.getSubmitId() + ":" + exec.getError());
//			// log.error("=====error====" + result.getSubmitId() + ":" + exec.getError());
//		} else {
//			// Stdout out = JSON.parseObject(exec.getStdout(), Stdout.class);
//			System.out.println("=====stdout====" + exec.getStdout());
//			// log.info("=====stdout====" + out);
////      result.setStatus(out.getStatus());
////      result.setTimeUsed(out.getMax_time().intValue());
////      result.setMemoryUsed(out.getMax_memory().intValue());
//		}
//	}
//
//}