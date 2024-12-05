import { HOST } from '../../commons/hosts'; 
import RestApiClient from "../../commons/api/rest-client";

const endpoint = {
    device: 'http://device:80/device' 
};

function getDevices(callback) {
    let request = new Request(endpoint.device, {  
        method: 'GET',
    });
    console.log(request.url);
    RestApiClient.performRequest(request, callback);
}

function getDeviceById(params, callback) {
    let request = new Request(`${endpoint.device}/${params.id}`, {  
        method: 'GET',
    });
    console.log(request.url);
    RestApiClient.performRequest(request, callback);
}

function postDevice(device, callback) {
    let request = new Request(endpoint.device, {  
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(device),
    });

    console.log("URL: " + request.url);
    RestApiClient.performRequest(request, callback);
}

export {
    getDevices,
    getDeviceById,
    postDevice,
};
