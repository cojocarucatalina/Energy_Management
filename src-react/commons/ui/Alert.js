import React from 'react';

const Alert = ({ children, className = '' }) => {
  return (
    <div className={`alert ${className}`}>
      {children}
    </div>
  );
};

const AlertTitle = ({ children }) => {
  return (
    <div className="alert-title font-bold">
      {children}
    </div>
  );
};

const AlertDescription = ({ children }) => {
  return (
    <div className="alert-description text-sm">
      {children}
    </div>
  );
};

export { Alert, AlertTitle, AlertDescription };
