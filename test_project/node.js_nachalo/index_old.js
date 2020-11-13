//заготовки без экспресса
var PORT = process.env.PORT || 9000;
var http = require('http');
//sessions = require("sessions"), //пока не установленно
//handler = new sessions(); // memory store by default
var ejs = require('ejs');
var fs = require('fs');
//require("fs").readFile("./public/netprav.png", (err, image) => {
//    response.end(image);
//});
var qs = require('querystring');
//var cookie = require('cookie'); //пока не установленно
//const bcrypt = require('bcryptjs'); //пока не установленно
var client = require('nrepl-client')

//преобразуем данные с поста xhr(принимаем данные с клиента)
function processPost(request, response, callback) {
    var queryData = "";
    if (typeof callback !== 'function')
        return null;
    if (request.method == 'POST') {
        request.on('data', function (data) {
            queryData += data;
            if (queryData.length > 1e6) {
                queryData = "";
                response.writeHead(413, { 'Content-Type': 'text/plain' }).end();
                request.connection.destroy();
            }
        });
        request.on('end', function () {
            var name = 'content-type';
            var encoding = request.headers[name];
            if (encoding != undefined && encoding == "application/json") {
                var o = JSON.parse(queryData);
                o = JSON.parse(o);
                request.post = o;
                callback();
            }
            else {
                request.post = qs.parse(queryData);
                callback();
            }
        });
    } else {
        response.writeHead(405, { 'Content-Type': 'text/plain' });
        response.end();
    }
}
function trim(str) {
    return str.replace(/^\s+/g, "").replace(/\s+$/g, "");
};
function parseCookies(req) {
    var cookies = {};
    req.headers.cookie && req.headers.cookie.split(";").forEach(function (param) {
        var part = param.split("=", 2);
        cookies[trim(part[0].toLowerCase())] = trim(part[1]) || true;
    });
    return cookies;
}
function convertDate(inputFormat) {
    function pad(s) { return (s < 10) ? '0' + s : s; }
    var d = new Date(inputFormat);
    return [d.getFullYear(), pad(d.getMonth() + 1), pad(d.getDate())].join('-');
}

const server = http.createServer(function (request, response) {
    if (request.url === '/favicon.ico') {
        response.writeHead(200);
        response.end();
        return;
    }
    if (request.method == 'POST') {
        console.log("post");
        processPost(request, response, function () {
            console.log(request.post.biography);
        });
    }
    else {
        var htmlContent = fs.readFileSync(__dirname + '/views/form.ejs', 'utf8');
        var htmlRenderized = ejs.render(htmlContent, { filename: 'form.ejs', title: "Форма", form: "Форма", });
        response.writeHead(200, {
            "Content-Type": "text/html",
        });
        response.write(htmlRenderized);
        response.end();
    }
})

server.listen(PORT, function (error) {
    if (error) {
        console.log("Wrong: " + error)
    }
    else {
        console.log("Server listening port: " + PORT)
    }
});

users = [];
connections = [];
const io = require('socket.io')(server);
io.on('connection', client => {
    console.log("tutu");
    client.on('event', data => { console.log("tuta");/* … */ });
    client.on('disconnect', () => { /* … */ });
});