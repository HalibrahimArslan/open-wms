/**
 * WMS bildirim websocket sunucusu (lokal gelistirme icin).
 *
 * Backend (WebSocketClientService) acilista buraya baglanir ve sayim/ERP
 * bildirimlerini duz metin olarak gonderir. Sunucu gelen her mesaji diger
 * tum istemcilere (UI) yayinlar.
 *
 * Ortam degiskenleri:
 *   PORT  - dinlenecek port (varsayilan 8080)
 *   PATH_ - websocket yolu   (varsayilan /websocket)
 */
const http = require('http');
const { WebSocketServer } = require('ws');

const PORT = parseInt(process.env.PORT || '8080', 10);
const WS_PATH = process.env.WS_PATH || '/websocket';

const server = http.createServer((req, res) => {
  if (req.url === '/health') {
    res.writeHead(200, { 'Content-Type': 'application/json' });
    res.end(JSON.stringify({ status: 'UP', clients: wss.clients.size }));
    return;
  }
  res.writeHead(404);
  res.end();
});

const wss = new WebSocketServer({ server, path: WS_PATH });

const stamp = () => new Date().toISOString();

wss.on('connection', (socket, req) => {
  const who = req.socket.remoteAddress;
  console.log(`[${stamp()}] baglandi: ${who} (toplam ${wss.clients.size})`);

  socket.on('message', data => {
    const text = data.toString();
    console.log(`[${stamp()}] mesaj: ${text}`);
    // Gonderen disindaki tum istemcilere yayinla
    for (const client of wss.clients) {
      if (client !== socket && client.readyState === client.OPEN) {
        client.send(text);
      }
    }
  });

  socket.on('close', () => {
    console.log(`[${stamp()}] ayrildi: ${who} (kalan ${wss.clients.size})`);
  });

  socket.on('error', err => {
    console.error(`[${stamp()}] soket hatasi: ${err.message}`);
  });
});

server.listen(PORT, () => {
  console.log(`[${stamp()}] websocket sunucusu hazir: ws://0.0.0.0:${PORT}${WS_PATH}`);
});
