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


package net.sourceforge.pdsplibj.pdsrv;


/** 
 * Esta classe só tem constantes
 *
 *
 * <br><br> Para usar esta classe é necessário escrever:
 *  <pre>  import net.sourceforge.pdsplibj.pdsrv.PdsRV; </pre>
 *
 * @author Fernando Pujaico Rivera <a href="mailto:fernando.pujaico.rivera@gmail.com">fernando.pujaico.rivera@gmail.com</a>
 * @version 0.01
 * @since 2015-05-25
 * @see <a href="http://pdsplib.sourceforge.net"> PDS Project Libraries in Java </a>
 */
public class PdsRV {
	/** 
	 *  Máximo número de elementos das sequencias aleatórias: PdsRV.PDS_RAND_MAX. 
	 *  
	 *  PdsRV.PDS_RAND_MAX e' um número primo o una potencia de um número primo.
	 *  Este número indica que la sequencia gerada tem um período de
	 *  PdsRV.PDS_RAND_MAX. Se ha usado el método congruencial multiplicativo 2^N,
	 *  N=31, para gerar as variáveis aleatórias uniformemente distribuídas.
	 */
	public static long PDS_RAND_MAX=2147483648L;

	/** 
	 *  Logaritmo natural do máximo número de elementos das sequencias aleatórias:
	 *  ln(PdsRV.PDS_RAND_MAX).
	 */
	public static double LN_PDS_RAND_MAX=21.487562597;

	/** 
	 *  Dos vesses o logaritmo natural do máximo número de elementos das 
	 *  sequencias aleatórias: 2*ln(PdsRV.PDS_RAND_MAX).	
	 */
	public static double _2LN_PDS_RAND_MAX=42.975125195;

}
