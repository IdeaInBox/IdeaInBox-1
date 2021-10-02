//login
function mostrarOcultarSenha(){
	
 var senha = document.getElementById("password");

  if(senha.type == "password"){
	
     senha.type = "text";

}
	else{
		
		if(senha.type == "text"){
			
  			  senha.type = "password";
	
	}
}

}
//Tema Dark
function mudaTema() { 
	document.body.classList.toggle("dark");
	} 
//timeline
function limitarCaracter() {
  var texto = document.getElementById("texto").value;

  if (texto.length > 8 && texto.length < 240) {
    document.getElementById("botao").disabled = false;
  } else {
    document.getElementById("botao").disabled = true;
  }
}

//Formulario
//cpf
function validaCpf() {
  var cpf = document.getElementById("cpf").value;

  var Soma;
    var Resto;
    Soma = 0;
  

  for (i=1; i<=9; i++) Soma = Soma + parseInt(cpf.substring(i-1, i)) * (11 - i);
  Resto = (Soma * 10) % 11;

    if ((Resto == 10) || (Resto == 11))  Resto = 0;
    if (Resto != parseInt(cpf.substring(9, 10)) ){
    alert ("Cpf inválido") 
    return;
}
  Soma = 0;
    for (i = 1; i <= 10; i++) Soma = Soma + parseInt(cpf.substring(i-1, i)) * (12 - i);
    Resto = (Soma * 10) % 11;

    if ((Resto == 10) || (Resto == 11))  Resto = 0;
    if (Resto != parseInt(cpf.substring(10, 11) ) ){
    alert ("Cpf inválido")
    return;
  }  
}

