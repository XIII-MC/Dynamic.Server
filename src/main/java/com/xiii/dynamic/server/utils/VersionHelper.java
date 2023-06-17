package com.xiii.dynamic.server.utils;

public class VersionHelper {

    //TODO: Try and get rid of this and get a more reliable way of getting snapshot versions
    public static String getVersion (final String version, final String serverSoftware) {

        switch (serverSoftware) {

            case "Spigot":

                switch (version) {

                    case "1.4.6":
                        return "1.4.6-R0.4-SNAPSHOT";

                    case "1.4.7":
                        return "1.4.7-R1.1-SNAPSHOT";

                    case "1.5.1":
                        return "1.5.1-R0.1-SNAPSHOT";

                    case "1.5.2":
                        return "1.5.2-R1.1-SNAPSHOT";

                    case "1.6.2":
                        return "1.6.2-R1.1-SNAPSHOT";

                    case "1.6.4":
                        return "1.6.4-R2.1-SNAPSHOT";

                    case "1.7.4":
                        return "1.7.2-R0.4-SNAPSHOT-1339";

                    case "1.7.5":
                        return "1.7.5-R0.1-SNAPSHOT-1387";

                    case "1.7.9":
                        return "1.7.9-R0.2-SNAPSHOT";

                    case "1.7.8":
                        return "1.7.8-R0.1-SNAPSHOT";

                    case "1.7.10":
                        return "1.7.10-SNAPSHOT-B1657";

                    case "1.8":
                        return "1.8-R0.1-SNAPSHOT-latest";

                    case "1.8.3":
                        return "1.8.3-R0.1-SNAPSHOT-latest";

                    case "1.8.4":
                        return "1.8.4-R0.1-SNAPSHOT-latest";

                    case "1.8.5":
                        return "1.8.5-R0.1-SNAPSHOT-latest";

                    case "1.8.6":
                        return "1.8.6-R0.1-SNAPSHOT-latest";

                    case "1.8.7":
                        return "1.8.7-R0.1-SNAPSHOT-latest";

                    case "1.8.8":
                        return "1.8.8-R0.1-SNAPSHOT-latest";

                    case "1.9":
                        return "1.9-R0.1-SNAPSHOT-latest";

                    case "1.9.2":
                        return "1.9.2-R0.1-SNAPSHOT-latest";

                    case "1.9.4":
                        return "1.9.4-R0.1-SNAPSHOT-latest";

                    case "1.10":
                        return "1.10-R0.1-SNAPSHOT-latest";

                    case "1.10.2":
                        return "1.10.2-R0.1-SNAPSHOT-latest";

                    default:
                        return version;
                }

            case "Bukkit":

                switch (version) {

                    case "1.0.0":
                        return "1.0.0-SNAPSHOT";

                    case "1.1":
                        return "1.1-R5-SNAPSHOT";

                    case "1.2.2":
                        return "1.2.2-R0.1-SNAPSHOT";

                    case "1.2.3":
                        return "1.2.3-R0.3-SNAPSHOT";

                    case "1.2.4":
                        return "1.2.4-R1.1-SNAPSHOT";

                    case "1.2.5":
                        return "1.2.5-R5.1-SNAPSHOT";

                    case "1.3.1":
                        return "1.3.1-R2.1-SNAPSHOT";

                    case "1.3.2":
                        return "1.3.2-R2.1-SNAPSHOT";

                    case "1.4.2":
                        return "1.4.2-R0.3-SNAPSHOT";

                    case "1.4.5":
                        return "1.4.5-R1.1-SNAPSHOT";

                    case "1.4.6":
                        return "1.4.6-R0.4-SNAPSHOT";

                    case "1.4.7":
                        return "1.4.7-R1.1-SNAPSHOT";

                    case "1.5":
                        return "1.5-R0.1-20130317.180842-21";

                    case "1.5.1":
                        return "1.5.1-R0.2-SNAPSHOT";

                    case "1.5.2":
                        return "1.5.2-R1.0";

                    case "1.6.1":
                        return "1.6.1-R0.1-SNAPSHOT";

                    case "1.6.2":
                        return "1.6.2-R0.1-SNAPSHOT";

                    case "1.6.4":
                        return "1.6.4-R2.0";

                    case "1.7.2":
                        return "1.7.2-R0.4-20140216.012104-3";

                    case "1.7.5":
                        return "1.7.5-R0.1-20140402.020013-12";

                    case "1.7.8":
                        return "1.7.8-R0.1-SNAPSHOT";

                    case "1.7.9":
                        return "1.7.9-R0.2-SNAPSHOT";

                    case "1.7.10":
                        return "1.7.10-R0.1-20140808.005431-8";

                    case "1.8":
                        return "1.8-R0.1-SNAPSHOT-latest";

                    case "1.8.3":
                        return "1.8.3-R0.1-SNAPSHOT-latest";

                    case "1.8.4":
                        return "1.8.4-R0.1-SNAPSHOT-latest";

                    case "1.8.5":
                        return "1.8.5-R0.1-SNAPSHOT-latest";

                    case "1.8.6":
                        return "1.8.6-R0.1-SNAPSHOT-latest";

                    case "1.8.7":
                        return "1.8.7-R0.1-SNAPSHOT-latest";

                    case "1.8.8":
                        return "1.8.8-R0.1-SNAPSHOT-latest";

                    case "1.9":
                        return "1.9-R0.1-SNAPSHOT-latest";

                    case "1.9.2":
                        return "1.9.2-R0.1-SNAPSHOT-latest";

                    case "1.9.4":
                        return "1.9.4-R0.1-SNAPSHOT-latest";

                    case "1.10":
                        return "1.10-R0.1-SNAPSHOT-latest";

                    case "1.10.2":
                        return "1.10.2-R0.1-SNAPSHOT-latest";

                    case "1.14":
                        return "1.14-R0.1-SNAPSHOT";

                    case "1.14.1":
                        return "1.14.1-R0.1-SNAPSHOT";

                    case "1.14.2":
                        return "1.14.2-R0.1-SNAPSHOT";

                    case "1.14.3":
                        return "1.14.3-R0.1-SNAPSHOT";

                    case "1.14.4":
                        return "1.14.4-R0.1-SNAPSHOT";

                    case "1.15":
                        return "1.15-R0.1-SNAPSHOT";

                    case "1.15.1":
                        return "1.15.1-R0.1-SNAPSHOT";

                    default:
                        return version;

                }
        }
        return null;
    }
}
