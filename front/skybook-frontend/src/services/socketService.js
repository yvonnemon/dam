// src/services/socketService.js
/*import SockJS from 'sockjs-client';
import Stomp from 'stompjs';

let stompClient = null;

export function connect(callback) {
  const socket = new SockJS('http://localhost:8080/ws');
  stompClient = Stomp.over(socket);

  stompClient.connect({}, () => {
    stompClient.subscribe('/topic/public', (message) => {
      callback(JSON.parse(message.body));
    });
  });
}

export function sendMessage(message) {
  stompClient.send('/app/chat.sendMessage', {}, JSON.stringify(message));
}
*/