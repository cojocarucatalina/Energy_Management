import React, { useEffect, useState } from 'react';
import { Form, FormGroup, Label, Input, Button } from 'reactstrap';
import axios from 'axios'; 

const DeviceForm = ({ reloadHandler }) => {

    const [userEmails, setUserEmails] = useState([]); 

    const [formState, setFormState] = useState({
        description: '',
        address: '',
        userEmail: '',
        mhec: '',
    });

    useEffect(() => {
        const fetchUserEmails = async () => {
            try {
                const response = await axios.get('http://user:80/user');
                const emails = response.data.map(user => user.email);
                console.log('Fetched user emails:', emails); // Debug log
                setUserEmails(emails);
            } catch (error) {
                console.error('Error fetching user emails:', error);
            }
        };
    
        fetchUserEmails();
    }, []);
    

    const handleChange = (event) => {
        const { id, value } = event.target;
        setFormState({
            ...formState,
            [id]: value,
        });
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        const newDevice = {
            description: formState.description,
            address: formState.address,
            userEmail: formState.userEmail,
            mhec: formState.mhec,
        };

        try {
            const response = await axios.post('http://device:80/device', newDevice);
            console.log('Device created successfully:', response.data);
            
            setFormState({
                description: '',
                address: '',
                userEmail: '',
                mhec: '',
            });

            reloadHandler();
        } catch (error) {
            console.error('Error creating device:', error);
        }
    };

    return (
        <Form onSubmit={handleSubmit}>
            <FormGroup>
                <Label for="description">Description</Label>
                <Input
                    type="text"
                    id="description"
                    value={formState.description}
                    onChange={handleChange}
                    placeholder="Enter device description..."
                    required
                />
            </FormGroup>
            <FormGroup>
                <Label for="address">Address</Label>
                <Input
                    type="text"
                    id="address"
                    value={formState.address}
                    onChange={handleChange}
                    placeholder="Enter device address..."
                    required
                />
            </FormGroup>
            <FormGroup>
                <Label for="mhec">Mhec</Label>
                <Input
                    type="number"
                    id="mhec"
                    value={formState.mhec}
                    onChange={handleChange}
                    placeholder="Enter device mhec..."
                    required
                />
            </FormGroup>
            {/* <FormGroup>
                <Label for="userEmail">User Email</Label>
                <Input
                    type="email"
                    id="userEmail"
                    value={formState.userEmail}
                    onChange={handleChange}
                    placeholder="Enter the user's email..."
                    required
                />
            </FormGroup> */}
            <FormGroup>
        <Label for="userEmail">Email</Label>
        <Input 
            type="select" 
            id="userEmail" 
            value={formState.userEmail} 
            onChange={(e) => {
                console.log('Selected email:', e.target.value); 
                setFormState({ ...formState, userEmail: e.target.value });
            }} 
        >
            <option value="">Select Email</option>
            {userEmails.map((userEmail, index) => (
                <option key={index} value={userEmail}>
                    {userEmail}
                </option>
            ))}
        </Input>
    </FormGroup>
            <Button color="info" type="submit">Add Device</Button>
        </Form>
    );
};

export default DeviceForm;
