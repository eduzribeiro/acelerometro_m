package com.example.acelerometro_m;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

import com.example.acelerometro_m.R;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import net.sourceforge.pdsplibj.pdsdf.*;

public class AccMActivity extends ActionBarActivity implements SensorEventListener {
	
	// Teste
	
	 private long last_timeAcel=0;		
	 private long current_timeAcel=0;
	
	SensorManager sm;
	Sensor acelerometro;
	
	TextView tituloAcc;
	TextView accX=null;
	TextView accY=null;
	TextView accZ=null;
	
	TextView accXF=null;
	TextView accYF=null;
	TextView accZF=null;
	
	TextView tituloVel;
	TextView velX=null;
	TextView velY=null;
	TextView velZ=null;
	
	TextView tituloPos;
	TextView posX=null;
	TextView posY=null;
	TextView posZ=null;
	
	ArrayList<Double> AccSalvas;
	ArrayList<Double> VelSalvas;
	ArrayList<Double> PosSalvas;
	ArrayList<Double> AccRx;
	ArrayList<Double> AccRy;
	ArrayList<Double> AccRz;
	
	double Vx = 0;
	double Vy = 0;
	double Vz = 0;
	double Sx = 0;
	double Sy = 0;
	double Sz = 0;
	double MediavarianciaRx;
	double MediavarianciaRy;
	double MediavarianciaRz;
	double varianciaRx;
	double varianciaRy;
	double varianciaRz;
	
	
	boolean gravaAcc = false;
	boolean gravaVel = false;
	boolean gravaPos = false;
	boolean valorR = false;
	
	int cont;
	
	DecimalFormat decimal = new DecimalFormat( "0.###" );
	
	PdsKalman1D fx = new PdsKalman1D (1,1,1,1);
	PdsKalman1D fy = new PdsKalman1D (1,1,1,1);
	PdsKalman1D fz = new PdsKalman1D (1,1,1,1);

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_acc_m);
		
		sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE); // Acessando os sensores
		acelerometro = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);// Acessando o acelerometro 

		tituloAcc = (TextView) findViewById(R.id.textView7);
		accX = (TextView) findViewById(R.id.textView1);
		accY = (TextView) findViewById(R.id.textView2);
		accZ = (TextView) findViewById(R.id.textView3);
		
		accXF = (TextView) findViewById(R.id.TextView13);
		accYF = (TextView) findViewById(R.id.TextView14);
		accZF = (TextView) findViewById(R.id.TextView15);
		
		tituloVel = (TextView) findViewById(R.id.textView8);
		velX = (TextView) findViewById(R.id.textView4);
		velY = (TextView) findViewById(R.id.textView5);
		velZ = (TextView) findViewById(R.id.textView6);
		
		tituloPos = (TextView) findViewById(R.id.textView10);
		posX = (TextView) findViewById(R.id.TextView09);
		posY = (TextView) findViewById(R.id.TextView10);
		posZ = (TextView) findViewById(R.id.TextView11);

		AccSalvas = new ArrayList<Double>();
		VelSalvas = new ArrayList<Double>();
		PosSalvas = new ArrayList<Double>();
		AccRx = new ArrayList<Double>();
		AccRy = new ArrayList<Double>();
		AccRz = new ArrayList<Double>();
		
		 final Button gravarAcc = (Button) this.findViewById(R.id.button1);
	        gravarAcc.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            
	            	tituloAcc.setText("Gravando Acc");
	            	gravaAcc = true;
	            	
	            }
	        });
	        
	        final Button salvaAcc = (Button) this.findViewById(R.id.button2);
	        salvaAcc.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {      	
	            	
	            	salvarAceleracoes();
	            	gravaAcc = false;
	            }
	        });
	        
	        final Button gravarVel = (Button) this.findViewById(R.id.Button01);
	        gravarVel.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            
	            	tituloVel.setText("Gravando Vel");
	            	gravaVel = true;
	            	
	            }
	        });
	        
	        final Button salvaVel = (Button) this.findViewById(R.id.Button02);
	        salvaVel.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {      	
	            	
	            	salvarVelocidades();
	            	gravaVel = false;
	            }
	        });
	        
	        
	        final Button gravarPos = (Button) this.findViewById(R.id.button3);
	        gravarPos.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            
	            	tituloPos.setText("Gravando Pos");
	            	gravaPos = true;
	            	
	            }
	        });
	        
	        final Button salvaPos = (Button) this.findViewById(R.id.button4);
	        salvaPos.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {      	
	            	
	            	salvarPosicoes();
	            	gravaPos = false;
	            }
	        });
		
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		sm.registerListener(this, acelerometro, SensorManager.SENSOR_DELAY_NORMAL); // Inicia o processo de captura do acelerometro

	}

	@Override
	protected void onPause() {
		super.onPause();
		sm.unregisterListener(this); // Irá parar o processo de captura do sensor
	}                                // Estes métodos(onResume/onPause)fazem poupar bateria, pois sem eles o aplicativo vai continuar  
	// captando informações mesmo que o usuário não esteja interagindo

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.acc_m, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	
	private void salvarAceleracoes() {
    	String filename = "aceleracoes_sem_filtro.txt";

    	FileOutputStream outputStream;
    	String entrada = new String();
    	int ii=0;
    	for (Double d : AccSalvas) {
    		if(ii==3) ii=0;
    		entrada = entrada.concat(d.toString());
    		if(ii!=2) entrada = entrada.concat("\t");
    		else     entrada = entrada.concat("\n");
    		ii++;
    	}
    	
    	File file = new File("/storage/emulated/0", filename);

    	try {
    	  outputStream = new FileOutputStream(file);
    	  outputStream.write(entrada.getBytes());
    	  outputStream.close();
    	  tituloAcc.setText("Acelerações");    	  
    	} catch (Exception e) {
    	  e.printStackTrace();
    	}
    }

    
    private void salvarVelocidades() {
    	String filename = "velocidades.txt";

    	FileOutputStream outputStream;
    	String entrada2 = new String();
    	int iii=0;
    	for (Double d : VelSalvas) {
    		
    		if(iii==3) iii=0;
    		entrada2 = entrada2.concat(d.toString());
    		if(iii!=2) entrada2 = entrada2.concat("\t");
    		else     entrada2 = entrada2.concat("\n");
    		iii++;
    	}
    	
    	File file = new File("/storage/emulated/0", filename);

    	try {
    	  outputStream = new FileOutputStream(file);
    	  outputStream.write(entrada2.getBytes());
    	  outputStream.close();
    	  tituloVel.setText("Velocidades");
    	} catch (Exception e) {
    	  e.printStackTrace();
    	}
    }
    
    private void salvarPosicoes() {
    	String filename = "posicoes.txt";

    	FileOutputStream outputStream;
    	String entrada3 = new String();
    	int iiii=0;
    	for (Double d : PosSalvas) {
    		
    		if(iiii==3) iiii=0;
    		entrada3 = entrada3.concat(d.toString());
    		if(iiii!=2) entrada3 = entrada3.concat("\t");
    		else     entrada3 = entrada3.concat("\n");
    		iiii++;
    	}
    	
    	File file = new File("/storage/emulated/0", filename);

    	try {
    	  outputStream = new FileOutputStream(file);
    	  outputStream.write(entrada3.getBytes());
    	  outputStream.close();
    	  tituloPos.setText("Posições");
    	} catch (Exception e) {
    	  e.printStackTrace();
    	}
    }

	
	@Override
	public void onSensorChanged(SensorEvent event) {

		double ax = (event.values[0]);
		double ay = (event.values[1]);
		double az = (event.values[2] - 9.78);
		
		 String AX = decimal.format(ax);
		 String AY = decimal.format(ay);
		 String AZ = decimal.format(az);
		
		accX.setText("x:"+(AX));
		accY.setText("y:"+(AY));
		accZ.setText("z:"+(AZ));
		

		if (gravaAcc) {
			AccSalvas.add(ax);
			AccSalvas.add(ay);
			AccSalvas.add(az);
		}
		
		if (valorR) {
			AccRx.add(ax);
			AccRy.add(ay);
			AccRz.add(az);
			cont++;
			
		}// Precionar o botão para parametrizar os valores do filtro (exibir no textview da área de parametrização
		 // para aguardar enquanto os valores são calculados até que seja emitida uma mensagem de fim), a variável 
		 // valorR se torna true, calcula a variância com os valores de AccR, que será passada para o filtro, após 
		 // o aviso de que já está pronto precionar o botão de ok para retornar a tela inicial e passar a variável 
		 // valorR para false.
		
		if (cont == 800){
			for(int i = 0; i < 800; ++i) {
				MediavarianciaRx += AccRx.get(i); //conferir aqui também, pois depende de como calcular a variância.
				MediavarianciaRy += AccRy.get(i);
				MediavarianciaRz += AccRz.get(i);
			}
		MediavarianciaRx = MediavarianciaRx/800.0d ;
		MediavarianciaRy = MediavarianciaRy/800.0d ;
		MediavarianciaRz = MediavarianciaRz/800.0d ;
		}
		
		if (cont > 800){
			
			//Passar para o textview na área de parametrização que está pronto e pode precionar ok para retornar.
			
		}
		
		
		double hatax = fx.EvaluateValue(ax);
		double hatay = fy.EvaluateValue(ay);
		double hataz = fz.EvaluateValue(az);
		
		 String Hatax = decimal.format(hatax);
		 String Hatay = decimal.format(hatay);
		 String Hataz = decimal.format(hataz);
		
		
		accXF.setText("x:"+(Hatax));
		accYF.setText("y:"+(Hatay));
		accZF.setText("z:"+(Hataz));
		
		
		
		//--------------------------------------------------------------------- INTEGRAIS SIMPLES
		
		
		double dt = 0.1;
		double VxBk=0;
		double VyBk=0;
		double VzBk=0;
		double SxBk=0;
		double SyBk=0;
		double SzBk=0;
						
		VxBk = Vx;
		VyBk = Vy;
		VzBk = Vz;
		SxBk = Sx;
		SyBk = Sy;
		SzBk = Sz;
		
		//----------------------------------------- VELOCIDADE
		
		
		this.current_timeAcel=event.timestamp;	
		if(this.last_timeAcel==0)	this.last_timeAcel=this.current_timeAcel; 
		
		Vx = VxBk + hatax*(this.current_timeAcel-this.last_timeAcel)/1000000000.0;
		Vy = VyBk + hatay*(this.current_timeAcel-this.last_timeAcel)/1000000000.0;
		Vz = VzBk + hataz*(this.current_timeAcel-this.last_timeAcel)/1000000000.0;
		
		 String VX = decimal.format(Vx);
		 String VY = decimal.format(Vy);
		 String VZ = decimal.format(Vz);
		
		
		velX.setText("x:"+(VX));
		velY.setText("y:"+(VY));
		velZ.setText("z:"+(VZ));
		
		if (gravaVel) {
			VelSalvas.add(Vx);
			VelSalvas.add(Vy);
			VelSalvas.add(Vz);
		
		}
		
		//----------------------------------------- DESLOCAMENTO

		Sx = SxBk + Vx*dt;
		Sy = SyBk + Vy*dt;
		Sz = SzBk + Vz*dt;
		
		String SX = decimal.format(Sx);
		String SY = decimal.format(Sy);
		String SZ = decimal.format(Sz);
		
		
		posX.setText("x:"+(SX));
		posY.setText("y:"+(SY));
		posZ.setText("z:"+(SZ));
		
		if (gravaPos) {
			PosSalvas.add(Sx);
			PosSalvas.add(Sy);
			PosSalvas.add(Sz);
		}
		
		this.last_timeAcel=this.current_timeAcel;
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
}
