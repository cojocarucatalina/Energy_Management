import React, { useState } from 'react';

const ChatApp = () => {
  const [username, setUsername] = useState('');

  // Function to handle form submission
  const onUsernameSubmit = (event) => {
    event.preventDefault();  // Prevent the default form submission
    if (username.trim()) {
      // Proceed with your login logic or state change
      console.log(`Username submitted: ${username}`);
      // Maybe hide the username input and show the chat page
    } else {
      alert('Please enter a username!');
    }
  };

  // Function to handle username input change
  const onUsernameChange = (event) => {
    setUsername(event.target.value);
  };

  return (
    <div>
      <div id="username-page">
        <div className="username-page-container">
          <h1 className="title">Type your username to enter the Chatroom</h1>
          <form id="usernameForm" onSubmit={onUsernameSubmit}>
            <div className="form-group">
              <input
                type="text"
                id="name"
                placeholder="Username"
                value={username}
                onChange={onUsernameChange} // Set the username as the state
                className="form-control"
              />
            </div>
            <div className="form-group">
              <button type="submit" className="accent username-submit">Start Chatting</button>
            </div>
          </form>
        </div>
      </div>

      {/* Chat Page Content Goes Here */}
    </div>
  );
};

export default ChatApp;
