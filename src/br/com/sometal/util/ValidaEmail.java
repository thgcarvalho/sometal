package br.com.sometal.util;

import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * @author tcarvalho
 * 
 */
public class ValidaEmail implements Validator {

	private static final Pattern RFC2822 = Pattern.compile("^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$");

	public void validate(FacesContext context, UIComponent componente,
			Object objeto) throws ValidatorException {
		String emailDigitado = (String) objeto;

		if (emailDigitado != null && !emailDigitado.equals("")) {
			if (RFC2822.matcher(emailDigitado).matches()) {
				FacesMessage message = new FacesMessage();

				message.setDetail("Email inválido.");
				message.setSummary(emailDigitado);

				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(message);
			}
		}
	}

}
