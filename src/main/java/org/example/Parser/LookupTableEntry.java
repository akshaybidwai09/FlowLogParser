package org.example.Parser;

import java.util.Objects;

public class LookupTableEntry {
    private final String port;
    private final String protocol;

    public LookupTableEntry(String port, String protocol) {
        this.port = port;
        this.protocol = protocol.toLowerCase();
    }

    public String getPort() {
        return port;
    }

    public String getProtocol() {
        return protocol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LookupTableEntry)) return false;
        LookupTableEntry that = (LookupTableEntry) o;
        return port.equals(that.port) && protocol.equals(that.protocol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(port, protocol);
    }
}
