package presentation.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

object JwtConfig {
	const val SECRET = "mySuperSecretKeyForJWTThatIsAtLeast32CharactersLong"
	const val ISSUER = "nobel-prize-api"
	const val EXPIRY_MINUTES = 30L

	fun generateToken(username: String): String = JWT.create()
		.withIssuer(ISSUER)
		.withClaim("username", username)
		.withExpiresAt(Date(System.currentTimeMillis() + EXPIRY_MINUTES * 60 * 1000))
		.sign(Algorithm.HMAC256(SECRET))
}