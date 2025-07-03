const password = document.getElementById("password");
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