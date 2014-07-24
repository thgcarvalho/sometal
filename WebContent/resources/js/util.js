function setCPF() {
	document.getElementById("form:cpf").style.background = "#FFFFFF";
	document.getElementById("form:cnpj").style.background = "#CCCCCC";
	document.getElementById("form:cnpj").value = "";
}

function setCNPJ() {
	var cpf = document.getElementById("form:cpf").value;
	console.log(cpf);
	if (cpf == "") {
		console.log("if");
		document.getElementById("form:cnpj").style.color = "#000000";
		document.getElementById("form:cnpj").style.background = "#FFFFFF";
		document.getElementById("form:cpf").style.background = "#CCCCCC";
		document.getElementById("form:cpf").value = "";
	} else{
		document.getElementById("form:cnpj").style.color = "#CCCCCC";
	}
}

function teste() {
	console.log("teste");
}

