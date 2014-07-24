package br.com.sga.converter;
import static br.com.sga.util.FacesUtils.addErrorMessage;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

/**
 * @author Thiago Carvalho
 * 
 * Converte valor no formato BigDecimal (12.30) para Number no formato (12,30)
 *
 */
public class ValorConverter implements Converter {

	public Object getAsObject(FacesContext arg0, UIComponent arg1, String valorTela) throws ConverterException {
		boolean erro = false;
		Number valorFormatado = null;
		if (valorTela == null || valorTela.toString().trim().equals("")) {
			return null;
		} else {
			NumberFormat nf2d;
			if (valorTela.contains(".") && valorTela.length() < 5) {
				erro = true;
			} else {
				valorTela = valorTela.replaceAll(Pattern.quote("."), "");
				try {
					nf2d = NumberFormat.getInstance(new Locale("pt", "BR"));
					nf2d.setMinimumFractionDigits(2);
					nf2d.setMaximumFractionDigits(2);
	
					valorFormatado = nf2d.parse(valorTela);
				} catch (Exception e) {
					erro = true;
				}
			}
			if (erro) {
				addErrorMessage("Valor " + valorTela + " incorreto!");
				throw new ConverterException("Valor " + valorTela + " incorreto!");
			} else {
				// retoran string, pois o tipo double gera imprecisao
				return valorFormatado;
			}
		}
	}

	public String getAsString(FacesContext arg0, UIComponent arg1, Object valorTela) throws ConverterException {

		if(valorTela == null || valorTela.toString().trim().equals("")) {
			return null;
		} else {
			NumberFormat nf2d = NumberFormat.getInstance(new Locale("pt", "BR"));
			nf2d.setMinimumFractionDigits(2);
			nf2d.setMaximumFractionDigits(2);
			return nf2d.format(Double.valueOf(valorTela.toString()));
		}
	}
}
