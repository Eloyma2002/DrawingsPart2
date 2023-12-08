const trashForms = document.querySelectorAll(".trashForm");

trashForms.forEach((form) => {
    form.addEventListener("submit", function (event) {
        event.preventDefault();
        if (confirm("Do you want delete yhos drawing?")) {
            form.submit();
        }
    });
});
