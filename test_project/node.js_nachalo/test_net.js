const net = require('net');

var client = new net.Socket();
client.connect(3333, 'localhost', function () {
    console.log('Connected');
})
client.on('data', function (data) {
    console.log('Reciived: ' + data);
    client.write("vasy\n", function (err) {
        if (err)
            console.log("error client weite:" + err);
        console.log("error client weite:" + err);
    });
})
client.on('close', function () {
    console.log('Connection cloce');
})
client.on('error', function () {
    console.log('УКККККККККК');
})

