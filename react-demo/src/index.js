// import React from 'react';
// import { createRoot } from 'react-dom/client';
// import './index.css';
// import * as serviceWorker from './serviceWorker';
// import App from './app'


// import 'bootstrap/dist/css/bootstrap.min.css';

// ReactDOM.render(
//     <App />,
//     document.getElementById('root')); 


// const root = ReactDOM.createRoot(document.getElementById('root'));
// root.render(
//   <React.StrictMode>
//     <App />
//   </React.StrictMode>
// );

// const rootElement = document.getElementById('root');
// if (rootElement) {
//   const root = createRoot(rootElement);  
//   root.render(
//     <React.StrictMode>
//       <App />
//     </React.StrictMode>
//   );
// } else {
//   console.error('Root element not found');
// }

// serviceWorker.register();
import React from 'react';
import { createRoot } from 'react-dom/client';
import './index.css';
import App from './app';

import 'bootstrap/dist/css/bootstrap.min.css';

const rootElement = document.getElementById('root');

if (rootElement) {
  const root = createRoot(rootElement);
  root.render(
    <React.StrictMode>
      <App />
    </React.StrictMode>
  );
} else {
  console.error('Root element not found');
}