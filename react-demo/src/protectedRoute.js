import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from './AuthContext'; 

const ProtectedRoute = ({ children, allowedRoles }) => {
    const { role } = useAuth(); 

    if (!allowedRoles.includes(role)) {
        return <Navigate to="/error" replace />;
    }

    return children;
};

export default ProtectedRoute;
