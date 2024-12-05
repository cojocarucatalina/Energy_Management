import React, { useState, useEffect } from 'react';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';

const WebSocketNotification = () => {
    const [notification, setNotification] = useState(null); 
    const [showPopup, setShowPopup] = useState(false); 

    useEffect(() => {
        const socket = new SockJS('http://measurement:80/ws'); 
        const stompClient = Stomp.over(socket);

        stompClient.connect({}, () => {
            console.log('WebSocket connected');
            stompClient.subscribe('/topic/notification', (message) => {
                const newNotification = JSON.parse(message.body);
                setNotification(newNotification.message); 
                setShowPopup(true); 

                setTimeout(() => {
                    setShowPopup(false);
                }, 2000); 
            });
        });

        return () => {
            stompClient.disconnect();
            console.log('WebSocket disconnected');
        };
    }, []);

    const closePopup = () => {
        setShowPopup(false); 
    };

    return (
        <div>
            {showPopup && notification && (
                <div style={styles.popup}>
                    <div style={styles.popupContent}>
                        <p>{notification}</p>
                        <button onClick={closePopup} style={styles.closeButton}>X</button>
                    </div>
                </div>
            )}
        </div>
    );
};


const styles = {
    popup: {
        position: 'fixed',
        top: '20%',
        left: '50%',
        transform: 'translate(-50%, -50%)', 
        backgroundColor: '#8a0060',
        color: '#fff',
        padding: '20px',
        borderRadius: '8px',
        boxShadow: '0 4px 10px rgba(0, 0, 0, 0.1)',
        zIndex: 1000,
        opacity: 1,
        transition: 'opacity 0.5s ease-in-out', 
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
    },
    popupContent: {
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
    },
    closeButton: {
        background: 'transparent',
        border: 'none',
        color: '#fff',
        fontSize: '10px',
        fontWeight: 'bold',
        cursor: 'pointer',
        position: 'absolute',
        top: '5px',
        right: '5px',
    }
};

export default WebSocketNotification;