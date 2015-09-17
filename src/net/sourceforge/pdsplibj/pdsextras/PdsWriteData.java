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
 
public class PdsWriteData
{

	private FileWriter fichero = null;
	private PrintWriter pw = null;
	
	public PdsWriteData(String path_with_filename){
		try{
			this.fichero	= new FileWriter(path_with_filename);
			this.pw		= new PrintWriter(this.fichero);
		} 
		catch (Exception e){
			e.printStackTrace();
			
			try{// Nuevamente aprovechamos el finally para 
				// asegurarnos que se cierra el fichero.
				if (null != this.fichero)	this.fichero.close();
			} 
			catch (Exception e2){
					e2.printStackTrace();
			}
		} 
	}
	
	public void Print(String cadena){
		try{
			this.pw.print(cadena);
		}
		catch (Exception e){
					e.printStackTrace();
		}
	}
	
	public void Close(){
		try{
			this.fichero.close();
		}
		catch (Exception e){
					e.printStackTrace();
		}
	}
    
}
