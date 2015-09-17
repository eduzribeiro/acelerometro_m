/*
 * Copyright (c) 2015.Fernando Pujaico Rivera <fernando.pujaico.rivera@gmail.com>
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

/**
 * Esta classe implementa o dado de retorno de um filtro FIR LMS normalizado de ordem M.
 *
 * Em verdade esta classe é uma estrutura com os dados Y e E. Também como é logico
 * foram implementados métodos para aceder a estes dados.
 * <br><br> 
 * <center><img src="{@docRoot}/doc/imagenes/PdsFirNlmsData.png" alt="Dado de retorno do Filtro NLMS FIR"></center><br>
 * <br>Para usar esta classe é necessário escrever:
 *  <pre>  import net.sourceforge.pdsplibj.pdsdf.PdsFirNlmsData; </pre>
 *
 * @author Fernando Pujaico Rivera <a href="mailto:fernando.pujaico.rivera@gmail.com">fernando.pujaico.rivera@gmail.com</a>
 * @version 0.05
 * @since 2015-05-25
 * @see <a href="http://pdsplib.sourceforge.net"> PDS Project Libraries in Java </a>
 */
public class PdsFirNlmsData {
    private double Y;
    private double E;

    /**
     * Este construtor cria um dado de tipo PdsFirNlmsData com Y=0 e E=0.
     */
    public PdsFirNlmsData(){
        this.Y=0;
        this.E=0;
    }

    /**
     * Este método escreve o valor de Y.
     *
     * @param y É o parâmetro Y a escrever.
     */
    public void SetY(double y){
        this.Y=y;
    }

    /**
     * Este método escreve o valor E.
     *
     * @param e É o parâmetro E a escrever.
     */
    public void SetE(double e){
        this.E=e;
    }

    /**
     * Retorna o valor de Y.
     *
     * @return Retorna o valor de Y.
     */
    public double GetY(){
        return this.Y;
    }

    /**
     * Retorna o valor de E.
     *
     * @return Retorna o valor de E.
     */
    public double GetE(){
        return this.E;
    }
}
