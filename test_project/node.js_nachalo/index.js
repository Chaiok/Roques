const PORT = process.env.PORT || 3000;
const app = require('express')();
const server = require('http').createServer(app);
const io = require('socket.io')(server);
const ejs = require('ejs');
const net = require('net');

app.set('view engine', 'ejs');
app.get('/', function (req, res) {
    res.render('form');
});

io.on('connection', function (socket) {
    console.log('a user connected');
    var client = new net.Socket();
    client.connect(3333, 'localhost', function () {
        console.log('Connected');
    })
    //console.log(socket);
    socket.on('chat message', function (data) {
        console.log('message: ' + data);
        //console.log(typeof data);
        client.write(data + "\n");
    });
    socket.on('disconnect', function () {
        console.log('Socket close');
        client.destroy();
    })
    client.on('data', function (data) {
        console.log('Reciived: ' + data);
        io.sockets.emit('add mess', { msg: data });
    })
    client.on('close', function () {
        console.log('Connection cloce');
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