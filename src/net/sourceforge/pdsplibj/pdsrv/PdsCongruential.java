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

import java.util.*;
import net.sourceforge.pdsplibj.pdsrv.PdsRV;

/** 
 * Esta classe implementa uma variável aleatória inteira K, uniformemente distribuída 
 * entre 0 e PdsRV.PDS_RAND_MAX.<br>
 *
 * <br> Esta variável aleatoria nunca atinge o valor PdsRV.PDS_RAND_MAX.<br><br>
 *  <center><img src="{@docRoot}/doc/imagenes/PdsCongruential.png" width="500" alt="0&le;k&lt;PdsRV.PDS_RAND_MAX"></center><br>
 *  Para usar esta classe é necessário escrever:
 *  <pre>  import net.sourceforge.pdsplibj.pdsrv.PdsCongruential; </pre>
 *
 * @author Fernando Pujaico Rivera <a href="mailto:fernando.pujaico.rivera@gmail.com">fernando.pujaico.rivera@gmail.com</a>
 * @version 0.01
 * @since 2015-05-25
 * @see <a href="http://pdsplib.sourceforge.net"> PDS Project Libraries in Java </a>
*/
public class PdsCongruential {
	/** 
	 * A semente inicial da sequencia aleatória. 0<=x0<PdsRV.PDS_RAND_MAX
	 */
	private long x0;

	/** 
	 * O multiplicador da sequencia aleatória. 1<a<PdsRV.PDS_RAND_MAX,
	 * a-1 debe ser múltiplo dos fatores primos de PdsRV.PDS_RAND_MAX,
	 * e dizer múltiplo de 2. Si PdsRV.PDS_RAND_MAX e multiplo de 4, a-1 
	 * também debe ser-lo.
	 */
	private long a;

	/** 
	 * O incremento da sequencia aleatória. 1 < c < PdsRV.PDS_RAND_MAX,
	 * ademas c y PdsRV.PDS_RAND_MAX são primos relativos.
	 */
	private long c;

	/** 
	 * A saída inteira da sequencia aleatória. 
	 * 0 <= xn < PdsRV.PDS_RAND_MAX. 
	 */
	private long xn;

	/**
	 * Este é o construtor da classe PdsCongruential.
	 *
	 * Sô pode-se criar 7829 variáveis aleatórias (sequencias pseudo
	 * aleatórias distintas), depois as variáveis aleatórias (sequencias)
	 * serão repetidas, mas serão mesmo assim versões deslocadas no tempo.
	 **/
	public PdsCongruential() {
		Date date = new Date();
		long time = date.getTime();

		this.xn = time%(PdsRV.PDS_RAND_MAX/2);
		this.x0 = this.xn;
		this.a  = 1103515245;
		this.c  =(long)pds_inc(t3,t5,t7,t11,t13,t17);
		
		// Buscando el nuevo incremento de c.
		pds_next_inc();
	}

	/**
	 * Este método o inicia a variável aleatória congruential.
	 * 
	 * Se x0 esta fora do rango [0,PdsRV.PDS_RAND_MAX> então ele toma 
	 * o valor de 0.
	 * @param x0 valor inicial da variável aleatória congruential.
	 **/
	public void Init(long x0) {
		this.xn=x0%PdsRV.PDS_RAND_MAX;
		this.x0=this.xn;
	}
	

	/**
	 * Este método retorna um valor da variável aleatória gaussiana.
	 *
	 *
	 * @return Retorna um valor da variável aleatória gaussiana.
	 **/
	public long GetValue() {
		this.x0=this.xn;
		this.xn=(this.a*this.x0+this.c)%PdsRV.PDS_RAND_MAX;
	
		return this.x0;
	}

////////////////////////////////////////////////////////////////////////	
////////////////////////////////////////////////////////////////////////
////// Variáveis estáticas para criar novas variáveis aleatórias
////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////
	private static long t3=1;
	private static long t5=0;
	private static long t7=0;
	private static long t11=0;
	private static long t13=0;
	private static long t17=0;
	
	// Avalia eo Incremento.
	private static double pds_inc(	long T3, long T5 ,long T7,
									long T11,long T13,long T17)
	{
		return	Math.pow(3.0,T3)*
				Math.pow(5.0,T5)*
				Math.pow(7.0,T7)*
				Math.pow(11.0,T11)*
				Math.pow(13.0,T13)*
				Math.pow(17.0,T17);
	}

	// Encontra os índices. cria 7829 índices distintos, o que e 
	// equivalente a dizer que se tem 7829 variáveis aleatórias.
	private static void pds_next_inc()
	{
		if(pds_inc(t3+1,t5,t7,t11,t13,t17)<(PdsRV.PDS_RAND_MAX*1.0))	{t3=t3+1;}
		else
		{	t3=1; 
			if(pds_inc(t3,t5+1,t7,t11,t13,t17)<(PdsRV.PDS_RAND_MAX*1.0))	{t5=t5+1;}
			else
			{	t5=0;
				if(pds_inc(t3,t5,t7+1,t11,t13,t17)<(PdsRV.PDS_RAND_MAX*1.0))	{t7=t7+1;}
				else
				{	t7=0;
					if(pds_inc(t3,t5,t7,t11+1,t13,t17)<(PdsRV.PDS_RAND_MAX*1.0))	{t11=t11+1;}
					else
					{	t11=0;
						if(pds_inc(t3,t5,t7,t11,t13+1,t17)<(PdsRV.PDS_RAND_MAX*1.0))	{t13=t13+1;}
						else
						{	t13=0;
							if(pds_inc(t3,t5,t7,t11,t13,t17+1)<(PdsRV.PDS_RAND_MAX*1.0))	{t17=t17+1;}
							else									{t17=0;	t3=1;t5=0;t7=0;t11=0;t13=0;}
						}
					}
				}
			}		
		}
	}
////////////////////////////////////////////////////////////////////////	
////////////////////////////////////////////////////////////////////////
}
