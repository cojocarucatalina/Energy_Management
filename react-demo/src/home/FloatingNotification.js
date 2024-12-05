// import React, { useState, useEffect } from 'react';
// import './FloatingNotification.css';  // We'll create this CSS file for styling

// const FloatingNotification = ({ message, visible }) => {
//   const [isVisible, setIsVisible] = useState(visible);

//   useEffect(() => {
//     if (visible) {
//       setIsVisible(true);
//       const timer = setTimeout(() => {
//         setIsVisible(false);
//       }, 3000);  // Notification will disappear after 3 seconds
//       return () => clearTimeout(timer);  // Cleanup timeout on unmount
//     }
//   }, [visible]);

//   if (!isVisible) return null;  // Do not render if not visible

//   return (
//     <div className="floating-notification">
//       <p>{message}</p>
//     </div>
//   );
// };

// export default FloatingNotification;

import React, { useEffect } from 'react';
import './FloatingNotification.css';

const FloatingNotification = ({ message, visible }) => {
  if (!visible) return null;

  return (
    <div className="floating-notification">
      <p>{message}</p>
    </div>
  );
};

export default FloatingNotification;
