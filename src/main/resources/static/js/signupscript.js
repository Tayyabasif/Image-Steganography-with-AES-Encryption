const password = document.querySelector(".password");
const eyeicon = document.querySelector(".fa-regular.fa-eye-slash");

eyeicon.onclick = function(){
    if(password.type == "password"){
        eyeicon.className = "fa-regular fa-eye";
        password.type = "text";
    }
    else{
        eyeicon.className = "fa-regular fa-eye-slash";
        password.type = "password";
    }
}


document.getElementById('signup-form').addEventListener('submit',function(event){
	var pass = document.querySelector("#pass").value,
    pwd = document.querySelector(".password").value;
	if(pwd == "")
    {
		event.preventDefault();
        alert("Password field cannot be empty");
    }
    else if(pass != pwd)
    {
		event.preventDefault();
        alert("Passwords do not match");
        
    }
});
