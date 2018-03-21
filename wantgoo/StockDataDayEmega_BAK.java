package wantgoo;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import stock5.conn.ConnectionDAO;

//股票資料(http://www.wantgoo.com/CorpInfom.aspx?StockNo=6244) (玩股網)
public class StockDataDayEmega_BAK {

	public static void main(String[] args) throws Exception {
		/***** 參數設定 *****/		
		String s1 = "1101,1102,1103,1104,1107,1108,1109,1110,1201,1203,1210,1213,1215,1216,1217,1218,1219,1220,1225,1227,1229,1231,1232,1233,1234,1235,1236,1301,1303,1304,1305,1307,1308,1309,1310,1312,1313,1314,1315,1316,1319,1321,1323,1324,1325,1326,1333,1336,1402,1409,1410,1413,1414,1416,1417,1418,1419,1423,1432,1434,1435,1436,1437,1438,1439,1440,1441,1442,1443,1444,1445,1446,1447,1449,1451,1452,1453,1454,1455,1456,1457,1459,1460,1463,1464,1465,1466,1467,1468,1469,1470,1471,1472,1473,1474,1475,1476,1477,1503,1504,1506,1507,1512,1513,1514,1515,1516,1517,1519,1521,1522,1523,1524,1525,1526,1527,1528,1529,1530,1531,1532,1533,1535,1536,1537,1538,1539,1540,1541,1558,1560,1565,1569,1570,1580,1582,1583,1584,1603,1604,1605,1606,1608,1609,1611,1612,1613,1614,1615,1616,1617,1618,1701,1702,1704,1707,1708,1709,1710,1711,1712,1713,1714,1715,1716,1717,1718,1720,1721,1722,1723,1724,1725,1726,1727,1729,1730,1731,1732,1733,1734,1735,1736,1737,1742,1752,1773,1777,1781,1784,1785,1787,1788,1795,1799,1802,1805,1806,1807,1808,1809,1810,1813,1815,1902,1903,1904,1905,1906,1907,1909,2002,2006,2007,2008,2009,2010,2012,2013,2014,2015,2017,2020,2022,2023,2024,2025,2027,2028,2029,2030,2031,2032,2033,2034,2035,2038,2049,2059,2062,2101,2102,2103,2104,2105,2106,2107,2108,2109,2114,2201,2204,2206,2207,2208,2221,2227,2231,2301,2302,2303,2305,2308,2311,2312,2313,2314,2315,2316,2317,2321,2323,2324,2325,2326,2327,2328,2329,2330,2331,2332,2337,2338,2340," +
				"2341,2342,2344,2345,2347,2348,2349,2350,2351,2352,2353,2354,2355,2356,2357,2358,2359,2360,2361,2362,2363,2364,2365,2367,2368,2369,2371,2373,2374,2375,2376,2377,2379,2380,2381,2382,2383,2384,2385,2387,2388,2390,2391,2392,2393,2395,2396,2397,2399,2401,2402,2403,2404,2405,2406,2408,2409,2410,2411,2412,2413,2414,2415,2417,2419,2420,2421,2423,2424,2425,2426,2427,2428,2429,2430,2431,2432,2433,2434,2436,2437,2438,2439,2440,2441,2442,2443,2444,2446,2448,2449,2450,2451,2452,2453,2454,2455,2456,2457,2458,2459,2460,2461,2462,2463,2464,2465,2466,2467,2468,2471,2472,2473,2474,2475,2476,2477,2478,2479,2480,2481,2482,2483,2484,2485,2486,2488,2489,2491,2492,2493,2494,2495,2496,2497,2498,2499,2501,2504,2505,2506,2509,2511,2514,2515,2516,2520,2524,2526,2527,2528,2530,2534,2535,2536,2537,2538,2539,2540,2542,2543,2545,2546,2547,2548,2596,2597,2601,2603,2605,2606,2607,2608,2609,2610,2611,2612,2613,2614,2615,2616,2617,2618,2636,2701,2702,2704,2705,2706,2707,2801,2809,2812,2816,2820,2823,2832,2833,2834,2836,2837,2838,2841,2845,2847,2849,2850,2851,2852,2854,2855,2856,2880,2881,2882,2883,2884,2885,2886,2887,2888,2889,2890,2891,2892,2901,2903,2904,2905,2906,2908,2910,2911,2912,2913,2915,2916,2921,3002,3003,3004,3005,3006,3008,3009,3010,3011,3013,3014,3015,3016,3017,3018,3019,3021,3022,3023,3024,3025,3026,3027,3028,3029,3030,3031,3032,3033,3034,3035,3036,3037,3038,3040,3041,3042,3043,3044,3045,3046,3047,3048,3049,3050,3051,3052,3054,3055,3056,3057,3058,3059,3060,3061,3062,3063,3064,3066,3067,3068,3069,3071,3073,3078,3080,3083,3085,3086,3087,3088,3089,3090,3093,3094,3095,3099,3114,3115,3118,3126,3128,3130,3144,3152,3162,3164,3169,3171,3188,3189,3191,3202,3205,3206,3207,3209,3211,3213,3217,3218,3219,3221,3224,3226,3227,3228,3229,3230,3231,3232,3236,3252,3259,3260,3264,3265,3266,3268,3276,3284,3287,3288,3289,3290,3291,3293,3294,3296,3297,3298,3299,3303,3305,3306,3308,3310," +
				"3311,3312,3313,3315,3317,3322,3323,3324,3325,3332,3339,3354,3356,3360,3362,3367,3369,3372,3373,3376,3380,3383,3388,3389,3390,3402,3406,3416,3419,3431,3434,3438,3443,3444,3450,3452,3454,3455,3465,3466,3474,3481,3483,3484,3489,3490,3491,3494,3498,3499,3501,3504,3508,3511,3512,3514,3515,3516,3518,3519,3520,3521,3522,3523,3526,3527,3531,3532,3533,3534,3535,3536,3537,3540,3541,3545,3546,3548,3551,3552,3553,3556,3557,3559,3561,3562,3570,3573,3576,3577,3579,3587,3588,3593,3596,3605,3607,3611,3614,3615,3617,3622,3623,3625,3630,3652,3653,3701,3702,3703,4102,4103,4104,4105,4106,4107,4108,4109,4111,4113,4114,4119,4120,4121,4123,4126,4127,4128,4129,4131,4133,4205,4207,4303,4304,4305,4306,4401,4402,4406,4408,4413,4414,4415,4416,4417,4419,4420,4426,4502,4503,4506,4510,4513,4523,4526,4527,4528,4529,4530,4532,4533,4534,4535,4609,4702,4703,4706,4707,4711,4712,4714,4716,4720,4721,4722,4725,4729,4733,4801,4903,4904,4905,4906,4907,4908,4909,5007,5009,5011,5013,5014,5015,5016,5102,5201,5202,5203,5204,5205,5206,5207,5209,5210,5211,5212,5213,5301,5302,5304,5305,5306,5309,5310,5312,5314,5315,5317,5318,5321,5324,5326,5328,5340,5344,5345,5346,5347,5348,5349,5351,5353,5355,5356,5364,5371,5381,5383,5384,5386,5387,5388,5392,5395,5398,5403,5410,5425,5426,5432,5434,5438,5439,5443,5450,5452,5455,5457,5460,5464,5465,5466,5467,5468,5469,5471,5474,5475,5478,5480,5481,5483,5484,5487,5488,5489,5490,5491,5492,5493,5498,5501,5505,5506,5508,5511,5512,5514,5515,5516,5519,5520,5521,5522,5523,5525,5529,5530,5531,5533,5534,5601,5603,5604,5607,5608,5609,5701,5703,5704,5706,5820,5854,5902,5903,5904,5905,6005,6008,6012,6015,6016,6020,6021,6022,6023,6024,6101,6103,6104,6105,6107,6108,6109,6111,6112,6113,6114,6115,6116,6117,6118,6119,6120,6121,6122,6123,6124,6125,6126,6127,6128,6129,6130,6131,6133,6134,6135,6136,6138,6139,6140,6141,6142,6143,6144,6145,6146,6147,6148,6149,6150,6151,6152,6153,6154,6155,6156,6158," +
				"6159,6160,6161,6163,6164,6165,6166,6167,6168,6169,6170,6171,6172,6173,6174,6175,6176,6177,6179,6180,6182,6183,6184,6185,6186,6187,6188,6189,6190,6191,6192,6194,6195,6196,6197,6198,6199,6201,6202,6203,6204,6205,6206,6207,6208,6209,6210,6211,6212,6213,6214,6215,6216,6217,6218,6219,6220,6221,6222,6223,6224,6225,6226,6227,6228,6229,6230,6231,6232,6233,6234,6235,6236,6237,6238,6239,6240,6241,6242,6243,6244,6245,6246,6247,6248,6250,6251,6255,6257,6259,6261,6263,6264,6265,6266,6269,6270,6271,6274,6275,6276,6277,6278,6279,6281,6282,6283,6284,6285,6286,6287,6289,6290,6291,6292,6294,6298,6402,6505,6508,6509,6603,6605,6609,8008,8016,8021,8024,8032,8033,8034,8039,8040,8042,8043,8044,8046,8047,8048,8049,8050,8053,8054,8059,8064,8066,8067,8068,8069,8070,8071,8072,8074,8076,8077,8078,8079,8080,8081,8082,8083,8084,8085,8086,8087,8088,8091,8092,8093,8096,8097,8099,8101,8103,8105,8107,8109,8110,8111,8112,8114,8121,8131,8163,8182,8183,8201,8210,8213,8234,8240,8249,8255,8261,8266,8277,8287,8289,8299,8349,8354,8374,8383,8390,8401,8705,8905,8906,8908,8913,8916,8917,8921,8923,8924,8925,8926,8927,8928,8929,8930,8931,8932,8933,8934,8935,8936,8937,8938,8940,8941,8942,8996,9101,9102,9103,910322,9104,910482,9105,910579,9106,9110,911201,911602,911868,912398,9136,913889,9151,9157,9188,9902,9904,9905,9906,9907,9908,9910,9911,9912,9914,9917,9918,9919,9921,9922,9924,9925,9926,9927,9928,9929,9930,9931,9933,9934,9935,9937,9938,9939,9940,9941,9942,9943,9944,9945,9946,9949,9950,9951,9955,9958,9960,9962";
		
		//String s1 = "6244";
		
		int sleepTime = 0; //單位：秒
		
		/***** 參數設定 *****/
		
		Calendar calStart = Calendar.getInstance();
		long millisStart = calStart.getTimeInMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
		
		String[] stockNum = s1.split(",");
		
		java.sql.Connection conn = ConnectionDAO.getConnection();
		
		java.sql.PreparedStatement PS1 = null, PS2=null;
		java.sql.ResultSet RS1 = null;
		String query = "insert into wantgoo_stock(stock_id,stock_deal,deal_change,deal_open,deal_percentage," +
		"deal_buy,deal_high,deal_sell,deal_low,total_volume,deal_last," +
		"mean_60_distance,mean_60_distance_rate,EPS,price_volume,no_new_high_low_dates," +
		"rank_duration,create_date,stock_name,ma60,stock_sum) "+
		" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),?,?,?)";
		PS1 = conn.prepareStatement(query);
		
		//***** 新增當日資料先刪除當日資料 *****
		Calendar cal = Calendar.getInstance();
		String dateStr = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH)+1) + "-" + cal.get(Calendar.DATE);
		
//		String sqlDel = "delete from wantgoo_stock where create_date='"+dateStr+"'";
//		PS2 = conn.prepareStatement(sqlDel);
//		PS2.executeUpdate();
//		PS2.close();
		//***** 新增當日資料先刪除當日資料 *****
		
		String errStockId = "";
		int errCount = 0;
		String[] dataArr = new String[19];
		for(int i=0; i<stockNum.length; i++) {
			try {
				//Get URL Content
				//基本資料
				ReaderURL ru = new ReaderURL();
				String strAll = ru.readTextFile2("http://www.emega.com.tw/z/zc/zca/zca_"+stockNum[i]+".djhtm","BIG5");
				strAll = strAll.replaceAll("\t","");
				strAll = strAll.replaceAll("\r\n","");
				strAll = strAll.replaceAll("<td class=\"t3n1\"><p class=\"t3r1\">","");
				strAll = strAll.replaceAll("<td class=\"t3n1\"><p class=\"t3g1\">","");
				strAll = strAll.replaceAll("<td class=\"t3n1\"><p class=\"t3n1\">","");
				strAll = strAll.replaceAll("<td class=\"t3n1\">","");
//				strAll = strAll.replaceAll("Red","");
//				strAll = strAll.replaceAll("Black","");
//				strAll = strAll.replaceAll("Green","");
//				strAll = strAll.replaceAll("background-color:#FFC0C0;","");
//				strAll = strAll.replaceAll("background-color:#C0FFC0;","");
				//System.err.println(strAll);
				
				//成交價
				String replaceStrS = "<td class=\"t4t1\">收盤價</td>";
				String replaceStrE = "</td></tr>";
				String strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
				strSP = strSP.replaceAll(replaceStrS,"");
				strSP = strSP.replaceAll("<td class=\"t3n1\">","");
				strSP = strSP.replaceAll("</td>","");
				dataArr[0] = strSP;
				
				//漲跌
				replaceStrS = "<td class=\"t4t1\">漲跌</td>";
				replaceStrE = "</p></td>";
				strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
				strSP = strSP.replaceAll(replaceStrS,"");
				dataArr[1] = strSP;
				
				//開盤價
				replaceStrS = "<td class=\"t4t1\">開盤價</td>";
				replaceStrE = "</td><td class=\"t4t1\">";
				strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
				strSP = strSP.replaceAll(replaceStrS,"");
				dataArr[2] = strSP;
				//System.err.println(dataArr[2]);
				
				//漲幅
//				replaceStrS = "<span id=\"ctl00_service_FormView1_lblPercentage\" style=\"color:;\">";
//				replaceStrE = "</span></td>";
//				strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
//				strSP = strSP.replaceAll(replaceStrS,"");
//				strSP = strSP.replaceAll("%","");
//				dataArr[3] = strSP;
				
//				//買進價
//				replaceStrS = "<span id=\"ctl00_service_FormView1_lblBuy\" style=\"color:;\">";
//				replaceStrE = "</span></td>";
//				strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
//				strSP = strSP.replaceAll(replaceStrS,"");
//				dataArr[4] = strSP;
				
				//最高價
				replaceStrS = "<td class=\"t4t1\">最高價</td>";
				replaceStrE = "</td><td class=\"t4t1\">";
				strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
				strSP = strSP.replaceAll(replaceStrS,"");
				dataArr[5] = strSP;
				//System.err.println(dataArr[5]);
				
//				//賣出價
//				replaceStrS = "<span id=\"ctl00_service_FormView1_lblSell\" style=\"color:;\">";
//				replaceStrE = "</span></td>";
//				strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
//				strSP = strSP.replaceAll(replaceStrS,"");
//				dataArr[6] = strSP;
				
				//最低價
				replaceStrS = "<td class=\"t4t1\">最低價</td>";
				replaceStrE = "</td><td class=\"t4t1\">";
				strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
				strSP = strSP.replaceAll(replaceStrS,"");
				dataArr[7] = strSP;
				//System.err.println(dataArr[7]);
				
				//成交量
				replaceStrS = "<td class=\"t4t1\">成交量</td>";
				replaceStrE = "</td></tr>";
				strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
				strSP = strSP.replaceAll(replaceStrS,"");
				strSP = strSP.replaceAll(",","");
				dataArr[8] = strSP;
				//System.err.println(dataArr[8]);
				
//				//昨收價
//				replaceStrS = "<span id=\"ctl00_service_FormView1_lblLast\">";
//				replaceStrE = "</span></td>";
//				strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
//				strSP = strSP.replaceAll(replaceStrS,"");
//				dataArr[9] = strSP;
				
//				//離季線
//				replaceStrS = "<span id=\"ctl00_service_FormView1_lblMean60Distance\">";
//				replaceStrE = "</span></td>";
//				strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
//				strSP = strSP.replaceAll(replaceStrS,"");
//				dataArr[10] = strSP;
				
//				//離季線%
//				replaceStrS = "<span id=\"ctl00_service_FormView1_lblMean60DistanceRate\">";
//				replaceStrE = "</span></td>";
//				strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
//				strSP = strSP.replaceAll(replaceStrS,"");
//				strSP = strSP.replaceAll("%","");
//				dataArr[11] = strSP;
				
				//本益比
				replaceStrS = "<td class=\"t4t1\">本益比</td>";
				replaceStrE = "</td><td class=\"t4t1\">";
				strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
				strSP = strSP.replaceAll(replaceStrS,"");
				dataArr[12] = strSP;
				//System.err.println(dataArr[12]);
				
//				//價量分數
//				replaceStrS = "<span id=\"ctl00_service_FormView1_lblPriceVolume\">";
//				replaceStrE = "</span></td>";
//				strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
//				strSP = strSP.replaceAll(replaceStrS,"");
//				dataArr[13] = strSP;
				
//				//幾日未創新高/低
//				replaceStrS = "<span id=\"ctl00_service_FormView1_lblNoNewHighLowDates\">";
//				replaceStrE = "</span>";
//				strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
//				strSP = strSP.replaceAll(replaceStrS,"");
//				dataArr[14] = strSP;
				
//				//連續多頭排列天數
//				replaceStrS = "<span id=\"ctl00_service_FormView1_lblRankDuration\">";
//				replaceStrE = "</span>";
//				strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
//				strSP = strSP.replaceAll(replaceStrS,"");
//				dataArr[15] = strSP;
				
				
				ReaderURL ru2 = new ReaderURL();
				String strAll2 = ru2.readTextFile2("http://www.emega.com.tw/z/zc/zcc/zcc_"+stockNum[i]+".djhtm","BIG5");
				strAll2 = strAll2.replaceAll("\t","");
				strAll2 = strAll2.replaceAll("\r\n","");
				strAll2 = strAll2.replaceAll("<td class=\"t3n1\"><p class=\"t3r1\">","");
				strAll2 = strAll2.replaceAll("<td class=\"t3n1\"><p class=\"t3g1\">","");
				strAll2 = strAll2.replaceAll("<td class=\"t3n1\"><p class=\"t3n1\">","");
				strAll2 = strAll2.replaceAll("<td class=\"t3n1\">","");
				//System.err.println(strAll2);
				
				//股名
				replaceStrS = "<td class=\"t10\" colspan=\"2\">";
				replaceStrE = "(";
				strSP = strAll2.substring(strAll2.indexOf(replaceStrS),strAll2.indexOf(replaceStrE,strAll2.indexOf(replaceStrS)));
				strSP = strSP.replaceAll(replaceStrS,"");
				dataArr[16] = strSP;
				//System.err.println(dataArr[16]);
				
//				//MA60
//				dataArr[17] = (Float.parseFloat(dataArr[0]) - Float.parseFloat(dataArr[10])) + "";
				
				
				//法人持股明細
				ReaderURL ru3 = new ReaderURL();
				strAll2 = ru3.readTextFile2("http://www.emega.com.tw/z/zc/zcl/zcl_"+stockNum[i]+".djhtm","BIG5");
				strAll2 = strAll2.replaceAll("\t","");
				strAll2 = strAll2.replaceAll("\r\n","");
				strAll2 = strAll2.replaceAll("<td class=\"t3n1\"><p class=\"t3r1\">","");
				strAll2 = strAll2.replaceAll("<td class=\"t3n1\"><p class=\"t3g1\">","");
				strAll2 = strAll2.replaceAll("<td class=\"t3n1\"><p class=\"t3n1\">","");
				strAll2 = strAll2.replaceAll("<td class=\"t3n1\">","");
				//System.err.println(strAll2);
				
				//三大法人買賣(單位：張)
				
				try {
					Calendar cal3 = Calendar.getInstance();
					replaceStrS = "<td class=\"t3n0\">"+sdf.format(cal3.getTime())+"</td><td class=\"t3r1\">";
					replaceStrE = "</td></tr><tr><td class=\"t3n0\">";
					strSP = strAll2.substring(strAll2.indexOf(replaceStrS),strAll2.indexOf(replaceStrE,strAll2.indexOf(replaceStrS)));
					strSP = strSP.replaceAll(replaceStrS,"");
					dataArr[18] = strSP;
				}
				catch(Exception ex) {
					dataArr[18] = "0";
				}
				//System.err.println(dataArr[18]);
				
//				//Insert Data
//				
//				PS1.setString(1,stockNum[i]);
//				PS1.setString(2,dataArr[0]);
//				PS1.setString(3,dataArr[1]);
//				PS1.setString(4,dataArr[2]);
//				PS1.setString(5,dataArr[3]);
//				PS1.setString(6,dataArr[4]);
//				PS1.setString(7,dataArr[5]);
//				PS1.setString(8,dataArr[6]);
//				PS1.setString(9,dataArr[7]);
//				PS1.setString(10,dataArr[8]);
//				PS1.setString(11,dataArr[9]);
//				PS1.setString(12,dataArr[10]);
//				PS1.setString(13,dataArr[11]);
//				PS1.setString(14,dataArr[12]);
//				PS1.setString(15,dataArr[13]);
//				PS1.setString(16,dataArr[14]);
//				PS1.setString(17,dataArr[15]);
//				PS1.setString(18,dataArr[16]);
//				PS1.setString(19,dataArr[17]);
//				PS1.setString(20,dataArr[18]);
//				
//				PS1.executeUpdate();
				
				
				for(int j=0; j<dataArr.length; j++) {
					System.err.println(stockNum[i]+":"+dataArr[j]);
				}
			}
			catch(Exception ex) {
				errCount++;
				System.err.println(stockNum[i]+":"+ex.toString());
				errStockId += stockNum[i]+",";
			}
			
			System.out.print(stockNum[i]+":"+(i+1)+"/"+stockNum.length+" (errCount:"+errCount+")"+"\r");
			Thread.sleep(sleepTime*1000);
		}
		
		Calendar calEnd = Calendar.getInstance();
		long millisEnd = calEnd.getTimeInMillis();
		
		System.out.println("");
		System.out.println("錯誤股票代碼："+errStockId);
		System.out.println("執行完成，共花時間："+((millisEnd-millisStart)/1000/60)+" 分鐘");
		
		if(RS1 != null) { RS1.close(); }
		if(PS1 != null) { PS1.close(); }
		if(PS2 != null) { PS2.close(); }
		if(conn != null) { conn.close(); }
	}
	
	
}
