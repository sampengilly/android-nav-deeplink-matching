package dev.pengilly.android.navdeeplinktest

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.createGraph
import androidx.navigation.fragment.fragment

fun NavController.getAppNavGraph() = createGraph(startDestination = "home") {
    fragment<HomeFragment>("home") {

    }

    fragment<ArticleFragment>("article/{id}") {
        deepLink { uriPattern = "https://{_host}/{_prefix}/{id}" }

        // Using an underscore prefix to identify arguments that aren't used by the destination,
        // just used for matching a deeplink.
        argument("_host") {
            type = InternalHostNamesType()
            nullable = false
            defaultValue = "www.examplewebsite.com"
        }

        argument("id") {
            type = ContentIdType()
            nullable = false
        }

        argument("_prefix") {
            type = NavType.EnumType(SupportedPrefixes::class.java)
            nullable = false
            defaultValue = SupportedPrefixes.SubOne
        }
    }

    fragment<FallbackFragment>("fallback") {
        deepLink { uriPattern = "{_host}/.*" }

        argument("_host") {
            type = InternalHostNamesType()
            nullable = false
            defaultValue = "www.examplewebsite.com"
        }
    }

}

fun ArticleFragment.getIdArgument(): String? = requireArguments().getString("id", "null")
fun ArticleFragment.getPrefixArgument(): SupportedPrefixes =
    requireArguments().get("_prefix") as SupportedPrefixes


enum class SupportedPrefixes {
    SubOne,
    SubTwo,
    SubThree
}

class ContentIdType : NavType<String>(isNullableAllowed = false) {

    override fun get(bundle: Bundle, key: String): String? = bundle.getString(key)
    override fun put(bundle: Bundle, key: String, value: String) {
        bundle.putString(key, value)
    }

    override fun parseValue(value: String): String = try {
        val asInt = value.toInt()

        // Example business rule: Content IDs must be even integers and 4 characters or longer
        if (asInt.mod(2) != 0) throw IllegalArgumentException()
        if (value.length < 4) throw IllegalArgumentException()

        value
    } catch (ex: NumberFormatException) {
        throw IllegalArgumentException()
    }

}

class InternalHostNamesType : NavType<String>(isNullableAllowed = false) {

    override fun get(bundle: Bundle, key: String): String? = bundle.getString(key)
    override fun put(bundle: Bundle, key: String, value: String) {
        bundle.putString(key, value)
    }

    override fun parseValue(value: String): String {
        val internalHosts = listOf(
            "www.examplewebsite.com",
            "subdomain.examplewebsite.com",
            "www.otherwebsite.com"
        )
        if (value !in internalHosts) throw IllegalArgumentException()
        return value
    }

}