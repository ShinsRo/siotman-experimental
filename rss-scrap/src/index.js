const http = require('http');

http.createServer((req, res) => {
  res.writeHead(200, {'Content-Type': 'text/html; charset=utf-8'});
  res.end('<h1>테스트 페이지</h1>');
}).listen(11211, () => {
  console.log('http://localhost:11211');
});