package stock5;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.visitors.HtmlPage;
import com.mysql.jdbc.*;
import java.sql.*;
import java.util.*;
import stock5.conn.*;

//年資料、季資料、估EPS(http://estock.marbo.com.tw/html/stock_info_3130.asp) (eStock)
//Table Name:estock_profit_year (一年跑一次,舊資料不必刪除)
//Table Name:estock_profit_xq (一季跑一次,舊資料不必刪除)
//Table Name:calculate_eps (每季跑一次,視情況而定,舊資料不必刪除)
public class eStock {
	
	public static void main(String[] args) throws Exception {
		String s1 = "1101,1102,1103,1104,1107,1108,1109,1110,1201,1203,1210,1213,1215,1216,1217,1218,1219,1220,1225,1227,1229,1231,1232,1233,1234,1235,1236,1301,1303,1304,1305,1307,1308,1309,1310,1311,1312,1313,1314,1315,1316,1319,1321,1323,1324,1325,1326,1333,1336,1402,1409,1410,1413,1414,1416,1417,1418,1419,1423,1432,1434,1435,1436,1437,1438,1439,1440,1441,1442,1443,1444,1445,1446,1447,1449,1451,1452,1453,1454,1455,1456,1457,1459,1460,1463,1464,1465,1466,1467,1468,1469,1470,1471,1472,1473,1474,1475,1476,1477,1503,1504,1506,1507,1512,1513,1514,1515,1516,1517,1519,1521,1522,1523,1524,1525,1526,1527,1528,1529,1530,1531,1532,1533,1535,1536,1537,1538,1539,1540,1541,1558,1560,1565,1569,1570,1580,1582,1583,1584,1603,1604,1605,1606,1608,1609,1611,1612,1613,1614,1615,1616,1617,1618,1701,1702,1704,1707,1708,1709,1710,1711,1712,1713,1714,1715,1716,1717,1718,1720,1721,1722,1723,1724,1725,1726,1727,1729,1730,1731,1732,1733,1734,1735,1736,1737,1742,1777,1781,1784,1785,1787,1802,1805,1806,1807,1808,1809,1810,1815,1902,1903,1904,1905,1906,1907,1909,2002,2006,2007,2008,2009,2010,2012,2013,2014,2015,2017,2020,2022,2023,2024,2025,2027,2028,2029,2030,2031,2032,2033,2034,2035,2059,2062,2101,2102,2103,2104,2105,2106,2107,2108,2109,2114,2201,2204,2206,2207,2221,2227,2301,2302,2303,2305,2308,2311,2312,2313,2314,2315,2316,2317,2321,2323,2324,2325,2326,2327,2328,2329,2330,2331,2332,2337,2338,2340,2341,2342,2344,2345,2347,2348,2349,2350,2351,2352,2353,2354,2355,2356,2357,2358,2359,2360,2361,2362,2363,2364,2365,2367,2368,2369,2371,2373,2374,2375,2376,2377,2379,2380,2381,2382,2383,2384,2385,2387,2388,2390,2391,2392,2393,2395,2396,2397,2399,2401,2402,2403,2404,2405,2406,2408,2409,2410,2411,2412,2413,2414,2415,2417,2419,2420,2421,2423,2424,2425,2426,2427,2428,2429,2430,2431,2432,2433,2434,2436,2437,2438,2439,2440,2441,2442,2443,2444,2446,2448,2449,2450,2451,2452,2453,2454,2455,2456,2457,2458,2459,2460,2461,2462,2463,2464,2465,2466,2467,2468,2471,2472,2473,2474,2475,2476,2477,2478,2479,2480,2481,2482,2483,2484,2485,2486,2488,2489,2491,2492,2493,2494,2495,2496,2497,2498,2499,2501,2504,2505,2506,2509,2511,2514,2515,2516,2520,2524,2526,2527,2528,2530,2534,2535,2536,2537,2538,2539,2540,2542,2543,2545,2546,2547,2548,2601,2603,2605,2606,2607,2608,2609,2610,2611,2612,2613,2614,2615,2616,2617,2618,2701,2702,2704,2705,2706,2707,2801,2809,2812,2816,2820,2823,2832,2833,2834,2836,2837,2838,2841,2845,2847,2849,2850,2851,2852,2854,2855,2856,2880,2881,2882,2883,2884,2885,2886,2887,2888,2889,2890,2891,2892,2901,2903,2904,2905,2906,2908,2910,2911,2912,2913,2915,2916,2921,3002,3003,3004,3005,3006,3008,3009,3010,3011,3013,3014,3015,3016,3017,3018,3019,3021,3022,3023,3024,3025,3026,3027,3028,3029,3030,3031,3032,3033,3034,3035,3036,3037,3038,3040,3041,3042,3043,3044,3045,3046,3047,3048,3049,3050,3051,3052,3053,3054,3055,3056,3057,3058,3059,3060,3061,3062,3063,3064,3066,3067,3068,3069,3071,3073,3078,3079,3080,3083,3085,3086,3087,3088,3089,3090,3093,3094,3095,3099,3114,3115,3126,3128,3130,3142,3144,3152,3162,3171,3188,3189,3191,3202,3205,3206,3207,3209,3211,3213,3217,3218,3219,3221,3224,3226,3227,3228,3229,3230,3231,3232,3236,3252,3259,3260,3264,3265,3266,3268,3271,3276,3284,3287,3288,3289,3290,3293,3294,3296,3297,3298,3299,3303,3305,3306,3308,3311,3312,3313,3315,3317,3323,3324,3325,3332,3339,3354,3356,3360,3362,3367,3369,3372,3373,3376,3380,3383,3388,3390,3402,3406,3416,3419,3431,3434,3438,3443,3450,3452,3454,3455,3465,3466,3474,3481,3483,3484,3489,3490,3491,3494,3498,3499,3501,3504,3508,3512,3514,3515,3516,3518,3519,3520,3521,3522,3523,3526,3527,3531,3532,3533,3534,3535,3536,3540,3545,3546,3548,3551,3556,3557,3701,3702,4102,4103,4104,4105,4106,4107,4108,4109,4111,4113,4114,4119,4120,4121,4123,4126,4127,4128,4129,4131,4205,4207,4303,4304,4305,4306,4401,4402,4406,4408,4413,4414,4415,4416,4417,4419,4420,4426,4502,4503,4506,4510,4513,4523,4526,4527,4528,4529,4530,4532,4533,4534,4535,4609,4702,4703,4706,4707,4711,4712,4714,4716,4720,4721,4722,4725,4801,4903,4904,4905,4906,4907,4908,4909,5007,5009,5011,5013,5014,5015,5016,5102,5201,5202,5203,5204,5205,5206,5207,5209,5210,5211,5212,5213,5301,5302,5304,5305,5306,5309,5310,5312,5314,5315,5317,5318,5321,5324,5326,5328,5340,5344,5345,5346,5347,5348,5349,5351,5353,5355,5356,5364,5371,5381,5383,5384,5386,5387,5388,5392,5395,5398,5403,5410,5414,5425,5426,5432,5434,5438,5439,5443,5450,5452,5455,5457,5460,5464,5465,5466,5467,5468,5469,5471,5474,5475,5478,5480,5481,5483,5484,5487,5488,5489,5490,5491,5492,5493,5498,5501,5505,5506,5508,5511,5512,5514,5515,5516,5519,5520,5521,5522,5523,5525,5529,5530,5531,5533,5534,5601,5603,5604,5605,5607,5608,5609,5701,5703,5704,5706,5820,5854,5902,5903,5904,5905,6005,6008,6012,6015,6016,6020,6021,6022,6023,6101,6103,6104,6105,6107,6108,6109,6110,6111,6112,6113,6114,6115,6116,6117,6118,6119,6120,6121,6122,6123,6124,6125,6126,6127,6128,6129,6130,6131,6133,6134,6135,6136,6138,6139,6140,6141,6142,6143,6144,6145,6146,6147,6148,6149,6150,6151,6152,6153,6154,6155,6156,6158,6159,6160,6161,6163,6164,6165,6166,6167,6168,6169,6170,6171,6172,6173,6174,6175,6176,6177,6179,6180,6182,6183,6184,6185,6186,6187,6188,6189,6190,6191,6192,6194,6195,6196,6197,6198,6199,6201,6202,6203,6204,6205,6206,6207,6208,6209,6210,6211,6212,6213,6214,6215,6216,6217,6218,6219,6220,6221,6222,6223,6224,6225,6226,6227,6228,6229,6230,6231,6232,6233,6234,6235,6236,6237,6238,6239,6240,6241,6242,6243,6244,6245,6246,6247,6248,6250,6255,6257,6259,6261,6263,6264,6265,6266,6269,6270,6271,6274,6275,6276,6277,6278,6279,6281,6282,6283,6284,6285,6286,6287,6289,6290,6291,6292,6294,6298,6402,6505,6508,6509,6603,6605,6609,8008,8016,8021,8024,8032,8033,8034,8039,8040,8042,8043,8044,8046,8047,8049,8050,8053,8054,8064,8066,8067,8068,8069,8070,8071,8072,8074,8076,8077,8078,8079,8080,8081,8082,8083,8084,8085,8087,8088,8091,8092,8093,8096,8097,8099,8101,8103,8105,8107,8109,8110,8111,8112,8114,8121,8131,8163,8182,8183,8201,8210,8234,8240,8249,8255,8261,8266,8277,8287,8289,8299,8349,8354,8374,8383,8705,8905,8906,8908,8913,8916,8917,8921,8923,8924,8925,8926,8927,8928,8929,8930,8931,8932,8933,8934,8935,8936,8937,8938,8940,8941,8942,8996,9101,9102,9103,9104,9105,9902,9904,9905,9906,9907,9908,9910,9911,9912,9914,9917,9918,9919,9921,9922,9924,9925,9926,9927,9928,9929,9930,9931,9933,9934,9935,9937,9938,9939,9940,9941,9942,9943,9944,9945,9946,9949,9950,9951,9955,9958,9960,9962";
		//String s1 = "";
		
		String[] stockNum = s1.split(",");
		
		java.sql.Connection conn = ConnectionDAO.getConnection();
		
		java.sql.PreparedStatement stmt = null;
		java.sql.ResultSet rs = null;
		String query;
		
		for(int i=0; i<stockNum.length; i++) {
			
			try {
				// Get URL Content
				ReaderURL ru = new ReaderURL();
				String strAll = ru.readTextFile("http://estock.marbo.com.tw/html/stock_info_"+stockNum[i]+".asp");
				
				String data1 = "\r\n";
				
				//股票代碼
				data1 += stockNum[i]+",";
				
				
				
				Parser myParser;
		        myParser = Parser.createParser(strAll, "BIG5");
		        HtmlPage visitor = new HtmlPage(myParser);
		        myParser.visitAllNodesWith(visitor);
		        String textInPage = visitor.getTitle();
		        TableTag[] tableTag = visitor.getTables();
		        //System.out.println(tableTag.length);
		        
		        int table_num = 9;
		        int[] tr_num = {1};
		        
		        for(int j=0; j<tableTag.length; j++) {
		        	//System.out.println("j:"+j);
		        	//System.out.println(tableTag[j]);
		        	if(j==table_num) {
		        		for(int k=0; k<tr_num.length; k++) {
			        		
		        			try{
		        			
			        			int td_Count = tableTag[j].getRow(tr_num[k]).getColumnCount();
				        		//System.out.println(td_Count);
				        		for(int j2=0; j2<td_Count; j2++) {
				        			NodeList a1 = tableTag[j].getRow(tr_num[k]).getColumns()[j2].getChildren();
							        Node[] n1 = a1.toNodeArray();
							        //System.out.println(n1.length);
							        for(int j3=0; j3<n1.length; j3++) {
							        	//System.out.println(n1[j3]);
							        	if (n1[j3] instanceof TextNode) {
								        	TextNode textnode = (TextNode) n1[j3];
								        	data1 += textnode.getText().replaceAll(",","")+",";
								        	
								        	//System.out.println(textnode.getText());
								        }
							        	else if(n1[j3] instanceof TagNode) {
							        		TagNode tagnode = (TagNode) n1[j3];
							        		NodeList a2 = tagnode.getChildren();
							        		Node[] n2 = a2.toNodeArray();
							        		for(int c3=0; c3<n2.length; c3++) {
							        			//System.out.println(n2[c3]);
							        			if (n2[c3] instanceof TextNode) {
										        	TextNode textnode = (TextNode) n2[c3];
										        	String textStr = textnode.getText().replaceAll(",","")+",";
										        	textStr = textStr.replaceAll("\r\n","");
										        	data1 += textStr;
										        	
										        	//System.out.println(textnode.getText());
										        }
							        			
							        			
							        			
							        		}
							        		
							        	}
							        }
							        
				        		}
				        		
				        		
		        			}
		        			catch(Exception ex) {}
		        		}
		        	}
		        	

		        }
				
		      	//字串特殊處理
		        data1 = ReplaceString.getStr(data1);
		        data1 = data1.replaceAll(",,",",");
		        
		        String data2 = "";
		        int table_num2 = 10;
		        int[] tr_num2 = {1};
		        
		        for(int j=0; j<tableTag.length; j++) {
		        	//System.out.println("j:"+j);
		        	//System.out.println(tableTag[j]);
		        	if(j==table_num2) {
		        		for(int k=0; k<tr_num2.length; k++) {
			        		
		        			try{
		        			
			        			int td_Count = tableTag[j].getRow(tr_num2[k]).getColumnCount();
				        		//System.out.println(td_Count);
				        		for(int j2=0; j2<td_Count; j2++) {
				        			NodeList a1 = tableTag[j].getRow(tr_num2[k]).getColumns()[j2].getChildren();
							        Node[] n1 = a1.toNodeArray();
							        //System.out.println(n1.length);
							        for(int j3=0; j3<n1.length; j3++) {
							        	//System.out.println(n1[j3]);
							        	if (n1[j3] instanceof TextNode) {
								        	TextNode textnode = (TextNode) n1[j3];
								        	String textStr = textnode.getText().replaceAll(",","")+",";
								        	textStr = textStr.replaceAll("\r\n","");
								        	data2 += textStr;
								        	
								        	//System.out.println(textnode.getText());
								        }
							        }
							        
				        		}
				        		
		        			}
		        			catch(Exception ex) {}
		        		}
		        	}
		        	
		        	
		        }
				
		      	//字串特殊處理
		        data2 = ReplaceString.getStr(data2);
		        
		        
		        String data3 = "";
		        int table_num3 = 13;
		        int[] tr_num3 = {1,2,3,4,5};
		        
		        for(int j=0; j<tableTag.length; j++) {
		        	//System.out.println("j:"+j);
		        	//System.out.println(tableTag[j]);
		        	if(j==table_num3) {
		        		for(int k=0; k<tr_num3.length; k++) {
			        		
		        			try{
		        			
			        			int td_Count = tableTag[j].getRow(tr_num3[k]).getColumnCount();
				        		//System.out.println(td_Count);
				        		for(int j2=0; j2<td_Count; j2++) {
				        			NodeList a1 = tableTag[j].getRow(tr_num3[k]).getColumns()[j2].getChildren();
							        Node[] n1 = a1.toNodeArray();
							        //System.out.println(n1.length);
							        for(int j3=0; j3<n1.length; j3++) {
							        	//System.out.println(n1[j3]);
							        	if (n1[j3] instanceof TextNode) {
								        	TextNode textnode = (TextNode) n1[j3];
								        	data3 += textnode.getText().replaceAll(",","")+",";
								        	
								        	//System.out.println(textnode.getText());
								        }
							        	else if(n1[j3] instanceof TagNode) {
							        		TagNode tagnode = (TagNode) n1[j3];
							        		NodeList a2 = tagnode.getChildren();
							        		Node[] n2 = a2.toNodeArray();
							        		for(int c3=0; c3<n2.length; c3++) {
							        			//System.out.println(n2[c3]);
							        			if (n2[c3] instanceof TextNode) {
										        	TextNode textnode = (TextNode) n2[c3];
										        	data3 += textnode.getText().replaceAll(",","")+",";
										        	
										        	//System.out.println(textnode.getText());
										        }
							        			else if(n2[c3] instanceof TagNode) {
									        		TagNode tagnode2 = (TagNode) n2[c3];
									        		NodeList a3 = tagnode2.getChildren();
									        		Node[] n3 = a3.toNodeArray();
									        		for(int d3=0; d3<n3.length; d3++) {
									        			//System.out.println(n3[d3]);
									        			if (n3[d3] instanceof TextNode) {
												        	TextNode textnode = (TextNode) n3[d3];
												        	data3 += textnode.getText().replaceAll(",","")+",";
												        	
												        	//System.out.println(textnode.getText());
												        }
									        			
									        			
									        			
									        		}
									        		
									        	}
							        			
							        			
							        		}
							        		
							        	}
							        }
							        
				        		}
				        		
		        			}
		        			catch(Exception ex) {}
		        		}
		        	}
		        	

		        }
		      	//字串特殊處理
		        data3 = ReplaceString.getStr(data3);
		        
		        String data4 = "";
		        int table_num4 = 14;
		        int[] tr_num4 = {1,2};
		        
		        for(int j=0; j<tableTag.length; j++) {
		        	//System.out.println("j:"+j);
		        	//System.out.println(tableTag[j]);
		        	if(j==table_num4) {
		        		for(int k=0; k<tr_num4.length; k++) {
			        		
		        			try{
		        			
			        			int td_Count = tableTag[j].getRow(tr_num4[k]).getColumnCount();
				        		//System.out.println(td_Count);
				        		for(int j2=0; j2<td_Count; j2++) {
				        			NodeList a1 = tableTag[j].getRow(tr_num4[k]).getColumns()[j2].getChildren();
							        Node[] n1 = a1.toNodeArray();
							        //System.out.println(n1.length);
							        for(int j3=0; j3<n1.length; j3++) {
							        	//System.out.println(n1[j3]);
							        	if (n1[j3] instanceof TextNode) {
								        	TextNode textnode = (TextNode) n1[j3];
								        	data4 += textnode.getText().replaceAll(",","")+",";
								        	
								        	//System.out.println(textnode.getText());
								        }
							        	else if(n1[j3] instanceof TagNode) {
							        		TagNode tagnode = (TagNode) n1[j3];
							        		NodeList a2 = tagnode.getChildren();
							        		Node[] n2 = a2.toNodeArray();
							        		for(int c3=0; c3<n2.length; c3++) {
							        			//System.out.println(n2[c3]);
							        			if (n2[c3] instanceof TextNode) {
										        	TextNode textnode = (TextNode) n2[c3];
										        	data4 += textnode.getText().replaceAll(",","")+",";
										        	
										        	//System.out.println(textnode.getText());
										        }
							        			else if(n2[c3] instanceof TagNode) {
									        		TagNode tagnode2 = (TagNode) n2[c3];
									        		NodeList a3 = tagnode2.getChildren();
									        		Node[] n3 = a3.toNodeArray();
									        		for(int d3=0; d3<n3.length; d3++) {
									        			//System.out.println(n3[d3]);
									        			if (n3[d3] instanceof TextNode) {
												        	TextNode textnode = (TextNode) n3[d3];
												        	data4 += textnode.getText().replaceAll(",","")+",";
												        	
												        	//System.out.println(textnode.getText());
												        }
									        			
									        			
									        			
									        		}
									        		
									        	}
							        			
							        			
							        		}
							        		
							        	}
							        }
							        
				        		}
				        		
		        			}
		        			catch(Exception ex) {}
		        		}
		        	}
		        	

		        }
				
		      	//字串特殊處理
		        data4 = ReplaceString.getStr(data4);
		        
		        
		        String data5 = "";
		        int table_num5 = 16;
		        int[] tr_num5 = {1,2,3,4,5};
		        
		        for(int j=0; j<tableTag.length; j++) {
		        	//System.out.println("j:"+j);
		        	//System.out.println(tableTag[j]);
		        	if(j==table_num5) {
		        		for(int k=0; k<tr_num5.length; k++) {
			        		
		        			try{
		        			
			        			int td_Count = tableTag[j].getRow(tr_num5[k]).getColumnCount();
				        		//System.out.println(td_Count);
				        		for(int j2=0; j2<td_Count; j2++) {
				        			NodeList a1 = tableTag[j].getRow(tr_num5[k]).getColumns()[j2].getChildren();
							        Node[] n1 = a1.toNodeArray();
							        //System.out.println(n1.length);
							        for(int j3=0; j3<n1.length; j3++) {
							        	//System.out.println(n1[j3]);
							        	if (n1[j3] instanceof TextNode) {
								        	TextNode textnode = (TextNode) n1[j3];
								        	data5 += textnode.getText().replaceAll(",","")+",";
								        	
								        	//System.out.println(textnode.getText());
								        }
							        }
							        
				        		}
				        		data5 = data5.replaceAll("　","");
				        		
		        			}
		        			catch(Exception ex) {}
		        		}
		        	}
		        	
		        }
				
		      	//字串特殊處理
		        data5 = ReplaceString.getStr(data5);
		        data5 = data5.replaceAll(",,",",");
		        
		      	//字串特殊處理
		        //data1 = ReplaceString.getStr(data1);
		        //data1 = data1.replaceAll(",,",",");
		        
		        
		        //System.out.println(data1);
		        
		        System.out.print(stockNum[i]+":"+(i+1)+"/"+stockNum.length+"\r");
		        
//		        System.out.println("data1:"+data1);	//估eps
//		        System.out.println("data2:"+data2);	//無用
//		        System.out.println("data3:"+data3);	//季資料
//		        System.out.println("data4:"+data4);	//年資料
//		        System.out.println("data5:"+data5);	//無用
		        
		        
		        // Insert data
		        
		        try {
		        	
        			//季資料
		        	String[] data3Arr = data3.split(",");
		        	for(int eb=0; eb<data3Arr.length; eb+=12) {
	    				query = "insert into estock_profit_xq(stock_id,year,quarter,capital_stock,income,net_profit,eps,gross,profitability,pure,fluid,liability,net_value,principal_stock,create_date)"+
	    						" values('"+stockNum[i]+"','"+data3Arr[0+eb].substring(0,2)+"','"+data3Arr[0+eb].substring(4,5)+"','"+data3Arr[1+eb]+"','"+data3Arr[2+eb]+"','"+data3Arr[3+eb]+"','"+data3Arr[4+eb]+"','"+data3Arr[5+eb]+"','"+data3Arr[6+eb]+"','"+data3Arr[7+eb]+"','"+data3Arr[8+eb]+"','"+data3Arr[9+eb]+"','"+data3Arr[10+eb]+"','"+data3Arr[11+eb]+"',now());";
	        			System.out.println(query);
	        			stmt = conn.prepareStatement(query);
	        			stmt.executeUpdate(query);
	        			stmt.close();
		        	}
		        	
		        	
		        	//年資料
//		        	String[] data4Arr = data4.split(",");
//		        	for(int eb=0; eb<data4Arr.length; eb+=10) {
//	    				query = "insert into estock_profit_year(stock_id,year,income,net_profit,eps,gross,profitability,pure,fluid,liability,net_value,create_date)"+
//	    						" values('"+stockNum[i]+"','"+data4Arr[0+eb]+"','"+data4Arr[1+eb]+"','"+data4Arr[2+eb]+"','"+data4Arr[3+eb]+"','"+data4Arr[4+eb]+"','"+data4Arr[5+eb]+"','"+data4Arr[6+eb]+"','"+data4Arr[7+eb]+"','"+data4Arr[8+eb]+"','"+data4Arr[9+eb]+"',now());";
//	        			System.out.println(query);
//	        			stmt = conn.prepareStatement(query);
//	        			stmt.executeUpdate(query);
//	        			stmt.close();
//		        	}
		        	
		        	
		        	//eStock 估2008 EPS資訊
		        	String[] data1Arr = data1.split(",");
		        	query = "update calculate_eps set eps_e_stock='"+data1Arr[2].replaceAll("元","")+"' where stock_id='"+stockNum[i]+"'";
					System.out.println(query);
					stmt = conn.prepareStatement(query);
					stmt.executeUpdate(query);
					stmt.close();
		        	
    			}
    			catch(Exception e1) {System.err.println(e1);}
    			
			}
			catch(Exception ex) {
				System.out.println(ex.toString());
			}
		}
		
	}
	
}
