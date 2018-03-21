package testing_bank_cpi;
public class Correlation{
	public static double getR(double[] Zp,double[]Zm){
		double R=0.0;
		double zps=0.0;
		double zms=0.0;
		double zpm=0.0;
		double zmm=0.0;
		double zpd2s=0.0;
		double zmd2s=0.0;
		double zpdzmd=0.0;
		double[] zpd=new double[Zp.length];
		double[] zmd=new double[Zm.length];
		for(int i=0;i<Zp.length;i++)zps+=Zp[i];
		for(int i=0;i<Zm.length;i++)zms+=Zm[i];
		zpm=zps/Zp.length;
		zmm=zms/Zm.length;
		for(int i=0;i<Zp.length;i++)zpd[i]=Zp[i]-zpm;
		for(int i=0;i<Zm.length;i++)zmd[i]=Zm[i]-zmm;
		for(int i=0;i<Zp.length;i++)zpd2s+=zpd[i]*zpd[i];
		for(int i=0;i<Zm.length;i++)zmd2s+=zmd[i]*zmd[i];
		for(int i=0;i<Zp.length;i++)zpdzmd+=zpd[i]*zmd[i];
		return zpdzmd/(Math.sqrt(zpd2s)*Math.sqrt(zmd2s));
	}
}