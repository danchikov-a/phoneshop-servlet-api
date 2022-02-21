package com.es.phoneshop.security;

public interface DosProtectionFilter {
    boolean isAllowed(String ip, long time);
}
