// Selecciona el canvas y su contexto
const canvas = document.querySelector('#canvas');
const context = canvas.getContext('2d');

// Elementos del formulario
const select = document.querySelector('#figure');
const colorInput = document.querySelector("#color");
const checkbox = document.querySelector("#fill");
const rangeInput = document.querySelector("#size");
const nameImage = document.querySelector('#name');
const buttonClean = document.querySelector('#clean');
const buttonSend = document.querySelector('#send');
const inputJSON = document.querySelector('#JSON');
const list = document.querySelector('#list');
const buttonDraw = document.querySelector('#draw');

// Obtiene la cadena JSON del elemento con id 'JSON' y la parsea
const jsonString = document.querySelector('#JSON').value;
const jsonObject = JSON.parse(jsonString);

// Variables 
let figureSelected = select.value;
let color = colorInput.value;
let checked = checkbox.checked;
let size = rangeInput.value;
let x, y;
let cont = 0;
let content = [];
let lineContent = [];
let isMouseDrawing = false;
let isDrawingEnabled = false;

// Configura el tamaño del canvas
canvas.width = 400;
canvas.height = 400;

// Establece el color del botón 'Draw'
buttonDraw.style.backgroundColor = isDrawingEnabled ? 'green' : 'red';

// Carga las figuras del objeto JSON
loadFigures(jsonObject);

// Evento: cambio en la selección de figura
select.addEventListener("change", function () {
    figureSelected = select.value;
});

// Evento: clic en el botón 'Send'
buttonSend.addEventListener("click", function () {
    inputJSON.value = JSON.stringify(content);
});

// Evento: cambio en el color
colorInput.addEventListener("input", function () {
    color = colorInput.value;
});

// Evento: cambio en la opción de rellenar
checkbox.addEventListener("change", function () {
    checked = checkbox.checked;
});

// Evento: cambio en el tamaño
rangeInput.addEventListener("input", function () {
    size = rangeInput.value;
});

// Evento: clic en el canvas
canvas.addEventListener('click', function (event) {
    x = event.offsetX;
    y = event.offsetY;
    if (isDrawingEnabled) return;
    drawFigureSelected(x, y, figureSelected, size, checked, color, false);
});

// Evento: clic en el botón 'Clean'
buttonClean.addEventListener('click', function () {
    cont = 0;
    content = [];
    list.innerHTML = "";
    context.clearRect(0, 0, canvas.width, canvas.height);
});

// Evento: clic en el botón 'Draw'
buttonDraw.addEventListener('click', function () {
    isDrawingEnabled = !isDrawingEnabled;
    buttonDraw.style.backgroundColor = isDrawingEnabled ? 'green' : 'red';
});

// Evento: mousedown en el canvas (dibujo de líneas)
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

    // Evento: mousemove durante el dibujo de líneas
    canvas.addEventListener("mousemove", (event) => drawMouse(event));

    // Evento: mouseup después del dibujo de líneas
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

    // Evento: mouseout durante el dibujo de líneas
    canvas.addEventListener("mouseout", () => {
        if (isMouseDrawing) {
            canvas.removeEventListener("mousemove", (event) => drawMouse(event));
            isMouseDrawing = false;
        }
    });
});

// Evento: mouseup después del dibujo de líneas (para el caso de soltar fuera del canvas)
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

// Función: dibujar con el mouse (líneas)
function drawMouse(event) {
    if (!isMouseDrawing) return;

    const x = event.clientX - canvas.getBoundingClientRect().left;
    const y = event.clientY - canvas.getBoundingClientRect().top;

    lineContent.push([x, y]);

    context.strokeStyle = color;
    context.lineTo(x, y);
    context.stroke();
}

// Función: agregar figura a la lista
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

// Función: escribir en la lista
function write(text) {
    list.innerHTML += "<li id='li_" + cont + "'> <button onclick='removeElement(" + cont + ")'>Remove</button> " + text + "</li>";
}

// Función: dibujar línea
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

// Función: eliminar elemento de la lista
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

// Función: dibujar figura seleccionada
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

// Función: cargar figuras desde un objeto JSON
function loadFigures(jsonObject) {
    jsonObject.forEach(figure => {
        if (figure.type != "line") {
            drawFigureSelected(figure.x, figure.y, figure.type, figure.size, figure.fill, figure.color, false);
        } else {
            drawLine(figure);
            write("Line");
            cont++;
        }
    });
}