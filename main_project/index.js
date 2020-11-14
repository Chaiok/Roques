const PORT = process.env.PORT || 3000;
const express = require('express');
const app = express();
const server = require('http').createServer(app);
const path = require('path');
const io = require('socket.io')(server);
const ejs = require('ejs');
const net = require('net');

app.set('view engine', 'ejs');
app.use(express.static('public'));
app.get('/', function (req, res) {
    res.render('form');
});

var players = {};
var stateplayers = {};
io.on('connection', function (socket) {
    console.log('a user connected');
    var client = new net.Socket();
    /* //временно отключим сервер на клодже
    client.connect(3333, 'localhost', function () {
        console.log('Connected');
    })
    */
    socket.on('chat message', function (data) {
        console.log('message: ' + data);
        //client.write(data + "\n");
    });
    socket.on('disconnect', function () {
        console.log('Socket close');
        //client.destroy();

        //game функция
        delete players[socket.id];
        delete stateplayers[socket.id];
    })
    client.on('data', function (data) {
        console.log('Reciived: ' + data);
        //io.sockets.emit('add mess', { msg: data }); //отправляет сообщение всей группе сокетов
        socket.emit('add mess', { msg: data }); //отправляет сообщение одному сокету
    })
    client.on('close', function () {
        console.log('Connection cloce');
        socket.disconnect(true);
    })
    //game функции
    players[socket.id] = {
        x: 300,
        y: 300,
    };
    stateplayers[socket.id] = {
        up: false,
        down: false,
        left: false,
        right: false
    };
    socket.on('movement', function (data) {
        stateplayers[socket.id].up = data.up;
        stateplayers[socket.id].down = data.down;
        stateplayers[socket.id].left = data.left;
        stateplayers[socket.id].right = data.right;
    });
});
//передает всем в game данные игрокам
setInterval(function () {
    io.sockets.emit('state', players);
}, 1000 / 60);
//отвечает за движение
var lastUpdateTime = (new Date()).getTime();
setInterval(function () {
    var currentTime = (new Date()).getTime();
    var timeDifference = currentTime - lastUpdateTime;
    for (var id in players) {
        var player = players[id];
        var stateplayer = stateplayers[id];
        if (stateplayer.up)
            player.y -= 0.1 * timeDifference;
        if (stateplayer.down)
            player.y += 0.1 * timeDifference;
        if (stateplayer.left)
            player.x -= 0.1 * timeDifference;
        if (stateplayer.right)
            player.x += 0.1 * timeDifference;
    }
    lastUpdateTime = currentTime;
}, 1000 / 60);

server.listen(PORT, function (error) {
    if (error) {
        console.log("Wrong: " + error)
    }
    else {
        console.log("Server listening port: " + PORT)
    }
});