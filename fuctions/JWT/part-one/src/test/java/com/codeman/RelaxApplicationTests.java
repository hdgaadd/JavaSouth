package com.codeman;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.codeman.entity.User;
import com.codeman.service.TestImpl;
import com.codeman.service.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RelaxApplicationTests {

	@Resource
	private TestImpl testImpl;

	@Test
	public void test() {//会报错，Test类不会
		String token = createToken();
		System.out.println(token);

	}
	public String createToken() {
		//Create and Sign a Token
		String token = null;
		try {
			Algorithm algorithm = Algorithm.HMAC256("secret");
			token = JWT.create()
					.withIssuer("hdgaadd")
					.sign(algorithm);
		} catch (JWTCreationException exception){
			//Invalid Signing configuration / Couldn't convert Claims.
		}
		return token;
	}
	public Map<String, Claim> create(String token) {
		//Verify a Token
		DecodedJWT jwt = null;
		try {
			Algorithm algorithm = Algorithm.HMAC256("secret");
			JWTVerifier verifier = JWT.require(algorithm)
					.withIssuer("auth0")
					.build(); //Reusable verifier instance
			jwt = verifier.verify(token);
		} catch (JWTVerificationException exception){
			//Invalid signature/claims
		}
		System.out.println(jwt.getClaims());
		return  jwt.getClaims();
	}

}
