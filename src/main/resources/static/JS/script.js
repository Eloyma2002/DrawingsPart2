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
const publicRadio = document.querySelector("#public");
const privateRadio = document.querySelector("#private");
const viewType = document.querySelector('#viewType');

// Recupera los valores del localStorage
const storedColor = localStorage.getItem("color");
const storedChecked = localStorage.getItem("checked");
const storedSize = localStorage.getItem("size");
const storedFigureSelected = localStorage.getItem("figureSelected");

// Configuración del canvas
canvas.width = 400;
canvas.height = 400;

// Imports para funciones en archivos js externos
import * as Draw from './draw.js';

// Inicializa las variables con los valores almacenados o valores predeterminados
let color = storedColor || colorInput.value;
let checked = storedChecked !== null ? JSON.parse(storedChecked) : checkbox.checked;
let size = storedSize || rangeInput.value;
let figureSelected = storedFigureSelected || select.value;

// Variables para manejar el estado del dibujo
let x, y;
let cont = 0;
let isMouseDrawing = false;
let content = [];
let lineContent = [];
let isDrawingEnabled = false;
let isModifyMode = false;
let idModifyFigure = -1;

// Actualiza los valores iniciales
colorInput.value = color;
checkbox.checked = checked;
rangeInput.value = size;
select.value = figureSelected;

// Establece el color del botón de dibujo según el estado
buttonDraw.style.backgroundColor = isDrawingEnabled ? 'green' : 'red';

// Event listeners para los elementos
select.addEventListener("change", function () {

    if (isModifyMode) {
        updateElementProperties();
    }
    figureSelected = select.value;
    localStorage.setItem("figureSelected", figureSelected);
});

buttonSend.addEventListener("click", function () {

    if (publicRadio.checked) {
        viewType.value = "1";
    } else {
        viewType.value = "0";
    }

    inputJSON.value = JSON.stringify(content);
});

// Agrega manejadores de eventos para los cambios en los elementos de entrada
colorInput.addEventListener("input", function () {
    if (isModifyMode) {
        updateElementProperties();
    }
    color = colorInput.value;
    localStorage.setItem("color", color);

});

rangeInput.addEventListener("input", function () {
    if (isModifyMode) {
        updateElementProperties();
    }
    size = rangeInput.value;
    localStorage.setItem("size", size);

});

checkbox.addEventListener("change", function () {
    if (isModifyMode) {
        updateElementProperties();
    }
    checked = checkbox.checked;
    localStorage.setItem("checked", checked);


});

// Dibuja la figura seleccionada al hacer clic en el canvas
canvas.addEventListener('click', function (event) {
    x = event.offsetX;
    y = event.offsetY;

    if (isModifyMode && idModifyFigure != -1) {
        let figure = Draw.obtainFigureById(idModifyFigure, content);
        figure.x = x;
        figure.y = y;
        Draw.redrawCanvas(content, context, canvas);
        return;
    }

    if (isDrawingEnabled) return;
    drawFigureSelected(x, y, figureSelected, size, checked, color, false);
});

document.addEventListener("click", function () {
    if (content.length === 0) {
        isModifyMode = false;
    }
})

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
    if (isModifyMode == true) return;
    buttonDraw.style.backgroundColor = isDrawingEnabled ? 'green' : 'red';
});



// Event listeners para el dibujo con el mouse
canvas.addEventListener("mousedown", (event) => {
    if (!isDrawingEnabled) return;
    if (isModifyMode) return;

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

// Genera un nombre de imagen aleatorio
nameImage.value = Draw.randomName();


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


// Función para escribir en la lista
function write(text) {
    if (text == "Line") {
        list.innerHTML += "<li id='li_" + cont + "'> <button onclick='removeElement(" + cont + ")'>Remove</button> " + text;
    } else {
        list.innerHTML += "<li id='li_" + cont + "'> <button onclick='removeElement(" + cont + ")'>Remove</button> " +
            "<button id='" + cont + "' onclick='modifyElement(" + cont + ")' styles='backgound-color = 'red''>Modify</button>" + text + "</li>";
        
        const buttonModify = document.getElementById(cont);
        buttonModify.style.backgroundColor = 'red';
    }
}

// Agrega la función modifyElement
function modifyElement(id) {
    if (isDrawingEnabled == true) {
        return;
    }
    
    isModifyMode = !isModifyMode; // Activa el modo de modificación

    if (id != idModifyFigure && idModifyFigure != -1) {
        const lastButton = document.getElementById(idModifyFigure);
        lastButton.style.backgroundColor = 'red'
        isModifyMode = false;
        return;
    }

    const buttonModify = document.getElementById(id);
    idModifyFigure = id;

    if (isModifyMode == false) {
        buttonModify.style.backgroundColor = 'red';
        idModifyFigure = -1;
        return;
    }
    buttonModify.style.backgroundColor = 'green';

    const elementToModify = Draw.obtainFigureById(id, content);

    // Llena los campos del formulario con los valores actuales del elemento
    select.value = elementToModify.type;
    colorInput.value = elementToModify.color;
    checkbox.checked = elementToModify.fill;
    rangeInput.value = elementToModify.size;
}

// Función para dibujar una línea
export function drawLine(lineObject) {
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
export function drawFigureSelected(x, y, figureSelected, size, checked, color, removeList) {
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
                Draw.addFigureToList(figureSelected, x, y, size, color, checked, content, cont);
                write("Circle");
                cont++;
            }
            break;
        case 'square':
            context.beginPath();
            context.fillRect(x - size / 2, y - size / 2, size * 2, size * 2);
            context.strokeRect(x - size / 2, y - size / 2, size * 2, size * 2);
            if (!removeList) {
                Draw.addFigureToList(figureSelected, x, y, size, color, checked, content, cont);
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
                Draw.addFigureToList(figureSelected, x, y, size, color, checked, content, cont);
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
                Draw.addFigureToList(figureSelected, x, y, size, color, checked, content, cont);
                write("Star");
                cont++;
            }
            break;
    }
}