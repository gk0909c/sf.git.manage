package gk0909c.sf.git.manage.test.helper;

import java.io.File;

public class TestHelper {
	public static void delete(String f){
		delete(new File(f));
	}
	
	public static void delete(File f){
		if( f.exists() == false ){
			return ;
		}

		if(f.isFile()){
			f.delete();
		}
			
		if(f.isDirectory()){
			File[] files=f.listFiles();
			for(int i=0; i<files.length; i++){
				delete( files[i] );
			}
			f.delete();
		}
	}
}
