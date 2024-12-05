import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import './fonts.css';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { Line, Bar } from 'react-chartjs-2';
import 'chart.js/auto';
import WebSocketNotification from '../websocket/WebSocketNotification'; 
import FloatingNotification from './FloatingNotification'; 

const DevicesPage = () => {
  const [devices, setDevices] = useState([]);
  const [errorMessage, setErrorMessage] = useState('');
  const [newDevice, setNewDevice] = useState({ description: '', address: '', mhec: '', userEmail: '' });
  const [isFormVisible, setIsFormVisible] = useState(false);
  const [selectedDate, setSelectedDate] = useState(null);
  const [deviceCharts, setDeviceCharts] = useState({});
  const [chartType, setChartType] = useState('line');
  const [selectedDeviceId, setSelectedDeviceId] = useState(null);
  const [isEditing, setIsEditing] = useState(false); 
  const [notificationMessage, setNotificationMessage] = useState(''); 
  const [isNotificationVisible, setIsNotificationVisible] = useState(false); 
  const location = useLocation();
  const { email } = location.state || {};

  const CONSUMPTION_THRESHOLD = 100;

  useEffect(() => {
    if (email) {
      fetchDevices(email);
    } else {
      setErrorMessage('No email provided. Please log in again.');
    }
  }, [email]);

  const fetchDevices = async (email) => {
    try {
      const response = await fetch('http://device:80/device/user-devices', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email }),
      });

      if (response.ok) {
        const data = await response.json();
        setDevices(data);
      } else {
        setErrorMessage('Failed to fetch devices. Please try again.');
      }
    } catch (error) {
      console.error('Error fetching devices:', error);
      setErrorMessage('An unexpected error occurred. Please try again.');
    }
  };

  const fetchEnergyDataForDevice = async (deviceId, date) => {
    try {
      const formattedDate = date.toISOString().split('T')[0];
      const response = await fetch(`http://measurement:80/measurement/from-date?startDate=${formattedDate}&device_id=${deviceId}`);
      if (response.ok) {
        const data = await response.json();

        const hours = Array.from({ length: 24 }, (_, i) => i);
        const consumptionPerHour = Array(24).fill(0);
        let totalConsumption = 0;  

        data.forEach((entry) => {
          const timestamp = new Date(entry.timestamp);
          const hour = timestamp.getHours();
          consumptionPerHour[hour] += entry.consum;
          totalConsumption += entry.consum; 
        });

        if (totalConsumption > CONSUMPTION_THRESHOLD) {
          setNotificationMessage(`Energy consumption for device ${deviceId} has exceeded the threshold! Total: ${totalConsumption} kWh`);
          setIsNotificationVisible(true); 

          setTimeout(() => {
            setIsNotificationVisible(false);
          }, 3000);
        }

        setDeviceCharts((prev) => ({
          ...prev,
          [deviceId]: {
            labels: hours,
            datasets: [
              {
                label: `Energy Consumption (kWh) for Device ${devices.find(device => device.id === deviceId)?.description}`,
                data: consumptionPerHour,
                borderColor: 'blue',
                backgroundColor: 'rgba(0, 0, 255, 0.1)',
                borderWidth: 2,
                fill: chartType === 'line',
              },
            ],
          },
        }));
      } else {
        setErrorMessage(`Failed to fetch energy data for device ${deviceId}. Please try again.`);
      }
    } catch (error) {
      console.error(`Error fetching energy data for device ${deviceId}:`, error);
      setErrorMessage('An unexpected error occurred while fetching energy data. Please try again.');
    }
  };

  const handleEditDevice = (device) => {
    setIsFormVisible(true);
    setNewDevice(device);
    setIsEditing(true); 
  };

  const handleDeleteDevice = async (deviceId) => {
    try {
      const response = await fetch(`http://device:80/device/${deviceId}`, {
        method: 'DELETE',
      });

      if (response.ok) {
        fetchDevices(email);
      } else {
        setErrorMessage('Failed to delete device. Please try again.');
      }
    } catch (error) {
      console.error('Error deleting device:', error);
      setErrorMessage('An unexpected error occurred while deleting the device. Please try again.');
    }
  };

  const handleCreateOrUpdateDevice = async (event) => {
    event.preventDefault();

    const url = isEditing
      ? `http://device:80/device/${newDevice.id}`  
      : 'http://device:80/device';  

    const method = isEditing ? 'PUT' : 'POST'; 

    try {
      const response = await fetch(url, {
        method,
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ ...newDevice, userEmail: email }),
      });

      if (response.ok) {
        setNewDevice({ description: '', address: '', mhec: '', userEmail: '' });
        fetchDevices(email);
        setIsFormVisible(false);
        setIsEditing(false); 
      } else {
        setErrorMessage('Failed to save device. Please try again.');
      }
    } catch (error) {
      console.error('Error saving device:', error);
      setErrorMessage('An unexpected error occurred while saving the device. Please try again.');
    }
  };

  const handleDateChange = (date, deviceId) => {
    setSelectedDate(date);
    setSelectedDeviceId(deviceId); 
    fetchEnergyDataForDevice(deviceId, date);
  };

  const handleFormClose = () => {
    setIsFormVisible(false);
    setNewDevice({ description: '', address: '', mhec: '', userEmail: '' });
    setIsEditing(false); 
  };

  return (
    <div className="container">
      {/* <WebSocketNotification />  */}
      <WebSocketNotification deviceIds={devices.map(device => device.id)} />

      
      <h2>Your Devices</h2>
      {errorMessage && <div className="error">{errorMessage}</div>}
      
      <button
        onClick={() => {
          setIsFormVisible(true);
          setNewDevice({ description: '', address: '', mhec: '', userEmail: '' });
          setIsEditing(false);
        }}
        className="toggle-button"
      >
        Add Device
      </button>

      {isFormVisible && (
        <form onSubmit={handleCreateOrUpdateDevice} className="form">
          <input
            type="text"
            placeholder="Description"
            value={newDevice.description}
            onChange={(e) => setNewDevice({ ...newDevice, description: e.target.value })}
            required
            className="input"
          />
          <input
            type="text"
            placeholder="Address"
            value={newDevice.address}
            onChange={(e) => setNewDevice({ ...newDevice, address: e.target.value })}
            required
            className="input"
          />
          <input
            type="number"
            placeholder="MHEC"
            value={newDevice.mhec}
            onChange={(e) => setNewDevice({ ...newDevice, mhec: e.target.value })}
            required
            className="input"
          />
          <div className="form-actions">
            <button type="submit" className="submit-button">{isEditing ? 'Update Device' : 'Add Device'}</button>
            <button type="button" onClick={handleFormClose} className="cancel-button">Cancel</button>
          </div>
        </form>
      )}

      {devices.length > 0 ? (
        <div>
          <table className="table">
            <thead>
              <tr>
                <th>Description</th>
                <th>Address</th>
                <th>MHEC</th>
                <th>User Email</th>
                <th>Actions</th>
                <th>Select Date</th>
              </tr>
            </thead>
            <tbody>
              {devices.map((device) => (
                <tr key={device.id}>
                  <td>{device.description}</td>
                  <td>{device.address}</td>
                  <td>{device.mhec}</td>
                  <td>{device.userEmail}</td>
                  <td>
                    <button onClick={() => handleEditDevice(device)} className="action-button">Edit</button>
                    <button onClick={() => handleDeleteDevice(device.id)} className="action-button">Delete</button>
                  </td>
                  <td>
                    <DatePicker
                      selected={selectedDate}
                      onChange={(date) => handleDateChange(date, device.id)}
                      dateFormat="yyyy-MM-dd"
                      className="date-picker"
                    />
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          {selectedDeviceId && selectedDate && deviceCharts[selectedDeviceId] && (
            <div className="chart-container">
              {chartType === 'line' ? (
                <Line data={deviceCharts[selectedDeviceId]} options={{ responsive: true }} />
              ) : (
                <Bar data={deviceCharts[selectedDeviceId]} options={{ responsive: true }} />
              )}
              <div className="chart-toggle">
                <button onClick={() => setChartType('line')}>Line Chart</button>
                <button onClick={() => setChartType('bar')}>Bar Chart</button>
              </div>
            </div>
          )}
        </div>
      ) : (
        <p>No devices available. Please add a device.</p>
      )}

      <FloatingNotification message={notificationMessage} visible={isNotificationVisible} />
    </div>
  );
};

export default DevicesPage;