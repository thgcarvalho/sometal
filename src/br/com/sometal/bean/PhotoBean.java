package br.com.sometal.bean;

import static br.com.sometal.util.FacesUtils.MSG_ERROR;
import static br.com.sometal.util.FacesUtils.addErrorMessage;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 * @author Thiago Carvalho
 * 
 */
@ManagedBean
@SessionScoped
public class PhotoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public StreamedContent getLoadPhoto() {
		String strPhoto = "C:\\Users\\tcarvalho\\sometal\\files\\sometal\\FOTOS\\1004.jpg";
		StreamedContent scPhoto = null;
		try {
			if (strPhoto == null || strPhoto.equals("")) {
				strPhoto = "\\resources\\images\\sem_foto.jpg";
			}
			System.out.println("PhotoBean.FL=" + strPhoto);
			final File filePhoto = new File(strPhoto);
			System.out.println("EXISTE=" + filePhoto.exists());
			final FileInputStream fileInputStream = new FileInputStream(filePhoto);
			final InputStream is = new BufferedInputStream(fileInputStream);
			scPhoto = new DefaultStreamedContent(is);
		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage(MSG_ERROR, "Erro ao carregar foto!");
		}
		System.out.println("PhotoBean.SC=" + (scPhoto == null ? "null" : scPhoto.getName()));
		return scPhoto;
	}
	
	public StreamedContent loadPhoto(String strPhoto) {
		System.out.println("Bean.FL=" + strPhoto);
		StreamedContent scPhoto = null;
		try {
			if (strPhoto == null || strPhoto.equals("")) {
				strPhoto = "";
			}
			final File filePhoto = new File(strPhoto);
			System.out.println("EXISTE=" + filePhoto.exists());
			final FileInputStream fileInputStream = new FileInputStream(filePhoto);
			final InputStream is = new BufferedInputStream(fileInputStream);
			scPhoto = new DefaultStreamedContent(is);
		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage(MSG_ERROR, "Erro ao carregar foto!");
		}
		System.out.println("Bean.SC=" + (scPhoto == null ? "null" : scPhoto));
		return scPhoto;
	}

}
