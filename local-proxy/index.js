const net = require('net');
const server = net.createServer();

server.on("error", (err) => {
    console.log("Error");
    console.log(err);
});

server.on("close", () => {
    console.log("Client disconnected");
});

server.listen({
    host: "0.0.0.0",
    port: 9898
}, () => {
    console.log("Server listening on 0.0.0.0:9898")
});

server.on("connection", (clientToProxySoc) => {
    console.log("connected");
    clientToProxySoc.once("data", (data) => {
        let dataStr = data.toString();
        let isTLSConn = dataStr.indexOf("CONNECT") !== -1;

        let serverPort = 80;
        let serverAddr;

        if (isTLSConn) {
            serverPort = 443;
            serverAddr = dataStr.substring("CONNECT".length + 1, dataStr.indexOf(":"));
        } else {
            serverAddr = dataStr.split("Host: ")[1].split("\n")[0];
        }

        let proxyToServSoc = net.createConnection({
            host: serverAddr,
            port: serverPort
        }, () => {
            console.log("Proxy to Set Up : " + serverAddr);
        });

        if (isTLSConn) {
            clientToProxySoc.write("HTTP/1.1 200 OK\r\n\n");
        } else {
            proxyToServSoc.write(data);
        }

        clientToProxySoc.pipe(proxyToServSoc);
        proxyToServSoc.pipe(clientToProxySoc);

        clientToProxySoc.on("error", (err) => {
            console.log("Client to proxy error");
            console.log(err);
        });

        proxyToServSoc.on("error", (err) => {
            console.log("Proxy to server error");
            console.log(err);
        });
    });

    clientToProxySoc.on("data", (data) => {
        console.log(data)
    })
});
