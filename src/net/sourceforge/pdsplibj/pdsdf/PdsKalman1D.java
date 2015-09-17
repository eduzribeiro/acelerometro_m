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


package net.sourceforge.pdsplibj.pdsdf;

import  net.sourceforge.pdsplibj.pdsra.PdsFifoValue;
import  net.sourceforge.pdsplibj.pdsrv.*;

/**
 * Esta classe implementa um filtro Kalman de entrada unidimensional 
 * <img src="{@docRoot}/doc/imagenes/Zk.png" height="14" alt="Z_k"> e saída
 * <img src="{@docRoot}/doc/imagenes/hatXk.png" height="18" alt="\hat{X}_k">,
 * onde este ultimo é uma predição de 
 * <img src="{@docRoot}/doc/imagenes/Xk.png" height="14" alt="X_k">
 * no modelo de fonte ideal.
 *
 * <br><br>
 * <center><img src="{@docRoot}/doc/imagenes/PdsKalman1D.png" width="700" alt="Z_k=H X_k + V_k  X_k=A X_{k-1} + U_k"></center>
 * <br><br>
 *  O filtro Kalman simula internamente uma fonte com a mesma estatística que
 *  <img src="{@docRoot}/doc/imagenes/Uk.png" width="14" alt="U_k">, esta nova fonte 
 *  é chamada de <img src="{@docRoot}/doc/imagenes/hatUk.png" width="14" alt="\hat{U}_k">,
 *  para criar esta fonte é necessario estimar no modelo de fonte ideal a variança de 
 *  <img src="{@docRoot}/doc/imagenes/Uk.png" width="14" alt="U_k">,
 *  para o resto de cálculos o filtro só necessita os parâmetros A,H e R, sendo R 
 *  a variância de <img src="{@docRoot}/doc/imagenes/Vk.png" width="14" alt="V_k">
 *  <br><br> Para usar esta classe é necessário escrever:
 *  <pre>  import net.sourceforge.pdsplibj.pdsdf.PdsKalman; </pre>
 *
 *  <br>Para usar este filtro pode-se usar este código de exemplo:
 *  <pre>  
 *  PdsKalman1D filtro=new PdsKalman1D(A,H,Q,R);
 *  
 *  hatUk=filtro.EvaluateValue(Zk);
 *  
 *  </pre>
 * 
 * @author Fernando Pujaico Rivera <a href="mailto:fernando.pujaico.rivera@gmail.com">fernando.pujaico.rivera@gmail.com</a>
 * @version 0.01
 * @since 2015-05-25
 * @see <a href="http://pdsplib.sourceforge.net"> PDS Project Libraries in Java </a>
 * @see PdsFir
 */
public class PdsKalman1D {
	//valores iterativos	
	private double X;
	private double P;

	//Variaveis do sistema
	private double A;
	private double H;
	private double Q;
	private double R;
	
	PdsGaussian G=null;

	/**
	 * Este construtor cria um filtro KALMAN.
	 *
	 * O filtro Kalman usa predizer seu comportamento um modelo de fonte real como:<br><br>
	 * <center><img src="{@docRoot}/doc/imagenes/XkAXk1Uk.png" height="22"  alt="X_k=A X_{k-1} + U_k"></center><br>
	 * e um modelo de fonte real como:<br>
	 * <center><img src="{@docRoot}/doc/imagenes/ZkHXkVk.png" height="22"  alt="Z_k=H X_k + V_k"></center><br>
	 * Onde   <img src="{@docRoot}/doc/imagenes/UkN0Q.png" height="18" alt="U_k --> N(0,Q), Q=SigmaQ^2"> e
	 * <img src="{@docRoot}/doc/imagenes/VkN0R.png" height="18" alt="V_k --> N(0,R), R=SigmaR^2">.
	 *
	 * @param A É um fator do modelo de fonte ideal <img src="{@docRoot}/doc/imagenes/Xk.png" height="14" alt="X_k"> onde <img src="{@docRoot}/doc/imagenes/XkAXk1Uk.png" height="14" alt="X_k=A X_{k-1} + U_k">
	 * @param H É um fator do modelo de fonte real <img src="{@docRoot}/doc/imagenes/Zk.png" height="14" alt="Z_k"> onde <img src="{@docRoot}/doc/imagenes/ZkHXkVk.png" height="14" alt="Z_k=H X_k + V_k">
	 * @param Q É a variância de <img src="{@docRoot}/doc/imagenes/UkN0Q.png" height="18" alt="U_k --> N(0,Q), Q=SigmaQ^2">
	 * @param R É a variância de <img src="{@docRoot}/doc/imagenes/VkN0R.png" height="18" alt="V_k --> N(0,R), R=SigmaR^2">
	 **/
	public PdsKalman1D(double A,double H,double Q,double R) {
		this.X=0.0;
		this.P=0.0;
		
		this.A=A;
		this.H=H;
		this.Q=Q;
		this.R=R;
		
		this.G=new PdsGaussian(0,Math.sqrt(Q));
		
	}

	/**
	 * Este método inicia los primeiros valores  de X predito e P predito
	 *
	 * @param X0 Valor inicial de X predito.
	 * @param P0 Valor inicial de P predito.
	 **/
	public void Init(double X0,double P0) {
		this.X=X0;
		this.P=P0;
	}

	/**
	 * Este método avalia o filtro
	 * 
	 * O filtro é avaliado de jeito que no filtro entra ZNow e é
	 * retirado um valor filtrado.
	 * 
	 * @param ZNow É a entrada do filtro.
	 * @return Retorna uma sinal filtrada <img src="{@docRoot}/doc/imagenes/hatXk.png" height="18" alt="\hat{X}_k">. 
	 **/
	public double EvaluateValue(double ZNow) {

		//variables temporales
		double Xminus;
		double Pminus;
		double K;

		double UNow;

		UNow=G.GetValue();

		//Prediction
		Xminus=this.X+ UNow;
		Pminus=this.A*this.P*this.A+this.Q;

		//Correction
		K    = Pminus*this.H / (this.H*Pminus*this.H+R);
		
		this.X = Xminus + K*(ZNow-this.H*Xminus);
		this.P = (1.0 - K*this.H)*Pminus;

		return this.X;
	}

	/**
	 * Este método retorna o valor atual da variável P.
	 * 
	 * Este método debe ser usado apos usar {@link #EvaluateValue(double ZNow)}.
	 * @return Retorna o valor atual da variável P
	 **/
	public double GetCurrentP() {
		return this.P;
	}
  
}
