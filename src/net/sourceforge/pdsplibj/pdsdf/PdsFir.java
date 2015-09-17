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

/** 
 * Esta classe implementa um filtro FIR de ordem M.
 * <br><br>
 *  <center><img src="{@docRoot}/doc/imagenes/PdsFir0.png" alt="Filtro FIR"></center><br>
 * <br> Apos criar o filtro FIR com {@link #PdsFir(int M)} é necesario inicializar os pessos H[n] com {@link #SetHValue(double hn, int n)}. <br><br>
 *  <center><img src="{@docRoot}/doc/imagenes/PdsFir.png" alt="Filtro FIR"></center><br>
 * <br>
 *  <center><img src="{@docRoot}/doc/imagenes/fireq.png" width="400" alt="Y[n]=X[n]*H[0] + X[n-1]*H[1] + ... + X[n-M]*H[M]"></center><br>
 *
 * <br><br> Para usar esta classe é necessário escrever:
 *  <pre>  import net.sourceforge.pdsplibj.pdsdf.PdsFirNlms; </pre>
 *
 * <br>Um exemplo de código de inicialização de filtro promédio - filtro passa baixo
 * <a href="http://octave.sourceforge.net">Octave</a>/Matlab: M=4; H=[1/(M+1) 1/(M+1) 1/(M+1) 1/(M+1) 1/(M+1)]; freqz(H); 
 *  <pre>  
 *  PdsFir Filtro = new PdsFir(M); 
 *
 *  Filtro.SetHValue(1.0/(M+1),0); 
 *  Filtro.SetHValue(1.0/(M+1),1); 
 *  ...
 *  Filtro.SetHValue(1.0/(M+1),M); 
 *  </pre>
 *
 * Se é desejado achar os valores de H[n], é possível usar ferramentas de calculo 
 * tipo <a href="http://octave.sourceforge.net">Octave</a>/Matlab. o seguinte codigo
 * ilustra o desenho.<br><br>
 * <pre>
 * Order=4;
 * NormalizedToOneCutOffFrequency=0.8;
 * H=fir1(Order,NormalizedToOneCutOffFrequency);	%% Design of Low Pass FIR Filter;
 * freqz(H)					%% Plot Frequency diagram of Filter.
 * </pre>
 *  <center><img src="{@docRoot}/doc/imagenes/data.png" width="700" alt="Filtro FIR de ordem 4 cutoff=0.8"></center><br>
 * 
 * @author Fernando Pujaico Rivera <a href="mailto:fernando.pujaico.rivera@gmail.com">fernando.pujaico.rivera@gmail.com</a>
 * @version 0.05
 * @since 2015-05-25
 * @see <a href="http://pdsplib.sourceforge.net"> PDS Project Libraries in Java </a>
*/
public class PdsFir {
	private int M;
	private double[] H;
	private PdsFifoValue X;

	/**
	 * Este é o construtor da classe PdsFir
	 *
	 * O filtro inicialmente tem  H[0]=1 e o resto de H[m] com zero, de jeito que o filtro não
	 * filtra, só deixa passar tudo sem alterar a informação.
	 *
	 * @param M É o ordem do filtro: Y[n]=X[n]*H[0] + X[n-1]*H[1] + ... + X[n-M]*H[M]
	 **/
	public PdsFir(int M) {
		this.M = M;
		this.H=new double[M+1];
		this.X=new PdsFifoValue(M+1);
		this.H[0]=1.0;
	}
	
	/**
	 * Este método asigna os valores H[n]=hn
	 * 
	 * @param hn É o valor que sera asignado em H[n].
	 * @param n  É o índice em H[n] onde será asignado hn.
	 **/
	public void SetHValue(double hn, int n) {
		if((n>=0)&&(n<=this.M)){
			this.H[n]=hn;
		}
	}
	
	/**
	 * Este método avalia o filtro
	 * 
	 * O filtro é avaliado de jeito que no filtro entra xvalue e é
	 * retirado um valor filtrado.
	 * 
	 * @param xvalue É a entrada do filtro.
	 * @return Retorna uma sinal filtrada.
	 **/
	public double EvaluateValue(double xvalue) {
		double TMP;
		this.X.WriteValue(xvalue);
		
		TMP=0;
		for(int i=0;i<=this.M;i++){
			TMP=TMP+this.X.GetValue(i)*this.H[i];
		}
		return TMP;
	}
	
	/**
	 * Este método retorna la dimensão do filtro.
	 *
	 * @return Retorna la dimensão do filtro.
	 * */
	public int GetDimension(){
		return this.M;
	}
	
	/**
	 * Este método retorna o valor de  H[n].
	 * 
	 * @param n É o índice em o elemento H[n].
	 * @return Retorna o valor de H[n].
	 * */	
	public double GetHValue(int n){
		return this.H[n];
	}

	/**
	 * Este método retorna o valor de  X[n].
	 * 
	 * @param n É o índice em o elemento X[n].
	 * @return Retorna o valor de X[n].
	 * */		
	public double GetXValue(int n){
		return this.X.GetValue(n);
	}

	/**
	 * Este método provoca que se usamos uma instancia de PdsFir
	 * num contexto que necessita-se ser String, então esta instancia
	 * retorna uma descrição do filtro.
	 *
	 * @return Retorna uma descrição do filtro em formato String.
	 * */
	public String toString() {
		String TMP;
		int i;
		
		TMP="M="+ this.M+"\n";
		
		for(i=0;i<=this.M;i++){
			TMP=TMP+"H(" + i + ")=" + this.H[i] + " ";
		}
		TMP=TMP+"\n";
		
		for(i=0;i<=this.M;i++){
			TMP=TMP+"X(" + i + ")=" + this.X.GetValue(i) + " ";
		}
		TMP=TMP+"\n";
		
		return TMP;
	}
}
