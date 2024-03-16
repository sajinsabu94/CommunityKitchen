package com.xeta.communitykitchen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class FirebaseDBCollectionToExcel {
	
	private String excelData;
	private String[] fieldsNames;
	private  File file;
	
	public FirebaseDBCollectionToExcel(String[] fieldsNames, String[] fieldsNamesOrg) {
		//Initialize properties
		this.fieldsNames = fieldsNames;
		excelData = "";
		file = null;
		
		//Initialize header in Excel file
		for ( int i = 0; i < fieldsNamesOrg.length; i++) {
			excelData +=  "\"" + fieldsNamesOrg[i] + "\"";
			
			if ( i < fieldsNamesOrg.length - 1)
				excelData += ",";
			else
				excelData += "\n";
		}
	}
	
	public void buildFileFromQuery(QueryDocumentSnapshot document ) {
		String rowData = "";
		for ( int i = 0; i < fieldsNames.length; i++) {
			
			rowData +=  "\"" + document.getString(fieldsNames[i]) + "\"";
			
			if ( i < fieldsNames.length - 1)
				rowData += ",";
			else
				rowData += "\n";
		}	
		
		excelData += rowData;
	}
	
	public void saveFileToStorage(String fileName, String dirName) {

		file   = null;
        File root   = Environment.getExternalStorageDirectory();

        if ( root.canWrite()) {
        	
            File dir    =   new File ( root.getAbsolutePath() + "/" + dirName);
            dir.mkdirs();
            file = new File( dir, fileName + ".csv");
            FileOutputStream out = null;
            
            try {
                out = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            try {
                out.write(excelData.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}
}
