const trashForm  = document.querySelector("#trashForm");

trashForm.addEventListener("submit" , function(event) {
    event.preventDefault();
    if (!confirm("Clicka en Aceptar o Cancelar")) {
        return;
    };
    trashForm.submit();
})
