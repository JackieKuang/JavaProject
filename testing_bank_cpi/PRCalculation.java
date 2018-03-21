package testing_bank_cpi;

/**
	get user's PR_ZRZ value of position
	Sp:���̦U�V�פ<��`�M
	Sm:¾�ȦU�V�פ<��`�M 
	MSDa:���饭���ȩM�зǮt
	MSDrz:�U¾�ȥ����ȩM�зǮt
	StatisticData0:�`�A�'G��
	Zp:���̦U�V��Z�� Zp[i]=(Sp[i]-MSDa[i][0])/MSDa[i][1]
	Zm:¾�ȦU�V��Z�� Zm[i]=(Sm-MSDa[i][0])/MSDa[i][1]
	R:�����  R(i)=�U(Zp[i]*Zm[i][j])/Zm[i].length
	RZ:����Ȫ��`�A�Ƥ<� RZ[i]=Math.log(Math.sqrt((1+R)/(1-R)))
	ZRZ:RZ���зǤƤ<�  ZRZ[i]=(RZ[i]-MSDrz[i][0])/MSDrz[0][1]
	PRZRZ:ZRZ��PR�� PRZRZ[i]=search(ZRZ[i])
*/
public class PRCalculation{
	private static double[][] StatisticData;	
	
	public static double[] getZp(float Sp[],double[][] MSDa){
		double[] Zp=new double[Sp.length];  
		for(int i=0;i<Sp.length;i++)Zp[i]=(Sp[i]-MSDa[i][0])/MSDa[i][1];
		return Zp;
	}
        
        public static double[] getZpInt(int Sp[],double[][] MSDa){
		double[] Zp=new double[Sp.length];  
		for(int i=0;i<Sp.length;i++)Zp[i]=(Sp[i]-MSDa[i][0])/MSDa[i][1];
		return Zp;
	}
        
	public static double[][] getZm(double[][] Sm,double[][] MSDa){
		double[][] Zm=new double[Sm.length][Sm[0].length];  
		for(int i=0;i<Sm.length;i++){
			for(int j=0;j<Sm[i].length;j++)Zm[i][j]=(Sm[i][j]-MSDa[j][0])/MSDa[j][1];
		}
		return Zm;
	}
	public static double[] getR(double[] Zp,double[][] Zm){
		double[] R=new double[Zm.length];
		for(int i=0;i<Zm.length;i++)
			R[i]=Correlation.getR(Zp,Zm[i]);
		return R;
	}
	public static double[] getRZ(double[] R){
		double[] RZ=new double[R.length];
		for(int i=0;i<R.length;i++){
			if(new Double(Math.log(Math.sqrt((1+R[i])/(1-R[i])))).isNaN())
			RZ[i]=0.0;
			else
			RZ[i]=Math.log(Math.sqrt((1+R[i])/(1-R[i])));
		}
		return RZ;
	}
	public static double[] getZRZ(double[][] MSDrz,double[] RZ){
		double[] ZRZ=new double[RZ.length];
                for(int i=0;i<RZ.length;i++) {
                    if(MSDrz[i][0]!=0.0 && MSDrz[i][1]!=0.0) {
                        ZRZ[i]=(RZ[i]-MSDrz[i][0])/MSDrz[i][1];
                    }
                }
//		for(int i=0;i<RZ.length;i++)ZRZ[i]=(RZ[i]-MSDrz[i][0])/MSDrz[i][1];
		return ZRZ;
	}
	public static int[] getPRZRZ(double[] ZRZ){
		int PRZRZ[]=new int[ZRZ.length];
		double zrz=0;
		for(int i=0;i<ZRZ.length;i++){
                        if(ZRZ[i]!=0.0) {
                            zrz=Math.rint(ZRZ[i]*100)/100;
                            if(zrz>3)zrz=3;
                            if(zrz<-3)zrz=-3;
                            PRZRZ[i]=search(zrz);
                        }
                        else {
                            PRZRZ[i]=0;
                        }
		}	
		return PRZRZ;
	}
	
     /**
      * get PR value
      * @param Sp
      * @param Sm
      * @param MSDa
      * @param MSDrz
      * @param StatisticData0
      * @return int[]
      */
	public static int[] getPRZRZValues(float[] Sp,double[][] Sm,double[][] MSDa,double[][] MSDrz,double[][] StatisticData0){
		StatisticData=StatisticData0; 
		double[] R=getR(getZp(Sp,MSDa),getZm(Sm,MSDa));
		double[] RZ=getRZ(R);
		double[] ZRZ=getZRZ(MSDrz,RZ);
		int[] PRZRZ=getPRZRZ(ZRZ);
		return PRZRZ;
	}
        
        /**
         * get PR value
         * @param Sp
         * @param Sm
         * @param MSDa
         * @param MSDrz
         * @param StatisticData0
         * @return int[]
         */
        public static int[] getPRZRZValues(int[] Sp,double[][] Sm,double[][] MSDa,double[][] MSDrz,double[][] StatisticData0){
		StatisticData=StatisticData0; 
		double[] R=getR(getZpInt(Sp,MSDa),getZm(Sm,MSDa));
		double[] RZ=getRZ(R);
		double[] ZRZ=getZRZ(MSDrz,RZ);
		int[] PRZRZ=getPRZRZ(ZRZ);
		return PRZRZ;
	}
        
	private static int search(double ZRZ){
		int mid;
		int min=0;
		int max=StatisticData.length;
		do
		{
			mid=(min+max)/2;
			if(StatisticData[mid][1]<ZRZ)min=mid;
			else max=mid;
		}while(StatisticData[mid][1]!=ZRZ);
		return (int)Math.floor(StatisticData[mid][2]*100);
	}
}