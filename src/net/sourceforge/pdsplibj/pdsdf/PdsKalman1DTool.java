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
import  net.sourceforge.pdsplibj.pdsra.PdsVector;

/**
 * Esta classe implementa um método para obter os parâmetros {H,R,A,Q}, 
 * do filtro Kalman de entrada unidimensional. No caso que deseja-se filtrar
 * uma sinal {@latex.inline $Z$}, com {@latex.inline $Z_k=H X_k + V_k$},
 * então deve-se ter os dados desta sinal em repouso {@latex.inline $Z^r$}
 * e em atividade {@latex.inline $Z^a$}.
 *
 * O procedimento de cálculo está explicado em {@cite Referencia1}.
 * 
 *  <br>Para usar esta classe é necessário importar-la com:
 *  <pre>  
  import net.sourceforge.pdsplibj.pdsdf.PdsKalman1DTool; 
 *  </pre>
 *
 *  <br>Para usar esta ferramenta de cálculo de parâmetros, pode-se usar este código de exemplo:
 *  <pre>  
  PdsKalman1DTool T=new PdsKalman1DTool(H,N);
  PdsVector D;

  T.AddValueR(Zr);
  T.AddValueR(Zr);
  ...
  T.AddValueR(Zr);

  T.AddValueA(Za);
  T.AddValueA(Za);
  ...
  T.AddValueA(Za);
  
  D=T.GetParameters();

  H=D.GetValue(0);	R=D.GetValue(1);	A=D.GetValue(2);	Q=D.GetValue(3);
 *  </pre>
 *
 * @bibitem Referencia1, Fernando Pujaico Rivera.
 *                     MAnual xxx xx xxxxxx,
 *                     xxxx. xxx, 2015.
 *
 * @author Fernando Pujaico Rivera <a href="mailto:fernando.pujaico.rivera@gmail.com">fernando.pujaico.rivera@gmail.com</a>
 * @version 0.01
 * @since 2015-08-25
 * @see <a href="http://pdsplib.sourceforge.net"> PDS Project Libraries in Java </a>
 * @see PdsKalman1D
 */
public class PdsKalman1DTool {
	//Variaveis do sistema
	private double A;
	private double H;
	private double Q;
	private double R;

	private int L;

	private PdsVector D;

	private PdsFifoValue fa;
	private PdsFifoValue fr;

	/**
	 * Este é o construtor do objeto que calculará os parâmetros {H,R,A,Q}
	 * do filtro Kalman de entrada unidimensional.
	 *
	 * {@latex.inline $H$} é o único parâmetro que pode ser escolhido,
	 * os demais parâmetros serão calculados em função dele.
	 * @param H É um fator do modelo de fonte real {@latex.inline $Z_k$} onde {@latex.inline $Z_k=H X_k + V_k$}.
	 * @param N É o número de elementos que se salvarão como máximo para obter
	 * uma aproximação dos parâmetros do filtro.
	 **/
	public PdsKalman1DTool(double H,int N) {

		this.H=H;

		this.L=5;

		this.D = new PdsVector(4);
		D.SetValue(0,this.H);

		this.fa= new PdsFifoValue(N);

		this.fr= new PdsFifoValue(N);
	}

	/**
	 * Este método agrega um valor ao pacote de dados do sensor em atividade.
	 *
	 * É necessário ter vários dados da sinal em atividade para calcular os 
	 * parâmetros {H,R,A,Q} do filtro Kalman.
	 * @param value Agrega um valor em de sinal em atividade ao pacote de analises.
	 **/
	public void AddValueA(double value) {

		this.fa.WriteValue(value);

	}

	/**
	 * Este método agrega um valor ao pacote de dados do sensor em repouso.
	 *
	 * É necessário ter vários dados da sinal em repouso para calcular os 
	 * parâmetros {H,R,A,Q} do filtro Kalman.
	 * @param value Agrega um valor em de sinal em repouso ao pacote de analises.
	 **/
	public void AddValueR(double value) {

		this.fr.WriteValue(value);

	}

	/**
	 * Retorna um vetor, do tipo PdsVector, com os parâmetros {H,R,A,Q}.
	 * 
	 * Este método calcula os parâmetros com a quantidade atual de dados do 
	 * sensor em repouso e em atividade.
	 * @return Retorna um vetor, do tipo PdsVector, com os parâmetros {H,R,A,Q}.
	 **/
	public PdsVector GetParameters( ) {

		double var_a;
		double cor,q;
		int i,j;
		PdsVector hF= new PdsVector(this.L);
		PdsVector  F= new PdsVector(this.L);
		PdsVector  J= new PdsVector(this.L);

		////////////////////////////////////////////////////////////////////////
		// R
		R=this.fr.GetVar();

		////////////////////////////////////////////////////////////////////////
		// A
		var_a=this.fa.GetVar();
		for(i=0;i<this.L;i++)
		{
			cor=this.fa.GetCor(i+1);
			hF.SetValue(i,cor/(var_a-R));
		}
		A=hF.GetValue(0);	// valor inicial de A
		//System.out.print (A+"\n");
		for(i=0;i<10;i++)
		{

			for(j=0;j<this.L;j++)	F.SetValue(j,Math.pow(A,j+1));	// Cargo F
			for(j=0;j<this.L;j++)	J.SetValue(j,Math.pow(A,j  ));	// Cargo J

			A=A+hF.NewSub(F).Dot(J)/J.Norm2();
			//System.out.print (A+"\n");
		}
		////////////////////////////////////////////////////////////////////////
		// Q
		q=(1-this.A*this.A)*(var_a-R);
		this.Q=q/(H*H);

		D.SetValue(1,R);
		D.SetValue(2,A);
		D.SetValue(3,Q);

		return this.D;
		
	}
 
}
