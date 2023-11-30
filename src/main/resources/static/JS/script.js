// Selecciona elementos del DOM
const canvas = document.querySelector('#canvas');
const context = canvas.getContext('2d');
const select = document.querySelector('#figure');
const colorInput = document.querySelector("#color");
const checkbox = document.querySelector("#fill");
const rangeInput = document.querySelector("#size");
const nameImage = document.querySelector('#name');
const buttonClean = document.querySelector('#clean');
const buttonSend = document.querySelector('#send');
const inputJSON = document.querySelector('#json');
const list = document.querySelector('#list');
const buttonDraw = document.querySelector('#draw');
const visualization = document.querySelector('visualization');

// Configuración del canvas
canvas.width = 400;
canvas.height = 400;

// Variables para manejar el estado del dibujo
let figureSelected = select.value;
let color = colorInput.value;
let checked = checkbox.checked;
let size = rangeInput.value;
let x, y;
let cont = 0;
let isMouseDrawing = false;
let content = [];
let lineContent = [];
let isDrawingEnabled = false;

// Establece el color del botón de dibujo según el estado
buttonDraw.style.backgroundColor = isDrawingEnabled ? 'green' : 'red';

// Event listeners para los elementos del DOM
select.addEventListener("change", function () {
    figureSelected = select.value;
});

buttonSend.addEventListener("click", function () {
    inputJSON.value = JSON.stringify(content);
});

colorInput.addEventListener("input", function () {
    color = colorInput.value;
});

checkbox.addEventListener("change", function () {
    checked = checkbox.checked;
});

rangeInput.addEventListener("input", function () {
    size = rangeInput.value;
});

// Dibuja la figura seleccionada al hacer clic en el canvas
canvas.addEventListener('click', function (event) {
    x = event.offsetX;
    y = event.offsetY;
    if (isDrawingEnabled) return;
    drawFigureSelected(x, y, figureSelected, size, checked, color, false);
});

// Limpia el canvas y la lista de figuras
buttonClean.addEventListener('click', function () {
    cont = 0;
    content = [];
    list.innerHTML = "";
    context.clearRect(0, 0, canvas.width, canvas.height);
});

// Activa/desactiva el modo de dibujo
buttonDraw.addEventListener('click', function () {
    isDrawingEnabled = !isDrawingEnabled;
    buttonDraw.style.backgroundColor = isDrawingEnabled ? 'green' : 'red';
});

// Event listeners para el dibujo con el mouse
canvas.addEventListener("mousedown", (event) => {
    if (!isDrawingEnabled) return;
    isMouseDrawing = true;
    lineContent = [];
    const x = event.clientX - canvas.getBoundingClientRect().left;
    const y = event.clientY - canvas.getBoundingClientRect().top;
    lineContent.push([x, y]);
    context.beginPath();
    context.moveTo(x, y);
    context.lineWidth = size / 10;
    canvas.addEventListener("mousemove", (event) => drawMouse(event));
    canvas.addEventListener("mouseup", () => {
        if (isMouseDrawing && lineContent.length > 1) {
            content.push({
                "id": cont,
                "type": "line",
                "size": size / 10,
                "color": color,
                "coordinates": lineContent
            });
            write("Linea");
            cont++;
        }
        isMouseDrawing = false;
    });
    canvas.addEventListener("mouseout", () => {
        if (isMouseDrawing) {
            canvas.removeEventListener("mousemove", (event) => drawMouse(event));
            isMouseDrawing = false;
        }
    });
});

canvas.addEventListener("mouseup", () => {
    if (isMouseDrawing && lineContent.length > 1) {
        content.push({
            "id": cont,
            "type": "line",
            "size": size / 10,
            "color": color,
            "coordinates": lineContent
        });
        write("Line");
        cont++;
    }
    isMouseDrawing = false;
});

visualization.addEventListener('change', function() {
                // Obtener todos los elementos de tipo radio con name="visualization"
                var radios = document.querySelectorAll('input[name="visualization"]');

                // Iterar sobre los radios para encontrar el marcado
                radios.forEach(function(radio) {
                    if (radio.checked) {
                        console.log('El radio marcado es:', radio.value);
                    }
                });
            });

// Genera un nombre de imagen aleatorio
nameImage.value = randomName();

// Función para generar un nombre aleatorio
function randomName() {
    return "image" + Math.floor(Math.random() * 9999 + 1);
}

// Función para dibujar con el mouse
function drawMouse(event) {
    if (!isMouseDrawing) return;
    const x = event.clientX - canvas.getBoundingClientRect().left;
    const y = event.clientY - canvas.getBoundingClientRect().top;
    lineContent.push([x, y]);
    context.strokeStyle = color;
    context.lineTo(x, y);
    context.stroke();
}

// Función para agregar una figura a la lista
function addFigureToList(figureSelected, x, y, size, color, checked) {
    content.push({
        "id": cont,
        "type": figureSelected,
        "x": x,
        "y": y,
        "color": color,
        "fill": checked,
        "size": size,
    });
}

// Función para escribir en la lista
function write(text) {
    list.innerHTML += "<li id='li_" + cont + "'> <button onclick='removeElement(" + cont + ")'>Remove</button> " + text + "</li>";
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

// Función para remover un elemento de la lista
function removeElement(id) {
    const elementToRemove = document.getElementById("li_" + id);
    let contentTemp = [];
    if (elementToRemove) {
        elementToRemove.remove();
    }
    for (let i = 0; i < content.length; i++) {
        if (content[i].id != id && content[i].type != "line") {
            contentTemp.push({
                "id": content[i].id,
                "type": content[i].type,
                "x": content[i].x,
                "y": content[i].y,
                "color": content[i].color,
                "fill": content[i].fill,
                "size": content[i].size,
            });
        } else if (content[i].id != id && content[i].type == "line") {
            contentTemp.push({
                "id": content[i].id,
                "type": content[i].type,
                "size": content[i].size,
                "color": content[i].color,
                "coordinates": content[i].coordinates
            });
        }
    }
    content = contentTemp;
    context.clearRect(0, 0, canvas.width, canvas.height);
    content.forEach(figure => {
        if (figure.type != "line") {
            drawFigureSelected(figure.x, figure.y, figure.type, figure.size, figure.fill, figure.color, true);
        } else {
            drawLine(figure);
        }
    });
}

// Función para dibujar la figura seleccionada
function drawFigureSelected(x, y, figureSelected, size, checked, color, removeList) {
    if (isDrawingEnabled && !removeList) {
        return;
    }
    context.strokeStyle = color;
    context.fillStyle = checked ? color : "transparent";
    context.lineWidth = 1;
    switch (figureSelected) {
        case 'circle':
            context.beginPath();
            context.arc(x, y, size, 0, 2 * Math.PI);
            context.fill();
            context.stroke();
            if (!removeList) {
                addFigureToList(figureSelected, x, y, size, color, checked);
                write("Circle");
                cont++;
            }
            break;
        case 'square':
            context.beginPath();
            context.fillRect(x - size / 2, y - size / 2, size * 2, size * 2);
            context.strokeRect(x - size / 2, y - size / 2, size * 2, size * 2);
            if (!removeList) {
                addFigureToList(figureSelected, x, y, size, color, checked);
                write("Square");
                cont++;
            }
            break;
        case 'triangle':
            context.beginPath();
            context.moveTo(x, y - Number(size));
            context.lineTo(x - Number(size), y + Number(size));
            context.lineTo(x + Number(size), y + Number(size));
            context.closePath();
            context.fill();
            context.stroke();
            if (!removeList) {
                addFigureToList(figureSelected, x, y, size, color, checked);
                write("Triangle");
                cont++;
            }
            break;
        case 'star':
            context.beginPath();
            for (let i = 0; i < 14; i++) {
                const angle = (Math.PI * 2 * i) / 14;
                const radius = i % 2 === 0 ? size : size / 2;
                context.lineTo(x + radius * Math.cos(angle), y + radius * Math.sin(angle));
            }
            context.closePath();
            context.fill();
            context.stroke();
            if (!removeList) {
                addFigureToList(figureSelected, x, y, size, color, checked);
                write("Star");
                cont++;
            }
            break;
    }
}
