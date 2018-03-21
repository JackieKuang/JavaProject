package wantgoo;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;

import stock5.conn.ConnectionDAO;

//股票資料(http://www.wantgoo.com/CorpInfom.aspx?StockNo=6244) (玩股網)
public class StockDataDay {

	public static void main(String[] args) throws Exception {
		Log4jUtil log = new Log4jUtil();
		
		/***** 參數設定 *****/
		String s1 = "1459,1721,2105,2301,3061,2367,3040,2536,2890,9935,9951,3265,5474,3454,5464,5201,6177,1101,1460,1722,2106,2305,3080,2368,3048,2537,2891,9938,3268,5490,3491,5475,5202,6212,1102,1463,1723,2107,2315,3383,2375,3055,2538,2892,9939,2061,3291,6105,3499,5480,5206,1103,1464,1724,2108,2324,3406,2383,3209,2539,5854,9940,4609,3317,6121,3632,5481,5209,2636,1104,1465,1725,2109,2331,3481,2392,3312,2540,6005,9941,3372,6123,4903,5488,5210,5601,1108,1466,1726,2114,2352,3504,2413,3315,2542,9942,1742,3438,6150,4905,5491,5211,5603,1109,1467,1727,2353,3514,2415,3702,2543,2901,9943,1787,3527,6160,4907,5498,5212,5604,1110,1468,1730,2201,2356,3535,2420,5434,2545,2903,9944,4702,3529,6161,4908,6101,5310,5609,1469,1732,2204,2357,3536,2421,6119,2546,2905,9945,4703,3553,6188,4909,6107,5403,1201,1470,1735,2206,2358,3557,2428,6189,2547,2906,9955,4706,3556,6199,5304,6114,5410,2718,1203,1472,1737,2207,2361,3561,2429,6281,2548,2908,4707,3567,6219,5306,6124,5478,5701,1210,1473,1773,2227,2362,3573,2431,8070,2597,2910,4711,3582,6222,5348,6126,6111,5703,1213,1474,4725,2364,3576,2437,8112,2841,2911,4712,5302,6228,5353,6127,6140,5704,1215,1475,4733,1437,2365,3584,2440,3703,2912,4714,5314,6247,6109,6134,6148,5706,1216,1476,2302,2376,3591,2456,2427,5515,2913,4716,5324,6298,6143,6158,6169,1217,1477,1701,2303,2377,3593,2457,2453,5522,2915,4720,5326,8050,6163,6173,6180,5820,1218,4414,1707,2311,2380,3622,2460,2468,5525,4721,5344,8076,6170,6174,6183,6008,1219,1720,2325,2382,3673,2462,2471,5531,2616,4722,5346,8114,6190,6175,6221," +
				"6015,1220,1503,1729,2329,2385,5305,2467,2480,5533,6505,6506,5347,8210,6218,6185,6231,6016,1225,1504,1731,2330,2387,5484,2472,3021,5534,8926,6509,5351,8234,6241,6194,6240,6020,1227,1506,1733,2337,2395,6116,2476,3029,9908,5387,8299,6245,6203,8044,6021,1229,1507,1734,2338,2397,6120,2478,3130,2208,9918,1565,5425,8410,6263,6204,8099,6022,1231,1512,1736,2342,2399,6131,2483,3356,2601,9926,1752,5455,6264,6207,6023,1232,1513,1762,2344,2405,6164,2484,5203,2603,9931,1777,5466,3066,8034,6208,1785,6024,1233,1514,3164,2351,2417,6168,2492,6112,2605,9937,1781,5468,3128,8048,6210,3064,1234,1515,3705,2363,2424,6176,2493,6214,2606,1784,5483,3230,8059,6211,3067,2916,1235,1516,4104,2369,2438,6209,3003,2607,9103,1788,5487,3297,8082,6217,3093,5902,1236,1517,4106,2379,2442,6225,3011,2312,2608,910322,1795,6103,3339,8097,6220,3131,5903,1702,1519,4108,2388,2465,6226,3015,2317,2609,9104,1799,6104,3362,6242,3289,5904,1521,4119,2401,2474,6278,3023,2354,2610,910482,1813,6129,3434,1333,6248,3303,5905,1301,1522,4133,2408,3002,6289,3026,2360,2611,9105,3118,6138,3441,1336,6250,3324,8941,1303,1524,4737,2425,3005,8072,3032,2371,2612,910579,3205,6147,3452,1815,6266,3373,9960,1304,1525,4746,2434,3013,8105,3037,2373,2613,9106,3218,6182,3455,3078,6274,3402,1305,1526,2436,3017,8215,3042,2390,2614,910801,3266,6198,3490,3089,6279,3466,8908,1307,1527,1802,2441,3022,3044,2404,2615,910861,4102,6223,3516,3092,6284,3498,8917,1308,1528,1805,2449,3046,2314,3058,2423,2617,910948,4103,6229,3520,3114,6290,3508,8927,1309,1529,1806," +
				"2451,3054,2321,3090,2433,2618,9110,4205,4105,6233,3522,3115,6292,3541,8931,1310,1530,1809,2454,3057,2332,3229,2461,2637,911201,4207,4107,6236,3523,3144,8042,3551,1312,1531,1810,2458,3060,2345,3296,2464,5607,911602,4109,6237,3531,3191,8043,3552,1107,1313,1533,2473,3231,2402,3308,2477,5608,911606,4303,4111,6261,3562,3202,8074,3563,2381,1314,1535,1902,2481,3494,2412,3376,2482,911608,4304,4114,6287,3615,3206,8080,3580,4801,1315,1536,1903,3006,3515,2419,3432,2488,2701,911609,4305,4120,6291,3623,3207,8091,3587,5204,1316,1537,1904,3014,3701,2439,3501,2495,2702,911610,6508,4121,8024,3629,3226,8093,3628,5318,1319,1538,1905,3016,4938,2444,3533,2497,2704,911611,8354,4123,8040,3630,3236,8109,4415,1321,1539,1906,3034,6117,2450,3550,3018,2705,911612,9950,4126,8054,3685,3276,8121,5205,911613,1323,1540,1907,3035,6128,2455,3605,3030,2706,911616,4127,8066,3691,3288,8182,5383,1324,1541,1909,3041,6166,2485,3607,3043,2707,911868,4401,4128,8079,4729,3294,8287,5452,1584,1325,1560,3056,6172,2496,3653,3052,2723,912000,4402,4129,8084,4944,3299,8289,5489,2221,1326,1583,1532,3094,6206,2498,5469,3305,8940,912398,4406,4131,8086,5315,3310,8358,5493,3284,1715,1590,2002,3189,6230,3025,6108,3367,9136,4408,4138,8088,5392,3313,5536,3388,4306,2049,2006,3257,6235,3027,6115,3450,2801,913889,4413,4139,8277,5395,3322,3224,6144,4430,2231,2007,3443,6277,3045,6133,3518,2809,9151,4417,4736,5432,3332,3232,6146,5312,1402,4526,2008,3474,8008,3047,6141,3617,2812,9157,4419,4911,1569,5443,3354,3360,6151,6186,1409,4532,2009,3519," +
				"9912,3062,6153,3665,2816,916665,4420,8403,3069,6125,3390,3389,6179,6803,1410,6605,2010,3532,3311,6155,6139,2820,9188,4426,3071,6156,3465,3444,6187,8390,1413,8374,2012,3534,2323,3380,6165,6192,2823,4429,2035,3088,6167,3484,3528,6238,8401,1414,2013,3545,2340,3419,6191,6196,2832,2062,2063,3126,6234,3511,5467,6275,8705,1416,1603,2014,3559,2349,3596,6205,6197,2833,2514,1558,5007,3171,6244,3512,6113,6276,8905,1417,1605,2015,3579,2374,3694,6213,6201,2834,2904,1566,5009,3211,6246,3526,6118,8047,8906,1418,1608,2017,3588,2384,3704,6224,6215,2836,6184,1570,5011,3213,6294,3537,6135,8085,8913,1419,1609,2020,3598,2393,4904,6251,8021,2837,8033,1580,5013,3217,8049,3548,6154,8092,8916,1423,1611,2022,3638,2406,4906,6269,8201,2838,8404,3162,5014,3287,8053,3609,6171,8183,8921,1432,1612,2023,3686,2409,5388,6282,2845,9902,4502,5015,3323,8064,3624,6195,8383,8923,1434,1613,2024,3697,2426,6136,8039,1436,2847,9904,4503,5016,3325,8069,3631,6227,8924,1438,1614,2025,4919,2443,6142,8046,1442,2849,9905,4506,8349,3416,8071,4912,6259,2596,8925,1439,1615,2027,5471,2448,6152,8103,1808,2850,9906,4510,8930,3479,8077,5301,6265,4113,8928,1440,1616,2028,6145,2466,6216,8163,2501,2851,9907,4513,9962,3483,8087,5309,6270,4416,8929,1441,1617,2029,6202,2475,6283,8213,2504,2852,9910,4523,3489,8107,5317,8032,5213,8932,1443,1618,2030,6239,2486,6285,8249,2505,2854,9911,4527,5102,3521,8111,5321,8067,5505,8933,1444,4930,2031,6243,2489,8078,2506,2855,9914,4528,3540,8240,5328,8068,5506,8934,1445,2032,6257,2491,8101,1435,2509,2856," +
				"9917,4529,3073,3577,8266,5340,8096,5508,8935,1446,1704,2033,6271,2499,2347,2511,2880,9919,4530,3087,3611,5345,5511,8936,1447,1708,2034,6286,3008,1471,2348,2515,2881,9921,4533,3169,3625,3068,5349,3083,5512,8937,1449,1709,2038,8016,3019,1582,2359,2516,2882,9924,4534,3188,3652,3095,5355,3085,5514,8938,1451,1710,3004,8081,3024,2059,2414,2520,2883,9925,4535,3219,5371,3152,5356,3086,5516,8942,1452,1711,9958,8110,3031,2308,2430,2524,2884,9927,6122,3227,5384,3221,5364,3293,5519,9946,1453,1712,8131,3038,2313,2459,2527,2885,9928,6603,3228,5386,3234,5381,3546,5520,9949,1454,1713,2101,8261,3049,2316,3010,2528,2886,9929,6609,3252,5426,3290,5398,3570,5521,1455,1714,2102,8271,3050,2327,3028,2530,2887,9930,8083,3259,5438,3306,5439,3664,5523,1456,1717,2103,3051,2328,3033,2534,2888,9933,8255,3260,5450,3363,5457,3687,5529,1457,1718,2104,1604,3059,2355,3036,2535,2889,9934,8996,3264,5465,3431,5460,4965,5530,";
		
//		String s1 = "3130";
		
		int sleepTime = 0; //單位：秒
		
		/***** 參數設定 *****/
		NumberFormat formatter = new DecimalFormat("00");
		
		Calendar calStart = Calendar.getInstance();
		long millisStart = calStart.getTimeInMillis();
		
		
		String[] stockNum = s1.split(",");
		
		java.sql.Connection conn = ConnectionDAO.getConnection();
		
		java.sql.PreparedStatement PS1 = null, PS2=null, PS_Query=null;
		java.sql.ResultSet RS_Query=null;
		
		Calendar cal = Calendar.getInstance();
		String dateStr = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH)+1) + "-" + cal.get(Calendar.DATE);
		
		//Query
		String sqlQuery = "select stock_id from wantgoo_stock where stock_id=? and create_date='"+dateStr+"'";
		PS_Query = conn.prepareStatement(sqlQuery);
		
		//Insert
		String strInsert = "insert into wantgoo_stock(stock_id,stock_deal,deal_change,deal_open,deal_percentage," +
		"deal_buy,deal_high,deal_sell,deal_low,total_volume,deal_last," +
		"mean_60_distance,mean_60_distance_rate,EPS,price_volume,no_new_high_low_dates," +
		"rank_duration,create_date,stock_name,ma60,stock_sum) "+
		" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),?,?,?)";
		PS1 = conn.prepareStatement(strInsert);
		
		//Update
		String strUpdate = "update wantgoo_stock set stock_deal=?,deal_change=?,deal_open=?," +
				"deal_percentage=?,deal_buy=?,deal_high=?,deal_sell=?,deal_low=?,total_volume=?," +
				"deal_last=?,mean_60_distance=?,mean_60_distance_rate=?,EPS=?,price_volume=?," +
				"no_new_high_low_dates=?,rank_duration=?,stock_name=?,ma60=?,stock_sum=?,stock_sum_flag=1 " +
				"where stock_id=? and create_date='"+dateStr+"'";
		PS2 = conn.prepareStatement(strUpdate);
		
		//***** 新增當日資料先刪除當日資料 *****
		
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
				ReaderURL ru = new ReaderURL();
				String strAll = ru.readTextFile2("http://www.wantgoo.com/CorpInfom.aspx?StockNo="+stockNum[i],"UTF-8");
				strAll = strAll.replaceAll("Red","");
				strAll = strAll.replaceAll("Black","");
				strAll = strAll.replaceAll("Green","");
				strAll = strAll.replaceAll("background-color:#FFC0C0;","");
				strAll = strAll.replaceAll("background-color:#C0FFC0;","");
				
				
				//成交價
				String replaceStrS = "<span id=\"ctl00_service_FormView1_lblDeal\" style=\"color:;\">";
				String replaceStrE = "</span></td>";
				String strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
				strSP = strSP.replaceAll(replaceStrS,"");
				dataArr[0] = strSP;
				
				//漲跌
				replaceStrS = "<span id=\"ctl00_service_FormView1_lblChange\" style=\"color:;\">";
				replaceStrE = "</span></td>";
				strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
				strSP = strSP.replaceAll(replaceStrS,"");
				dataArr[1] = strSP;
				
				//開盤價
				replaceStrS = "<span id=\"ctl00_service_FormView1_lblOpen\" style=\"color:;\">";
				replaceStrE = "</span></td>";
				strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
				strSP = strSP.replaceAll(replaceStrS,"");
				dataArr[2] = strSP;
				
				//漲幅
				replaceStrS = "<span id=\"ctl00_service_FormView1_lblPercentage\" style=\"color:;\">";
				replaceStrE = "</span></td>";
				strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
				strSP = strSP.replaceAll(replaceStrS,"");
				strSP = strSP.replaceAll("%","");
				dataArr[3] = strSP;
				
				//買進價
				replaceStrS = "<span id=\"ctl00_service_FormView1_lblBuy\" style=\"color:;\">";
				replaceStrE = "</span></td>";
				strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
				strSP = strSP.replaceAll(replaceStrS,"");
				dataArr[4] = strSP;
				
				//最高價
				replaceStrS = "<span id=\"ctl00_service_FormView1_lblHigh\" style=\"color:;\">";
				replaceStrE = "</span></td>";
				strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
				strSP = strSP.replaceAll(replaceStrS,"");
				dataArr[5] = strSP;
				
				//賣出價
				replaceStrS = "<span id=\"ctl00_service_FormView1_lblSell\" style=\"color:;\">";
				replaceStrE = "</span></td>";
				strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
				strSP = strSP.replaceAll(replaceStrS,"");
				dataArr[6] = strSP;
				
				//最低價
				replaceStrS = "<span id=\"ctl00_service_FormView1_lblLow\" style=\"color:;\">";
				replaceStrE = "</span></td>";
				strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
				strSP = strSP.replaceAll(replaceStrS,"");
				dataArr[7] = strSP;
				
				//成交量
				replaceStrS = "<span id=\"ctl00_service_FormView1_lblTotalVolume\">";
				replaceStrE = "</span></td>";
				strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
				strSP = strSP.replaceAll(replaceStrS,"");
				strSP = strSP.replaceAll(",","");
				dataArr[8] = strSP;
				
				//昨收價
				replaceStrS = "<span id=\"ctl00_service_FormView1_lblLast\">";
				replaceStrE = "</span></td>";
				strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
				strSP = strSP.replaceAll(replaceStrS,"");
				dataArr[9] = strSP;
				
				//離季線
				replaceStrS = "<span id=\"ctl00_service_FormView1_lblMean60Distance\">";
				replaceStrE = "</span></td>";
				strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
				strSP = strSP.replaceAll(replaceStrS,"");
				dataArr[10] = strSP;
				
				//離季線%
				replaceStrS = "<span id=\"ctl00_service_FormView1_lblMean60DistanceRate\">";
				replaceStrE = "</span></td>";
				strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
				strSP = strSP.replaceAll(replaceStrS,"");
				strSP = strSP.replaceAll("%","");
				dataArr[11] = strSP;
				
				//本益比
				replaceStrS = "<span id=\"ctl00_service_FormView1_lblEPS\">";
				replaceStrE = "</span></td>";
				strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
				strSP = strSP.replaceAll(replaceStrS,"");
				dataArr[12] = strSP;
				
				//價量分數
				replaceStrS = "<span id=\"ctl00_service_FormView1_lblPriceVolume\">";
				replaceStrE = "</span></td>";
				strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
				strSP = strSP.replaceAll(replaceStrS,"");
				dataArr[13] = strSP;
				
				//幾日未創新高/低
				replaceStrS = "<span id=\"ctl00_service_FormView1_lblNoNewHighLowDates\">";
				replaceStrE = "</span>";
				strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
				strSP = strSP.replaceAll(replaceStrS,"");
				dataArr[14] = strSP;
				
				//連續多頭排列天數
				replaceStrS = "<span id=\"ctl00_service_FormView1_lblRankDuration\">";
				replaceStrE = "</span>";
				strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
				strSP = strSP.replaceAll(replaceStrS,"");
				dataArr[15] = strSP;
				
				//股名
				replaceStrS = "<span id=\"ctl00_service_FormView1_Name\">";
				replaceStrE = "</span>";
				strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
				strSP = strSP.replaceAll(replaceStrS,"");
				dataArr[16] = strSP;
				
				//MA60
				dataArr[17] = (Float.parseFloat(dataArr[0]) - Float.parseFloat(dataArr[10])) + "";
				
				//三大法人買賣(單位：張) - 抓取當天
				try {
					String nowDayStr = "<span id=\"ctl00_service_gvStock_ctl02_lblDate_main\" class=\"number\">"+formatter.format((cal.get(Calendar.MONTH)+1))+"/"+formatter.format(cal.get(Calendar.DATE))+"</span></td><td>";
					if(strAll.indexOf(nowDayStr)>=0) {
						replaceStrS = "<span id=\"ctl00_service_gvStock_ctl02_lblSum\" class=\"number\" style=\"color:;\">";
						replaceStrE = "</span></td>";
						strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
						strSP = strSP.replaceAll(replaceStrS,"");
						strSP = strSP.replaceAll(",","");
						dataArr[18] = strSP;
					}
					else {
						dataArr[18] = null;
					}
				}
				catch(Exception ex) {
					dataArr[18] = null;
				}
				
				//Check Insert or Update
				int flagCheck = 0;
				PS_Query.setString(1,stockNum[i]);
				RS_Query = PS_Query.executeQuery();
				if(RS_Query.next()) {
					flagCheck = 1;
				}
				
				if(flagCheck == 1) {
					//Update Data
					PS2.setString(1,dataArr[0]);
					PS2.setString(2,dataArr[1]);
					PS2.setString(3,dataArr[2]);
					PS2.setString(4,dataArr[3]);
					PS2.setString(5,dataArr[4]);
					PS2.setString(6,dataArr[5]);
					PS2.setString(7,dataArr[6]);
					PS2.setString(8,dataArr[7]);
					PS2.setString(9,dataArr[8]);
					PS2.setString(10,dataArr[9]);
					PS2.setString(11,dataArr[10]);
					PS2.setString(12,dataArr[11]);
					PS2.setString(13,dataArr[12]);
					PS2.setString(14,dataArr[13]);
					PS2.setString(15,dataArr[14]);
					PS2.setString(16,dataArr[15]);
					PS2.setString(17,dataArr[16]);
					PS2.setString(18,dataArr[17]);
					PS2.setString(19,dataArr[18]);
					PS2.setString(20,stockNum[i]);
					PS2.executeUpdate();
				}
				else {
					//Insert Data
					PS1.setString(1,stockNum[i]);
					PS1.setString(2,dataArr[0]);
					PS1.setString(3,dataArr[1]);
					PS1.setString(4,dataArr[2]);
					PS1.setString(5,dataArr[3]);
					PS1.setString(6,dataArr[4]);
					PS1.setString(7,dataArr[5]);
					PS1.setString(8,dataArr[6]);
					PS1.setString(9,dataArr[7]);
					PS1.setString(10,dataArr[8]);
					PS1.setString(11,dataArr[9]);
					PS1.setString(12,dataArr[10]);
					PS1.setString(13,dataArr[11]);
					PS1.setString(14,dataArr[12]);
					PS1.setString(15,dataArr[13]);
					PS1.setString(16,dataArr[14]);
					PS1.setString(17,dataArr[15]);
					PS1.setString(18,dataArr[16]);
					PS1.setString(19,dataArr[17]);
					PS1.setString(20,dataArr[18]);
					PS1.executeUpdate();
				}
				
//				for(int j=0; j<dataArr.length; j++) {
//					System.err.println(j+":"+dataArr[j]);
//				}
			}
			catch(Exception ex) {
				errCount++;
				log.writeError(stockNum[i]+":"+ex.toString());
				errStockId += stockNum[i]+",";
			}
			
			log.writeInfo(stockNum[i]+":"+(i+1)+"/"+stockNum.length+" (errCount:"+errCount+")");
			//System.out.print(stockNum[i]+":"+(i+1)+"/"+stockNum.length+" (errCount:"+errCount+")"+"\r");
			Thread.sleep(sleepTime*1000);
		}
		
		Calendar calEnd = Calendar.getInstance();
		long millisEnd = calEnd.getTimeInMillis();
		
		log.writeInfo("錯誤股票代碼："+errStockId);
		log.writeInfo("執行完成，共花時間："+((millisEnd-millisStart)/1000/60)+" 分鐘");
//		System.out.println("");
//		System.out.println("錯誤股票代碼："+errStockId);
//		System.out.println("執行完成，共花時間："+((millisEnd-millisStart)/1000/60)+" 分鐘");
		
		if(RS_Query != null) { RS_Query.close(); }
		if(PS_Query != null) { PS_Query.close(); }
		if(PS1 != null) { PS1.close(); }
		if(PS2 != null) { PS2.close(); }
		if(conn != null) { conn.close(); }
	}
	
	
}
