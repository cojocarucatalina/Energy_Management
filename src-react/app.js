import React from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import MainNavigationBar from './admin-nav-bar';
import NavigationBar from './nav-bar-users';
import Home from './home/home';
import Login from './home/login';
import UserContainer from './user/user-container';
import DeviceContainer from './device/device-container';
import UserHomepage from './home/userHomepage';
import DevicesPage from './home/devicesPage';
import ErrorPage from './commons/errorhandling/error-page';
import ProtectedRoute from './protectedRoute';
import styles from './commons/styles/project-style.css';
import { AuthProvider, useAuth } from './AuthContext';
import ConsumptionNotificationPage from './ConsumptionNotificationPage';


const App = () => {
    const { role } = useAuth(); 
    return (
        <div className={styles.back}>
            {role === 'user' ? <NavigationBar /> : <MainNavigationBar />}
            <Routes>
                {/* neutral */}
                <Route path="/" element={<Navigate to="/login" replace />} />
                <Route path="/login" element={<Login />} />
                <Route path="/home" element={<Home />} />
                
                {/* user  */}
                <Route
                    path="/user"
                    element={
                        <ProtectedRoute allowedRoles={['user']}>
                            <UserHomepage />
                        </ProtectedRoute>
                    }
                />
                <Route
                    path="/user-devices"
                    element={
                        <ProtectedRoute allowedRoles={['user']}>
                            <DevicesPage />
                        </ProtectedRoute>
                    }
                />

                {/* admin  */}
                <Route
                    path="/admin"
                    element={
                        <ProtectedRoute allowedRoles={['admin']}>
                            <UserContainer />
                        </ProtectedRoute>
                    }
                />
                <Route
                    path="/devices"
                    element={
                        <ProtectedRoute allowedRoles={['admin']}>
                            <DeviceContainer />
                        </ProtectedRoute>
                    }
                />
                <Route
                    path="/users"
                    element={
                        <ProtectedRoute allowedRoles={['admin']}>
                            <UserContainer />
                        </ProtectedRoute>
                    }
                />

                {/* error  */}
                <Route path="/error" element={<ErrorPage />} />
                <Route path="*" element={<ErrorPage />} />


                <Route
                    path="/consumption-notifications"
                    element={
                        <ProtectedRoute allowedRoles={['admin', 'user']}>
                            <ConsumptionNotificationPage />
                        </ProtectedRoute>
                    }
                />
            </Routes>
        </div>
    );
};

export default () => (
    <AuthProvider>
        <Router>
            <App />
        </Router>
    </AuthProvider>
);
