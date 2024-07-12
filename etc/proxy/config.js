const express = require('express');
const fs = require('fs');

const {createProxyMiddleware} = require('http-proxy-middleware');


const app = express();

let text = fs.readFileSync(`config.json`);
let obj = JSON.parse(text);

obj.forEach(e => {
  app.use(e.path, createProxyMiddleware(
      {
        target: e.target,
        changeOrigin: true,
        logLevel: 'debug',
      }
  ));
})

app.listen(8080);

console.log('[PROXY] Server: listening on port 8080');