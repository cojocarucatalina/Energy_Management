import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';  // Import useNavigate
import './main.css';  // Assuming the path is correct for your styles

const UsernamePage = ({ onUsernameSubmit }) => {
  const [username, setUsername] = useState('');
  const navigate = useNavigate();  // Initialize the useNavigate hook

  const handleSubmit = (event) => {
    event.preventDefault();  // Prevent the default form submission
    if (username.trim()) {
      onUsernameSubmit(username);  // Call the function passed from the parent
      navigate('/admin-chat');  // Replace '/chat' with the actual path for your chat page
    }
  };

  return (
    <div id="username-page">
      <div className="username-page-container">
        <h1 className="title">Type your username to enter the Chatroom</h1>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <input
              type="text"
              id="name"
              placeholder="Username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              autoComplete="off"
              className="form-control"
            />
          </div>
          <div className="form-group">
            <button type="submit" className="accent username-submit">Start Chatting</button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default UsernamePage;
