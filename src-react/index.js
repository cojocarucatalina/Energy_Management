import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import * as serviceWorker from './serviceWorker';
import App from './app'


import 'bootstrap/dist/css/bootstrap.min.css';

// ReactDOM.render(
//     <App />,
//     document.getElementById('root')); 


const rootElement = document.getElementById('root');
const root = ReactDOM.createRoot(rootElement); // Correct usage of createRoot
root.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: http://bit.ly/CRA-PWA
serviceWorker.register();
//serviceWorker.unregister();
