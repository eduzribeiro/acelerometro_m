/*
 * Copyright (c) 2015. Fernando Pujaico Rivera <fernando.pujaico.rivera@gmail.com>
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */


package net.sourceforge.pdsplibj.pdsrv;

import net.sourceforge.pdsplibj.pdsrv.PdsCongruential;
import net.sourceforge.pdsplibj.pdsrv.PdsUniform;
import net.sourceforge.pdsplibj.pdsrv.PdsRV;
/** 
 * Esta classe implementa uma variável aleatória gaussiana.
 *
 * <br><br>
 * <center><img src="{@docRoot}/doc/imagenes/PdsGaussian.png" alt="X \sim \mathcal{N}(\mu,\,\sigma^2)"></center><br>
 * Se na fonte aleatória Gaussiana a variância é 1.0 e a meia é zero então a saída  está maiormente concentrada entre -1.0 e +1.0
 * <center><img src="{@docRoot}/doc/imagenes/XGaussian.png" width="600" alt="X"></center><br>
 * Isto pode ser visto na sua função de densidade de probabilidade.
 * <center><img src="{@docRoot}/doc/imagenes/PDF-Gaussian.png" width="600" alt="f_X(x)=\frac{1}{\sigma\sqrt{2\pi}}\, e^{-\frac{(x - \mu)^2}{2 \sigma^2}}"></center><br>
 * <br><br> Para usar esta classe é necessário escrever:
 *  <pre>  import net.sourceforge.pdsplibj.pdsrv.PdsGaussian; </pre>
 *
 * Um exemplo de código seria:
 * <pre>
 *  PdsGaussian X=new PdsGaussian(0,1);
 *
 *  //Logo simplesmente pedir os dados
 *  value=X.GetValue();
 * </pre>
 * <br>
 * @author Fernando Pujaico Rivera <a href="mailto:fernando.pujaico.rivera@gmail.com">fernando.pujaico.rivera@gmail.com</a>
 * @version 0.01
 * @since 2015-05-25
 * @see <a href="http://pdsplib.sourceforge.net"> PDS Project Libraries in Java </a>
*/
public class PdsGaussian {
	// Uma sequencia aleatória congruencial
	private PdsCongruential X1=null;
	// Uma variável aleatória uniformemente distribuída entre [0,1).
	private PdsUniform X2=null;
	// A meia da variável aleatória gaussiana.
	private double U;
	// O desvio padrão da variável aleatória gaussiana.
	private double Sigma;
	// A saída da variável aleatória gaussiana.
	private double x;

	/**
	 * Este é o construtor da classe PdsGaussian
	 *
	 *
	 * @param U E a media da variável aleatória gaussiana.
	 * @param Sigma E o desvio padrão da variável aleatória gaussiana.
	 **/
	public PdsGaussian(double U,double Sigma) {



		this.X1= new PdsCongruential();	
		this.X2= new PdsUniform(0.0,1.0);	

		if( (this.X1!=null)&&(this.X2!=null) ){		
			this.U=U;
			this.Sigma=Sigma;
			this.x=0;
		}
		
	}

	/**
	 * Este método o inicia a variável aleatória gaussiana.
	 *
	 *
	 * @param x0 valor inicial da variável aleatória gaussiana.
	 **/
	public void Init(double x0) {
		long x1;
		double x2;
		long N;

		N=PdsRV.PDS_RAND_MAX;

		x2=0.5;
		if(this.Sigma!=0)	x1=(long)( N-N*Math.exp((-0.5)*((x0-this.U)/this.Sigma)*((x0-this.U)/this.Sigma)) );
		else				x1=N;
		
		if(x1>=N)	x1=N-1;	//As variáveis Congruential vão de [0,PdsRV.PDS_RAND_MAX>
	
		if(this.X1!=null)	this.X1.Init(x1);
		if(this.X2!=null)	this.X2.Init(x2);
	
		this.x=x0;

	}
	

	/**
	 * Este método retorna um valor da variável aleatória gaussiana.
	 *
	 *
	 * @return Retorna um valor da variável aleatória gaussiana.
	 **/
	public double GetValue() {

		long x1;
		double x2;
		long N;
		double K;

		x1=this.X1.GetValue();	//RV Congruential
		x2=this.X2.GetValue();	//RV Uniform

		K=PdsRV._2LN_PDS_RAND_MAX;
		N=PdsRV.PDS_RAND_MAX;

		this.x=this.Sigma*Math.sqrt(K-2.0*Math.log(N-x1))*Math.cos(2.0*Math.PI*x2)+this.U;

		return this.x;	
	}
}
