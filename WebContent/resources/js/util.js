function setCPF() {
	alert("setCPF");
	console.log("setCPF");
	console.log(value);
	document.getElementById("formAdd:cpf").style.background = "#FFFFFF";
	document.getElementById("formAdd:cnpj").style.background = "#CCCCCC";
	document.getElementById("formAdd:cnpj").value = "";
}

function setCNPJ() {
	alert("setCNPJ");
	console.log("setCNPJ");
	var cpf = document.getElementById("formAdd:cpf").value;
	console.log(cpf);
	if (cpf == "") {
		console.log("if");
		document.getElementById("formAdd:cnpj").style.color = "#000000";
		document.getElementById("formAdd:cnpj").style.background = "#FFFFFF";
		document.getElementById("formAdd:cpf").style.background = "#CCCCCC";
		document.getElementById("formAdd:cpf").value = "";
	} else{
		document.getElementById("formAdd:cnpj").style.color = "#CCCCCC";
	}
}

function teste() {
	console.log("teste");
}

