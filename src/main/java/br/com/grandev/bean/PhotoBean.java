package br.com.grandev.bean;

import static br.com.grandev.util.FacesUtils.MSG_ERROR;
import static br.com.grandev.util.FacesUtils.addErrorMessage;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.grandev.model.Funcionario;

/**
 * @author Thiago Carvalho
 * 
 */
@ManagedBean
@SessionScoped
public class PhotoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private boolean debug = false;
	
	public StreamedContent loadPhoto(Funcionario funcionario) {
		if(debug){System.out.println("PhotoBean.loadPhoto=" + funcionario);}
		StreamedContent scPhoto = new DefaultStreamedContent();
		String strPhoto = "";
		try {
			if (funcionario != null && funcionario.getFoto() != null && !funcionario.getFoto().equals("")) {
				strPhoto = funcionario.getFoto();
			} else {
				strPhoto = "C:\\Users\\tcarvalho\\sometal\\files\\sometal\\FOTOS\\sem_foto.jpg";
			}
			if(debug){System.out.println("FL=" + strPhoto);}
			final File filePhoto = new File(strPhoto);
			if(debug){System.out.println("EXISTE=" + filePhoto.exists());}
			final FileInputStream fileInputStream = new FileInputStream(filePhoto);
			final InputStream is = new BufferedInputStream(fileInputStream);
			scPhoto = new DefaultStreamedContent(is);
		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage(MSG_ERROR, "Erro ao carregar foto!");
		}
		if(debug){System.out.println("SC=" + (scPhoto == null ? "null" : scPhoto));}
		return scPhoto;
	}

}
