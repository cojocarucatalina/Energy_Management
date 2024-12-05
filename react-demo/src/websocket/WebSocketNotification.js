import SockJS from 'sockjs-client';
import Stomp from 'stompjs';

const WebSocketNotification = ({ deviceIds, onNotification }) => {
  const socketUrl = 'http://localhost:8082/ws';  
  
  const socket = new SockJS(socketUrl, null, { withCredentials: true });

  const stompClient = Stomp.over(socket);

  stompClient.connect({}, (frame) => {
    console.log('Connected: ' + frame);

    deviceIds.forEach((deviceId) => {
      stompClient.subscribe(`/topic/notification/${deviceId}`, (message) => {
        const notification = JSON.parse(message.body);
        onNotification(notification);
      });
    });
  });

  return null;
};

export default WebSocketNotification;

