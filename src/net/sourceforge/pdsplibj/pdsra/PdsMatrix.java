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
 * Esta classe implementa uma matriz real com N elementos.
 *
 * <br><br> Para usar esta classe é necessário escrever:
 *  <pre>  import net.sourceforge.pdsplibj.pdsra.PdsMatrix; </pre>
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
public class PdsMatrix {
	private int Nlin;
	private int Ncol;
	private double[][] M;

	/**
	 * Este construtor da classe é necessário inicializar-lo indicando o 
	 * numero Nlin de linha e Ncol de colunas da matriz.
	 * 
	 * @param Nlin É o numero de linhas da matriz.
	 * @param Ncol É o numero de colunas da matriz.
	 **/
	public PdsMatrix(int Nlin,int Ncol) {
		this.Nlin = Nlin;
		this.Ncol = Ncol;
		this.M=new double[this.Nlin][this.Ncol];
		
		if(M==null){
			this.Nlin = 0;
			this.Ncol = 0;
		}
	}

	/**
	 * Este construtor da classe é necessário inicializar-lo indicando 
	 * uma matriz a ser clonada.
	 * 
	 * @param MatSrc É a matriz de fonte.
	 **/
	public PdsMatrix(PdsMatrix MatSrc) {
		if(MatSrc!=null){
			this.Nlin = MatSrc.Nlin;
			this.Ncol = MatSrc.Ncol;
			
			this.M=new double[this.Nlin][this.Ncol];
			
			if(this.M==null){
				this.Nlin = 0;
				this.Ncol = 0;
			}

			for(int i=0; i<this.Nlin;i++){
				for(int j=0; j<this.Ncol;j++){
					this.M[i][j]=MatSrc.M[i][j];
				}
			}
		}
		else{
			this.Nlin = 0;
			this.Ncol = 0;
			this.M=null;
		}
	}

	/**
	 * Este método inicia toda a matriz com o valor Val
	 * <br>
	 * 
	 * @param Val É o valor que tomará todos os elementos da matriz.
	 **/
	public void InitValue(double Val) {

		for(int i=0;i<this.Nlin;i++)	
			for(int j=0;j<this.Ncol;j++)
				this.M[i][j]=Val;
	}
	
	/**
	 * Este método inicia toda a matriz com a matriz MatSrc.
	 * <br>
	 * 
	 * se a matriz MatSrc é menor ou maior, então se copiará ate onde se possa.
	 * @param MatSrc É a matriz de fonte de onde se copiarão os dados.
	 **/
	public void InitMatrix(PdsMatrix MatSrc) {
		if(MatSrc==null){		
			for(int i=0;(i<this.Nlin)&&(i<MatSrc.Nlin);i++)	
				for(int j=0;(j<this.Ncol)&&(j<MatSrc.Ncol);j++)
					this.M[i][j]=MatSrc.M[i][j];
		}
	}

	/**
	 * Este método inicia um elemento da matriz com o valor Val
	 * <br>
	 * 
	 * @param idlin É o indicie da linha do elemento a iniciar, id inicia em cero.
	 *          se o índice esta fora de rango não da erro simplesmente não faz nada.  
	 * @param idcol É o indicie da coluna do elemento a iniciar, id inicia em cero.
	 *          se o índice esta fora de rango não da erro simplesmente não faz nada.  
	 * @param Val É o valor que tomará o elemento da matriz.
	 **/
	public void SetValue(int idlin,int idcol,double Val) {
		if( (idlin>=0) && (idlin<this.Nlin) )
		if( (idcol>=0) && (idcol<this.Ncol) )
		this.M[idlin][idcol]=Val;
	}
	
	/**
	 * Este método lê um elemento da matriz.
	 * <br>
	 * 
	 * @param idlin É o indicie da linha do elemento a ler, id inicia em cero.
	 *          se o índice esta fora de rango não da erro simplesmente não faz nada.  
	 * @param idcol É o indicie da coluna do elemento a ler, id inicia em cero.
	 *          se o índice esta fora de rango não da erro simplesmente não faz nada.  
	 * @return Retorna o valor que toma o elemento (idlin,idcol) da matriz.
	 **/
	public double GetValue(int idlin,int idcol) {
		if( (idlin>=0) && (idlin<this.Nlin) && (idcol>=0) && (idcol<this.Ncol) ){
			return this.M[idlin][idcol];
		}
		else
		{
			return 0.0;
		}
		
	}

	/**
	 * Este método inicia toda a matriz com a matriz identidade de 
	 * diagonal com valor Val
	 * <br>
	 * 
	 * @param Val É o valor que tomará todos os elementos da diagonal 
	 * da matriz, o resto de valores tomarão o valor zero.
	 **/
	public void InitIdentity(double Val) {

		for(int i=0;i<this.Nlin;i++)	
			for(int j=0;j<this.Ncol;j++)
				if(i==j)	this.M[i][j]=Val;
				else		this.M[i][j]=0;

				
	}


	/**
	 * Este método intercambia duas linhas da matriz.
	 * <br>
	 * 
	 * @param i Primeira linha a intercambiar.
	 * @param j Segunda linha a intercambiar.
	 **/
	public void SwapRows(int i,int j) {
		double[] temp = this.M[i];
        this.M[i] = this.M[j];
        this.M[j] = temp;
	}

	/**
	 * Este método multiplica uma matriz M com a matriz MatSrc e retorna 
	 * uma nova matriz.
	 * <br>
	 * Mout=M*Matsrc. Se o numero de linhas da matriz MatSrc e diferente 
	 * que o numero de colunas da matriz, então retorna null.
	 * @param MatSrc É a matriz de fonte para a multiplicaçao Mout=M*Matsrc.
	 * @return Retorna a matriz Mout de Mout=M*Matsrc.
	 **/
	public PdsMatrix MulNew(PdsMatrix MatSrc) {
		PdsMatrix	Mat=this;
		PdsMatrix	Mout=null;
		int N;
		
		if( MatSrc==null )				return null;
		if( Mat.Ncol != MatSrc.Nlin )	return null;
		
		N=Mat.Ncol;
		
		Mout=new PdsMatrix(Mat.Nlin,MatSrc.Ncol);
		if(Mout==null)	return null;
				
		for(int i=0;i<Mat.Nlin;i++)	
		{
			for(int j=0;j<MatSrc.Ncol;j++)
			{
				Mout.M[i][j]=0;
				for(int k=0;k<N;k++)
				{
					Mout.M[i][j]=Mout.M[i][j]+Mat.M[i][k]*MatSrc.M[k][j];
				}
			}
		}
		return Mout;
	}


	/**
	 * Este método retorna a transposta de uma matriz M.
	 * <br>
	 * 
	 * @return Retorna a matriz Mout de Mout=M^T.
	 **/
	public PdsMatrix TransposeNew() {
        
        PdsMatrix Mout = new PdsMatrix(this.Ncol, this.Nlin);
        
        if(Mout==null)	return null;
        
        for (int i = 0; i < this.Nlin; i++)
            for (int j = 0; j < this.Ncol; j++)
                Mout.M[j][i] = this.M[i][j];
        return Mout;
	}

	/**
	 * Este método provoca que se usamos uma instância de PdsMatrix
	 * num contexto que necessita-se ser String, então esta instância
	 * retorna uma descrição da matriz.
	 *
	 * @return Retorna uma descrição da matriz.
	 * */
	public String toString() {
		String TMP;
		TMP="";
		for(int i=0;i<this.Nlin;i++){
			for(int j=0;j<this.Ncol;j++){
				TMP=TMP+ this.M[i][j] + "\t";
			}
			TMP=TMP+"\n";
		}
		
		return TMP;
	}
}
