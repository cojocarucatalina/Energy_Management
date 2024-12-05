import React, { useEffect, useState } from 'react';
import useWebSocket from './websocket/useWebSocket';
import { Bell, Clock, AlertTriangle } from 'lucide-react';

const PopupNotification = ({ message, onClose }) => {
  return (
    <div className="fixed top-5 right-5 bg-orange-500 text-white rounded-lg shadow-lg min-w-[300px] max-w-[400px] z-50 overflow-hidden animate-fade-in">
      <div className="p-4">
        <button 
          onClick={onClose}
          className="absolute right-2 top-2 text-white hover:text-gray-200 transition-colors"
        >
          <span className="text-xl">×</span>
        </button>
        
        <div className="flex items-center space-x-2 mb-2">
          <AlertTriangle className="h-5 w-5" />
          <span className="font-semibold">High Consumption Alert</span>
        </div>
        
        <p className="text-sm">{message}</p>
      </div>
      <div className="bg-orange-600 px-4 py-2 text-xs">
        Click to dismiss
      </div>
    </div>
  );
};

const NotificationHistory = ({ notifications }) => {
  return (
    <div className="mt-8 bg-white rounded-lg shadow-md">
      <div className="p-4 border-b border-gray-200">
        <div className="flex items-center space-x-2">
          <Clock className="h-5 w-5 text-gray-500" />
          <h2 className="text-lg font-semibold text-gray-700">Notification History</h2>
        </div>
      </div>
      
      <div className="divide-y divide-gray-200">
        {notifications.length === 0 ? (
          <div className="p-4 text-center text-gray-500">
            No notifications yet
          </div>
        ) : (
          notifications.map((notification, index) => (
            <div key={index} className="p-4 hover:bg-gray-50 transition-colors">
              <div className="flex items-start space-x-3">
                <Bell className="h-5 w-5 text-orange-500 mt-1" />
                <div>
                  <p className="text-sm text-gray-600">{notification}</p>
                  <span className="text-xs text-gray-400">
                    {new Date().toLocaleTimeString()}
                  </span>
                </div>
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
};

const ConsumptionNotificationPage = () => {
  const messages = useWebSocket('http://measurement:80/ws', '/topic/notification');
  const [activeNotification, setActiveNotification] = useState(null);
  const [notificationHistory, setNotificationHistory] = useState([]);
  const [isConnected, setIsConnected] = useState(false);

  // Request permission for push notifications
  const requestPushPermission = async () => {
    const permission = await Notification.requestPermission();
    if (permission === 'granted') {
      console.log('Push notification permission granted');
    }
  };

  const showPushNotification = (message) => {
    if ('Notification' in window && Notification.permission === 'granted') {
      new Notification('High Consumption Alert', {
        body: message,
        icon: '/icon.png',
      });
    }
  };

  useEffect(() => {
    requestPushPermission();

    if (messages.length > 0) {
      const latestMessage = messages[messages.length - 1];
      
      // Set active notification
      setActiveNotification(latestMessage);
      
      // Add to history
      setNotificationHistory(prev => [latestMessage, ...prev].slice(0, 50)); // Keep last 50 notifications

      // Show push notification
      showPushNotification(latestMessage);

      // Auto-dismiss after 5 seconds
      const timer = setTimeout(() => {
        setActiveNotification(null);
      }, 5000);

      return () => clearTimeout(timer);
    }
  }, [messages]);

  useEffect(() => {
    // Simulate WebSocket connection status
    setIsConnected(true);
    return () => setIsConnected(false);
  }, []);

  return (
    <div className="max-w-4xl mx-auto p-6">
      {/* Connection Status */}
      <div className="flex items-center space-x-2 mb-4">
        <div className={`h-2 w-2 rounded-full ${isConnected ? 'bg-green-500' : 'bg-red-500'}`} />
        <span className="text-sm text-gray-600">
          {isConnected ? 'Connected to notification service' : 'Disconnected'}
        </span>
      </div>

      {/* Page Header */}
      <div className="bg-white rounded-lg shadow-md p-6 mb-6">
        <div className="flex items-center space-x-3">
          <Bell className="h-6 w-6 text-orange-500" />
          <h1 className="text-2xl font-bold text-gray-800">
            Consumption Notifications
          </h1>
        </div>
        <p className="mt-2 text-gray-600">
          Monitor your device's energy consumption in real-time. You'll receive alerts when consumption exceeds normal levels.
        </p>
      </div>

      {/* Active Notification */}
      {activeNotification && (
        <PopupNotification 
          message={activeNotification} 
          onClose={() => setActiveNotification(null)}
        />
      )}

      {/* Notification History */}
      <NotificationHistory notifications={notificationHistory} />
    </div>
  );
};

export default ConsumptionNotificationPage;
