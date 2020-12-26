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
//server
var client = new net.Socket();
client.connect(4444, 'localhost', function () {
    console.log('Connected to server');
})
client.on('data', function (data) {
    //console.log('Reciived server: ' + data);
    io.sockets.emit('state', { msg: data })
})
client.on('close', function () {
    console.log('Connection server cloce');
})
//players
var players = {};
var stateplayers = {};
io.on('connection', function (socket) {
    console.log('a user connected');

    var client = new net.Socket();

    client.connect(3333, 'localhost', function () {
        console.log('Connected');
    })
    client.on('data', function (data) {
        //console.log('Reciived: ' + data);
        //io.sockets.emit('add mess', { msg: data }); //отправляет сообщение всей группе сокетов
        socket.emit('add mess', { msg: data }); //отправляет сообщение одному сокету
    })
    client.on('close', function () {
        console.log('Connection cloce');
        socket.disconnect(true);
    })

    socket.on('chat message', function (data) {
        //console.log('message: ' + data);
        client.write(data + "\n");
    });
    socket.on('movement', function (data) {
        //console.log('message: ' + JSON.stringify(data));
        client.write( JSON.stringify(data) + "\n");
    });
    socket.on('disconnect', function () {
        console.log('Socket close');
        client.destroy();
    })
});

server.listen(PORT, function (error) {
    if (error) {
        console.log("Wrong: " + error)
    }
    else {
        console.log("Server listening port: " + PORT)
    }
});