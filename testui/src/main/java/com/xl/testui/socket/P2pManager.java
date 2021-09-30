package com.xl.testui.socket;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.net.wifi.p2p.nsd.WifiP2pUpnpServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pUpnpServiceRequest;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.Pair;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import androidx.annotation.NonNull;

import com.xl.testui.util.DeviceConfigUtil;

public class P2pManager {

    private static final String TAG = P2pManager.class.getSimpleName();

    public static final String THREAD_NAME = "NET_THREAD";
    public static final String DNSSD_INSTANCE_NAME = "QI_RUI";
    private static final int MSG_START_SERVICE = 1;
    private static final int MSG_SCAN_DEVICE = 2;
    private static final int MSG_STOP_SERVICE = 3;

    private Context mContext;
    private WifiP2pManager mWifiP2pManager;
    private WifiP2pManager.Channel mChannel;

    private HandlerThread mHandlerThread;
    private NetHandler mNetHandler;

    private WifiP2pDevice mWifiP2pDevice;

    private P2pInfoListener mP2pInfoListener;

    private WifiP2pManager.DnsSdTxtRecordListener mDnsSdTxtRecordListener = new WifiP2pManager.DnsSdTxtRecordListener() {
        @Override

        public void onDnsSdTxtRecordAvailable(
                String fullDomain, Map record, WifiP2pDevice device) {
            Log.d(TAG, "serviceAvailable onDnsSdTxtRecordAvailable() called with: fullDomain = [" + fullDomain + "], record = [" + record + "], device = [" + device + "]");
            if (fullDomain.contains(DNSSD_INSTANCE_NAME.toLowerCase(Locale.ROOT))){
                connectDevice(device);
            }
        }
    };
    private WifiP2pManager.DnsSdServiceResponseListener mDnsSdServiceResponseListener = new WifiP2pManager.DnsSdServiceResponseListener() {
        @Override
        public void onDnsSdServiceAvailable(String instanceName, String registrationType,
                                            WifiP2pDevice resourceType) {
            Log.d(TAG, "serviceAvailable onDnsSdServiceAvailable() called with: instanceName = [" + instanceName + "], registrationType = [" + registrationType + "], resourceType = [" + resourceType + "]");

        }
    };
    private WifiP2pManager.UpnpServiceResponseListener mUpnpServiceResponseListener = new WifiP2pManager.UpnpServiceResponseListener() {
        @Override
        public void onUpnpServiceAvailable(List<String> uniqueServiceNames, WifiP2pDevice srcDevice) {
            Log.d(TAG, "serviceAvailable onUpnpServiceAvailable() called with: uniqueServiceNames = [" + uniqueServiceNames + "], srcDevice = [" + srcDevice + "]");
        }
    };
    private WifiP2pManager.ServiceResponseListener mServiceResponseListener = new WifiP2pManager.ServiceResponseListener() {
        @Override
        public void onServiceAvailable(int protocolType, byte[] responseData, WifiP2pDevice srcDevice) {
            Log.d(TAG, "serviceAvailablev onServiceAvailable() called with: protocolType = [" + protocolType + "], responseData = [" + responseData + "], srcDevice = [" + srcDevice + "]");
        }
    };

    private P2pManager() {
    }

    private static class SingletonInstance {
        private static final P2pManager INSTANCE = new P2pManager();
    }

    public static P2pManager getInstance() {
        return SingletonInstance.INSTANCE;
    }


    public void init(Context context) {
        this.mContext = context;
        mHandlerThread = new HandlerThread(THREAD_NAME);
        mHandlerThread.start();
        mNetHandler = new NetHandler(mHandlerThread.getLooper());
        mWifiP2pManager = (WifiP2pManager) mContext.getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mWifiP2pManager.initialize(mContext, Looper.getMainLooper(), () -> {
            Log.d(TAG, "init() called with: context = [" + context + "]");
        });
        Log.d(TAG, "init() called with: mChannel = [" + mChannel + "]");
        if (mChannel == null) {
            mWifiP2pManager = null;
        }
    }

    public void registerP2pInfoListener(P2pInfoListener p2pInfoListener) {
        this.mP2pInfoListener = p2pInfoListener;
    }

    public void unregisterP2pInfoListener() {
        this.mP2pInfoListener = null;
    }

    /**
     * 启动服务
     */
    @SuppressLint("MissingPermission")
    public void startP2pService() {
        if (mWifiP2pManager == null) {
            return;
        }
        startDnssdService();
//        startUpnpService();
    }


    @SuppressLint("MissingPermission")
    private void startUpnpService() {
        String uuid = UUID.randomUUID().toString();
        Log.d(TAG, "startP2pService() called uuid:" + uuid);
        WifiP2pUpnpServiceInfo serviceInfoUpnp = WifiP2pUpnpServiceInfo.newInstance(uuid, "_ipp._tcp", new ArrayList<>());
        mWifiP2pManager.addLocalService(mChannel, serviceInfoUpnp, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "upnp addLocalService onSuccess() called");
            }

            @Override
            public void onFailure(int arg0) {
                Log.d(TAG, "upnp addLocalService onFailure() called with: arg0 = [" + arg0 + "]");
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void startDnssdService(){
        Map data = new HashMap();
        data.put("service_port", String.valueOf(37000));
        data.put("p2p_mac", DeviceConfigUtil.getP2pMac());

        WifiP2pDnsSdServiceInfo serviceInfoDnssd = WifiP2pDnsSdServiceInfo.newInstance(DNSSD_INSTANCE_NAME,"_ipp._tcp", data);
        mWifiP2pManager.addLocalService(mChannel, serviceInfoDnssd, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Dnssd addLocalService onSuccess() called");
            }

            @Override
            public void onFailure(int arg0) {
                Log.d(TAG, "Dnssd addLocalService onFailure() called with: arg0 = [" + arg0 + "]");
            }
        });
    }

    @SuppressLint("MissingPermission")
    public void discoverService() {
        discoverDnssdService();
//        discoverUpnpService();
//        mWifiP2pManager.setServiceResponseListener(mChannel,mServiceResponseListener);

    }

    private void discoverDnssdService(){
        mWifiP2pManager.setDnsSdResponseListeners(mChannel, mDnsSdServiceResponseListener, mDnsSdTxtRecordListener);
        WifiP2pDnsSdServiceRequest dnsSdServiceRequest = WifiP2pDnsSdServiceRequest.newInstance();
        mWifiP2pManager.addServiceRequest(mChannel, dnsSdServiceRequest, new WifiP2pManager.ActionListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onSuccess() {
                Log.d(TAG, "dnsSd addServiceRequest onSuccess() called");
                mWifiP2pManager.discoverServices(mChannel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "discoverServices onSuccess() called");
                    }

                    @Override
                    public void onFailure(int reason) {
                        Log.d(TAG, "discoverServicesonFailure() called with: reason = [" + reason + "]");
                        parseActionListenerOnFailure(reason);
                    }
                });
            }

            @Override
            public void onFailure(int reason) {
                Log.d(TAG, "dnsSd addServiceRequest onFailure() called with: reason = [" + reason + "]");
                parseActionListenerOnFailure(reason);
            }
        });
    }

    private void discoverUpnpService(){
        mWifiP2pManager.setUpnpServiceResponseListener(mChannel,mUpnpServiceResponseListener);
        WifiP2pUpnpServiceRequest upnpServiceRequest = WifiP2pUpnpServiceRequest.newInstance();
        mWifiP2pManager.addServiceRequest(mChannel, upnpServiceRequest, new WifiP2pManager.ActionListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onSuccess() {
                Log.d(TAG, "Upnp addServiceRequest onSuccess() called");
                mWifiP2pManager.discoverServices(mChannel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "discoverServices onSuccess() called");
                    }

                    @Override
                    public void onFailure(int reason) {
                        Log.d(TAG, "discoverServicesonFailure() called with: reason = [" + reason + "]");
                        parseActionListenerOnFailure(reason);
                    }
                });
            }

            @Override
            public void onFailure(int reason) {
                Log.d(TAG, "Upnp addServiceRequest onFailure() called with: reason = [" + reason + "]");
                parseActionListenerOnFailure(reason);
            }
        });
    }

    @SuppressLint("MissingPermission")
    public void createGroup() {
        if (mWifiP2pManager == null) {
            return;
        }
        mWifiP2pManager.createGroup(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "createGroup onSuccess() called");
                if (mP2pInfoListener != null) {
                    mP2pInfoListener.onCreateGroupResult(true);
                }
            }

            @Override
            public void onFailure(int reason) {
                Log.d(TAG, "createGroup onFailure() called with: reason = [" + reason + "]");
                parseActionListenerOnFailure(reason);
                if (mP2pInfoListener != null) {
                    mP2pInfoListener.onCreateGroupResult(false);
                }
            }
        });

    }

    /**
     * 扫描P2p设备
     */
    @SuppressLint("MissingPermission")
    public void discoverPeer() {

        mWifiP2pManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "discoverPeers onSuccess() called");
            }

            @Override
            public void onFailure(int reason) {
                Log.d(TAG, "discoverPeers onFailure() called with: reason = [" + reason + "]");
                parseActionListenerOnFailure(reason);
            }
        });
    }

    /**
     * 请求p2p设备信息
     */
    @SuppressLint("MissingPermission")
    public void requestPeers() {
        if (mWifiP2pManager == null) {
            return;
        }
        mWifiP2pManager.requestPeers(mChannel, new WifiP2pManager.PeerListListener() {
            @Override
            public void onPeersAvailable(WifiP2pDeviceList peers) {
                Log.d(TAG, "onPeersAvailable() called with: peers = [" + peers + "]");
                ArrayList<WifiP2pDevice> devices = new ArrayList<>();
                for (WifiP2pDevice device : peers.getDeviceList()) {
                    Log.d(TAG, "-------------------------------");
                    Log.d(TAG, "Device name:" + device.deviceName);
                    Log.d(TAG, "Device deviceAddress:" + device.deviceAddress);
                    Log.d(TAG, "Device primaryDeviceType:" + device.primaryDeviceType);
                    Log.d(TAG, "Device secondaryDeviceType:" + device.secondaryDeviceType);
                    Log.d(TAG, "Device status:" + device.status);
                    devices.add(device);
                }
                mP2pInfoListener.onDevicesUpdate(devices);
            }
        });
    }

    @SuppressLint("MissingPermission")
    public void connectDevice(WifiP2pDevice wifiP2pDevice) {
        Log.d(TAG, "connectDevice() called with: wifiP2pDevice = [" + wifiP2pDevice.deviceName + "]");
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = wifiP2pDevice.deviceAddress;
        config.wps.setup = WpsInfo.PBC;
        mWifiP2pManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "connectDevice onSuccess() called");
//                requestConnectedDeviceInfo();
                mWifiP2pManager.stopPeerDiscovery(mChannel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "stopPeerDiscovery onSuccess() called");
                    }

                    @Override
                    public void onFailure(int reason) {
                        Log.d(TAG, "stopPeerDiscovery onFailure() called with: reason = [" + reason + "]");
                        parseActionListenerOnFailure(reason);

                    }
                });
            }

            @Override
            public void onFailure(int reason) {
                Log.d(TAG, "connectDevice onFailure() called with: reason = [" + reason + "]");
                parseActionListenerOnFailure(reason);
            }
        });
    }

    @SuppressLint("MissingPermission")
    public void requestConnectedDeviceInfo() {
        Log.d(TAG, "getConnectedDeviceInfo() called");
        if (mWifiP2pManager == null) {
            return;
        }
        mWifiP2pManager.requestConnectionInfo(mChannel, new WifiP2pManager.ConnectionInfoListener() {
            @Override
            public void onConnectionInfoAvailable(WifiP2pInfo info) {
                Log.d(TAG, "onConnectionInfoAvailable() called with: info = [" + info + "]");
                try {
                    if (info.groupOwnerAddress == null) {
                        Log.e(TAG, "groupOwnerAddress is null");
                        return;
                    }
                    InetAddress address = InetAddress.getByName(info.groupOwnerAddress.getHostAddress());

                    if (address == null) {
                        Log.e(TAG, "address is null");
                        return;
                    }
                    Log.i(TAG, "address " + address.getHostAddress());
                    Log.d(TAG, "onConnectionInfoAvailable() called with: info.isGroupOwner = [" + info.isGroupOwner + "]");
                    if (info.isGroupOwner) {
                        mP2pInfoListener.onServiceConnected();
                    } else {
                        mP2pInfoListener.onClientConnect(address.getHostAddress(), Arrays.toString(address.getAddress()));
                    }
                } catch (Exception e) {
                    Log.w(TAG, e);
                }
            }
        });

    }

    @SuppressLint("MissingPermission")
    public void requestGroupInfo() {
        mWifiP2pManager.requestGroupInfo(mChannel, new WifiP2pManager.GroupInfoListener() {
            @Override
            public void onGroupInfoAvailable(WifiP2pGroup group) {
                Log.d(TAG, "onGroupInfoAvailable() called with: group = [" + group + "]");
                if (group == null) {
                    Log.e(TAG, "onGroupInfoAvailable() called with: group is null");
                    return;
                }
                List<Pair<String, String>> macAndDevicesName = new ArrayList<>();
                for (WifiP2pDevice device : group.getClientList()) {
                    Log.d(TAG, "onGroupInfoAvailable() called with: client device = [" + device.toString() + "] is group owner:" + device.isGroupOwner());
                    Log.d(TAG, "onGroupInfoAvailable() called with: client device deviceAddress = [" + device.deviceAddress + "]");
                    Pair<String, String> macAndDeviceName = new Pair<>(device.deviceAddress, device.deviceName);
                    macAndDevicesName.add(macAndDeviceName);
                }
                if (mP2pInfoListener != null) {
                    mP2pInfoListener.onClientUpdate(macAndDevicesName);
                }
            }
        });
    }

    public void cancelConnect() {
        mWifiP2pManager.cancelConnect(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "cancelConnect onSuccess() called");
            }

            @Override
            public void onFailure(int reason) {
                Log.d(TAG, "cancelConnect onFailure() called with: reason = [" + reason + "]");
                parseActionListenerOnFailure(reason);
            }
        });
    }

    public void stopService() {
        cancelConnect();
        mWifiP2pManager.removeGroup(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "removeGroup onSuccess() called");
            }

            @Override
            public void onFailure(int reason) {
                Log.d(TAG, "removeGroup onFailure() called with: reason = [" + reason + "]");
                parseActionListenerOnFailure(reason);
            }
        });
        mWifiP2pManager.clearLocalServices(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "clearLocalServices onSuccess() called");
            }

            @Override
            public void onFailure(int reason) {
                Log.d(TAG, "clearLocalServices onFailure() called with: reason = [" + reason + "]");
                parseActionListenerOnFailure(reason);
            }
        });
        mWifiP2pManager.clearServiceRequests(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "clearServiceRequests onSuccess() called");
            }

            @Override
            public void onFailure(int reason) {
                Log.d(TAG, "clearServiceRequests onFailure() called with: reason = [" + reason + "]");
                parseActionListenerOnFailure(reason);
            }
        });
    }

    private void parseActionListenerOnFailure(int reason) {
        switch (reason) {
            case WifiP2pManager.ERROR:
                Log.e(TAG, "failure error");
                break;
            case WifiP2pManager.P2P_UNSUPPORTED:
                Log.e(TAG, "failure p2p unsupported");
                break;
            case WifiP2pManager.BUSY:
                Log.e(TAG, "failure busy");
                break;
            default:
                Log.e(TAG, "failure unknown");
                break;
        }
    }


    static class NetHandler extends Handler {

        public NetHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.d(TAG, "handleMessage() called with: msg = [" + msg + "]");
        }
    }

}