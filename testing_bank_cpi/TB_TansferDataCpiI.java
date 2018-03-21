package testing_bank_cpi;

import java.sql.*;

public class TB_TansferDataCpiI{
	//MYSQL Connection
	java.sql.Connection connMysql = null;
	String mysql_url=GetProperty.readValue("mysql_url");
	String mysqlLoginId = GetProperty.readValue("mysqlLoginId");
	String mysqlLoginPwd = GetProperty.readValue("mysqlLoginPwd");
	
	//Oracle Connection
	java.sql.Connection connOracle = null;
	String oracle_url=GetProperty.readValue("oracle_url");
	String oracleLoginId = GetProperty.readValue("oracleLoginId");
	String oracleLoginPwd = GetProperty.readValue("oracleLoginPwd");
	
	//Log
	WriteFile wf = null;
	String logPath = GetProperty.readValue("logPath");
	
	public static void main(String[] args) throws Exception{
		
		TB_TansferDataCpiI tbDao = new TB_TansferDataCpiI();
		tbDao.exeStart(args);
		
	}
	
	public void exeStart(String[] args) throws Exception{
		String custnoList1=GetProperty.readValue("custnoList1");
		//String custnoList1="";
		String[] custno1=custnoList1.split(",");
		String custnoList2=GetProperty.readValue("custnoList2");
		//String custnoList2="";
		String[] custno2=custnoList2.split(",");
		
		for(int i=0;i<custno1.length;i++){
			wf=new WriteFile(logPath+"TB-TansferDataCpiI_"+custno1[i]+"-log.txt");
			
			System.out.println("***** Start "+custno1[i]+" *****");
			transferData(custno1[i],custno2[i]);
			System.out.println("***** End "+custno1[i]+" *****");
			System.out.println("");
			Thread.sleep(5000);
		}
		System.err.println("All Finish!!");
	}
	
	public void transferData(String custno1,String custno2) throws Exception{
		//Oracle
		Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
		connOracle=DriverManager.getConnection (oracle_url,oracleLoginId,oracleLoginPwd);
		System.err.println("connOracle:"+connOracle);
		
		//MYSQL
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		connMysql=DriverManager.getConnection (mysql_url,mysqlLoginId,mysqlLoginPwd);
		connMysql.setAutoCommit(false);
		System.err.println("connMysql:"+connMysql);
		
		java.sql.PreparedStatement psOracleCount=null,psOracle1=null,psMysql1=null,psMysql2=null,psMysqlInsert1=null,psMysqlInsert2=null,psMysqlInsert3=null,psMysqlInsert4=null;
		java.sql.ResultSet rsOracleCount=null,rsOracle1=null,rsMysql1=null,rsMysql2=null;
	    String invoice1=custno1.substring(0,custno1.length()-4);
	    //String invoice2=custno2.substring(0,custno2.length()-4);
		
		//依invoice取得受測完成總筆數==============================START
	    String sqlOracleCount="select count(*) NUM from B2B_VIP_MODULE_USER u,B2B_VIP_MODULE_USER_INDEX r where u.BVMU_ID in(select max(BVMU_ID) from B2B_VIP_MODULE_USER_INDEX where INVOICE="+invoice1+" and EXAM_FINISHED_DATE is not null group by USER_ID) and u.BVMU_ID=r.BVMU_ID";
	    //String sqlOracleCount="select count(*) NUM from B2B_VIP_MODULE_USER u,B2B_VIP_MODULE_USER_INDEX r where u.BVMU_ID in() and u.BVMU_ID=r.BVMU_ID";
	    System.out.println(sqlOracleCount);
	    psOracleCount=connOracle.prepareStatement(sqlOracleCount);
	    rsOracleCount=psOracleCount.executeQuery();
		rsOracleCount.next();
		String totalCount=rsOracleCount.getString("NUM");
    	System.err.println("totalCount="+totalCount);
	    if("0".equals(totalCount)){
	    	wf.writeFile("totalCount=0");
	    	return;
	    }
	    //依invoice取得受測完成總筆數==============================END
	    
		//依invoice取得受測完成資料==============================START
	    String sqlOracle1="select u.*,r.EXAM_FINISHED_DATE from B2B_VIP_MODULE_USER u,B2B_VIP_MODULE_USER_INDEX r where u.BVMU_ID in(select max(BVMU_ID) from B2B_VIP_MODULE_USER_INDEX where INVOICE="+invoice1+" and EXAM_FINISHED_DATE is not null group by USER_ID) and u.BVMU_ID=r.BVMU_ID";
	    //String sqlOracle1="select u.*,r.EXAM_FINISHED_DATE from B2B_VIP_MODULE_USER u,B2B_VIP_MODULE_USER_INDEX r where u.BVMU_ID in() and u.BVMU_ID=r.BVMU_ID";
	    System.out.println(sqlOracle1);
	    psOracle1=connOracle.prepareStatement(sqlOracle1);
	    rsOracle1=psOracle1.executeQuery();
	    //依invoice取得受測完成資料==============================END
	    
	    //依CUSTNO取得單位管理員編號==============================START
        String sqlMysql1="select MGR_ID from TB_MANAGER where CUSTNO="+custno2+" and ROLE=1";
        System.out.println(sqlMysql1);
        psMysql1=connMysql.prepareStatement(sqlMysql1);
        rsMysql1=psMysql1.executeQuery();
        String mgrIdRole1="0";
	    if(rsMysql1.next()){
	    	mgrIdRole1=rsMysql1.getString("MGR_ID");//單位管理員編號
		    System.err.println("MGR_ID="+mgrIdRole1);
	    }else{
	    	System.err.println("查無TB_MANAGER.CUSTNO="+custno2+"的單位管理員");
	    	wf.writeFile("No TB_MANAGER.CUSTNO="+custno2);
	    	return;
	    }
        //依CUSTNO取得單位管理員編號==============================END
        
        //insert TB_USER==============================START
		String sqlMysqlInsert1="insert into TB_USER(USER_ID,CUSTNO,TMPL_ID,RETRY_STATUS,STATUS,USERID,PASSWORD,TESTINGID,PERMISSION_IDS,IS_OPENED,USER_NAME,SEX,BIRTHDAY_DATE,EDUCATION,EDU_DEP_ID,EDU_DEP_NAME,CAREER_STATUS,INDUSTRY_ID,INDUSTRY_NAME,POSITION_ID,POSITION_NAME,DEPARTMENT,JOB,TITLE,EXP_NOW_YEAR,EXP_TOTAL_YEAR,CREATE_DATE,MGR_ID,EMAIL,ROLE)"+
                               "             values(?,"+custno2+",8,1,0,'buser@104.com.tw','dk454cj84',?,'"+mgrIdRole1+"',?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"+mgrIdRole1+",null,1)";
		System.out.println(sqlMysqlInsert1);
		psMysqlInsert1=connMysql.prepareStatement(sqlMysqlInsert1);
		//insert TB_USER==============================END
		
		//insert TB_ANSWER_CPI==============================START
		String sqlMysqlInsert2="insert into TB_ANSWER_CPI(USER_ID";
		for(int i=1;i<=7;i++){
			sqlMysqlInsert2+=",PART1_"+i;
		}
		for(int i=1;i<=149;i++){
			if(i==6 || i==16 || i==17 || i==29 || i==32 || i==33 || i==37 || i==70 || i==73 || i==74 || i==81 || i==84 || i==85 || i==92 || i==99 || i==100 || i==102 || i==104 || i==126){
				continue;
			}
			sqlMysqlInsert2+=",Q"+i;
		}
		sqlMysqlInsert2+=",RANDOM_NUM,PAGE_NO,FIRST_SEC,LAST_SEC,TOTAL_SEC,INCORRECT_TIMES,INCORRECT_PAGES,CREATE_DATE)";
		sqlMysqlInsert2+=" values(?";
		for(int i=1;i<=145;i++){
			sqlMysqlInsert2+=",?";
		}
		sqlMysqlInsert2+=")";
		System.out.println(sqlMysqlInsert2);
		psMysqlInsert2=connMysql.prepareStatement(sqlMysqlInsert2);
		//insert TB_ANSWER_CPI==============================END
		
		//insert TB_RESULT_CPI==============================START
		String sqlMysqlInsert3="insert into TB_RESULT_CPI(USER_ID,PR_A01,PR_A02,PR_A03,PR_A04,PR_A05,PR_A06,PR_A07,PR_B01,PR_B02,PR_B03,PR_B04,PR_B05,PR_B06,PR_B07,PR_C01,PR_C02,PR_C03,PR_C04,PR_C05,PR_C06,PR_C07,PR_I01,PR_I02,PR_I03,PR_I04,PR_I05,PR_I06,PR_I07,PR_I08,PR_L01,PR_L02,PR_L03,PR_L04,PR_L05,PR_L06,PR_L07,PR_L08,PR_R01,PR_R02,PR_R03,PR_R04,PR_R05,PR_R06,PR_R07,PR_R08,PR_R09,PR_S01,PR_S02,PR_S03,PR_S04,PR_S05,PR_S06,PR_S07,PR_D01,PR_D02,PR_D03,PR_D04,PR_D05,PR_D06,PR_D07,PR_D08,PR_D09,PR_D10,PR_D11,PR_D12,PR_D13,PR_D14,PR_D15,PR_D16,PR_D17,PR_D18,PR_D19,PR_D20,PR_D21,PR_D22,PR_D23,PR_D24,PR_D25,PR_D26,PR_D27,PR_D28,CREATE_DATE)";
		sqlMysqlInsert3+=" values(?";
		for(int i=1;i<=81;i++){
			sqlMysqlInsert3+=",?";
		}
		sqlMysqlInsert3+=",?)";
		System.out.println(sqlMysqlInsert3);
		psMysqlInsert3=connMysql.prepareStatement(sqlMysqlInsert3);
		//insert TB_RESULT_CPI==============================END
		
		//insert TB_REPORT_INDEX==============================START
		String sqlMysqlInsert4="insert into TB_REPORT_INDEX(USER_ID,CUSTNO,CREATE_DATE) values(?,"+custno2+",?)";
		System.out.println(sqlMysqlInsert4);
		psMysqlInsert4=connMysql.prepareStatement(sqlMysqlInsert4);
		//insert TB_REPORT_INDEX==============================END
		
	    int rowCount=0;//目前筆數
	    int insertCount=0;//insert MYSQL 筆數
	    while(rsOracle1.next()){
			rowCount++;
    		
			try{
				//取得流水號==============================START
			    psMysql2=connMysql.prepareStatement("select uuid_short() NEW_USER_ID");
				rsMysql2=psMysql2.executeQuery();
				rsMysql2.next();
				String userPk=rsMysql2.getString("NEW_USER_ID");
				//System.out.println(userPk);
				//取得流水號==============================END
				
				//insert TB_USER==============================START
				psMysqlInsert1.setString(1,userPk);
				psMysqlInsert1.setString(2,rsOracle1.getString("USER_ID"));
				psMysqlInsert1.setString(3,rsOracle1.getString("IS_REPORT"));
				psMysqlInsert1.setString(4,rsOracle1.getString("USER_NAME"));
				psMysqlInsert1.setString(5,rsOracle1.getString("SEX"));
				if(rsOracle1.getString("AGE_YEAR")==null || rsOracle1.getString("AGE_MONTH")==null || rsOracle1.getString("AGE_DAY")==null){
					psMysqlInsert1.setString(6,null);
				}else{
					int AGE_YEAR=Integer.parseInt(rsOracle1.getString("AGE_YEAR"));
					int AGE_MONTH=Integer.parseInt(rsOracle1.getString("AGE_MONTH"));
					int AGE_DAY=Integer.parseInt(rsOracle1.getString("AGE_DAY"));
					if(AGE_YEAR<1900 || AGE_YEAR>2012 || AGE_MONTH<1 || AGE_MONTH>12 || AGE_DAY<1 || AGE_DAY>31){
						psMysqlInsert1.setString(6,null);
					}else{
						psMysqlInsert1.setString(6,AGE_YEAR+"/"+AGE_MONTH+"/"+AGE_DAY);
					}
				}
				if(rsOracle1.getString("EDUCATION")==null){
					psMysqlInsert1.setString(7,null);
				}else if("0".equals(rsOracle1.getString("EDUCATION"))){//0:國中(含)以下畢業 ==> 11:國中(含)以下
					psMysqlInsert1.setString(7,"11");
				}else if("1".equals(rsOracle1.getString("EDUCATION"))){//1:高中職畢業 ==> 9:高中
					psMysqlInsert1.setString(7,"9");
				}else if("2".equals(rsOracle1.getString("EDUCATION"))){//2:專科畢業 ==> 8:五專
					psMysqlInsert1.setString(7,"8");
				}else if("3".equals(rsOracle1.getString("EDUCATION"))){//3:大學畢業 ==> 3:大學
					psMysqlInsert1.setString(7,"3");
				}else if("4".equals(rsOracle1.getString("EDUCATION"))){//4:碩士班畢業 ==> 2:碩士
					psMysqlInsert1.setString(7,"2");
				}else if("5".equals(rsOracle1.getString("EDUCATION"))){//5:博士班畢業 ==> 1:博士
					psMysqlInsert1.setString(7,"1");
				}else{
					psMysqlInsert1.setString(7,null);
				}
				psMysqlInsert1.setString(8,rsOracle1.getString("EDU_DEP_ID"));
				psMysqlInsert1.setString(9,rsOracle1.getString("EDU_DEP_NAME"));
				psMysqlInsert1.setString(10,rsOracle1.getString("CAREER_STATUS"));
				psMysqlInsert1.setString(11,rsOracle1.getString("INDUSTRY_ID"));
				psMysqlInsert1.setString(12,rsOracle1.getString("INDUSTRY_NAME"));
				psMysqlInsert1.setString(13,rsOracle1.getString("POSITION_ID"));
				psMysqlInsert1.setString(14,rsOracle1.getString("POSITION_NAME"));
				psMysqlInsert1.setString(15,rsOracle1.getString("DEPARTMENT"));
				psMysqlInsert1.setString(16,rsOracle1.getString("PROFESSIONAL_NAME"));
				psMysqlInsert1.setString(17,rsOracle1.getString("TITLE"));
				if(rsOracle1.getString("EXPIRE")==null){
					psMysqlInsert1.setString(18,null);
				}else if("99".equals(rsOracle1.getString("EXPIRE"))){//99:一年以下 ==> 0:1年以下
					psMysqlInsert1.setString(18,"0");
				}else{
					psMysqlInsert1.setString(18,rsOracle1.getString("EXPIRE"));
				}
				if(rsOracle1.getString("TOTAL_EXPIRE")==null){
					psMysqlInsert1.setString(19,null);
				}else if("99".equals(rsOracle1.getString("TOTAL_EXPIRE"))){//99:一年以下 ==> 0:1年以下
					psMysqlInsert1.setString(19,"0");
				}else{
					psMysqlInsert1.setString(19,rsOracle1.getString("TOTAL_EXPIRE"));
				}
				psMysqlInsert1.setString(20,rsOracle1.getString("LOGIN_DATE"));
				//insert TB_USER==============================END
				
				//insert TB_ANSWER_CPI==============================START
				psMysqlInsert2.setString(1,userPk);
				for(int i=1;i<=7;i++){
					psMysqlInsert2.setString(i+1,rsOracle1.getString("EXAM_PART1_"+i));
				}
				for(int i=1,j=9;i<=149;i++){
					if(i==6 || i==16 || i==17 || i==29 || i==32 || i==33 || i==37 || i==70 || i==73 || i==74 || i==81 || i==84 || i==85 || i==92 || i==99 || i==100 || i==102 || i==104 || i==126){
						continue;
					}
					psMysqlInsert2.setString(j,rsOracle1.getString("Q_"+i));
					j++;
				}
				psMysqlInsert2.setString(139,rsOracle1.getString("RANDOM_NUM"));
				psMysqlInsert2.setString(140,rsOracle1.getString("PAGE_NO"));
				psMysqlInsert2.setString(141,rsOracle1.getString("FIRST_PAGE_SEC"));
				psMysqlInsert2.setString(142,rsOracle1.getString("LAST_PAGE_SEC"));
				psMysqlInsert2.setString(143,rsOracle1.getString("USED_TIME"));
				psMysqlInsert2.setString(144,rsOracle1.getString("INCORRECT_TIMES"));
				psMysqlInsert2.setString(145,rsOracle1.getString("IS_CORRECT"));
				psMysqlInsert2.setString(146,rsOracle1.getString("LOGIN_DATE"));
				//insert TB_ANSWER_CPI==============================END
				
				//insert TB_RESULT_CPI==============================START
				EvalEachDimensionForeScoreI edfs=new EvalEachDimensionForeScoreI(connOracle);//計算職能PR值
				edfs.setDimensionScore(rsOracle1.getString("BVMU_ID"),"1","1");
				int[] PR_C=edfs.getPR();//職能PR值
				EvalEachDimensionScoreI eds=new EvalEachDimensionScoreI();//計算性格PR值
				eds.setDimensionScore(connOracle,rsOracle1.getString("BVMU_ID"),"1");
				int[] PR_P=eds.getPR();//性格PR值
				
				psMysqlInsert3.setString(1,userPk);
				for(int i=0;i<53;i++){
					psMysqlInsert3.setString(i+2,PR_C[i]+"");
				}
				for(int i=0;i<28;i++){
					psMysqlInsert3.setString(i+55,PR_P[i]+"");
				}
				psMysqlInsert3.setString(83,rsOracle1.getString("EXAM_FINISHED_DATE"));
				//insert TB_RESULT_CPI==============================END
				
				//insert TB_REPORT_INDEX==============================START
				psMysqlInsert4.setString(1,userPk);
				psMysqlInsert4.setString(2,rsOracle1.getString("EXAM_FINISHED_DATE"));
				//insert TB_REPORT_INDEX==============================END
				
	    		if(psMysqlInsert1.executeUpdate()==1 && psMysqlInsert2.executeUpdate()==1 && psMysqlInsert3.executeUpdate()==1 && psMysqlInsert4.executeUpdate()==1){
					connMysql.commit();
	    			insertCount++;
	        		System.err.println("OK B2B_VIP_MODULE_USER.USER_ID="+rsOracle1.getString("USER_ID")+", totalCount="+totalCount+", rowCount="+rowCount+", insertCount="+insertCount);
	        	}
			}catch(Exception ex){
				connMysql.rollback();
	    		System.err.println("ERROR BVMU_ID="+rsOracle1.getString("BVMU_ID")+", USER_ID="+rsOracle1.getString("USER_ID")+", CUSTNO="+custno1+": "+ex.toString());
	    		wf.writeFile("ERROR BVMU_ID="+rsOracle1.getString("BVMU_ID")+", USER_ID="+rsOracle1.getString("USER_ID")+", CUSTNO="+custno1+": "+ex.toString()+"\n");
			}
	    }
	    
		if(psOracleCount!=null){psOracleCount.close();}
		if(psOracle1!=null){psOracle1.close();}
		if(psMysql1!=null){psMysql1.close();}
		if(psMysql2!=null){psMysql2.close();}
		if(psMysqlInsert1!=null){psMysqlInsert1.close();}
		if(psMysqlInsert2!=null){psMysqlInsert2.close();}
		if(psMysqlInsert3!=null){psMysqlInsert3.close();}
		if(psMysqlInsert4!=null){psMysqlInsert4.close();}
		if(rsOracleCount!=null){rsOracleCount.close();}
		if(rsOracle1!=null){rsOracle1.close();}
		if(rsMysql1!=null){rsMysql1.close();}
		if(rsMysql2!=null){rsMysql2.close();}
		if((connOracle!=null) && (!connOracle.isClosed())){connOracle.close();}
		if((connMysql!=null) && (!connMysql.isClosed())){connMysql.close();}
		
		System.err.println("Finish CUSTNO="+custno1+", totalCount="+totalCount+", insertCount="+insertCount);
		wf.writeFile("Finish CUSTNO="+custno1+", totalCount="+totalCount+", insertCount="+insertCount+"\n");
		wf.closeFile();
	}
}