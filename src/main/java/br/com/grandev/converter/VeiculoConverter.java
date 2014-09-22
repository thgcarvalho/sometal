package br.com.grandev.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import br.com.grandev.daoImp.VeiculoDaoImp;
import br.com.grandev.model.Veiculo;

public class VeiculoConverter implements Converter {

	public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
		try {
			VeiculoDaoImp veiculoDao = new VeiculoDaoImp();
			Veiculo veiculo = veiculoDao.findByPlaca(submittedValue);
			return veiculo;
		} catch (NumberFormatException exception) {
			exception.printStackTrace();
			throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro de convercao", "Veículo inválido"));
		}
	}

	public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
		if (value == null || value.equals("")) {
			return "";
		} else {
			return String.valueOf(((Veiculo) value).getPlaca());
		}
	}
}
