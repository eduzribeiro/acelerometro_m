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

/**
 * Esta classe implementa um filtro FIR NLMS (Normalized - Least Mean Square) de ordem N,.
 * <br>
 * <center><img src="{@docRoot}/doc/imagenes/filtrobloco.png" alt="Filtro NLMS FIR"></center><br>
 * <br>
 * <center><img src="{@docRoot}/doc/imagenes/lms.png" alt="Filtro NLMS FIR"></center><br>
 * <br>
 * <center><img src="{@docRoot}/doc/imagenes/firlms.png" alt="Actorialização de pesos"></center><br>
 * <br>
 * <br>
 * <center><img src="{@docRoot}/doc/imagenes/eqfirnlms2.png" width="400" alt="eq2"></center><br>
 * <br>
 * <center><img src="{@docRoot}/doc/imagenes/eqfirnlms4.png" width="350" alt="eq4"></center><br>
 * <br>
 * <center><img src="{@docRoot}/doc/imagenes/eqfirnlms1.png" width="280" alt="eq1"></center><br>
 * <br>
 * <center><img src="{@docRoot}/doc/imagenes/eqfirnlms3.png" width="250" alt="eq3"></center><br>
 * <br>
 * <center><img src="{@docRoot}/doc/imagenes/eqfirnlms5.png" width="450" alt="eq5"></center><br>
 * <br>
 *
 * <br><br> Para usar esta classe é necessário escrever:
 *  <pre>  import net.sourceforge.pdsplibj.pdsdf.PdsFirNlms; </pre>
 *
 * <br>Para usar este filtro pode-se usar este código de exemplo:
 *  <pre>  
 *  int M=12;                      // Ordem do filtro.
 *  double Mhu=0.6;                // Fator de aprendizagem
 *  PdsFirNlmsData Dat;
 *  double Y,E;
 *
 *  PdsFirNlms Filtro = new PdsFirNlms(Mhu,M);
 *  
 *  Filtro.SetLambda(0.0000001);   // Qualquer valor lambda muito pequeno.
 *
 *  Dat=Filtro.EvaluateValue(D,X);
 *  Y=Dat.GetY();
 *  E=Dat.GetE();
 *  </pre>
 * 
 * @author Fernando Pujaico Rivera <a href="mailto:fernando.pujaico.rivera@gmail.com">fernando.pujaico.rivera@gmail.com</a>
 * @version 0.05
 * @since 2015-05-25
 * @see <a href="http://pdsplib.sourceforge.net"> PDS Project Libraries in Java </a>
 * @see PdsFir
 */
public class PdsFirNlms {
    private int Work;
    private double Mhu;
    private double Lambda;
    private PdsFir Fir;
    private PdsFirNlmsData Data;


    /**
     * Este constructor crea un filtro FIR NLMS.
     *
     * Os parâmetros H_i[n] do filtro FIR tem um
     * valor inicial de H_i[n]=1/(1+M). Por defeito o filtro FIR NLMS estará
     *  auto configurando-se continuamente, a no ser que se desabilite com
     *  o método {@link #Disable()}.
     *
     * @param mhu Este é passo da constante de adaptação.
     * @param M É o ordem do filtro: Y[n]=X[n]*H[0] + X[n-1]*H[1] + ... + X[n-M]*H[M]
     **/
    public PdsFirNlms(double mhu,int M) {

        this.Data   = new PdsFirNlmsData();

        this.Fir    = new PdsFir(M);
        for(int i=0;i<=M;i++)   this.Fir.SetHValue(1.0/(M+1),i);

        this.Work=1;
        this.Mhu=mhu;
        this.Lambda=0;
    }

    /**
     * Desabilita a reconfiguração dos pesos H_i[n] do filtro FIR NLMS.
     */
    public void Disable(){
        this.Work=0;
    }

    /**
     * Habilita la reconfiguração de los pesos H_i[n] del filtro FIR NLMS.
     */
    public void Enable(){
        this.Work=1;
    }

    /**
     * Coloca o valor Mhu do filtro FIR NLMS.
     *
     * @param mhu É o passo da constante de adaptação.
     */
    public void SetMhu(double mhu){
        this.Mhu=mhu;
    }

    /**
     * Coloca o valor <img src="{@docRoot}/doc/imagenes/lambda.png" height="12" alt="Lambda"> do filtro FIR NLMS.
     *
     * 
     * @param lambda É um parâmetro que tenta prever que de um erro por divisão por zero se 
     *               a entrada X é toda zero. Aconselha-se que lambda seja muito menor que  1.0.
     */
    public void SetLambda(double lambda){
        this.Lambda=lambda;
    }

    /**
     * Este método avalia o filtro FIR LMS normalizado
     *
     * O filtro é avaliado de jeito que no filtro entra X e D e é
     * retirado um valor filtrado Y e a diferença E.
     *
     * @param D É um valor na entrada D.
     * @param X É um valor na entrada X.
     * @return  Retorna os valores de Y e E depois de avaliar o filtro FIR NLMS.
     */
    public PdsFirNlmsData EvaluateValue(double D,double X){
        double Y,E,XtX;
        int i,N;
        double Hi;

        Y=this.Fir.EvaluateValue(X);
        E=D-Y;

        if(this.Work==1) {
            N=this.Fir.GetDimension();

            XtX = 0;
            for (i = 0; i <= N; i++) {
                XtX = XtX + this.Fir.GetXValue(i);
            }

            for(i=0;i<=N;i++) {
                Hi=this.Fir.GetHValue(i);

                Hi = Hi + this.Mhu*E*this.Fir.GetXValue(i) /(this.Lambda + XtX);

                this.Fir.SetHValue(Hi,i);
            }

        }

        Data.SetY(Y);
        Data.SetE(E);
        return this.Data;
    }
}
