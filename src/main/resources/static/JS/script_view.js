// Selecciona el canvas y su contexto
const canvas = document.querySelector('#canvas');
const context = canvas.getContext('2d');

// Obtiene la cadena JSON del elemento con id 'JSON' y la parsea
const jsonString = document.querySelector('#json').value;
const jsonObject = JSON.parse(jsonString);
cargaFiguras(jsonObject);

// Agregar un controlador de eventos para el cambio en el select
document.getElementById('versionSelect').addEventListener('change', function () {
    // Obtener el valor seleccionado
    var selectedValue = this.value;

    context.clearRect(0, 0, canvas.width, canvas.height);
    var jsonObjectVersion = JSON.parse(selectedValue);
    cargaFiguras(jsonObjectVersion);
});

// Itera sobre el array de figuras y las dibuja
function cargaFiguras(jsonObject) {
    jsonObject.forEach(figure => {
        context.beginPath();
        if (figure.type != "line") {
            drawFigureSelected(figure);
        } else {
            drawLine(figure);
        }
        context.closePath();
    });    
}


// Función para dibujar una línea
function drawLine(lineObject) {
    context.strokeStyle = lineObject.color;
    context.lineWidth = lineObject.size;

    context.beginPath();
    context.moveTo(lineObject.coordinates[0][0], lineObject.coordinates[0][1]);

    for (let i = 1; i < lineObject.coordinates.length; i++) {
        context.lineTo(lineObject.coordinates[i][0], lineObject.coordinates[i][1]);
    }
    context.stroke();
}

// Función para dibujar una figura seleccionada
function drawFigureSelected(figure) {
    context.lineWidth = 1;  // Restablecer el grosor de la línea
    context.strokeStyle = figure.color;
    context.fillStyle = figure.fill ? figure.color : "transparent";

    switch (figure.type) {
        case 'circle':
            context.beginPath();
            context.arc(figure.x, figure.y, figure.size, 0, 2 * Math.PI);
            context.fill();
            context.stroke();
            break;

        case 'square':
            context.beginPath();
            context.fillRect(figure.x - figure.size / 2, figure.y - figure.size / 2, figure.size * 2, figure.size * 2);
            context.strokeRect(figure.x - figure.size / 2, figure.y - figure.size / 2, figure.size * 2, figure.size * 2);
            break;

        case 'triangle':
            context.beginPath();
            context.moveTo(figure.x, figure.y - Number(figure.size));
            context.lineTo(figure.x - Number(figure.size), figure.y + Number(figure.size));
            context.lineTo(figure.x + Number(figure.size), figure.y + Number(figure.size));
            context.closePath();
            context.fill();
            context.stroke();
            break;

        case 'star':
            context.beginPath();
            for (let i = 0; i < 14; i++) {
                const angle = (Math.PI * 2 * i) / 14;
                const radius = i % 2 === 0 ? figure.size : figure.size / 2;
                context.lineTo(figure.x + radius * Math.cos(angle), figure.y + radius * Math.sin(angle));
            }
            context.closePath();
            context.fill();
            context.stroke();
            break;
    }
}
