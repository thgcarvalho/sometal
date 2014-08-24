package br.com.grandev.validator;

import static br.com.grandev.util.FacesUtils.MSG_ERROR;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
 
@FacesValidator("emailValidator")
public class EmailValidator implements Validator{
 
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\." +
			"[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*" + 
			"(\\.[A-Za-z]{2,})$";
 
	private Pattern pattern;
	private Matcher matcher;
 
	public EmailValidator(){
		  pattern = Pattern.compile(EMAIL_PATTERN);
	}
 
	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
 
		matcher = pattern.matcher(value.toString());
		if(!matcher.matches()){
 
			FacesMessage msg = 
				new FacesMessage(MSG_ERROR, "Email em formato inválido!");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
 
		}
 
	}
}

//public class EmailValidator implements Validator {
//
//	public void validate(FacesContext facesContext, UIComponent uIComponent, Object object) throws ValidatorException {
//
//		String enteredEmail = (String) object;
//		Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
//		Matcher m = p.matcher(enteredEmail);
//
//		boolean matchFound = m.matches();
//
//		if (!matchFound) {
//			FacesMessage message = new FacesMessage();
//			message.setSummary("E-mail incorreto!");
//			message.setDetail(enteredEmail);
//			message.setSeverity(FacesMessage.SEVERITY_ERROR);
//			throw new ValidatorException(message);
//		}
//	}
//}
