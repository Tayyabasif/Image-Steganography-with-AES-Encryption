const inputImage=document.querySelector("#inputimg");
const downloadBtn= document.querySelector("#download");
let uploadedImage="";
const display = document.querySelector("#inner-display");

inputImage.addEventListener("change", function () {
const reader = new FileReader();
reader.addEventListener("load", () =>{
        uploadedImage=reader.result;
        
        display.style.backgroundImage = `url(${uploadedImage})`
        display.style.backgroundSize = "contain";
        display.style.backgroundRepeat = "no-repeat";
        display.style.backgroundPosition = "center";

        downloadBtn.href = uploadedImage;
        downloadBtn.download = file.name;
    })
reader.readAsDataURL(this.files[0])
})

const password = document.getElementById("password");
const eyeicon = document.getElementById("eyeicon");

eyeicon.onclick = function(){
    if(password.type == "password"){
        password.type="text";
        eyeicon.src="images/eyeopen.png";
    }else{
        password.type="password";
        eyeicon.src="images/eyeclose.png";
    }
}

const clear = document.querySelector("#clear");

clear.onclick = function(){
    document.querySelector("#message-box").value="";
}

const copy = document.querySelector("#copy");

copy.onclick = function(){
   let copiedText = document.querySelector("#message-box").value;
   navigator.clipboard.writeText(copiedText).then(()=>{
    alert("Message Copied!!");
    });
}

   
    
