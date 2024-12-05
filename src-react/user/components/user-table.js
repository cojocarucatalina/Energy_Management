// src/user/components/UserTable.js
import React, { useState } from 'react';
import { Table, Button, Modal, ModalHeader, ModalBody, ModalFooter, Form, FormGroup, Label, Input } from 'reactstrap';
import axios from 'axios';

const UserTable = ({ tableData, onDeleteSuccess, onUpdateClick }) => {
    const [modal, setModal] = useState(false);
    const [currentUser, setCurrentUser] = useState({ id: '', name: '', email: '', password: '' });
    const [originalEmail, setOriginalEmail] = useState('');
    const toggle = () => setModal(!modal);

    const handleDelete = async (user) => {
        try {
            await axios.delete(`http://user:80/user/${user.id}`);
            const encodedEmail = encodeURIComponent(user.email);
            await axios.delete(`http://device:80/device/delete/${encodedEmail}`);
            onDeleteSuccess(user.id);
        } catch (error) {
            console.error('Error deleting user:', error);
        }
    };
    
    const refreshUsers = async () => {
        try {
            const response = await axios.get('http://user:80/user'); 
        } catch (error) {
            console.error('Error fetching users:', error);
        }
    };
    
    const handleUpdateClick = (user) => {
        setCurrentUser(user);
        setOriginalEmail(user.email);  // aici
        toggle(); 
    };

    const handleSaveChanges = async (e) => {
        e.preventDefault();
        try {
            await axios.put(`http://user:80/user/${currentUser.id}`, currentUser);
            if (originalEmail !== currentUser.email) {
                await axios.put(
                    'http://device:80/device/updateEmail',
                    {
                        oldEmail: originalEmail,
                        newEmail: currentUser.email
                    }
                );
            }
            
            onUpdateClick(currentUser); 
            toggle(); 
        } catch (error) {
            console.error('Error saving user:', error);
        }
    };

    return (
        <>
            <Table striped>
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {tableData.map(user => (
                        <tr key={user.id}>
                            <td>{user.name}</td>
                            <td>{user.email}</td>
                            <td>
                                <Button 
                                    color="info" 
                                    size="sm" 
                                    onClick={() => handleUpdateClick(user)} 
                                    style={{ marginRight: '5px' }}
                                >
                                    Update
                                </Button>
                                <Button 
                                    color="info" 
                                    size="sm" 
                                    onClick={() => handleDelete(user)} 
                                >
                                    Delete
                                </Button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </Table>

            <Modal isOpen={modal} toggle={toggle}>
                <ModalHeader toggle={toggle}>Update User</ModalHeader>
                <ModalBody>
                    <Form onSubmit={handleSaveChanges}>
                        <FormGroup>
                            <Label for="name">Name</Label>
                            <Input 
                                type="text" 
                                id="name" 
                                value={currentUser.name} 
                                onChange={(e) => setCurrentUser({ ...currentUser, name: e.target.value })} 
                                required
                            />
                        </FormGroup>
                        <FormGroup>
                            <Label for="email">Email</Label>
                            <Input 
                                type="email" 
                                id="email" 
                                value={currentUser.email} 
                                onChange={(e) => setCurrentUser({ ...currentUser, email: e.target.value })} 
                                required
                            />
                        </FormGroup>
                        <FormGroup>
                            <Label for="password">Password</Label>
                            <Input 
                                type="password" 
                                id="password" 
                                value={currentUser.password} 
                                onChange={(e) => setCurrentUser({ ...currentUser, password: e.target.value })} 
                            />
                        </FormGroup>
                        <ModalFooter>
                            <Button color="primary" type="submit" onClick={toggle}>Save Changes</Button>
                            <Button color="secondary" onClick={toggle}>Cancel</Button>
                        </ModalFooter>
                    </Form>
                </ModalBody>
            </Modal>
        </>
    );
};

export default UserTable;
