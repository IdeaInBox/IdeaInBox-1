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
