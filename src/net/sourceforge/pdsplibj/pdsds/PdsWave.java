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

package net.sourceforge.pdsplibj.pdsds;

/**
 * Esta classe gera uma sinal a partir de um vetor de dados, e se repete ciclicamente.
 * 
 * <br><br> Para usar esta classe é necessário escrever:
 *  <pre>  import net.sourceforge.pdsplibj.pdsds.PdsWave; </pre>
 * Um exemplo de código seria:
 * <pre>
 *  PdsWave fonte=new PdsWave(64);
 *
 *  //Carregar os valores da fonte.
 *  for(int i;i&lt;64;i++)  fonte.SetData(i,i); 
 *
 *  //Logo simplesmente pedir os dados
 *  value=fonte.GetData();
 * </pre>
 * <br>
 *  <center><img src="{@docRoot}/doc/imagenes/PdsWave.png" alt="Señal de salida y[n]"></center><br>
 *
 * @author Fernando Pujaico Rivera <a href="mailto:fernando.pujaico.rivera@gmail.com">fernando.pujaico.rivera@gmail.com</a>
 * @version 0.03
 * @since 2015-05-25
 * @see <a href="http://pdsplib.sourceforge.net"> PDS Project Libraries in Java </a>
 */
public class PdsWave {

    // Um arreglo de Nel elementos.
    double[] V;

    // Número de elementos.
    int Nel;

    // ID do elemento atual, que ainda não foi entregado.
    int ID;

    // Ultimo valor entregado pelo gerador de ondas PdsWave.
    double x;

    /**
     * Este método é o construtor e cria una classe de tipo PdsWave com Nel elementos.
     * <br>
     * Ao ser criada uma instancia todos os Nel elementos são recheados com zeros.
     * Para carregar os valores é necessário usar o método {@link #SetData(double v,int n)}  .
     * @param nel É o número de elementos por ciclo.
     */
    public PdsWave(int nel){
        this.Nel=nel;
        this.V=new double[this.Nel];
        this.ID=0;
        this.x=0;
    }

    /**
     * Escreve o valor v na posição (n) do gerador PdsWave.
     * <br>
     * Em caso de erro por estar fora de rango de (n) então não faz nada e
     * não se considera como erro.
     * @param v O valor v na posição (n).
     * @param n Posição (n), O primeiro valor de n es cero.
     */
    public void SetData(double v,int n){
        if((n>=0)&&(n<this.Nel)) {
            this.V[n]=v;
        }
    }

    /**
     * Obtém um valor na saida e itera o gerador de ondas.
     *
     * @return Retorna o novo valor à saída do gerador de ondas de tipo PdsWave.
     */
    public double GetValue(){

        this.x=this.V[this.ID];

        this.ID=this.ID+1;
        if(this.ID==this.Nel)	this.ID=0;

        return this.x;
    }

    /**
     * Obtém o ultimo valor entregado na saída do gerador de ondas PdsWave.
     *
     * @return Retorna o ultimo valor entregado na saída do gerador de ondas PdsWave.
     */
    public double GetLastValue(){
        return this.x;
    }
}
