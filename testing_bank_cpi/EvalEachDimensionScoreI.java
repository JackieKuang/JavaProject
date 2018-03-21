
package testing_bank_cpi;

/**
 * @author jackie
 *
 * �p��¾��ʮ����`�һ`����i���G
 * SP�BPR�BZ�B�A�X��
 */

import java.sql.*;
import java.util.*;

public class EvalEachDimensionScoreI {
	
	private float[] SPArr;
	private double[] ZArr;
	private int[] PRArr;
	private double[] SN;
	private double[][] MA;
	private int[][] score;
	private int[][] scoreOrder;
	private int singlePRZRZ=0;
	private int[] PRZRZ;
	private String positionName;
	private String[] positionNameArr;
	private Vector hh=new Vector(1);
	private Vector hm=new Vector(1);
	private Vector ll=new Vector(1);
	private Vector lm=new Vector(1);
	private Vector m=new Vector(1);
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
	
	/*
	public static void main(String[] args) throws Exception {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@10.0.7.10:1521:labora01", "testing", "111111");
		
		EvalEachDimensionScore a = new EvalEachDimensionScore();
		a.setDimensionScore(conn,"8459");
		float[] f1 = a.getSP();
		
		conn.close();
	}
	*/
	
	/**
	 * �غc�l
	 *
	 */
	public EvalEachDimensionScoreI() {} 
	
	/**
	 * ��o�}�CSP
	 * @return float SPArr[]
	 */
	public float[] getSP() {
		return SPArr;
	}
	
	/**
	* ��o�}�CZ
	* @return double ZArr[]
	*/
	public double[] getZ() {
		return ZArr;
	}
	
	/**
	* ��o�}�CPR
	* @return int PRArr[]
	*/
	public int[] getPR() {
		return PRArr;
	}
	
	/**
	 * ��o�}�CPRZRZ
	 * @return
	 */
	public int getSinglePRZRZ() {
		return singlePRZRZ;
	}
	
	/**
	 * ��o�зǤ<Ƥα`�Ҥ<�(�Ϫ�)
	 * @return score[][x](0:���q�A1:�ӤH)
	 */
	public int[][] getUserScore() {
		return score;
	}
	
	public int[][] getUserScoreOrder() {
		return scoreOrder;
	}
	
	/**
	 * ��o�}�CpositionName
	 * @return
	 */
	public String getPositionName() {
		return positionName;
	}
	
	/**
	 * ��o��3����i�U��ԭz����¾��
	 * @return Vector
	 */
	public Vector getDescHH() {
		return hh;
	}
	
	public Vector getDescHM() {
		return hm;
	}
	
	public Vector getDescLL() {
		return ll;
	}
	
	public Vector getDescLM() {
		return lm;
	}
	
	public Vector getDescM() {
		return m;
	}
	
	/**
	 * �]�w�p��U����(19)���`��SP�BZ�BPR
	 * @param conn Connection
	 * @param puedId puedId
	 * @throws Exception
	 */
	public void setDimensionScore(Connection conn, String bvmuId, String positionId) throws Exception {
		SPArr = new float[questionIdArr.length];	// questionIdArr.length �����ת��
		ZArr = new double[questionIdArr.length];
		PRArr = new int[questionIdArr.length];
		dimensionScore(conn, bvmuId);	//	�p�� SP��
		evalZPR(conn, positionId);	//	�p�� Z�ȡBPR��
		setUserScore(conn, positionId);	//�p��зǤ<Ƥα`�Ҥ<�(�Ϫ�)
		setSinglePRZRZ(conn, positionId);	// �p���@¾�ȾA�X��
	}
	
	/**
	 * �p�� Z�ȡBPR��
	 * @param conn Connection
	 * @throws Exception
	 */
	private void evalZPR(Connection conn, String positionId) throws Exception {
		PreparedStatement PS1=null;
		ResultSet RS1=null;
		
		try {
			//	�d�X�����Ʊ`�Ҹ��
			
			String sqlStr = "select * from b2b_vip_module where position_id=? order by dimension_id";
			PS1 = conn.prepareStatement(sqlStr);
			PS1.setString(1,positionId);
			RS1 = PS1.executeQuery();
			for(int i=0; i<SPArr.length && RS1.next(); i++) {
				ZArr[i]= ((SPArr[i]-RS1.getDouble("mean")) / RS1.getDouble("standard_deviation"));
				PRArr[i]=ZTOPR.getPR(ZArr[i]);
				if(i!=4) {
						if(ZArr[i]>0.52)hm.addElement(RS1.getString("dimension_name"));
						if(ZArr[i]>1.03)hh.addElement(RS1.getString("dimension_name"));
						if(ZArr[i]<=0.52 && ZArr[i]>=-0.52)m.addElement(RS1.getString("dimension_name"));
						if(ZArr[i]<-0.52)lm.addElement(RS1.getString("dimension_name"));
						if(ZArr[i]<-1.03)ll.addElement(RS1.getString("dimension_name"));
				}
			}
		}
		catch(Exception ex) {
			throw ex;
		}
		finally {
			try {
				if(RS1!=null) {RS1.close();}
				if(PS1!=null) {PS1.close();}
			}
			catch(Exception ex) {
				throw ex;
			}
		}
	}
	
	/**
	 * �p�� SP��
	 * @param conn Connection
	 * @param puedId puedId
	 * @throws Exception
	 */
	private void dimensionScore(Connection conn, String bvmuId) throws Exception {
		PreparedStatement PS1 = null;
		ResultSet RS1=null;
		
		try {
			String sqlStr = "select * from b2b_vip_module_user where bvmu_id=?";
			PS1 = conn.prepareStatement(sqlStr);
			PS1.setString(1,bvmuId);
			RS1 = PS1.executeQuery();
			if(RS1.next()) {
				for(int i=0; i<questionIdArr.length; i++) {
					float count = 0.0f;
					for(int j=0; j<questionIdArr[i].length; j++) {
						count += RS1.getFloat("Q_"+questionIdArr[i][j]);
					}
					SPArr[i] = count / questionIdArr[i].length;
				}
			}
			else {
				throw new Exception("No This Bnmat_id Number : "+bvmuId);
			}
		}
		catch(Exception ex) {
			throw ex;
		}
		finally {
			try {
				if(RS1!=null) {RS1.close();}
				if(PS1!=null) {PS1.close();}
			}
			catch(Exception ex) {
				throw ex;
			}
		}
	}
	
	/**
	 * �p��ӤH�зǤ<Ƥα`�Ҥ<�(�Ϫ�)
	 * @param conn Connection
	 * @param positionId Position_id
	 * @throws Exception
	 */
	private void setUserScore(Connection conn, String positionId) throws Exception {
		PreparedStatement PSSN=null, PSMA=null;
		ResultSet RSSN=null, RSMA=null;
		
		try {
			SN = new double[questionIdArr.length];
			MA = new double[questionIdArr.length][2];
			score = new int[questionIdArr.length][2];
			scoreOrder = new int[questionIdArr.length][2];
			
			String sqlstr="select dimension_id,mean from b2b_vip_module where position_id='"+positionId+"' order by dimension_id";
			PSSN = conn.prepareStatement(sqlstr);
			RSSN = PSSN.executeQuery();

			sqlstr="select position_id,dimension_id,mean,standard_deviation from b2b_vip_module where position_id='1'  order by dimension_id";
			PSMA = conn.prepareStatement(sqlstr);
			RSMA = PSMA.executeQuery();
			
			for(int i=0;RSSN.next();i++){
					SN[i]=RSSN.getDouble("mean");
			}
			
			for(int i=0;RSMA.next();i++){
					MA[i][0]=RSMA.getDouble("mean");
					MA[i][1]=RSMA.getDouble("standard_deviation");
			}
			
			for(int i=0;i<questionIdArr.length;i++){
					double sp=(SPArr[i]-MA[i][0])/MA[i][1];
					double sn=(SN[i]-MA[i][0])/MA[i][1];
					if(sp<-3)sp=-3;
					if(sp>3)sp=3;
					if(sn<-3)sn=-3;
					if(sn>3)sn=3;
					score[i][1]=(int)Math.round(sp*20+60);
					score[i][0]=(int)Math.round(sn*20+60);
			}
			
			
			int[] orderList = {13,1,14,27,16,3,26,11,7,22,12,2,20,8,17,25,24,0,15,10,19,23,6,5,9,21,18,4};
			for(int i=0; i<orderList.length; i++) {
				scoreOrder[i][0] = score[orderList[i]][0];
				scoreOrder[i][1] = score[orderList[i]][1];
			}
			
			
			//scoreOrder = score;
		}
		catch(Exception ex) {
			throw ex;
		}
		finally{
			try {
				if(RSSN!=null) {RSSN.close();}
				if(RSMA!=null) {RSMA.close();}
				if(PSSN!=null) {PSSN.close();}
				if(PSMA!=null) {PSMA.close();}
			}
			catch(Exception ex) {throw ex;}
		}
	}
	
	/**
	 * �p���@¾�ȾA�X��(���w�n�����oSP��)
	 * @param conn Connection
	 * @param positionId positionId
	 * @throws Exception
	 */
	private void setSinglePRZRZ(Connection conn, String positionId) throws Exception {
		PreparedStatement PS1 = null, PS2 = null, PS3 = null;
		ResultSet RS1=null, RS2 = null, RS3 = null;
		
		double[][] MSDRZ=new double[1][2];
		double[][] SM=new double[1][questionIdArr.length];
		double[][] MSDA=new double[questionIdArr.length][2];
		double[][] SD=ZTOPR.STATISTIC_DATA;
		
		try {
			String sqlStr1 = "select position_name,ZRM,ZRSD from b2b_vip_position_m where position_id =? order by position_id";
			PS1 = conn.prepareStatement(sqlStr1);
			PS1.setString(1,positionId);
			RS1 = PS1.executeQuery();
			RS1.next();
			
			String sqlStr2="select mean from b2b_vip_module where position_id=? order by dimension_id";
			PS2 = conn.prepareStatement(sqlStr2);
			
			String sqlStr3="select mean,standard_deviation from b2b_vip_module where position_id='1'  order by dimension_id";
			PS3 = conn.prepareStatement(sqlStr3);
			RS3 = PS3.executeQuery();
			
			
			MSDRZ[0][0]=RS1.getDouble("ZRM");
			MSDRZ[0][1]=RS1.getDouble("ZRSD");
			positionName = RS1.getString("position_name");
			
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
					
					int[] PRZRZS=PRCalculation.getPRZRZValues(SPArr,SM,MSDA,MSDRZ,SD);
					singlePRZRZ=PRZRZS[0];
			}
			
		}
		catch(Exception ex) {
			throw ex;
		}
		finally {
			try {
				if(RS1!=null) {RS1.close();}
				if(PS1!=null) {PS1.close();}
				if(RS2!=null) {RS2.close();}
				if(RS3!=null) {RS3.close();}
				if(PS2!=null) {PS2.close();}
				if(PS3!=null) {PS3.close();}
			}
			catch(Exception ex) {
				throw ex;
			}
		}
	}
	
	/**
	 * �p��Ҧ�¾�ȾA�X��
	 * @param conn Connection
	 * @param String bvId
	 * @param String positionIdList
	 * @param int positionIdListCount
	 * @throws Exception
	 */
	public void setPRZRZ(Connection conn,String bvId,String positionIdList,int positionIdListCount) throws Exception{
		SPArr = new float[questionIdArr.length];	// questionIdArr.length �����ת��
		dimensionScore(conn,bvId);	//	�p�� SP��
		
		PreparedStatement PS1=null,PS2=null,PS3=null;
		ResultSet RS1=null,RS2=null,RS3=null;
		
		double[][] MSDRZ = new double[positionIdListCount][2];
		double[][] SM = new double[positionIdListCount][questionIdArr.length];
		double[][] MSDA = new double[questionIdArr.length][2];
		double[][] SD = ZTOPR.STATISTIC_DATA;
		
		try{
			String sqlStr1 = "select POSITION_ID,POSITION_NAME,ZRM,ZRSD from B2B_VIP_POSITION_M where POSITION_ID in("+positionIdList+") order by POSITION_ID";
			PS1 = conn.prepareStatement(sqlStr1);
			RS1 = PS1.executeQuery();
			
			String sqlStr2 = "select MEAN from B2B_VIP_MODULE where POSITION_ID=? order by DIMENSION_ID";
			PS2 = conn.prepareStatement(sqlStr2);
			
			String sqlStr3 = "select MEAN,STANDARD_DEVIATION from B2B_VIP_MODULE where POSITION_ID=1 order by DIMENSION_ID";
			PS3 = conn.prepareStatement(sqlStr3);
			RS3 = PS3.executeQuery();
			
			positionNameArr=new String[positionIdListCount];
			for(int i=0;RS1.next();i++){
				positionNameArr[i]=RS1.getString("POSITION_NAME");
				MSDRZ[i][0]=RS1.getDouble("ZRM");
				MSDRZ[i][1]=RS1.getDouble("ZRSD");
				
				PS2.setString(1,RS1.getString("POSITION_ID"));
				RS2 = PS2.executeQuery();
				for(int j=0; RS2.next(); j++){
					SM[i][j] = RS2.getDouble("MEAN");
				}
			}
			
			for(int a=0; RS3.next(); a++){
				MSDA[a][0] = RS3.getDouble("MEAN");
				MSDA[a][1] = RS3.getDouble("STANDARD_DEVIATION");
			}
				
			PRZRZ = PRCalculation.getPRZRZValues(SPArr, SM, MSDA, MSDRZ, SD);
		}catch(Exception ex){
			throw ex;
		}finally{
			try{
				if(RS1 != null) {RS1.close();}
				if(PS1 != null) {PS1.close();}
				if(RS2 != null) {RS2.close();}
				if(RS3 != null) {RS3.close();}
				if(PS2 != null) {PS2.close();}
				if(PS3 != null) {PS3.close();}
			}catch(Exception ex) {
				throw ex;
			}
		}
	}
	
	/**
	 * ��o�}�CPRZRZ
	 * @return int PRZRZ[]
	 */
	public int[] getPRZRZ(){
		return PRZRZ;
	}
	
	/**
	 * ��o�}�CpositionNameArr
	 * @return int PRZRZ[]
	 */
	public String[] getPositionNameArr(){
		return positionNameArr;
	}
}