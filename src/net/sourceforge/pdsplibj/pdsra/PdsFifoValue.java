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

package net.sourceforge.pdsplibj.pdsra;

/** 
 * Esta classe implementa uma memoria FIFO (First Input First Output), de 
 * entrada real, com N elementos como máximo na linha de espera.
 * <br><br> Para usar esta classe é necessário escrever:
 *  <pre>  import net.sourceforge.pdsplibj.pdsra.PdsFifoValue; </pre>
 * <br><br>
 *  <center><img src="{@docRoot}/doc/imagenes/PdsFifoValueFig.png" alt="Memoria FIFO"></center><br>
 * <br><br>
 *  <center><img src="{@docRoot}/doc/imagenes/PdsFifoValueFig2.png" alt="Memoria FIFO"></center><br>
 * Esta classe só tem que ser usada para memorias FIFO pequenas,
 * dado que os espaços de memoria já estão reservados e são salvados em espaços
 * de memoria contínuos, não tem-se feito alocação dinâmica de memoria,
 * isto foi para ganhar em velocidade de execução. 
 * <br>
 * Para memorias FIFO maiores
 * se implementará a classe PdsBigFifoValue um pouco mais lenta dado que usará
 * alocação dinâmica de memoria. Os métodos serão os mesmos.
 *
 * @author Fernando Pujaico Rivera <a href="mailto:fernando.pujaico.rivera@gmail.com">fernando.pujaico.rivera@gmail.com</a>
 * @version 0.01
 * @since 2015-05-25
 * @see <a href="http://pdsplib.sourceforge.net"> PDS Project Libraries in Java </a>
 */
public class PdsFifoValue {
	private int N;
	private int Elements;
	private int IDofLastValueInput;
	private double[] x;

	/**
	 * Este método é o construtor da classe.
	 * <br>
	 * É necessário inicializar-lo indicando o numero N de elementos 
	 * da memoria FIFO.
	 * 
	 * @param N É o numero de elementos da memoria FIFO.
	 **/
	public PdsFifoValue(int N) {
		this.N = N;
		this.IDofLastValueInput = -1;
		this.Elements = 0;
		this.x=new double[N];
	}
	
	/**
	 * Este método envia um dado na entrada da memoria FIFO. 
	 * <br>
	 * Obviamente isto provoca que se a memoria FIFO este cheia, é
	 * jogado fora o valor mas antigo na memoria.
	 * 
	 * @param value E' o novo dato que será introduzido na memoria FIFO
	 **/
	public void WriteValue(double value) {
		double	TMP=0;

		// Vou contando a quantidade de elementos na memoria FIFO
		this.Elements=this.Elements+1;
		if(this.Elements>this.N)	this.Elements=this.N;
	
		//Escrevo um novo dado na memoria fifo
		if(this.IDofLastValueInput>=this.N-1)
		{	
			this.IDofLastValueInput=0;
		}
		else
		{									
			this.IDofLastValueInput=this.IDofLastValueInput+1;
		}
		
		this.x[this.IDofLastValueInput]=value;
	}
	
	/**
	 * Este método retorna um valor numa posição na memoria FIFO.
	 * 
	 * @param id É posição da memoria FIFO, onde 0 é o valor mais novo
	 *           na memoria e N-1 seria o valor mais antigo.
	 * @return O conteúdo da posição id na memoria FIFO. 
	 **/
	public double GetValue(int id) {
		int	ID;
		
		if(id>(this.N-1))	id=this.N-1;

		ID=this.IDofLastValueInput-id;

		if(ID<0)	ID=ID+this.N;
		
		return this.x[ID];
		
	}

	/**
	 * Este método retorna o valor mais antigo da memoria FIFO.
	 * 
	 * Se a FIFO não tem elementos, não da erro, simplesmente retorna 0.
	 * @return O conteúdo do valor mais antigo na memoria FIFO.
	 **/
	public double ReadValue() {

		double value;
		int ID;
		
		if( this.Elements==0 ){
			return 0.0;
		}
		else{

			ID=this.IDofLastValueInput-(this.Elements-1);
			if(ID<0)	ID=ID+this.N;

			value=this.x[ID];

			this.x[ID]=0;
			this.Elements=this.Elements-1;
		
			return value;
		}
		
	}

	/**
	 * Este método retorna a dimensão (numero de elementos) da memoria.
	 *
	 * @return Retorna a dimensão (numero de elementos) da memoria.
	 * */	
	public int GetDimension(){
		return this.N;
	}
	
	/**
	 * Este me'todo provoca que se usamos uma instância de PdsFifoValue
	 * num contexto que necessita-se ser String, então esta instância
	 * retorna uma descrição da memoria.
	 *
	 * @return Retorna uma descrição da memoria.
	 * */
	public String toString() {
		String TMP;
		TMP="N="+ this.N;
		for(int i=0;i<=this.N;i++){
			TMP=TMP+" fifo(" + i + ")=" + this.x[i] + " ";
		}
		TMP=TMP+"\n";
		return TMP;
	}
}
