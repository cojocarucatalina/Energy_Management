import React from 'react';
import useWebSocket from './useWebSocket';

const ConsumptionNotificationPage = () => {
    const messages = useWebSocket('http://measurement:80/ws', '/topic/notification');

    return (
        <div>
            <h1>High Consumption Notifications</h1>
            <ul>
                {messages.map((msg, index) => (
                    <li key={index}>{msg}</li>
                ))}
            </ul>
        </div>
    );
};

export default ConsumptionNotificationPage;
