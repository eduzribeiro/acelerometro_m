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

import java.lang.*;

/** 
 * Esta classe implementa um vetor real com N elementos.
 *
 * <br><br> Para usar esta classe é necessário escrever:
 *  <pre>  import net.sourceforge.pdsplibj.pdsra.PdsVector; </pre>
 * <br><br>
 * Os espaços de memoria são salvados em espaços
 * de memoria contínuos, não tem-se feito alocação dinâmica de memoria
 * para os elementos, isto foi para ganhar em velocidade de execução. 
 * <br>
 *
 * @author Fernando Pujaico Rivera <a href="mailto:fernando.pujaico.rivera@gmail.com">fernando.pujaico.rivera@gmail.com</a>
 * @version 0.01
 * @since 2015-05-25
 * @see <a href="http://pdsplib.sourceforge.net"> PDS Project Libraries in Java </a>
 */
public class PdsVector {
	private int Nel;
	private double[] V;

	/**
	 * Este construtor da classe é necessário inicializar-lo indicando o numero N de elementos 
	 * do vetor.
	 * 
	 * @param N É o numero de elementos do vetor.
	 **/
	public PdsVector(int N) {
		this.Nel = N;
		this.V=new double[this.Nel];
		
		if(this.V==null)	this.Nel = 0;
	}

	/**
	 * Este construtor da classe é necessário inicializar-lo indicando um vetor a ser clonado.
	 * 
	 * @param VecSrc É o vetor de fonte.
	 **/
	public PdsVector(PdsVector VecSrc) {
		if(VecSrc!=null){
			this.Nel = VecSrc.Nel;
			this.V=new double[this.Nel];
			
			if(this.V==null)	this.Nel = 0;

			for(int i=0; i<this.Nel;i++)
				this.V[i]=VecSrc.V[i];
		}
		else{
			this.Nel = 0;
			this.V=null;	
		}
	}

	/**
	 * Este método inicia todo o vetor com o valor Val
	 * <br>
	 * 
	 * @param Val É o valor que tomará todos os elementos do vetor.
	 **/
	public void InitValue(double Val) {

		for(int i=0;i<this.Nel;i++)	this.V[i]=Val;
	}

	/**
	 * Este método inicia todo o vetor a partir de outro vetor.
	 *
	 * <br>
	 * se o vetor VecSrc é menor ou maior, então se copiará ate onde se possa.
	 * @param VecSrc É o vetor de fonte de onde se copiarão os dados.
	 **/
	public void InitVector(PdsVector VecSrc) {
		if(VecSrc!=null){
			for(int i=0;(i<this.Nel)&&(i<VecSrc.Nel);i++)	
				this.V[i]=VecSrc.V[i];
		}
	}

	/**
	 * Este método inicia um elemento do vetor com o valor Val
	 * <br>
	 * 
	 * @param id É o indicie do elemento a iniciar, id inicia em cero.
	 *          se o índice esta fora de rango não da erro simplesmente não faz nada.  
	 * @param Val É o valor que tomará o elemento do vetor.
	 **/
	public void SetValue(int id,double Val) {
		if( (id>=0) && (id<this.Nel) )
		this.V[id]=Val;
	}

	/**
	 * Este método retorna o valor de um elemento de índice id do vetor.
	 * <br>
	 * 
	 * @param id É o indicie do elemento a ler, id inicia em cero.
	 *          se o índice esta fora de rango não da erro simplesmente não faz nada.  
	 * @return Retorna o valor do vetor na posição id.
	 **/
	public double GetValue(int id) {
		if( (id>=0) && (id<this.Nel) )		return this.V[id];
		else 								return 0;
	}
	
	/**
	 * Este método retorna o numero de elementos do vetor.
	 * <br>
	 * 
	 * @return Retorna o numero de elementos do vetor.
	 **/
	public double GetNelements() {
		return this.Nel;
	}
	
	/**
	 * Este método soma a este vetor com outro vetor VecSrc e o resultado 
	 * é copiado em si mesmo. 
	 * 
	 * <br>
	 * Se os tamanhos são diferentes intersecta os tamanhos faz a copia 
	 * somente na interseção. 
	 * Vector.Add(VecSrc) é equivalente a Vector=Vector+VecSrc. 
	 * 
	 * @param VecSrc É o vetor a usar em   Vector=Vector+VecSrc.  
	 **/
	public void Add(PdsVector VecSrc) {

		if(VecSrc!=null){
			for(int i=0;(i<this.Nel)&&(i<VecSrc.Nel);i++)	
				this.V[i]=this.V[i]+VecSrc.V[i];
		}

	}

	/**
	 * Este método soma a este vetor com um valor real e o resultado 
	 * é copiado em si mesmo. 
	 * 
	 * <br>
	 * Vector.Add(Val) é equivalente a Vector=Vector+Val. 
	 * 
	 * @param Val É o valor a usar em   Vector=Vector+Val.  
	 **/
	public void Add(double Val) {
		for(int i=0;i<this.Nel;i++)	
			this.V[i]=this.V[i]+Val;
	}
	
	/**
	 * Este método soma a este vetor com outro vetor Val*VecSrc e o resultado 
	 * é copiado em si mesmo. 
	 * 
	 * <br>
	 * Se os tamanhos são diferentes intersecta os tamanhos faz a copia 
	 * somente na interseção. 
	 * Vector.Add(Val,VecSrc) é equivalente a Vector=Vector+Val*VecSrc. 
	 * 
	 * @param Val É o valor real a usar em Vector=Vector+Val*VecSrc.  
	 * @param VecSrc É o vetor a usar em  Vector=Vector+Val*VecSrc.  
	 **/
	public void Add(double Val, PdsVector VecSrc) {

		if(VecSrc!=null){
			for(int i=0;(i<this.Nel)&&(i<VecSrc.Nel);i++)	
				this.V[i]=this.V[i]+Val*VecSrc.V[i];
		}

	}
			
	/**
	 * Este método diminui a este vetor com outro vetor VecSrc e o resultado 
	 * é copiado em si mesmo. 
	 * 
	 * <br>
	 * Se os tamanhos são diferentes intersecta os tamanhos faz a copia 
	 * somente na interseção. 
	 * Vector.Sub(VecSrc) é equivalente a Vector=Vector-VecSrc. 
	 * 
	 * @param VecSrc É o vetor a usar em   Vector=Vector-VecSrc.  
	 **/
	public void Sub(PdsVector VecSrc) {

		if(VecSrc!=null){
			for(int i=0;(i<this.Nel)&&(i<VecSrc.Nel);i++)	
				this.V[i]=this.V[i]-VecSrc.V[i];
		}

	}	
	
	/**
	 * Este método multiplica a este vetor com outro vetor VecSrc elemento 
	 * a elemento e o resultado é copiado em si mesmo. 
	 * 
	 * <br>
	 * Se os tamanhos são diferentes intersecta os tamanhos faz a copia 
	 * somente na interseção. 
	 * Vector.Mul(VecSrc) é equivalente a Vector=Vector*VecSrc. 
	 * 
	 * @param VecSrc É o vetor a usar em   Vector=Vector*VecSrc.  
	 **/
	public void Mul(PdsVector VecSrc) {

		if(VecSrc!=null){
			for(int i=0;(i<this.Nel)&&(i<VecSrc.Nel);i++)	
				this.V[i]=this.V[i]*VecSrc.V[i];
		}

	}
	
	/**
	 * Este método multiplica a este vetor com um valor real e o resultado 
	 * é copiado em si mesmo. 
	 * 
	 * <br>
	 * Vector.Mul(Val) é equivalente a Vector=Vector*Val. 
	 * 
	 * @param Val É o valor a usar em   Vector=Vector*Val.  
	 **/
	public void Mul(double Val) {
		for(int i=0;i<this.Nel;i++)	
			this.V[i]=this.V[i]*Val;
	}
	
	
	/**
	 * Este método divide a este vetor com um valor real e o resultado 
	 * é copiado em si mesmo. 
	 * 
	 * <br>
	 * Se Val é cero o parâmetro vai ser passado e vai dar problemas.
	 * 
	 * Vector.Div(Val) é equivalente a Vector=Vector/Val. 
	 * 
	 * @param Val É o valor a usar em   Vector=Vector/Val.  
	 **/
	public void Div(double Val) {
		for(int i=0;i<this.Nel;i++)	
			this.V[i]=this.V[i]/Val;
	}

	/**
	 * Este método faz um produto ponto a este vetor com outro vetor VecSrc
	 * o resultado é um valor real.
	 * 
	 * <br>
	 * Se os tamanhos são diferentes intersecta os tamanhos faz produto 
	 * somente na interseção. 
	 * Vector.Dot(VecSrc) é equivalente a Val=Vector*VecSrc^T.  Onde ^T
	 * indica transposta do vetor.
	 * 
	 * @param VecSrc É o vetor a usar em   Val=Vector*VecSrc^T. 
	 * @return Retorna o produto punto dos vetores  Vector e VecSrc.
	 **/
	public double Dot(PdsVector VecSrc) {
		double S=0;
		if(VecSrc!=null){
			for(int i=0;(i<this.Nel)&&(i<VecSrc.Nel);i++)	
				S=S+this.V[i]*VecSrc.V[i];
		}
		return S;
	}

	/**
	 * Este método retorna a norma do vetor
	 * 
	 * <br>
	 * Vector.Norm() é equivalente a Norm=SQRT(Vector*Vector^T).  Onde ^T
	 * indica transposta do vetor.
	 * 
	 * @return Retorna a norma do vetor
	 **/
	public double Norm() {
		double S=0;
		for(int i=0;i<this.Nel;i++)	S=S+this.V[i]*this.V[i];
		return Math.sqrt(S);
	}

	/**
	 * Este método retorna a norma ao quadrado do vetor
	 * 
	 * <br>
	 * Vector.Norm2() é equivalente a Norm2=Vector*Vector^T.  Onde ^T
	 * indica transposta do vetor.
	 * 
	 * @return Retorna a norma ao quadrado do vetor.
	 **/
	public double Norm2() {
		double S=0;
		for(int i=0;i<this.Nel;i++)	S=S+this.V[i]*this.V[i];
		return S;
	}

	/**
	 * Este método retorna o valor rms (raiz quadratico meio) do vetor.
	 * 
	 * <br>
	 * Vector.Rms() é equivalente a RMS=SQRT(Vector*Vector^T/Nel).  Onde ^T
	 * indica transposta do vetor.
	 * 
	 * @return Retorna o valor rms do vetor
	 **/
	public double Rms() {
		double S=0;
		for(int i=0;i<this.Nel;i++)	S=S+this.V[i]*this.V[i];
		return Math.sqrt(S/this.Nel);
	}
	
	/**
	 * Este método retorna o valor meio do vetor
	 * 
	 * <br>
	 * 
	 * @return Retorna o valor meio do vetor
	 **/
	public double Mean() {
		double S=0;
		for(int i=0;i<this.Nel;i++)	S=S+this.V[i];
		return S/this.Nel;
	}
	
	/**
	 * Este método retorna o valor da variança amostral $\sigma^2$ do vetor.
	 * 
	 * <br>
	 * Vector.Var() é equivalente a Var=Sigma^2=(Vector-M)*(Vector-M)^T.  Onde ^T
	 * indica transposta do vetor e M=Vector.Mean().
	 * 
	 * @return Retorna o valor da variança amostral.
	 **/
	public double Var() {
		double S=0;
		double M=0;
		
		M=this.Mean();
		
		for(int i=0;i<this.Nel;i++)	S=S+(this.V[i]-M)*(this.V[i]-M);
		return S/this.Nel;
	}
	
	/**
	 * Este método retorna o valor do desvio padrão, Sigma=sqrt(variança),   do vetor.
	 * 
	 * <br>
	 * Vector.Sigma() é equivalente a Sigma=sqrt((Vector-M)*(Vector-M)^T).  Onde ^T
	 * indica transposta do vetor, M=Vector.Mean() e sqrt() é a raiz quadrada.
	 * 
	 * @return Retorna o valor do desvio padrão.
	 **/
	public double Sigma() {
		double S=0;
		double M=0;
		
		M=this.Mean();
		
		for(int i=0;i<this.Nel;i++)	S=S+(this.V[i]-M)*(this.V[i]-M);
		return Math.sqrt(S/this.Nel);
	}

	/**
	 * Este método provoca que se usamos uma instância de PdsVector
	 * num contexto que necessita-se ser String, então esta instância
	 * retorna uma descrição da memoria.
	 *
	 * @return Retorna uma descrição da memoria.
	 * */
	public String toString() {
		String TMP;
		TMP="";
		for(int i=0;i<this.Nel;i++){
			TMP=TMP+ this.V[i] + "\t";
		}
		TMP=TMP+"\n";
		return TMP;
	}
}
