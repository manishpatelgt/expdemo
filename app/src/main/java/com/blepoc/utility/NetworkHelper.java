package com.blepoc.utility;

/**
 * Created by Manish Patel on 1/10/2019.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Class working with network connectivity
 */

public class NetworkHelper {


    public static final String GOOGLE_COM_URL = "http://www.google.com";

    // Timeouts
    private static final int NETWORK_CONNECTION_POOL_TIMEOUT = 1000;
    private static final int NETWORK_CONNECTION_CONNECT_TIMEOUT = 10000;        // 10 sec
    private static final int NETWORK_CONNECTION_READ_TIMEOUT = 10000;
    private static final int NETWORK_SOCKET_CONNECT_TIMEOUT = 10000;

    /**
     * Network types
     */
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;


    private NetworkHelper() {
    }

    public static boolean connectedToWiFi(Context context) {
        NetworkInfo networkInfo = getNetworkInfo(context);
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        if (!isConnected)
            return false;

        return (networkInfo.getType() == ConnectivityManager.TYPE_WIFI);
    }

    public static boolean connectedToMobile(Context context) {
        NetworkInfo networkInfo = getNetworkInfo(context);
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        if (!isConnected)
            return false;

        return (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    /**
     * Verifies that the handset is connected to the Internet using cellular data.
     */
    public static boolean connectedToMobileData(Context context) {
        if (!connectedToMobile(context))
            return false;

        final TelephonyManager tm = getTelephonyManager(context);
        if (connectedToNetwork(context) && tm.getDataState() == TelephonyManager.DATA_CONNECTED)
            return true;

        return false;
    }

    public static boolean connectedToWiFiOrMobile(Context context) {
        NetworkInfo networkInfo = getNetworkInfo(context);
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        if (!isConnected)
            return false;

        return (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) || (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    public static boolean connectedToNetwork(ConnectivityManager connectivityManager) {
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
    }

    public static boolean connectedToNetwork(Context context) {
        NetworkInfo networkInfo = getNetworkInfo(context);
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }

    public static int getConnectivityStatus(Context context) {
        NetworkInfo networkInfo = getNetworkInfo(context);

        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }

        return TYPE_NOT_CONNECTED;
    }


    public static boolean connectedToFastNetwork(Context context) {
        final ConnectivityManager cm = getConnectivityManager(context);
        final TelephonyManager tm = getTelephonyManager(context);
        NetworkInfo info = cm.getActiveNetworkInfo();

        return (info != null && info.isConnected() && connectedToFastNetwork(info.getType(), info.getSubtype()));

    }

    /**
     * Checks whether the network that the phone is connected to is fast.
     *
     * @param type    networkInfo.info.getType()
     * @param subType networkInfo.getSubtype()
     * @return
     */
    public static boolean connectedToFastNetwork(int type, int subType) {
        if (type == ConnectivityManager.TYPE_WIFI) {
            return true;
        }

        if (type == ConnectivityManager.TYPE_MOBILE) {
            switch (subType) {
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return false; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return false; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return true; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return true; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return true; // ~ 400-7000 kbps
                /*
                 * Above API level 7, make sure to set android:targetSdkVersion
                 * to appropriate level to use these
                 */
                case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                    return true; // ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                    return true; // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                    return true; // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                    return false; // ~25 kbps
                case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                    return true; // ~ 10+ Mbps
                // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return false;
            }
        }

        return false;
    }

    /**
     * Returns the absolute Wi-Fi signal level
     *
     * @param context
     * @return A level of the signal (1-Poor to 5-Excellent)
     */
    public static int getWiFiSignalStrength(Context context) {
        final WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int rssi = wm.getConnectionInfo().getRssi();
        return wm.calculateSignalLevel(rssi, 5);
    }

    private static ConnectivityManager getConnectivityManager(Context context) {
        final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm;
    }

    private static TelephonyManager getTelephonyManager(Context context) {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm;
    }

    private static NetworkInfo getNetworkInfo(Context context) {
        final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    //<editor-fold desc="WiFi Stats">

    public static String getWifiEncryptionName(Context context) {
        @SuppressLint("WifiManagerLeak") WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        List<ScanResult> networkList = wifiManager.getScanResults();

        if (networkList != null && wifiInfo != null) {
            for (ScanResult network : networkList) {
                if (wifiInfo.getBSSID() != null && wifiInfo.getBSSID().equalsIgnoreCase(network.BSSID)) {
                    //Logs("SecurityType " + network.capabilities);
                    return network.capabilities;
                }
            }
        }
        return "WIFI_NONE";
    }

    public static String getWifiFrequency(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        List<ScanResult> networkList = wifiManager.getScanResults();

        if (networkList != null && wifiInfo != null) {
            for (ScanResult network : networkList) {
                if (wifiInfo.getBSSID() != null && wifiInfo.getBSSID().equalsIgnoreCase(network.BSSID)) {
                    //Logs("Frequency " + network.frequency);
                    return String.valueOf(network.frequency).toString();
                }
            }
        }
        return "N/A";
    }

    public static int getWiFiStrength(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState());
        if (state == NetworkInfo.DetailedState.CONNECTED || state == NetworkInfo.DetailedState.OBTAINING_IPADDR) {
            int rssi = wifiManager.getConnectionInfo().getRssi();
            return WifiManager.calculateSignalLevel(rssi, 5);
        }

        return 0;
    }

    //</editor-fold>

    //<editor-fold desc="Hosts and IP addresses">

    /**
     * Checks a host's reachability/availability using the hostname {@code host} andthe given port
     * number {@code port}. The hostname is tried to be resolved and cannot be {@code null}.
     * The range for valid port numbers is between 0 and 65535 inclusive.
     *
     * @param host the specified hostname
     * @param port Port number
     * @return
     */
    public static boolean lookupHost(String host, int port) {
        boolean hostExists = true;

        try {
            // Create an unbound socket
            SocketAddress socketAddress = new InetSocketAddress(host, port);
            Socket socket = new Socket();
            socket.connect(socketAddress, NETWORK_SOCKET_CONNECT_TIMEOUT);

        } catch (Exception e) {
            e.printStackTrace();
            hostExists = false;
        }

        return hostExists;
    }

    /**
     * Converts an IP address to an integer.
     */
    public static int lookupHost(String hostname) {
        InetAddress address;

        try {
            address = InetAddress.getByName(hostname);
        } catch (UnknownHostException e) {
            return -1;
        }

        byte[] addrBytes = address.getAddress();

        return ((addrBytes[3] & 0xff) << 24) | ((addrBytes[2] & 0xff) << 16) | ((addrBytes[1] & 0xff) << 8) | (addrBytes[0] & 0xff);
    }

    //</editor-fold>

    //<editor-fold desc="SSL Certificates">

    // SSL support
    private SSLSocketFactory defaultSSLSocketFactory = null;
    private HostnameVerifier defaultHostnameVerifier = null;

    // always verify the host - don't check for certificate
    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    /**
     * Create a trust manager that does not validate SSL certificate chains.
     */
    public void trustAllHosts() {

        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }

            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
        }};

        // Install the all-trusting trust manager
        try {
            // Backup the current SSL socket factory
            defaultSSLSocketFactory = HttpsURLConnection
                    .getDefaultSSLSocketFactory();
            // Install our all trusting manager
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
