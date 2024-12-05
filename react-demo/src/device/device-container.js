import React from 'react';
import APIResponseErrorMessage from "../commons/errorhandling/api-response-error-message";
import {
    Button,
    Card,
    CardHeader,
    Col,
    Modal,
    ModalBody,
    ModalHeader,
    Row
} from 'reactstrap';
import DeviceForm from "./components/device-form";  
import * as API_DEVICES from "./api/device-api";  
import DeviceTable from "./components/device-table"; 

class DeviceContainer extends React.Component {
    constructor(props) {
        super(props);
        this.toggleDeviceForm = this.toggleDeviceForm.bind(this);
        this.reload = this.reload.bind(this);
        this.state = {
            selectedDevice: false,
            deviceTableData: [],
            isDeviceLoaded: false,
            errorStatus: 0,
            error: null
        };
    }

    componentDidMount() {
        this.fetchDevices();
    }

    fetchDevices() {
        return API_DEVICES.getDevices((result, status, err) => {
            if (result !== null && status === 200) {
                const filteredData = result.map(({ id, address, userEmail, mhec, description }) => ({
                    id,
                    address,
                    userEmail,
                    mhec,
                    description 
                }));
                this.setState({
                    deviceTableData: filteredData,
                    isDeviceLoaded: true
                });
            } else {
                this.setState({
                    errorStatus: status,
                    error: err
                });
            }
        });
    }

    toggleDeviceForm() {
        this.setState({ selectedDevice: !this.state.selectedDevice });
    }

    reload() {
        this.setState({ isDeviceLoaded: false });
        this.toggleDeviceForm();
        this.fetchDevices();  
    }

    render() {
        return (
            <div>
                <CardHeader>
                    <strong>Device Management</strong>
                </CardHeader>
                <Card>
                    <br />
                    <Row>
                        <Col sm={{ size: '8', offset: 1 }}>
                            <Button color="info" onClick={this.toggleDeviceForm}>Add Device</Button>
                        </Col>
                    </Row>
                    <br />
                    <Row>
                        <Col sm={{ size: '8', offset: 1 }}>
                            {this.state.isDeviceLoaded && (
                                <DeviceTable tableData={this.state.deviceTableData} />
                            )}
                            {this.state.errorStatus > 0 && (
                                <APIResponseErrorMessage
                                    errorStatus={this.state.errorStatus}
                                    error={this.state.error}
                                />
                            )}
                        </Col>
                    </Row>
                </Card>

                <Modal isOpen={this.state.selectedDevice} toggle={this.toggleDeviceForm} className={this.props.className} size="lg">
                    <ModalHeader toggle={this.toggleDeviceForm}>Add Device:</ModalHeader>
                    <ModalBody>
                        <DeviceForm reloadHandler={this.reload} />
                    </ModalBody>
                </Modal>
            </div>
        );
    }
}

export default DeviceContainer;
