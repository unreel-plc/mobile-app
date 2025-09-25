package com.unreel.unreel.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface OfflineRepository {
    suspend fun setUuid(uuid: String)
    suspend fun setAccessToken(accessToken: String);
    suspend fun setRefreshToken(refreshToken: String);
    suspend fun setUserId(userId: String);
    suspend fun setIdentifier(identifier: String);
    suspend fun setCountryCode(countryCode: String);
    suspend fun setLoggedInWith(loggedInWith: String)
    suspend fun setLoggedIn(loggedIn: String)
    suspend fun setVerified(verified: String)
    suspend fun setFirstName(firstName: String)
    suspend fun setLastName(lastName: String)
    suspend fun setPreferredLanguage(language: String)
    suspend fun setIsWelcome(isWelcome: String)
    suspend fun setStripeCustomerId(stripeCustomerId: String)
    suspend fun setIsAgreeToNotification(isAgreeToNotification: Boolean)

    fun getUuid():Flow<String?>
    fun getAccessToken(): Flow<String?>;
    fun getRefreshToken(): Flow<String?>;
    fun getUserId(): Flow<String?>;
    fun getIdentifier(): Flow<String?>
    fun getCountryCode(): Flow<String?>
    fun getLoggedInWith(): Flow<String?>
    fun getLoggedIn(): Flow<String?>;
    fun getVerified(): Flow<String?>;
    fun getFirstName(): Flow<String?>
    fun getLastName(): Flow<String?>
    fun getPreferredLanguage(): Flow<String?>

    fun getIsWelcome(): Flow<String?>
    fun getStripeCustomerId(): Flow<String?>
    fun getIsAgreeToNotification(): Flow<Boolean?>
}

class OfflineRepositoryImpl(private val dataStore: DataStore<Preferences>) : OfflineRepository {
    companion object {
        val UUID_KEY = stringPreferencesKey(name = "uuid")
        val ACCESS_TOKEN_KEY = stringPreferencesKey(name = "access_token")
        val USER_ID_KEY = stringPreferencesKey(name = "user_id")
        val REFRESH_TOKEN_KEY = stringPreferencesKey(name = "refresh_token")
        val IDENTIFIER_KEY = stringPreferencesKey(name = "identifier")
        val COUNTRY_CODE_KEY = stringPreferencesKey(name = "country_code")
        val LOGGED_IN_WITH_KEY = stringPreferencesKey(name = "logged_in_with")
        val LOGGED_IN_KEY = stringPreferencesKey(name = "logged_in")
        val VERIFIED_KEY = stringPreferencesKey(name = "verified")
        val FIRST_NAME_KEY = stringPreferencesKey(name = "firstName")
        val LAST_NAME_KEY = stringPreferencesKey(name = "lastName")
        val LANGUAGE_KEY = stringPreferencesKey(name = "language")
        val WELCOME_KEY = stringPreferencesKey(name = "welcome")
        val STRIPE_CUSTOMER_ID_KEY = stringPreferencesKey(name = "stripe_customer_id")
        val IS_AGREE_TO_NOTIFICATION_KEY = booleanPreferencesKey(name = "is_agree_to_notification")
    }
    suspend fun <T> put(key: Preferences.Key<T>, value: T) {
        dataStore.edit {
            it[key] = value
        }
    }

    suspend fun <T> remove(key: Preferences.Key<T>) {
        dataStore.edit {
            it.remove(key = key)
        }
    }

    fun <T> get(key: Preferences.Key<T>, default: T): Flow<T> =
        dataStore.data.map { it[key] ?: default }

    fun <T> get(key: Preferences.Key<T>): Flow<T?> = dataStore.data.map { it[key] }

    override suspend fun setUuid(uuid: String) {
        this.put(UUID_KEY, uuid)
    }
    override suspend fun setLoggedIn(loggedIn: String) {
        this.put(LOGGED_IN_KEY, loggedIn)
    }
    override suspend fun setVerified(verified: String) {
        this.put(VERIFIED_KEY, verified)
    }
    override suspend fun setAccessToken(accessToken: String) {
        this.put(ACCESS_TOKEN_KEY, accessToken);
    }
    override suspend fun setUserId(userId: String) {
        this.put(USER_ID_KEY, userId)
    }
    override suspend fun setRefreshToken(refreshToken: String) {
        this.put(REFRESH_TOKEN_KEY, refreshToken);
    }
    override suspend fun setIdentifier(identifier: String) {
        this.put(IDENTIFIER_KEY, identifier);
    }
    override suspend fun setCountryCode(countryCode: String) {
        this.put(COUNTRY_CODE_KEY, countryCode);
    }
    override suspend fun setLoggedInWith(loggedInWith: String) {
        this.put(LOGGED_IN_WITH_KEY, loggedInWith);
    }

    override suspend fun setFirstName(firstName: String) {
        this.put(FIRST_NAME_KEY, firstName)
    }
    override suspend fun setLastName(lastName: String) {
        this.put(LAST_NAME_KEY, lastName)
    }

    override suspend fun setPreferredLanguage(language: String) {
        this.put(LANGUAGE_KEY, language)
    }
    override suspend fun setIsWelcome(isWelcome: String) {
        this.put(WELCOME_KEY, isWelcome)
    }
    override suspend fun setStripeCustomerId(stripeCustomerId: String) {
        this.put(STRIPE_CUSTOMER_ID_KEY, stripeCustomerId)
    }
    override suspend fun setIsAgreeToNotification(isAgreeToNotification: Boolean) {
        this.put(IS_AGREE_TO_NOTIFICATION_KEY, isAgreeToNotification)
    }

    override fun getUuid() = this.get(UUID_KEY, "")
    override fun getAccessToken() = this.get(ACCESS_TOKEN_KEY, "");
    override fun getUserId() = this.get(USER_ID_KEY, "");
    override fun getRefreshToken() = this.get(REFRESH_TOKEN_KEY, "");
    override fun getIdentifier() = this.get(IDENTIFIER_KEY, "")
    override fun getCountryCode() = this.get(COUNTRY_CODE_KEY, "")
    override fun getLoggedInWith() = this.get(LOGGED_IN_WITH_KEY, "")
    override fun getLoggedIn(): Flow<String> = this.get(LOGGED_IN_KEY,"")
    override fun getVerified(): Flow<String> = this.get(VERIFIED_KEY,"")
    override fun getFirstName() = this.get(FIRST_NAME_KEY, "")
    override fun getLastName() = this.get(LAST_NAME_KEY, "")
    override fun getPreferredLanguage() =  this.get(LANGUAGE_KEY, "")
    override fun getIsWelcome() = this.get(WELCOME_KEY, "")
    override fun getStripeCustomerId() = this.get(STRIPE_CUSTOMER_ID_KEY, "")
    override fun getIsAgreeToNotification() = this.get(IS_AGREE_TO_NOTIFICATION_KEY, false)
}