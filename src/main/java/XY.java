
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class XY {

	public XY(int month,int day,int  terminalDay) throws Exception {
		int KSJC = 0, JSJC = 0;
		String date,data;
		PrintWriter printWriter = null;
		int paraTransfer = 0, time = 0; // time是时段
		Character para = 'a'; // para是参数
		// 外层最大的循环
		while (day <= terminalDay) {
			date = "2021-"+month+"-" + day;
			time = 0;
			int colorNumber = 0;
			printWriter = new PrintWriter(new File(Path.getPath()+"/1/1"+month + day + ".json"));
			printWriter.print("[");

			while (time < 1000) {
				if (time == 0 || time == 1 || time == 2 || time == 3)
					printWriter.print("{\"a\":\"\",\"b\":\"\",\"c\":\"\",\"d\":\"" + time + "\"},");
				// 发送post请求
				if (time == 0) {
					KSJC = 1;
					JSJC = 12;
					colorNumber = 0;
				}
				if (time == 1) {
					KSJC = 1;
					JSJC = 5;
					colorNumber = 0;
				}
				if (time == 2) {
					KSJC = 6;
					JSJC = 9;
					colorNumber = 0;
				}
				if (time == 3) {
					KSJC = 10;
					JSJC = 12;
					colorNumber = 0;
				}
				if (time == 112) {
					KSJC = 1;
					JSJC = 2;
					colorNumber = 1;
				}
				if (time == 135) {
					KSJC = 3;
					JSJC = 5;
					colorNumber = 2;
				}
				if (time == 267) {
					KSJC = 6;
					JSJC = 7;
					colorNumber = 1;
				}
				if (time == 289) {
					KSJC = 8;
					JSJC = 9;
					colorNumber = 2;
				}

				String postBody = "XXXQDM=1&JXLDM=117,123,131&JASLXDM=02&KXRQ=" + date + "&KSJC=" + KSJC + "&JSJC="
						+ JSJC;
				data = post.sendPost(Path.getKey(),
						postBody);
				int begin = data.indexOf("rows") + 6;
				int end = data.lastIndexOf("]");
				data = data.substring(begin, end + 1); // data是初始的的JSON数组
				//
				List<assistClass> userList = JSON.parseArray(data, assistClass.class); // userList是原始JSON的对象数组
				String emptyClass;
				// emptyClassArray是为了排除干扰和排序
				ArrayList<String> emptyClassArray = new ArrayList<String>();
				for (assistClass assist : userList) { // assist是原始JSON的对象
					emptyClass = assist.JASMC.substring(2, assist.JASMC.length());// emptyClass是教室名称
					emptyClassArray.add(emptyClass);
				}
				// 排序
				java.util.Collections.sort(emptyClassArray);
				/* 至此，emptyClassArray里面均为阶梯教室或者二教教室 */

				int inline = 0;
				// 开始阶梯教室
				printWriter.print("{\"a\":\"1\",\"b\":\"第一教学楼(" + KSJC + "~" + JSJC + "节)\",\"c\":\"" + colorNumber
						+ "\",\"d\":\"\"},");
				int ladderNumber = 0;
				paraTransfer = 0;
				for (String emptyNumber : emptyClassArray) {
					if (paraTransfer == 4)
						paraTransfer = 0;
					para = (char) ('a' + paraTransfer);
					if (emptyNumber.matches(".*1-.*")) {
						emptyNumber = emptyNumber.substring(2, emptyNumber.length());
						if (inline == 0) {
							printWriter.print("{");
						}
						if (inline == 3) {
							printWriter.print("\"" + para + "\":" + "\"" + emptyNumber + "\"" + "},");
							inline = 0;
						} else {
							printWriter.print("\"" + para + "\":" + "\"" + emptyNumber + "\"" + ",");
							inline++;
						}
						paraTransfer++;
					}
				}
				switch (inline) {
				case 1:
					printWriter.print("\"b\":\"\",\"c\":\"\",\"d\":\"\"},");
					break;
				case 2:
					printWriter.print("\"c\":\"\",\"d\":\"\"},");
					break;
				case 3:
					printWriter.print("\"d\":\"\"},");
					break;
				}

				// 非阶梯的输出
				printWriter.print("{\"a\":\"1\",\"b\":\"第二教学楼(" + KSJC + "~" + JSJC + "节)\",\"c\":\"" + colorNumber
						+ "\",\"d\":\"\"},");
				paraTransfer = 0;
				inline = 0;
				ladderNumber = 0;
				for (String emptyNumber : emptyClassArray) {
					if (emptyNumber.matches(".*2-.*")) {
						emptyNumber = emptyNumber.substring(2, emptyNumber.length());
						ladderNumber++;
						if (paraTransfer == 4)
							paraTransfer = 0;
						para = (char) ('a' + paraTransfer);
						if (inline == 0) {
							printWriter.print("{");
						}
						if (inline == 3) {
							printWriter.print("\"" + para + "\":" + "\"" + emptyNumber + "\"" + "},");
							inline = 0;
						} else {
							printWriter.print("\"" + para + "\":" + "\"" + emptyNumber + "\"" + ",");
							inline++;
						}
						paraTransfer++;
					}
				}
				switch (inline) {
				case 1:
					printWriter.print("\"b\":\"\",\"c\":\"\",\"d\":\"\"},");
					break;
				case 2:
					printWriter.print("\"c\":\"\",\"d\":\"\"},");
					break;
				case 3:
					printWriter.print("\"d\":\"\"},");
					break;
				}
				if (ladderNumber == 0)
					printWriter.print("{\"a\":\"无\",\"b\":\"\",\"c\":\"\",\"d\":\"\"},");

				// 四教
				printWriter.print("{\"a\":\"1\",\"b\":\"第四教学楼(" + KSJC + "~" + JSJC + "节)\",\"c\":\"" + colorNumber
						+ "\",\"d\":\"\"},");
				paraTransfer = 0;
				inline = 0;
				ladderNumber = 0;
				for (String emptyNumber : emptyClassArray) {
					if (emptyNumber.matches(".*4-.*")) {
						emptyNumber = emptyNumber.substring(2, emptyNumber.length());
						ladderNumber++;
						if (paraTransfer == 4)
							paraTransfer = 0;
						para = (char) ('a' + paraTransfer);
						if (inline == 0) {
							printWriter.print("{");
						}
						if (inline == 3) {
							printWriter.print("\"" + para + "\":" + "\"" + emptyNumber + "\"" + "},");
							inline = 0;
						} else {
							printWriter.print("\"" + para + "\":" + "\"" + emptyNumber + "\"" + ",");
							inline++;
						}
						paraTransfer++;
					}
				}
				switch (inline) {
				case 0:
					if (time == 3)
						printWriter.print("{\"a\":\"1\",\"b\":\"到底了~\",\"c\":\"" + colorNumber + "\",\"d\":\"\"}");
					else if (time == 135 || time == 289||time==0 )
						printWriter.print("{\"a\":\"1\",\"b\":\"到底了~\",\"c\":\"" + colorNumber + "\",\"d\":\"\"},");
					break;
				case 1:
					printWriter.print("\"b\":\"\",\"c\":\"\",\"d\":\"\"},");
					if (time == 3)
						printWriter.print("{\"a\":\"1\",\"b\":\"到底了~\",\"c\":\"" + colorNumber + "\",\"d\":\"\"}");
					else if (time == 135 || time == 289||time==0 )
						printWriter.print("{\"a\":\"1\",\"b\":\"到底了~\",\"c\":\"" + colorNumber + "\",\"d\":\"\"},");
					break;
				case 2:
					printWriter.print("\"c\":\"\",\"d\":\"\"},");
					if (time == 3)
						printWriter.print("{\"a\":\"1\",\"b\":\"到底了~\",\"c\":\"" + colorNumber + "\",\"d\":\"\"}");
					else if (time == 135 || time == 289||time==0 )
						printWriter.print("{\"a\":\"1\",\"b\":\"到底了~\",\"c\":\"" + colorNumber + "\",\"d\":\"\"},");
					break;
				case 3:
					printWriter.print("\"d\":\"\"},");
					if (time == 3)
						printWriter.print("{\"a\":\"1\",\"b\":\"到底了~\",\"c\":\"" + colorNumber + "\",\"d\":\"\"}");
					else if (time == 135 || time == 289||time==0 )
						printWriter.print("{\"a\":\"1\",\"b\":\"到底了~\",\"c\":\"" + colorNumber + "\",\"d\":\"\"},");
					break;
				}
				/*
				 * if(time==0) {
				 * printWriter.print("{\"a\":\"\",\"b\":\"课程表时间\",\"c\":\"2\",\"d\":\"11\"},");
				 * printWriter.
				 * print("{\"a\":\"1~2节  08:00-09:35\",\"b\":\"3~5节  09:50-12:15\",\"c\":\"\",\"d\":\"10\"},"
				 * ); printWriter.
				 * print("{\"a\":\"6~7节  13:30-15:05\",\"b\":\"8~9节  15:20-16:55\",\"c\":\"\",\"d\":\"10\"},"
				 * ); printWriter.
				 * print("{\"a\":\"10~12节  18:30-20:55\",\"b\":\"\",\"c\":\"\",\"d\":\"10\"},");
				 * printWriter.print("{\"a\":\"\",\"b\":\"到底了~\",\"c\":\"2\",\"d\":\"11\"},"); }
				 */
				// time的跳转关系
				switch (time) {
				case 0:
					time = 1;
					break;
				case 1:
					time = 112;
					break;
				case 112:
					time = 135;
					break;
				case 135:
					time = 2;
					break;
				case 2:
					time = 267;
					break;
				case 267:
					time = 289;
					break;
				case 289:
					time = 3;
					break;
				case 3:
					time = 1000;
					break;
				}
			}

			printWriter.print("]");
			printWriter.close();
			System.out.print(day + "号 ");
			day++;
		}
	}

}
