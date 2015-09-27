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
 * La memoria FIFO reserva  a memoria para os N elementos.
 *
 * <br><br> Para usar esta classe é necessário importar-la com:
 *  <pre>  import net.sourceforge.pdsplibj.pdsra.PdsFifoValue; </pre>
 * e logo usar um código similar a:
 * <br>
 *  <pre>  
    PdsFifoValue F= new PdsFifoValue(N);
    F.WriteValue(value);
    value = F.ReadValue();
 *  </pre>
 * <br>
 *
 *  <center><img src="{@docRoot}/doc/imagenes/PdsFifoValueFig.png" alt="Memoria FIFO"></center><br>
 * <br><br>
 *  <center><img src="{@docRoot}/doc/imagenes/PdsFifoValueFig2.png" alt="Memoria FIFO"></center><br>
 * Esta classe só deveria ser usada para memorias FIFO pequenas,
 * porque esta reserva desde o inicio a quantidade máxima de memoria a usar (em espaços
 * de memoria contínuos). Não tem-se feito alocação dinâmica de memoria;
 * isto foi para ganhar em velocidade de execução (tive preguiça de pensar). 
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
		if(this.Elements > this.N)	this.Elements=this.N;
	
		//Escrevo um novo dado na memoria fifo
		if(this.IDofLastValueInput>=(this.N-1))
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
	 * {@latex.inline $n$} é o número de elementos da FIFO. 
	 * {@latex.inline $N$} é o tamanho da FIFO,
	 * {@latex.inline $n \\leq N$}. Qualquer id maior que 
	 * {@latex.inline $n-1$} retorna 0.
	 * @param id É posição da memoria FIFO, onde 0 é o valor mais novo
	 *           na memoria e n-1 seria o valor mais antigo.
	 * @return O conteúdo da posição id na memoria FIFO. 
	 **/
	public double GetValue(int id) {
		int	ID;
		
		if(id>=this.Elements)	return 0;

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
	 * Este método retorna o tamanho total da memoria.
	 *
	 * @return Retorna o tamanho total da memoria.
	 * */	
	public int GetDimension(){
		return this.N;
	}

	/**
	 * Este método retorna o número de elementos da memoria.
	 *
	 * @return Retorna o número de elementos da memoria.
	 * */	
	public int GetElements(){
		return this.Elements;
	}

	/**
	 * Este método retorna o valor medio {@latex.inline $m$} de todos os valores 
	 * da memoria FIFO com {@latex.inline $n$} amostras {@latex.inline $X_i$}.
	 *
	 * <center>{@latex.ilb %preamble{\\usepackage{amssymb}} %resolution{150} 
	 * $m  = \\frac{1}{n}\\sum^n_i{X_i}$
	 * }</center>
	 * Como maximo a memoria pode ter {@latex.inline $N$} elementos, com {@latex.inline $n \\leq N$}.
	 * @return Retorna o valor médio de todas as mostras da FIFO.
	 * */	
	public double GetMean(){
		double S;
		int id,ID;
		
		if( this.Elements==0 ){
			return 0.0;
		}
		else{

			S=0;
			for(id=0;id<this.Elements;id++)
			{
				ID=this.IDofLastValueInput-id;
				if(ID<0)	ID=ID+this.N;

				S=S+this.x[ID];

			}
			return S/(this.Elements*1.0);
		}
	}
	

	/**
	 * Este método retorna a variância populacional {@latex.inline $\\sigma^2$} de todos os valores 
	 * da memoria FIFO com {@latex.inline $n$} amostras {@latex.inline $X_i$}.
	 *
	 * <center>{@latex.ilb %preamble{\\usepackage{amssymb}} %resolution{150} 
	 * $\\sigma^2  = \\frac{1}{n}\\sum^n_i{(X_i-m)^2}$
	 * }</center>
	 * Como máximo a memoria pode ter {@latex.inline $N$} elementos, com {@latex.inline $n \\leq N$} .
	 * @return Retorna a variância populacional de todas as mostras da FIFO.
	 * */	
	public double GetVar(){
		double S,m;
		int id,ID;
		
		if( this.Elements==0 ){
			return 0.0;
		}
		else{
	
			m=this.GetMean();

			S=0;
			for(id=0;id<this.Elements;id++)
			{
				ID=this.IDofLastValueInput-id;
				if(ID<0)	ID=ID+this.N;

				S=S+(this.x[ID]-m)*(this.x[ID]-m);

			}
			return S/(this.Elements*1.0);
		}
	}

	/**
	 * Este método retorna a autocorrelação {@latex.inline $Cor(X_i,X_{i+d})$} de todos os valores 
	 * da memoria FIFO com {@latex.inline $n$} amostras {@latex.inline $X_i$}.
	 *
	 * <center>{@latex.ilb %preamble{\\usepackage{amssymb}} %resolution{150} 
	 * $Cor(X_i,X_{i+d}) = \\frac{1}{n-d} \\sum \\limits^{n-d}_i {(X_i-m_{X})(X_{i+d}-m_{X})}$
	 * }</center>
	 * sendo:
	 * <center>{@latex.ilb %preamble{\\usepackage{amssymb}} %resolution{150} 
	 * $ m_{X} = \\frac{1}{n}\\sum^{n}_i {X_i}$
	 * }</center>
	 * Como máximo a memoria pode ter {@latex.inline $N$} elementos, com {@latex.inline $n \\leq N$}.
	 * @return Retorna a autocorrelação dos elementos da FIFO.
	 *         Se o número de elementos é zero {@latex.inline $n=0$}, então retorna zero.
	 *         Se o número de elementos {@latex.inline $n \\leq |d|$}, então retorna zero.
	 * */	
	public double GetCor(int d){
		double S,m;
		int id,ID1,ID2;
		
		if (d<0)	d=-d;

		if( (this.Elements==0)||(this.Elements<=d) ){
			return 0.0;
		}
		else{
	
			m=this.GetMean();

			S=0;
			for(id=0;id<(this.Elements-d);id++)
			{
				ID1=this.IDofLastValueInput-id;
				if(ID1<0)	ID1=ID1+this.N;

				ID2=this.IDofLastValueInput-id-d;
				if(ID2<0)	ID2=ID2+this.N;

				S=S+(this.x[ID1]-m)*(this.x[ID2]-m);

			}
			return S/((this.Elements-d)*1.0);
		}
	}

	/**
	 * Este método provoca que se usamos uma instância de PdsFifoValue
	 * num contexto que necessita-se ser String, então esta instância
	 * retorna uma descrição da memoria desde o mais recente ate o mais antigo.
	 *
	 * @return Retorna uma descrição da memoria.
	 * */
	public String toString() {
	
		String TMP;
		int i,ID;

		TMP="N="+ this.N + "\n";
		TMP=TMP+"n="+ this.Elements + "\n";
		for(i=0;i<this.Elements;i++)
		{
			ID=this.IDofLastValueInput-i;
			if(ID<0)	ID=ID+this.N;

			TMP=TMP+"fifo(" + i + ")=" + this.x[ID] + "\n";
		}
		//TMP=TMP+"\n";
		return TMP;
	}
}
