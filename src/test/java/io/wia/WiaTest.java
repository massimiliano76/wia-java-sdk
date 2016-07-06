package io.wia;

import io.wia.model.*;
import io.wia.net.*;
import io.wia.exception.*;

import junit.framework.Assert;

import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class WiaTest {
    private static Logger logger = LogManager.getLogger(WiaTest.class);

    static String getSecretKey() {
        return System.getProperty("testusersecretkey") != null ? System.getProperty("testusersecretkey") : System.getenv("WIA_TEST_USER_SECRET_KEY");
    }

    static String getDeviceSecretKey() {
        return System.getProperty("testdevicesecretkey") != null ? System.getProperty("testdevicesecretkey") : System.getenv("WIA_TEST_DEVICE_SECRET_KEY");
    }

    static String getRestApiBase() {
        return System.getenv("WIA_TEST_REST_API") != null ? System.getenv("WIA_TEST_REST_API") : "https://api.wia.io";
    }

    static Map<String, Object> getCreateDeviceParams() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "Test Device");
        return params;
    }

    @Test
    public void initTests() {
        BasicConfigurator.configure();
        logger.debug("Starting tests.");
        WiaClient.getInstance().overrideRestApiBase(getRestApiBase());
    }

    @Test
    public void testCreateDevice() throws WiaException {
        Wia.secretKey = getSecretKey();
        Device device = Device.create(getCreateDeviceParams());
        assertNotNull(device);
    }

    @Test
    public void testRetrieveDevice() throws WiaException {
        Wia.secretKey = getSecretKey();
        Device createdDevice = Device.create(getCreateDeviceParams());
        assertNotNull(createdDevice);
        Device retrievedDevice = Device.retrieve(createdDevice.getId());
        assertNotNull(retrievedDevice);
    }

    @Test
    public void testUpdateDevice() throws WiaException {
        Wia.secretKey = getSecretKey();

        Map<String, Object> createParams = new HashMap<String, Object>();
        createParams.put("name", "Old device name");

        Device createdDevice = Device.create(createParams);
        assertNotNull(createdDevice);

        Device retrievedDevice = Device.retrieve(createdDevice.getId());
        assertNotNull(retrievedDevice);

        Map<String, Object> updateParams = new HashMap<String, Object>();
        updateParams.put("name", "New device name");

        retrievedDevice.update(updateParams);
    }

    @Test
    public void testDeleteDevice() throws WiaException {
        Wia.secretKey = getSecretKey();

        Map<String, Object> createParams = new HashMap<String, Object>();
        createParams.put("name", "Device name");

        Device createdDevice = Device.create(createParams);
        assertNotNull(createdDevice);

        WiaDeletedObject deletedDevice = createdDevice.delete();
        assertEquals(createdDevice.getId(), deletedDevice.getId());
    }

    @Test
    public void testListDevices() throws WiaException {
        Wia.secretKey = getSecretKey();

        DeviceCollection devicesCollection = Device.list(null);
        System.out.println("Device count: " + devicesCollection.getCount());
        assertNotNull(devicesCollection);
    }

    @Test
    public void testListDevicesWithParams() throws WiaException {
        Wia.secretKey = getSecretKey();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("limit", 10);

        DeviceCollection devicesCollection = Device.list(params);
        System.out.println("Device count: " + devicesCollection.getCount());
        assertNotNull(devicesCollection);
    }

    @Test
    public void testPublishEvent() throws WiaException {
        Wia.secretKey = getDeviceSecretKey();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "testEvent");

        Event event = Event.publish(params);
        assertNotNull(event);
    }

    @Test
    public void testPublishEventWithData() throws WiaException {
        Wia.secretKey = getDeviceSecretKey();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "testEventNumber");
        params.put("data", 213.545);

        Event event = Event.publish(params);
        assertNotNull(event);
    }

    @Test
    public void testPublishEventWithDataObject() throws WiaException {
        Wia.secretKey = getDeviceSecretKey();

        Map<String, Object> dataObj = new HashMap<String, Object>();
        dataObj.put("x", 12.45);
        dataObj.put("y", 456.74);
        dataObj.put("z", 2.56);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "testEventObj");
        params.put("data", dataObj);

        Event event = Event.publish(params);
        assertNotNull(event);
    }

    @Test
    public void testListEvents() throws WiaException {
        Wia.secretKey = getSecretKey();

        Map<String, Object> createParams = new HashMap<String, Object>();
        createParams.put("name", "Device name");

        Device createdDevice = Device.create(createParams);
        assertNotNull(createdDevice);

        logger.debug("Getting events for device: " + createdDevice.getId());

        Map<String, Object> listParams = new HashMap<String, Object>();
        listParams.put("device", createdDevice.getId());

        EventCollection eventsCollection = Event.list(listParams);
        assertNotNull(eventsCollection);
        assertNotNull(eventsCollection.getEvents());
        assertNotNull(eventsCollection.getCount());
    }

    @Test
    public void testPublishLocation() throws WiaException {
        Wia.secretKey = getDeviceSecretKey();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("latitude", 40.7144);
        params.put("longitude", -74.006);

        Location location = Location.publish(params);
        assertNotNull(location);
    }

    @Test
    public void testListLocations() throws WiaException {
        Wia.secretKey = getSecretKey();

        Map<String, Object> createParams = new HashMap<String, Object>();
        createParams.put("name", "testListLocations");

        Device createdDevice = Device.create(createParams);
        assertNotNull(createdDevice);

        Map<String, Object> listParams = new HashMap<String, Object>();
        listParams.put("device", createdDevice.getId());

        LocationCollection locationsCollection = Location.list(listParams);
        assertNotNull(locationsCollection);
        assertNotNull(locationsCollection.getLocations());
        assertNotNull(locationsCollection.getCount());
    }
}