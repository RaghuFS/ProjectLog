package com.finstack.projectlog.jwtauth.configuration.service;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
@Profile("JWT")
public class RSAUtil {

	private final String publicKeyExponent;
	private final String publicKeyModulus;
	private final String privateKeyExponent;
	private final String privateKeyModulus;

	public RSAUtil(@Value("${publicKey.exponent}") String publicKeyExponent,
			@Value("${publicKey.modulus}") String publicKeyModulus,
			@Value("${privateKey.exponent}") String privateKeyExponent,
			@Value("${privateKey.modulus}") String privateKeyModulus) {
		this.publicKeyExponent = publicKeyExponent;
		this.publicKeyModulus = publicKeyModulus;
		this.privateKeyExponent = privateKeyExponent;
		this.privateKeyModulus = privateKeyModulus;
	}

	public static Map<String, BigInteger> generateNewRSAKeys() {
		KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.RS512);
		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();
		HashMap<String, BigInteger> hashMap = new HashMap<String, BigInteger>();
		hashMap.put("privateKey.exponent", ((RSAPrivateKey) privateKey).getPrivateExponent());
		hashMap.put("privateKey.modulus", ((RSAPrivateKey) privateKey).getModulus());
		hashMap.put("publicKey.exponent", ((RSAPublicKey) publicKey).getPublicExponent());
		hashMap.put("publicKey.exponent", ((RSAPublicKey) publicKey).getModulus());

		return hashMap;
	}

	public static void main(String[] args) {
		KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.RS512);
		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();

		// Below lines to print the key details
		System.out.println("privateKey.exponent=" + ((RSAPrivateKey) privateKey).getPrivateExponent());
		System.out.println("privateKey.modulus=" + ((RSAPrivateKey) privateKey).getModulus());
		System.out.println("publicKey.exponent=" + ((RSAPublicKey) publicKey).getPublicExponent());
		System.out.println("publicKey.exponent=" + ((RSAPublicKey) publicKey).getModulus());

	}

	public PrivateKey getPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
		RSAPrivateKeySpec privateKeySpec = new RSAPrivateKeySpec(new BigInteger(privateKeyModulus),
				new BigInteger(privateKeyExponent));
		KeyFactory factory = KeyFactory.getInstance("RSA");
		return factory.generatePrivate(privateKeySpec);
	}

	public PublicKey getPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
		RSAPublicKeySpec spec = new RSAPublicKeySpec(new BigInteger(publicKeyModulus),
				new BigInteger(publicKeyExponent));
		KeyFactory factory = KeyFactory.getInstance("RSA");
		return factory.generatePublic(spec);
	}
}
