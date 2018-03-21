
package testing_bank_cpi;

/**
 * @author jackie&peipei.chou&Iris
 *
 * �p�⤺��¾��ʮ�����i4�B5��SP�ȡBB�ȡBPR�ȡB¾�ȾA�X�׵��G
 * 
 */

import java.sql.*;
import java.util.*;

public class EvalEachDimensionForeScoreI{
		Connection conn = null;
		private String errMassage = "";
		private String positionName;
		private float[] SPArr;
		private float[] BArr = new float[53];
		private double[] ZArr = new double[53];
		private int[] PRArr = new int[53];
		private double[] SN;
		private double[][] MA;
		private int[][] score;
		//private int[][] scoreOrder;
		private int singlePRZRZ=0;
		private String isBeforeTimeStr = "0"; // 0:�¤���  1:2007/8/14�s����  2:2007/11/1�s����
		private int[][] questionIdArr =  
		{
			{121,137,52,108,42},
			{142,2,76,125},
			{140,21,54,109},
			{105,23,9,111,5},
			{148,62,123,87,45,103},
			{4,91,78,97,106},
			{128,139,40,77,55},
			{113,119,141,53},
			{88,31,7,131,115},
			{24,63,129,15,138},
			{58,134,118,79},
			{143,56,133,13,66},
			{34,132,39,82,127},
			{149,47,112,110,11},
			{96,36,44,12},
			{130,1,41,27,3},
			{75,49,93,86,8},
			{94,51,89,71},
			{120,72,144,46},
			{64,35,117,107},
			{67,98,18,114,83},
			{116,147,50,90,38},
			{25,135,101,69},
			{145,28,48,10},
			{20,80,14,61},
			{22,30,60,136,95},
			{57,59,146,43,26},
			{122,124,65,68,19}
		};
	
	
	/**
	* ��o��~�T��
	* @return String errMassage
	*/
	public String getErrMassage(){
		return errMassage;
	}
	
	
	/**
	 * ��opositionName
	 * @return String positionName
	 */
	public String getPositionName(){
		return positionName;
	}
	
	
	/**
	* ��o�}�CPR
	* @return int PRArr[]
	*/
	public int[] getPR(){
		return PRArr;
	}
	
	
	/**
	 * ��o�}�CPRZRZ
	 * @return int singlePRZRZ
	 */
	public int getSinglePRZRZ(){
		return singlePRZRZ;
	}
	
	
	/**
	 * ��o�}�C BArr
	 * @return float BArr[]
	 */
	public float[] getBArr(){
		return BArr;
	}
	
	
	/**
	 * ��o�}�CSP
	 * @return float SPArr[]
	 */
	public float[] getSP(){
		return SPArr;
	}
	
	
	/**
	 * ��o�зǤ<Ƥα`�Ҥ<�(�Ϫ�)
	 * @return score[][x](0:���q�A1:�ӤH)
	 */
	public int[][] getUserScore(){
		return score;
	}
	
	
	//public int[][] getUserScoreOrder() {
	//	return scoreOrder;
	//}


	/**
	 * �غc�l
	 * @param conn Connection
	 */
	public EvalEachDimensionForeScoreI(Connection conn){
		this.conn = conn;
	}
	
	
	/**
	 * �ǤJ�ϥΪ�PK�P¾�ȦW�٭p��
	 * @param bvmuId
	 * @param positionId
	 * @param prPositionId
	 * @throws Exception
	 */
	public void setDimensionScore(String bvmuId, String positionId, String prPositionId) throws Exception{
			SPArr = new float[questionIdArr.length];	// questionIdArr.length ���V�ת��
			dimensionScore(bvmuId);		//	�p�� SP��
			dimensionForeBScore();	//�p��B��
			setUserScore(positionId);	//�p��ӤH�зǤ<Ƥα`�Ҥ<�(�Ϫ�)
			setSinglePRZRZ(positionId);		// �p���@¾�ȾA�X��
			evalZPRFore(prPositionId);	//�p��s��PR��(�HBArr�Ȩӭp��)
	}	
	
	/**
	 *  �p��SP��
	 *  @param String bvmuId
	 *  @throws Exception
	 */
	private void dimensionScore(String bvmuId) throws Exception{
		PreparedStatement PS1 = null;
		ResultSet RS1 = null;
		
		try{
			String sqlStr = "select u.*,ui.EXAM_FINISHED_DATE from b2b_vip_module_user u,b2b_vip_module_user_index ui where u.bvmu_id=? and u.bvmu_id=ui.bvmu_id";
			PS1 = conn.prepareStatement(sqlStr);
			PS1.setString(1, bvmuId);
			RS1 = PS1.executeQuery();
			
			if(RS1.next()){
				long tempSec = RS1.getTimestamp("EXAM_FINISHED_DATE").getTime();
				
				checkTime(tempSec);
				
				for(int i = 0; i < questionIdArr.length; i++){
					float count = 0.0f;
					for(int j = 0; j < questionIdArr[i].length; j++){
						count += RS1.getFloat("Q_" + questionIdArr[i][j]);
					}
					SPArr[i] = count / questionIdArr[i].length;
					//System.out.println("SPArr["+i+"]="+SPArr[i]);
				}
			}else{
				errMassage = "No This bvmu_id Number : "+bvmuId;
			}
		}catch(Exception ex){
			errMassage = ex.toString();
		}finally{
			try{
				if(RS1!= null) {RS1.close();}
				if(PS1!= null) {PS1.close();}
			}catch(Exception ex){
				errMassage = ex.toString();
			}
		}
	}
	
	
	/**
	 *  �p��B��
	 *  @throws Exception
	 */	
	private void dimensionForeBScore() throws Exception{
		PreparedStatement PS1 = null;
		ResultSet RS1 = null;
		
		try{
			String sqlStr = "select * from b2b_cpi_fore_module_b where is_new="+isBeforeTimeStr+" order by title_dimension_id";
			PS1 = conn.prepareStatement(sqlStr);
			RS1 = PS1.executeQuery();
			for(int i=0; i<BArr.length; i++){
				BArr[i]=0.0f;
			}
			int bNum = 0;
			while(RS1.next()){
				for(int i=0; i<SPArr.length; i++){
					BArr[bNum] += SPArr[i] * RS1.getFloat("d"+(i+1)+"_dev");
					//System.out.println(SPArr[i]+"*"+RS1.getFloat("d"+(i+1)+"_dev")+"="+SPArr[i] * RS1.getFloat("d"+(i+1)+"_dev"));
				}
				BArr[bNum] += RS1.getFloat("cons");
				//System.out.println("BArr["+bNum+"]="+BArr[bNum]);
				bNum++;
			}
		}catch(Exception ex){
			errMassage = ex.toString();
		}finally{
			try{
				if(RS1!= null) {RS1.close();}
				if(PS1!= null) {PS1.close();}
			}catch(Exception ex){
				errMassage = ex.toString();
			}
		}
	}
	
	
	/**
	 * �p��ӤH�зǤ<Ƥα`�Ҥ<�(�Ϫ�)(���w�n���oB�ȡA��HSP�ȭp��A�{�אּ�HBArr�p��)
	 * @param positionId Position_id
	 * @throws Exception
	 */
	private void setUserScore(String positionId) throws Exception{
		PreparedStatement PSSN = null, PSMA = null;
		ResultSet RSSN = null, RSMA = null;
		
		try{
			SN = new double[53];
			MA = new double[53][2];
			score = new int[53][2];
			//scoreOrder = new int[53][2];
			
			String sqlstr="";
			if(Integer.parseInt(positionId)<=100){  //���2��~��¾�ȱ`�Ҧ��7s�¤���
				sqlstr = "select dimension_id,mean from b2b_cpi_module where  is_new="+isBeforeTimeStr+" and position_id='" + positionId + "' order by dimension_id";
			}else{   //���q��¾�ȱ`�Ҥ��7s�¤���
				sqlstr = "select dimension_id,mean from b2b_cpi_module where position_id='" + positionId + "' order by dimension_id";
			}
			PSSN = conn.prepareStatement(sqlstr);
			RSSN = PSSN.executeQuery();

			sqlstr = "select position_id,dimension_id,mean,standard_deviation from b2b_cpi_module where  is_new="+isBeforeTimeStr+" and position_id='1'  order by dimension_id";
			PSMA = conn.prepareStatement(sqlstr);
			RSMA = PSMA.executeQuery();
			
			for(int i = 0; RSSN.next(); i++){
					SN[i] = RSSN.getDouble("mean");
			}
			
			for(int i = 0; RSMA.next(); i++){
					MA[i][0] = RSMA.getDouble("mean");
					MA[i][1] = RSMA.getDouble("standard_deviation");
			}
			
			for(int i = 0; i < 53; i++){
					double sp = (BArr[i] - MA[i][0]) / MA[i][1];
					double sn = (SN[i] - MA[i][0]) / MA[i][1];
					if(sp < -3)sp = -3;
					if(sp > 3)sp = 3;
					if(sn < -3)sn = -3;
					if(sn > 3)sn = 3;
					score[i][1] = (int)Math.round(sp * 20 + 60);
					score[i][0] = (int)Math.round(sn * 20 + 60);
			}
			
			//int[] orderList = {13,1,14,27,16,3,26,11,7,22,12,2,20,8,17,25,24,0,15,10,19,23,6,5,9,21,18,4};
			//for(int i=0; i<orderList.length; i++) {
			//	scoreOrder[i][0] = score[orderList[i]][0];
			//	scoreOrder[i][1] = score[orderList[i]][1];
			//}
		}catch(Exception ex){
			throw ex;
		}finally{
			try{
				if(RSSN != null) {RSSN.close();}
				if(RSMA != null) {RSMA.close();}
				if(PSSN != null) {PSSN.close();}
				if(PSMA != null) {PSMA.close();}
			}catch(Exception ex){
				throw ex;
			}
		}
	}
		
	
	/**
	 * �p���@¾�ȾA�X��(���w�n���oB�ȡA��HSP�ȭp��A�{�אּ�HBArr�p��)
	 * @param String positionId
	 * @throws Exception
	 */
	private void setSinglePRZRZ(String positionId) throws Exception{
		PreparedStatement PS1 = null, PS2 = null, PS3 = null;
		ResultSet RS1 = null, RS2 = null, RS3 = null;
			
		double[][] MSDRZ=new double[1][2];
		double[][] SM=new double[1][53];
		double[][] MSDA=new double[53][2];
		double[][] SD=ZTOPR.STATISTIC_DATA;
			
		try{
			String sqlStr1="";
			if(Integer.parseInt(positionId)<=100){  //���2��~��¾�ȱ`�Ҧ��7s�¤���
				sqlStr1 = "select position_name,ZRM,ZRSD from b2b_cpi_position_m where is_new="+isBeforeTimeStr+" and position_id =? order by position_id";
			}else{   //���q��¾�ȱ`�Ҥ��7s�¤���
				sqlStr1 = "select position_name,ZRM,ZRSD from b2b_cpi_position_m where position_id =? order by position_id";
			}
			PS1 = conn.prepareStatement(sqlStr1);
			PS1.setString(1,positionId);
			RS1 = PS1.executeQuery();
			RS1.next();
			
			String sqlStr2="";
			if(Integer.parseInt(positionId)<=100){  //���2��~��¾�ȱ`�Ҧ��7s�¤���
				sqlStr2 = "select mean from b2b_cpi_module where is_new="+isBeforeTimeStr+" and position_id=? order by dimension_id";
			}else{   //���q��¾�ȱ`�Ҥ��7s�¤���
				sqlStr2 = "select mean from b2b_cpi_module where position_id=? order by dimension_id";
			}
			PS2 = conn.prepareStatement(sqlStr2);
				
			String sqlStr3="select mean,standard_deviation from b2b_cpi_module where is_new="+isBeforeTimeStr+" and  position_id='1'  order by dimension_id";
			PS3 = conn.prepareStatement(sqlStr3);
			RS3 = PS3.executeQuery();
			
			positionName = RS1.getString("position_name");
			MSDRZ[0][0] = RS1.getDouble("ZRM");
			MSDRZ[0][1] = RS1.getDouble("ZRSD");
				
			if(MSDRZ[0][0]!=0.0 && MSDRZ[0][1]!=0.0){
				PS2.setString(1,positionId);
				RS2 = PS2.executeQuery();
						
				for(int a=0;RS2.next();a++){
					SM[0][a]=RS2.getDouble("mean");
				}
	                	
				for(int a=0;RS3.next();a++){
					MSDA[a][0]=RS3.getDouble("mean");
					MSDA[a][1]=RS3.getDouble("standard_deviation");
				}
						
				int[] PRZRZS=PRCalculation.getPRZRZValues(BArr,SM,MSDA,MSDRZ,SD);
				singlePRZRZ=PRZRZS[0];
			}
		}catch(Exception ex){
			throw ex;
		}finally{
			try{
				if(RS1!=null) {RS1.close();}
				if(PS1!=null) {PS1.close();}
				if(RS2!=null) {RS2.close();}
				if(RS3!=null) {RS3.close();}
				if(PS2!=null) {PS2.close();}
				if(PS3!=null) {PS3.close();}
			}catch(Exception ex){
				throw ex;
			}
		}
	}
	
	/**
	 * �p��s�W�h��PR��(��HSP�ȭp��A�{�אּ�HBArr�p��)
	 * @param String positionId
	 * @throws Exception
	 */
	private void evalZPRFore(String prPositionId) throws Exception{
		PreparedStatement PS1 = null;
		ResultSet RS1 = null;
	
		try{
			//�d�X�����Ʊ`�Ҹ��
			String sqlStr = "select * from b2b_cpi_module where is_new="+isBeforeTimeStr+" and  position_id="+prPositionId+" order by dimension_id";
			PS1 = conn.prepareStatement(sqlStr);
			RS1 = PS1.executeQuery();
			for(int i = 0; i < BArr.length && RS1.next(); i++){
				ZArr[i] = ((BArr[i]-RS1.getDouble("mean")) / RS1.getDouble("standard_deviation"));
				PRArr[i] = ZTOPR.getPR(ZArr[i]);
				//System.out.println("BArr["+i+"]="+BArr[i]+",   ZArr["+i+"]="+PRArr[i]+",   PRArr["+i+"]="+PRArr[i]);
			}
		}catch(Exception ex){
			throw ex;
		}finally{
			try{
				if(RS1!=null) {RS1.close();}
				if(PS1!=null) {PS1.close();}
			}catch(Exception ex){
				throw ex;
			}
		}
	}

	private void checkTime(long tempSec){
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.set(2007,7,14,0,0,0);
		cal2.set(2007,10,1,0,0,0);
		long finshTime1 = cal1.getTime().getTime();
		long finshTime2 = cal2.getTime().getTime();
		if(tempSec >= finshTime2){
			isBeforeTimeStr = "2";
		}else if(tempSec >= finshTime1){
			isBeforeTimeStr = "1";
		}
	}
}