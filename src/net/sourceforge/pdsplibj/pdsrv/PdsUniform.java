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
import net.sourceforge.pdsplibj.pdsrv.PdsRV;

/** 
 * Esta classe implementa uma variável aleatória uniforme distribuída entre os valores A e B.
 *
 * <br><br>
 * <center><img src="{@docRoot}/doc/imagenes/PdsUniform.png" width="400" alt=" X maior igual a A e menor que B "></center><br>
 *
 * Se a fonte aleatória Uniforme esta distribuída entre 0.0 e 1.0 então:
 * <center><img src="{@docRoot}/doc/imagenes/XUniform.png" width="600" alt="X"></center><br>
 * Isto pode ser visto na sua função de densidade de probabilidade.
 * <center><img src="{@docRoot}/doc/imagenes/PDF-Uniform.png" width="600" alt="f_X(x)=1"></center><br>

 * <br><br> Para usar esta classe é necessário escrever:
 *  <pre>  import net.sourceforge.pdsplibj.pdsrv.PdsUniform; </pre>
 *
 * Um exemplo de código seria:
 * <pre>
 *  PdsUniform X=new PdsUniform(0,1);
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
public class PdsUniform {
	/** Uma sequencia aleatória gerada com o método congruencial misto.
	 *  sua saída vai entre 0<=x0<PdsRV.PDS_RAND_MAX
	 */
	private PdsCongruential X1=null;

	/** A saída real da variável aleatória uniformemente distribuída  
	 *  vai entre A<=x<B. 
	 */
	private double x;

	/** Rango inicial esquerdo da variável aleatória. */
	private double A;

	/** Rango final direito da variável aleatória. */
	private double B;

	/**
	 * Este é o construtor da classe PdsUniform
	 *
	 *
	 * @param A Valor inicial esquerdo da variável aleatória..
	 * @param B Valor final direito da variável aleatória.
	 **/
	public PdsUniform(double A,double B) {
		

		if(A!=B)	this.X1= new PdsCongruential();	
		else		this.X1= null;	
		
		this.A=A;
		this.B=B;
		this.x=0;
	
	}

	/**
	 * Este método o inicia a variável aleatória uniforme.
	 * 
	 * Se x0 esta fora do rango [A,B> então ele toma o valor de A
	 * @param x0 valor inicial da variável aleatória uniforme.
	 **/
	public void Init(double x0) {
		long x1;

		if((x0 < this.A)||(x0 >= this.B))	x0=this.A;

		if(this.B!=this.A)	x1=(long)(((x0 - this.A)/(this.B - this.A))*(PdsRV.PDS_RAND_MAX-64));
		else				x1=0;

		if(this.X1!=null)	this.X1.Init(x1);

		this.x=x0;
	}
	

	/**
	 * Este método retorna um valor da variável aleatória uniforme.
	 *
	 *
	 * @return Retorna um valor da variável aleatória uniforme.
	 **/
	public double GetValue() {

		long x1;

		if(this.X1!=null)	x1=this.X1.GetValue();
		else				x1=0;

		this.x=((x1*1.0)/((PdsRV.PDS_RAND_MAX+64)))*(this.B-this.A)+this.A;

		return this.x;	
	}
}
