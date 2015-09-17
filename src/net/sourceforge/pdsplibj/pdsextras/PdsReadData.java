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

package net.sourceforge.pdsplibj.pdsextras;

import java.io.*;
 
public class PdsReadData{
	
	private File archivo = null;
	private FileReader fr = null;
	private BufferedReader br = null;
	
	public PdsReadData(String path_with_filename){
		try{
			this.archivo	= new File (path_with_filename);
			this.fr		= new FileReader(this.archivo);
			this.br		= new BufferedReader(this.fr);
		} 
		catch (Exception e){
			e.printStackTrace();
			
			try{// Nuevamente aprovechamos el finally para 
				// asegurarnos que se cierra el fichero.
				if (null != this.fr)		this.fr.close();
				//if (null != this.archivo)	this.archivo.close();
			} 
			catch (Exception e2){
					e2.printStackTrace();
			}
		} 
	}
	
	public String Scan(){
		String cadena=null;
		
		try{
			cadena=this.br.readLine();
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return cadena;
	}
	
	public void Close(){
		try{
			this.fr.close();
			//this.archivo.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
    
}
