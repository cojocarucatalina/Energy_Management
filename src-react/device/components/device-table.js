import React, { useEffect, useState } from 'react';
import { Table, Button, Modal, ModalHeader, ModalBody, ModalFooter, Form, FormGroup, Label, Input } from 'reactstrap';
import axios from 'axios';

const DeviceTable = ({ tableData, onDeleteSuccess, onUpdateClick }) => {
    const [modal, setModal] = useState(false);
    const [currentDevice, setCurrentDevice] = useState({ id: '', description: '', address: '', userEmail: '', password: '', mhec: '' });
    const [userEmails, setUserEmails] = useState([]); 

    useEffect(() => {
        const fetchUserEmails = async () => {
            try {
                const response = await axios.get('http://user:80/user');
                const emails = response.data.map(user => user.email);
                setUserEmails(emails);
            } catch (error) {
                console.error('Error fetching user emails:', error);
            }
        };

        fetchUserEmails();
    }, []); 

    const toggle = () => setModal(!modal);

    const handleDelete = async (deviceId) => {
        try {
            await axios.delete(`http://device:80/device/${deviceId}`);
            onDeleteSuccess(deviceId);
        } catch (error) {
            console.error('Error deleting device:', error);
        }
    };

    const handleUpdateClick = (device) => {
        setCurrentDevice(device);
        toggle(); 
    };

    const handleSaveChanges = async (e) => {
        e.preventDefault();
        try {
            await axios.put(`http://device:80/device/${currentDevice.id}`, currentDevice);
            onUpdateClick(currentDevice); 
            toggle(); 
        } catch (error) {
            console.error('Error saving device:', error);
        }
    };

    return (
        <>
            <Table striped>
                <thead>
                    <tr>
                        <th>Description</th>
                        <th>User Email</th> 
                        <th>MHEC</th>
                        <th>Address</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {tableData.map(device => (
                        <tr key={device.id}>
                            <td>{device.description}</td>
                            <td>{device.userEmail}</td> 
                            <td>{device.mhec}</td>
                            <td>{device.address}</td>
                            <td>
                                <Button 
                                    color="info" 
                                    size="sm" 
                                    onClick={() => handleUpdateClick(device)} 
                                    style={{ marginRight: '5px' }}
                                >
                                    Update
                                </Button>
                                <Button 
                                    color="info" 
                                    size="sm" 
                                    onClick={() => handleDelete(device.id)} 
                                >
                                    Delete
                                </Button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </Table>

            <Modal isOpen={modal} toggle={toggle}>
                <ModalHeader toggle={toggle}>Update Device</ModalHeader>
                <ModalBody>
                    <Form onSubmit={handleSaveChanges}>
                        <FormGroup>
                            <Label for="description">Description</Label>
                            <Input 
                                type="text" 
                                id="description" 
                                value={currentDevice.description} 
                                onChange={(e) => setCurrentDevice({ ...currentDevice, description: e.target.value })} 
                            />
                        </FormGroup>
                        <FormGroup>
                            <Label for="address">Address</Label> 
                            <Input 
                                type="text" 
                                id="address" 
                                value={currentDevice.address} 
                                onChange={(e) => setCurrentDevice({ ...currentDevice, address: e.target.value })} 
                            />
                        </FormGroup>
                        <FormGroup>
                            <Label for="mhec">mhec</Label> 
                            <Input 
                                type="text" 
                                id="mhec" 
                                value={currentDevice.mhec} 
                                onChange={(e) => setCurrentDevice({ ...currentDevice, mhec: e.target.value })} 
                            />
                        </FormGroup>
                        <FormGroup>
                            <Label for="userEmail">Email</Label>
                            <Input 
                                type="select" 
                                id="userEmail" 
                                value={currentDevice.userEmail} 
                                onChange={(e) => setCurrentDevice({ ...currentDevice, userEmail: e.target.value })} 
                            >
                                <option value="" >Select Email</option>
                                {userEmails.map((userEmail, index) => (
                                    <option key={index} value={userEmail}>
                                        {userEmail}
                                    </option>
                                ))}
                            </Input>
                        </FormGroup>
                        <ModalFooter>
                            <Button color="info" type="submit" onClick={toggle}>Save Changes</Button>
                            <Button color="info" onClick={toggle}>Cancel</Button>
                        </ModalFooter>
                    </Form>
                </ModalBody>
            </Modal>
        </>
    );
};

export default DeviceTable;
