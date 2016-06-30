package io.wia;

import io.wia.model.*;
import io.wia.net.*;
import io.wia.exception.*;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class WiaTest {

    static String getSecretKey() {
        return System.getProperty("testusersecretkey") != null ? System.getProperty("testusersecretkey") : System.getenv("WIA_TEST_USER_SECRET_KEY");
    }

    static String getRestApiBase() {
        return "https://api.wia.io";
    }

    static Map<String, Object> getCreateDeviceParams() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "Test Device");
        return params;
    }

    @Test
    public void initTests() {
        WiaClient.getInstance().overrideRestApiBase(getRestApiBase());
    }

    @Test
    public void testCreateDevice() throws WiaException {
        System.out.println("Running test testCreateDevice");
        WiaClient.getInstance().setSecretKey(getSecretKey());
        Device device = WiaClient.getInstance().createDevice(getCreateDeviceParams());
        assertNotNull(device);
    }

    @Test
    public void testRetrieveDevice() throws WiaException {
        WiaClient.getInstance().setSecretKey(getSecretKey());
        Device createdDevice = WiaClient.getInstance().createDevice(getCreateDeviceParams());
        assertNotNull(createdDevice);
        Device retrievedDevice = WiaClient.getInstance().retrieveDevice(createdDevice.getId());
        assertNotNull(retrievedDevice);
    }

    @Test
    public void listDevices() throws WiaException {
        WiaClient.getInstance().setSecretKey(getSecretKey());
        DeviceCollection devicesCollection = WiaClient.getInstance().listDevices(null);
        System.out.println("Device count: " + devicesCollection.getCount());
        if (devicesCollection.getDevices() != null) {
            for (Device d : devicesCollection.getDevices()) {
                System.out.println(d.getId());
            }
        }
        assertNotNull(devicesCollection);
    }
}