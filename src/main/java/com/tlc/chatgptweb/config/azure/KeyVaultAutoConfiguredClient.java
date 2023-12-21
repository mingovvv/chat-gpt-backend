package com.tlc.chatgptweb.config.azure;

import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeyVaultAutoConfiguredClient implements KeyVaultClient {

    private final SecretClient secretClient;

    @Override
    public SecretClient getSecretClient() {
        return secretClient;
    }

    @Override
    public KeyVaultSecret getSecret(String key) {
        return secretClient.getSecret(key);
    }

}
