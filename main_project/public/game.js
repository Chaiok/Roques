var socket = io();
let MapOfPlayers = new Map();
var $all_messages = $("#all_mess");
$('form').submit(function () {
    socket.emit('chat message', $('#m').val());
    $('#m').val('');
    return false;
});
socket.on('add mess', function (data) {
    var decoder = new TextDecoder("utf-8");
    var per = decoder.decode(new Uint8Array(data.msg));
    //$('#otvet').val(per);
    $all_messages.append("<div >" + per + "</div>");
});
//game функции
var movement = {
    up: false,
    down: false,
    left: false,
    right: false
}
document.addEventListener('keydown', function (event) {
    switch (event.keyCode) {
        case 65: // A
            movement.left = true;
            break;
        case 87: // W
            movement.up = true;
            break;
        case 68: // D
            movement.right = true;
            break;
        case 83: // S
            movement.down = true;
            break;
    }
});
document.addEventListener('keyup', function (event) {
    switch (event.keyCode) {
        case 65: // A
            movement.left = false;
            break;
        case 87: // W
            movement.up = false;
            break;
        case 68: // D
            movement.right = false;
            break;
        case 83: // S
            movement.down = false;
            break;
    }
});
setInterval(function () {
   socket.emit('movement', movement);
}, 1000 / 60);

class Formate {
    string = "{}";
    regExp = /"?(\w+)"?\s*:\s*({.*?|".*?"|\w+)/ig;

    constructor(string) {
        this.string = string;
    }

    get validJSON() {
        return JSON.parse(this.string.replace(this.regExp, '"$1": $2'));
    }
}
//функция отрисовки значений в таблице очков
function updateTablePlayers() {
    var table = document.getElementById('table');
    var tr = document.createElement("tr");
    let isPositive=true;
    MapOfPlayers.forEach((value, key, map) => {
        for ( var i = 0; i < table.rows.length; i++ ) {
            for ( var j = 0; j < table.rows[0].cells.length; j++ ){
                if (table.rows[i].cells[0].innerHTML == key)
                    isPositive=false
            } 
        }
        if(isPositive==true){
            tr.innerHTML = `<td>${key}</td> <td>${value}</td>`;
            table.appendChild(tr); 
        }
        isPositive=true;
    });
}
function updateTableOchki() {
    var table = document.getElementById('table');
    var tr = document.createElement("tr");
    MapOfPlayers.forEach((value, key, map) => {
        for ( var i = 0; i < table.rows.length; i++ ) {
            for ( var j = 0; j < table.rows[0].cells.length; j++ ){
                if (table.rows[i].cells[0].innerHTML == key)
                table.rows[i].cells[1].innerHTML =  `<td>${value}</td>`            
            } 
        }
    });      
};
//обработка графики и state
var canvas = document.getElementById('canvas');
canvas.width = 800;
canvas.height = 600;
var context = canvas.getContext('2d');
socket.on('state', function (players) {
    context.clearRect(0, 0, 800, 600);
    var decoder = new TextDecoder("utf-8");
    var per = decoder.decode(new Uint8Array(players.msg));
    console.log("per:" + per);
    const value = new Formate(per);
    //console.log(value.validJSON);
    var obj = value.validJSON;
    //console.log(obj);
    prevNumberOfPlayers=MapOfPlayers.size;
    for (var key in obj) {
        if(key != "red" && key != "block")
        {
            MapOfPlayers.set(key,obj[key].ochki);
            if(MapOfPlayers.size != prevNumberOfPlayers){
                updateTablePlayers();
            }
            updateTableOchki();
            context.fillStyle = 'green';
            context.beginPath();
            context.arc(obj[key].x, obj[key].y, 10, 0, 2 * Math.PI);
            context.fill();
        }
        else if(key == "red"){
            context.fillStyle = 'red';
            for (var redt in obj[key]) {
                context.beginPath();
                context.arc(obj[key][redt].x, obj[key][redt].y, 5, 0, 2 * Math.PI);
                context.fill();
            }
        }
        else {
            context.fillStyle = 'blue';
            for (var redt in obj[key]) {
                context.beginPath();
                context.rect(obj[key][redt].x, obj[key][redt].y, 30, 30);
                context.fill();
            }
        }
    }
    //updateTable();
});