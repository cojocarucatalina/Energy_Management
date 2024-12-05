import { useState, useEffect } from 'react';

const useWebSocket = (url, topic) => {
    const [messages, setMessages] = useState([]);
    
    useEffect(() => {
        const socket = new WebSocket(url);
        
        socket.onopen = () => {
            console.log('WebSocket connected');
            socket.send(JSON.stringify({ topic }));
        };
        
        socket.onmessage = (event) => {
            const newMessages = [...messages, event.data];
            setMessages(newMessages);
        };
        
        socket.onerror = (error) => {
            console.error('WebSocket Error: ', error);
        };
        
        socket.onclose = () => {
            console.log('WebSocket disconnected');
        };

        return () => {
            socket.close();
        };
    }, [url, topic]);

    return messages;
};

export default useWebSocket;
