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
 * {@latex.inline $Z_k$} e saída {@latex.inline $\\hat{X}_k$},
 * onde este ultimo é uma predição de {@latex.inline $X_k$}
 * no modelo de fonte ideal. {@cite Referencia1}
 *
 * <br><br>
 * <center><img src="{@docRoot}/doc/imagenes/PdsKalman1D/PdsKalman1D.png" width="700" alt="Z_k=H X_k + V_k  X_k=A X_{k-1} + U_k"></center>
 * <br><br>
 *  O filtro Kalman simula internamente uma fonte com a mesma estatística que
 *  {@latex.inline $U_k$}, esta nova fonte é chamada de {@latex.inline $\\hat{U}_k$},
 *  para criar esta fonte é necessario estimar no modelo de fonte ideal a variança de 
 *  {@latex.inline $U_k$},
 *  para o resto de cálculos o filtro só necessita os parâmetros A,H e R, sendo R 
 *  a variância de {@latex.inline $V_k$}
 *  <br> 
 *  <br>Para usar esta classe é necessário importar-la com:
 *  <pre>  
	import net.sourceforge.pdsplibj.pdsdf.PdsKalman1D; 
 *  </pre>
 *
 *  <br>Para usar este filtro pode-se usar este código de exemplo:
 *  <pre>  
    PdsKalman1D filtro=new PdsKalman1D(A,H,Q,R);
  
    hatUk=filtro.EvaluateValue(Zk);
 *  </pre>
 * 
 * @bibitem Referencia1, Fernando Pujaico Rivera.
 *                     MAnual xxx xx xxxxxx,
 *                     xxxx. xxx, 2015.

 * @author Fernando Pujaico Rivera <a href="mailto:fernando.pujaico.rivera@gmail.com">fernando.pujaico.rivera@gmail.com</a>
 * @version 0.01
 * @since 2015-05-25
 * @see <a href="http://pdsplib.sourceforge.net"> PDS Project Libraries in Java </a>
 * @see PdsKalman1DTool
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
	 * O filtro Kalman usa (para predizer seu comportamento) um modelo de fonte ideal como:<br><br>
	 * <center>{@latex.ilb %preamble{\\usepackage{amssymb}} %resolution{150} 
	 * $X_k =A X_{k-1} + U_k$
	 * }</center>
	 * <br>
	 * e um modelo de fonte real como:<br>
	 * <center>{@latex.ilb %preamble{\\usepackage{amssymb}} %resolution{150} 
	 * $Z_k=H X_k + V_k$
	 * }</center>
	 * <br>
	 * Onde   {@latex.inline $U_k$} é uma variavel aleatoria Gaussiana com uma
	 * função densidade de probabilidade {@latex.inline $N(0,Q)$}  e
	 * {@latex.inline $V_k$} é uma variavel aleatoria Gaussiana com uma
	 * função densidade de probabilidade {@latex.inline $N(0,R)$}
	 *
	 * <br><br>
	 * <center><img src="{@docRoot}/doc/imagenes/PdsKalman1D/algoritmo.png" width="700"></center>
	 *
	 * @param A É um fator do modelo de fonte ideal {@latex.inline $X_k$} onde {@latex.inline $X_k =A X_{k-1} + U_k$}.
	 * @param H É um fator do modelo de fonte real {@latex.inline $Z_k$} onde {@latex.inline $Z_k=H X_k + V_k$}.
	 * @param Q É a variância de {@latex.inline $U_k$}.
	 * @param R É a variância de {@latex.inline $V_k$}.
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
	 * Este método avalia o filtro Kalman.
	 * 
	 * Este é avaliado do jeito que na entrada recebe ZNow{@latex.inline $\\equiv Z_k$} e é
	 * retirado na sua saída um valor filtrado {@latex.inline $\\hat{X}_k$}.
	 * Sendo que {@latex.inline $\\hat{X}_k$} é um valor aproximado de
	 * {@latex.inline $X_k$}.
	 * <center><img src="{@docRoot}/doc/imagenes/PdsKalman1D/KalmanModel.png" width="300" alt="Kalman Model"></center>
	 * 
	 * @param ZNow É a entrada do filtro.
	 * @return Retorna uma sinal filtrada {@latex.inline $\\hat{X}_k$}. 
	 **/
	public double EvaluateValue(double ZNow) {

		//variaveis temporais
		double Xminus;
		double Pminus;
		double K;

		double UNow;

		UNow=G.GetValue();

		//Prediction
		Xminus=this.A*this.X+ UNow;
		Pminus=this.A*this.P*this.A+this.Q;

		//Correction
		K    = Pminus*this.H / (this.H*Pminus*this.H+R);
		
		this.X = Xminus + K*(ZNow-this.H*Xminus);
		this.P = (1.0 - K*this.H)*Pminus;

		return this.X;
	}

	/**
	 * Este método retorna o valor atual da variável interna {@latex.inline $P_k$},
	 * que é a variança de {@latex.inline $\\hat{X}$}.
	 * 
	 * <center>{@latex.ilb %preamble{\\usepackage{amssymb}} %resolution{150} 
	 * $P_k  = Var(\\hat{X}) = E[(\\hat{X}-\\mu_{\\hat{X}})^2]$
	 * }</center>
	 * <br>
	 * Se deseja-se usar este método, então deve-se aplicar após a função {@link #EvaluateValue(double ZNow)}.

	 * @return Retorna o valor atual da variável {@latex.inline $P_k$}
	 **/
	public double GetCurrentP() {
		return this.P;
	}

	/**
	 * Este método inicia los primeiros valores  de {@latex.inline $\\hat{X}_k$} 
	 * predito e {@latex.inline $P_k$} predito.
	 *
	 * Esta funcão é só para os mais curiosos que desejam ver/mexer adentro
	 * do filtro Kalman.
	 * @param X0 Valor inicial de {@latex.inline $\\hat{X}_k$} predito.
	 * @param P0 Valor inicial de {@latex.inline $P_k$} predito.
	 **/
	public void Init(double X0,double P0) {
		this.X=X0;
		this.P=P0;
	}
 
}
